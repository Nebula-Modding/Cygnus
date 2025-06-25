package top.girlkisser.cygnus.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import top.girlkisser.cygnus.api.space.Planet;
import top.girlkisser.cygnus.api.space.PlanetUtils;
import top.girlkisser.cygnus.config.CygnusServerConfig;

import java.util.Optional;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Shadow
	private Level level;

	@ModifyExpressionValue(method = "getGravity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getDefaultGravity()D"))
	public double modifyDefaultGravity(double originalValue)
	{
		Optional<Planet> planet = PlanetUtils.getPlanetForDimension(this.level);
		if (planet.isEmpty())
		{
			return originalValue;
		}
		else
		{
			double gravity = planet.get().getGravityInVanillaUnits();
			if (gravity == 0d && CygnusServerConfig.enableZeroGravityControl)
			{
				// Living entities (ie, players and such) can move around by crouching to descend or holding jump to ascend
				if ((Entity) (Object) this instanceof LivingEntity livingEntity)
				{
					// We do this so that landing won't cause particles. Technically it also negates fall damage, but I handle that "properly" via a NeoForge event in CygnusListeners.
					livingEntity.fallDistance = 0;
					if (livingEntity.isCrouching())
						gravity = 0.01;
					else if (((LivingEntityAccessor) livingEntity).cygnus$isJumping())
						gravity = -0.01;
				}
			}
			return gravity;
		}
	}
}
