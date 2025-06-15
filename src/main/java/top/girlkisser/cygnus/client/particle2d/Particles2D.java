package top.girlkisser.cygnus.client.particle2d;

import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2f;
import org.joml.Vector2i;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.foundation.client.particle2d.IParticle2D;
import top.girlkisser.cygnus.foundation.client.particle2d.SimpleParticle2D;

public final class Particles2D
{
	public static final ResourceLocation CHRONITE_TEXTURE = Cygnus.id("particles/chronite");
	public static final Vector2i CHRONITE_SIZE = new Vector2i(12, 12);

	public static IParticle2D makeChroniteParticle(float x, float y, float dx, float dy, float ddx, float ddy, float scale, int lifetime)
	{
		return new SimpleParticle2D(
			CHRONITE_TEXTURE,
			new Vector2f(x, y),
			new Vector2f(dx, dy),
			new Vector2f(ddx, ddy),
			new Vector2i((int) (CHRONITE_SIZE.x * scale), (int) (CHRONITE_SIZE.y * scale)),
			lifetime
		);
	}

	public static IParticle2D makeChroniteParticle(float x, float y, float dx, float dy, float scale, int lifetime)
	{
		return makeChroniteParticle(x, y, dx, dy, 0f, 0f, scale, lifetime);
	}
}
