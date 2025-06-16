package top.girlkisser.cygnus.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor
{
	@Accessor(value = "jumping")
	boolean cygnus$isJumping();

	@Invoker(value = "calculateFallDamage")
	int cygnus$calculateFallDamage(float fallDistance, float damageMultiplier);
}
