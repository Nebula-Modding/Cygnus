package top.girlkisser.cygnus.foundation.client.particle2d;

public class InstancedParticle2D
{
	public IParticle2D particle;
	public int lifetimeLeft;

	public InstancedParticle2D(
		IParticle2D particle,
		int lifetimeLeft
	)
	{
		this.particle = particle;
		this.lifetimeLeft = lifetimeLeft;
	}
}
