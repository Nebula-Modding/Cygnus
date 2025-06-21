package top.girlkisser.cygnus.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import top.girlkisser.cygnus.content.entity.EntityLandingBeam;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin
{
	@WrapWithCondition(method = "handleSetEntityPassengersPacket", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;setOverlayMessage(Lnet/minecraft/network/chat/Component;Z)V"))
	public boolean shouldShowDismountMessage(Gui instance, Component component, boolean animateColor, @Local(name = "entity") Entity entity)
	{
		return !(entity instanceof EntityLandingBeam);
	}

	@WrapWithCondition(method = "handleSetEntityPassengersPacket", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/GameNarrator;sayNow(Lnet/minecraft/network/chat/Component;)V"))
	public boolean shouldNarrateDismountMessage(GameNarrator instance, Component message, @Local(name = "entity") Entity entity)
	{
		return !(entity instanceof EntityLandingBeam);
	}
}
