package net.witixin.seteffect.armor;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.witixin.seteffect.SetEffect;
import org.openzen.zencode.java.ZenCodeType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam on 20/06/2018.
 */
@ZenRegister
@ZenCodeType.Name("mods.seteffect.ArmorSetEffect")
public class ArmorSet
{


	private final Multimap<EquipmentSlotType, ItemStack> armor;
	private final List<IArmorEffect> effects;
	private final List<IArmorEffect> attackerEffects;
	private final List<IArmorEffect> attackEffects;
	private final List<IArmorEffect> attackedEffects;

	private boolean flyEffect;
	private final List<String> requiredStages;
	private String name = "";
	private String packmode;
	private boolean strict, ignoreNBT;

	@ZenCodeType.Constructor
	public ArmorSet(String name)
	{
		this.name = name;
	    this.armor = ArrayListMultimap.create();
		this.effects = new ArrayList<>();
		this.attackerEffects = new ArrayList<>();
		this.requiredStages = new ArrayList<>();
		this.attackEffects = new ArrayList<>();
		this.attackedEffects = new ArrayList<>();
		this.strict = false;
		this.ignoreNBT = false;
		this.flyEffect = false;
	}

	@ZenCodeType.Method
	public ArmorSet requireGamestages(String... stages)
	{
		for (String s : stages){
			requiredStages.add(s);
		}
		return this;
	}

	@ZenCodeType.Method
	public void register()
	{
		if (name.length() <= 1)throw new IllegalArgumentException("ArmorSetEffects require a name");
		ArmorSets.addSet(this);
		CraftTweakerAPI.logInfo("Registering an ArmorSet with name: " + this.getName());
	}


	@ZenCodeType.Method
	public ArmorSet addParticle(String particleName, float minx, float miny, float minz, float maxx, float maxy, float maxz, float minxoffset, float minyoffset, float minzoffset, float maxxoffset, float maxyoffset, float maxzoffset, float minspeed, float maxspeed, int amount)
	{
		ParticleType type = ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(particleName));
		if(type == null)
			throw new NullPointerException("No such particle " + particleName);

		ArmorEffectParticle eff = new ArmorEffectParticle(type, minx, miny, minz, maxx, maxy, maxz, minxoffset, minyoffset, minzoffset, maxxoffset, maxyoffset, maxzoffset, minspeed, maxspeed, amount);
		effects.add(eff);
		return this;
	}

	@ZenCodeType.Method
	public ArmorSet addParticleWithDefaultSpread(String particleName){
			return addParticle(particleName,  1.0f, 1.0f, 1.0f, 3.0f, 3.0f, 3.0f, 0.2f, 0.2f, 0.2f, 0.6f, 0.6f, 0.6f, 5.0f, 5.0f, 1);
	}

	@ZenCodeType.Method
	public String getPackmode(){
		return packmode;
	}

	@ZenCodeType.Method
    public ArmorSet addImmunity(Effect effect)
    {
		effects.add(new ArmorEffectImmune(effect));
		return this;
    }
    @ZenCodeType.Method
	public static void dumpParticleNames(){
		CraftTweakerAPI.logInfo("Dumping registered particle names:");
		for (ParticleType t : ForgeRegistries.PARTICLE_TYPES.getValues()){
			CraftTweakerAPI.logInfo(t.getRegistryName().toString());
		}
		CraftTweakerAPI.logInfo("Finished dumping registered particle names");
	}

	@ZenCodeType.Method
	public ArmorSet addEffect(EffectInstance effect)
	{
		effects.add(new ArmorEffectPotion(effect));
		return this;
	}
	@ZenCodeType.Method
	public ArmorSet addAttributeEffect(Attribute atr, AttributeModifier mod){
		//attrEffects.add(new ArmorEffectAttributes(atr, mod));
		CraftTweakerAPI.logInfo("addAttributeEffect is currently under maintenance and doesn't do anything!");
		return this;
	}
	@ZenCodeType.Method
	public ArmorSet addAttackEffect(EffectInstance effect){
		attackEffects.add(new ArmorEffectPotion(effect));
		return this;
	}
	@ZenCodeType.Method
	public ArmorSet addAttackerEffect(EffectInstance effect)
	{
		attackerEffects.add(new ArmorEffectPotion(effect));
		return this;
	}

	@ZenCodeType.Method
	public ArmorSet applyFlight(boolean flyEffect){
		this.flyEffect = flyEffect;
		return this;
	}

	@ZenCodeType.Method
	public ArmorSet addAttackedEffect(EffectInstance effect){
		attackedEffects.add(new ArmorEffectPotion(effect));
		return this;
	}

	@ZenCodeType.Method
	public ArmorSet inSlot(EquipmentSlotType slot, IItemStack stack){
		armor.put(slot, stack.getInternal());
		return this;
	}

	@ZenCodeType.Method
	public ArmorSet setStrict()
	{
		this.strict = true;
		return this;
	}
	
	@ZenCodeType.Method
	public ArmorSet setPackmode(String packmode){
		this.packmode = packmode;
		return this;
	}
	
	@ZenCodeType.Method
    public ArmorSet setIgnoreNBT()
    {
        this.ignoreNBT = true;
        return this;
    }

	@ZenCodeType.Method
	public boolean getFlight(){
		return this.flyEffect;
	}

	@ZenCodeType.Method
	public static void setCycleTicks(int ticks){
		if (ticks < 1)throw new IllegalArgumentException("Ticks can't be negative or 0!");
		SetEffect.cycleTicks = ticks;
	}

	public boolean isPlayerWearing(LivingEntity player)
	{
		return isPlayerWearing(player, strict);
	}

	private boolean isPlayerWearing(LivingEntity player, boolean strict)
	{
		if(player instanceof PlayerEntity && !SetEffect.hasGamestage((PlayerEntity) player, requiredStages) && !SetEffect.correctPackmode(getPackmode()))
			return false;

		for(EquipmentSlotType slot : armor.keySet())
		{
		    boolean match = false;

		    for(ItemStack stack : armor.get(slot))
            {
                ItemStack playerStack = player.getItemBySlot(slot);
                match = itemMatch(playerStack, stack, strict);

                if(match)
                    break;
            }


			if(!match)
				return false;
		}

		return true;
	}

	private boolean itemMatch(ItemStack playerStack, ItemStack compareStack, boolean strict)
	{
		if(strict)
			return isItemStackEqualStrict(playerStack, compareStack);

		return isItemStackEqual(playerStack, compareStack);
	}

	private boolean isItemStackEqual(ItemStack playerStack, ItemStack compareStack)
	{
		if (playerStack.getCount() != playerStack.getCount())
		{
			return false;
		}
		else if (playerStack.getItem() != compareStack.getItem())
		{
			return false;
		}
		else if (playerStack.getTag() == null && compareStack.getTag() != null)
		{
			return false;
		}
		else
		{
			if((playerStack.getTag() != null && compareStack != null) && !ignoreNBT)
			{
				CompoundNBT playerTags = playerStack.getTag();
				CompoundNBT compareTags = compareStack.getTag();

				if(compareTags == null || compareTags.getAllKeys() == null)
					return false;

				for(String tag : compareTags.getAllKeys())
				{
					if(playerTags.contains(tag))
					{
						CompoundNBT pTag = playerTags.getCompound(tag);
						CompoundNBT cTag = compareTags.getCompound(tag);

						if(!pTag.equals(cTag))
							return false;
					}
					else
					{
						return false;
					}
				}
				return true;
			}
			else
			{
				return true;
			}
		}
	}

	private boolean isItemStackEqualStrict(ItemStack playerStack, ItemStack compareStack)
	{
		if (playerStack.getCount() != playerStack.getCount())
		{
			return false;
		}
		else if (playerStack.getItem() != compareStack.getItem())
		{
			return false;
		}
		else if (playerStack.getTag() == null && compareStack.getTag() != null)
		{
			return false;
		}
		else
		{
			return (playerStack.getTag() == null || playerStack.getTag().equals(compareStack.getTag())) && playerStack.areCapsCompatible(compareStack);
		}
	}

	public void print()
	{
		for(EquipmentSlotType slot : armor.keySet())
		{
			System.out.println(slot + " - " + armor.get(slot));
		}
	}

	public void applyEffects(LivingEntity livingBase)
	{

		effects.forEach(e ->e.apply(livingBase));
	}

	public void applyAttackerEffect(LivingEntity livingBase)
	{
		attackerEffects.forEach(e -> e.apply(livingBase));
	}

	public void applyAttrEffects(LivingEntity living){
		/* attrEffects.forEach(effect -> {
			effect.apply(living);}); */
	}
	public void removeAttrEffects(LivingEntity entity){
		/* attrEffects.forEach(effect -> {
			((ArmorEffectAttributes) effect).remove(entity);
		}); */
	}
	public void applyAttackEffect(LivingEntity livingBase){
		attackEffects.forEach(e -> e.apply(livingBase));
	}

	public void applyAttackedEffects(LivingEntity living){
		attackedEffects.forEach(e -> e.apply(living));
	}

	public Multimap<EquipmentSlotType, ItemStack> getArmor()
	{
		return armor;
	}

	public String getName()
	{
		return name;
	}
}
