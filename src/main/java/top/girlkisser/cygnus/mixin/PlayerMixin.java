package top.girlkisser.cygnus.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import top.girlkisser.cygnus.content.entity.EntityLandingBeam;

@Mixin(Player.class)
public abstract class PlayerMixin extends Entity
{
	public PlayerMixin(EntityType<?> entityType, Level level)
	{
		super(entityType, level);
		throw new UnsupportedOperationException();
	}

	@WrapOperation(method = "wantsToStopRiding", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isShiftKeyDown()Z"))
	public boolean wrapWantsToStopRiding(Player instance, Operation<Boolean> original)
	{
		// Prevent players from getting out of a landing beam so that they don't fall from ~300 blocks and die without a chance to save themselves
		return original.call(instance) && !(this.getVehicle() instanceof EntityLandingBeam);
	}
}
