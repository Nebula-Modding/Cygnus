package top.girlkisser.cygnus.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ChroniteParticleProvider implements ParticleProvider<SimpleParticleType>
{
	public final SpriteSet spriteSet;

	public ChroniteParticleProvider(SpriteSet spriteSet)
	{
		this.spriteSet = spriteSet;
	}

	@Override
	public @Nullable ChroniteParticle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
	{
		return new ChroniteParticle(level, x, y, z, spriteSet, type);
	}
}
