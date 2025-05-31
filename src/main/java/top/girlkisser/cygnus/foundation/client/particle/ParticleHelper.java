package top.girlkisser.cygnus.foundation.client.particle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ParticleHelper
{
	public static void addDust(DustParticleOptions options, Level level, Vec3 pos, Vec3 vel)
	{
		level.addParticle(options, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
	}

	public static void addDust(DustParticleOptions options, Level level, Vec3 pos)
	{
		addDust(options, level, pos, Vec3.ZERO);
	}

	public static void addDustInBlock(DustParticleOptions options, Level level, BlockPos pos)
	{
		final int spread = 1;
		final float density = 3f;
		var centre = pos.getCenter();
		// insane loop nesting, go!
		for (int y = -spread ; y < spread + 1 ; y++)
			for (int x = -spread ; x < spread + 1 ; x++)
				for (int z = -spread ; z < spread + 1 ; z++)
					addDust(options, level, centre.add(x / density, y / density, z / density));
	}
}
