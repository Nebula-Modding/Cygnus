package top.girlkisser.cygnus.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor
{
	@Accessor(value = "jumping")
	boolean cygnus$isJumping();
}
