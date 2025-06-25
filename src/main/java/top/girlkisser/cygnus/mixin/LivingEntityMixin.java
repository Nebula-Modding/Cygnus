package top.girlkisser.cygnus.mixin;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.girlkisser.cygnus.api.space.Planet;
import top.girlkisser.cygnus.api.space.PlanetUtils;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
	public LivingEntityMixin(EntityType<?> entityType, Level level)
	{
		super(entityType, level);
		throw new UnsupportedOperationException();
	}

	@Inject(method = "calculateFallDamage", at = @At("HEAD"), cancellable = true)
	public void calculateFallDamageForPlanet(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> cir)
	{
		Optional<Planet> planet = PlanetUtils.getPlanetForDimension(this.level());
		if (planet.isPresent())
		{
			double gravity = planet.get().getGravityInVanillaUnits();
			if (gravity == 0d)
			{
				cir.setReturnValue(0);
			}
			else
			{
				int damage = Mth.floor(Math.floor((gravity * (fallDistance - 2)) / 0.08d) * damageMultiplier); // 0.08d is the MC gravity for Earth
				cir.setReturnValue(damage);
			}
		}
	}
}
