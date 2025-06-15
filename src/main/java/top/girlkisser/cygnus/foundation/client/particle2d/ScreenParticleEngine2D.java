package top.girlkisser.cygnus.foundation.client.particle2d;

import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

// Renders particles in GUIs
public class ScreenParticleEngine2D implements IParticleEngine2D
{
	public static final ScreenParticleEngine2D INSTANCE = new ScreenParticleEngine2D();

	public static final int MAX_PARTICLES = Short.MAX_VALUE;

	protected List<InstancedParticle2D> particles = new ArrayList<>();
	protected Queue<IParticle2D> particleQueue = new ArrayDeque<>();

	@Override
	public void addParticle(IParticle2D particle)
	{
		particleQueue.add(particle);
	}

	@Override
	public void render(GuiGraphics graphics, float partialTick)
	{
		if (!particleQueue.isEmpty())
		{
			particleQueue.forEach(it -> particles.add(new InstancedParticle2D(it, it.lifetime())));
			particleQueue.clear();
		}

		for (var particle : particles)
			particle.particle.render(particle, graphics, partialTick);

		particles.removeIf(it -> it.lifetimeLeft <= 0);
	}

	@Override
	public void clear()
	{
		particles.clear();
		particleQueue.clear();
	}
}
