package top.girlkisser.cygnus.foundation.client.particle;

import net.minecraft.core.particles.DustParticleOptions;
import org.joml.Vector3f;

public interface DustParticlePresets
{
	DustParticleOptions TELEPAD = new DustParticleOptions(new Vector3f(.1f, .9f, .2f), 1f);
}
