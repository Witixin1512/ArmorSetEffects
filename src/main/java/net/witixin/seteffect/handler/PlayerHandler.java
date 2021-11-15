package net.witixin.seteffect.handler;

import com.teamacronymcoders.packmode.PackModeAPIImpl;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.witixin.seteffect.armor.ArmorSet;
import net.witixin.seteffect.armor.ArmorSets;

import java.util.List;

/**
 * Created by Sam on 20/06/2018.
 */
public class PlayerHandler
{
	static int ticks = 0;


	@SubscribeEvent
	public static void onEntityTick(LivingEvent.LivingUpdateEvent event)
	{
		if(event.getEntityLiving().level.isClientSide || event.getEntityLiving() == null || !event.getEntityLiving().isAlive())
			return;
		ticks++;
		if(ticks % 100 == 0)
		{
			ticks = 0;

			for(ArmorSet set : ArmorSets.sets)
			{
				if(set.isPlayerWearing(event.getEntityLiving()))
				{

					set.applyEffects(event.getEntityLiving());
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityHurt(LivingAttackEvent event)
	{
			for(ArmorSet set : ArmorSets.sets) {
				if(set.isPlayerWearing(event.getEntityLiving()))
				{
					if(event.getSource().getDirectEntity() != null && event.getSource().getDirectEntity() instanceof LivingEntity)
						set.applyAttackerEffect((LivingEntity) event.getSource().getDirectEntity());
				}
			}
	}
	@SubscribeEvent
	public static void onPlayerArmor(LivingEquipmentChangeEvent event){
		if (event.getEntityLiving().level.isClientSide)return;
		for (ArmorSet set : ArmorSets.sets){
			if (event.getEntityLiving() instanceof PlayerEntity && set.getFlight()){
				PlayerEntity playerEntity = (PlayerEntity) event.getEntityLiving();
				if (!playerEntity.isCreative() && !playerEntity.isSpectator()){
						updateThings(playerEntity, set.isPlayerWearing(playerEntity));
				}
			}
		}
	}
	private static void updateThings(PlayerEntity pl, boolean thing){
		pl.abilities.mayfly = thing;
		pl.onUpdateAbilities();
	}

	public static boolean hasGamestage(PlayerEntity player, List<String> gameStages)
	{
		if(ModList.get().isLoaded("gamestages") && !gameStages.isEmpty())
		{
			return GameStageHelper.hasAllOf(player, gameStages);
		}
		return true;
	}
	public static boolean correctPackmode(String packmode){
		if (ModList.get().isLoaded("packmode") && packmode != null){
			return PackModeAPIImpl.getInstance().getPackMode().equalsIgnoreCase(packmode);
		}
		return true;
	}
}
