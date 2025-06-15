package top.girlkisser.cygnus.content.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;
import top.girlkisser.cygnus.Cygnus;

@SuppressWarnings("unused")
@ApiStatus.NonExtendable
public interface CygnusParticleTypes
{
	DeferredRegister<ParticleType<?>> R = DeferredRegister.create(Registries.PARTICLE_TYPE, Cygnus.MODID);

	DeferredHolder<ParticleType<?>, SimpleParticleType> CHRONITE = R.register("chronite", () -> new SimpleParticleType(false));
}
