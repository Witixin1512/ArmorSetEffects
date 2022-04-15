package net.witixin.seteffect.armor;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

/**
 * Created by Sam on 21/06/2018.
 */
public class ArmorEffectParticle implements IArmorEffect
{
	private final ParticleType type;
	private final float minx, miny, minz, maxx, maxy, maxz, minxoffset, minyoffset, minzoffset, maxxoffset, maxyoffset, maxzoffset, minspeed, maxspeed;
	private int amount;

	public ArmorEffectParticle(ParticleType type, float minx, float miny, float minz, float maxx, float maxy, float maxz, float minxoffset, float minyoffset, float minzoffset, float maxxoffset, float maxyoffset, float maxzoffset, float minspeed, float maxspeed, int amount)
	{
		this.type = type;
		this.minx = minx;
		this.miny = miny;
		this.minz = minz;
		this.maxx = maxx;
		this.maxy = maxy;
		this.maxz = maxz;
		this.minxoffset = minxoffset;
		this.minyoffset = minyoffset;
		this.minzoffset = minzoffset;
		this.maxxoffset = maxxoffset;
		this.maxyoffset = maxyoffset;
		this.maxzoffset = maxzoffset;
		this.amount = amount;
		this.minspeed = minspeed;
		this.maxspeed = maxspeed;
	}

	@Override
	public void apply(LivingEntity player){
		ServerWorld level = (ServerWorld) player.level;
		Random random = level.getRandom();
			for(int i = 0; i < amount; i++)
			{
				float posX = randomRange(random, minx, maxx);
				float posY = randomRange(random, miny, maxy);
				float posZ = randomRange(random, minz, maxz);
				float offsetX = randomRange(random, minxoffset, maxxoffset);
				float offsetY = randomRange(random, minyoffset, maxyoffset);
				float offsetZ = randomRange(random, minzoffset, maxzoffset);

				float speed = randomRange(random, minspeed, maxspeed);

				for (ServerPlayerEntity playerEntity :  player.getServer().getPlayerList().getPlayers()){
					level.sendParticles(playerEntity, (IParticleData) type, false, player.getX() + posX, player.getY() + posY, player.getZ() + posZ, 1, offsetX, offsetY, offsetZ, speed);
				}


		}

	}
	private static float randomRange(Random random, float min, float max)
	{
		float val = min + random.nextFloat() * (max - min);
		return val;
	}
}
