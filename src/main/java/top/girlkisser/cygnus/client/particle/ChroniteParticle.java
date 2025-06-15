package top.girlkisser.cygnus.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.cygnus.client.CygnusClient;

public class ChroniteParticle extends TextureSheetParticle
{
	public final SpriteSet spriteSet;

	public ChroniteParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, ParticleOptions options) {
		super(level, x, y, z, 0, 0, 0);
		this.spriteSet = spriteSet;

		this.lifetime = random.nextInt(16, 32);

		// From net.minecraft.client.particle.HeartParticle
		this.speedUpWhenYMotionIsBlocked = true;
		this.friction = 0.86F;
		this.xd *= 0.009999999776482582;
		this.yd *= 0.009999999776482582;
		this.zd *= 0.009999999776482582;
		this.yd += 0.05;
		this.hasPhysics = false;

		setSpriteFromAge(spriteSet);
	}

	@Override
	public void tick() {
		setSpriteFromAge(spriteSet);
		super.tick();
	}

	@Override
	public @NotNull ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	public static void spawn(Level level, Vec3 pos, float heightMod) {
		final double step = Math.PI / 18;
		for (double r = 0; r < 2 * Math.PI; r += step) {
			level.addParticle(
				new SimpleParticleType(false),
				pos.x + Math.sin(r),
				pos.y + (CygnusClient.RANDOM.nextDouble() * heightMod),
				pos.z + Math.cos(r),
				0, 0, 0
			);
		}
	}
}
