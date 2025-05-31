package top.girlkisser.cygnus.content.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.content.entity.EntityLandingBeam;

import java.util.function.Supplier;

public interface CygnusEntityTypes
{
	DeferredRegister<EntityType<?>> R = DeferredRegister.create(Registries.ENTITY_TYPE, Cygnus.MODID);

	static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> reg(String id, Supplier<EntityType<T>> entity)
	{
		return R.register(id, entity);
	}

	static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> reg(String id, EntityType.Builder<T> builder)
	{
		return R.register(id, () -> builder.build(id));
	}

	DeferredHolder<EntityType<?>, EntityType<EntityLandingBeam>> LANDING_BEAM = reg("landing_beam",
		EntityType.Builder.<EntityLandingBeam>of(EntityLandingBeam::new, MobCategory.MISC)
			.fireImmune()
			.noSummon()
			.sized(0.5f, 2f)
			.setUpdateInterval(1)
			.passengerAttachments(Vec3.ZERO)
			.clientTrackingRange(10));
}
