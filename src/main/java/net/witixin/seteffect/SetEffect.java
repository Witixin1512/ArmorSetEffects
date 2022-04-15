package net.witixin.seteffect;

import com.teamacronymcoders.packmode.PackModeAPIImpl;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.witixin.seteffect.armor.ArmorSet;
import net.witixin.seteffect.armor.ArmorSets;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam on 20/06/2018.
 */
@Mod(SetEffect.MODID)
public class SetEffect
{
	public static final String MODID = "seteffect";

	public SetEffect(){
		MinecraftForge.EVENT_BUS.register(this.getClass());
	}
		public static int cycleTicks = 100;

		@SubscribeEvent
		public static void onEntityTick(LivingEvent.LivingUpdateEvent event)
		{
			if(event.getEntityLiving().level.isClientSide || event.getEntityLiving() == null || !event.getEntityLiving().isAlive())
				return;
		/*if(event.getEntityLiving().level.getGameTime()    % cycleTicks == 0  )
		{*/

			for(ArmorSet set : ArmorSets.sets)
			{
				if(set.isPlayerWearing(event.getEntityLiving()))
				{

					set.applyEffects(event.getEntityLiving());
				}
			}
			//}
		}

		@SubscribeEvent
		public static void onEntityHurt(LivingAttackEvent event)
		{
			for(ArmorSet set : ArmorSets.sets) {
				if(set.isPlayerWearing(event.getEntityLiving()))
				{
					set.applyAttackedEffects(event.getEntityLiving());
					if(event.getSource().getDirectEntity() != null && event.getSource().getDirectEntity() instanceof LivingEntity)
						set.applyAttackerEffect((LivingEntity) event.getSource().getDirectEntity());
				}
				if (event.getSource().getDirectEntity() != null && event.getSource().getDirectEntity() instanceof LivingEntity){
					if (set.isPlayerWearing((LivingEntity) event.getSource().getDirectEntity())){
						set.applyAttackEffect(event.getEntityLiving());
					}
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
			if (set.isPlayerWearing(event.getEntityLiving())){
				set.applyAttrEffects(event.getEntityLiving());
				return;
			}
			else {
				set.removeAttrEffects(event.getEntityLiving());
			}
		}
	}
		@SubscribeEvent
		public static void reload(AddReloadListenerEvent event){
		ArmorSets.sets = new ArrayList<>();
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
		if (ModList.get().isLoaded("packmode") && packmode != null && !packmode.isEmpty()){
			return PackModeAPIImpl.getInstance().getPackMode().equalsIgnoreCase(packmode);
		}
		return true;
	}
}
