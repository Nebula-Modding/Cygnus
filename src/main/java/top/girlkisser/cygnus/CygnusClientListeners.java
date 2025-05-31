package top.girlkisser.cygnus;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import top.girlkisser.cygnus.client.CygnusClient;
import top.girlkisser.cygnus.client.screen.terminal.ScreenTerminal;
import top.girlkisser.cygnus.content.entity.EntityLandingBeam;
import top.girlkisser.cygnus.content.menu.ContainerTerminal;
import top.girlkisser.cygnus.content.registry.CygnusEntityTypes;
import top.girlkisser.cygnus.content.registry.CygnusFluidTypes;
import top.girlkisser.cygnus.content.registry.CygnusMenuTypes;
import top.girlkisser.cygnus.foundation.fluid.BasicFluidType;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
class CygnusClientListeners
{
	@EventBusSubscriber(modid = Cygnus.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	static class ModEventListeners
	{
		@SubscribeEvent
		static void onClientSetup(FMLClientSetupEvent event)
		{
		}

		@SubscribeEvent
		static void registerMenus(RegisterMenuScreensEvent event)
		{
			event.register(CygnusMenuTypes.TERMINAL.get(), new MenuScreens.ScreenConstructor<ContainerTerminal, ScreenTerminal>()
			{
				@Override
				@ParametersAreNonnullByDefault
				public @NotNull ScreenTerminal create(ContainerTerminal menu, Inventory playerInventory, Component title)
				{
					return new ScreenTerminal(menu, title);
				}
			});
		}

		@SubscribeEvent
		static void registerClientExtensions(RegisterClientExtensionsEvent event)
		{
			event.registerFluidType(BasicFluidType.getClientExtensionsFor(CygnusFluidTypes.OXYGEN_GAS.get()), CygnusFluidTypes.OXYGEN_GAS);
		}

		@SubscribeEvent
		static void setup(final FMLClientSetupEvent event)
		{
			event.enqueueWork(() ->
			{
				//noinspection Convert2MethodRef
				setupEntityRenderers();
			});
		}

		private static void setupEntityRenderers()
		{
			EntityRenderers.register(CygnusEntityTypes.LANDING_BEAM.get(), context -> new EntityRenderer<>(context)
			{
				@Override
				@ParametersAreNonnullByDefault
				public @NotNull ResourceLocation getTextureLocation(EntityLandingBeam entity)
				{
					return ResourceLocation.withDefaultNamespace("missingno");
				}

				@Override
				@ParametersAreNonnullByDefault
				public boolean shouldRender(EntityLandingBeam livingEntity, Frustum camera, double camX, double camY, double camZ)
				{
					return false;
				}
			});
		}
	}

	@EventBusSubscriber(modid = Cygnus.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
	static class GameEventListeners
	{
		@SubscribeEvent
		static void onTick(ClientTickEvent.Post event)
		{
			if (!CygnusClient.isGameActive())
				return;

			CygnusClient.clientTicks++;

			// Reset scroll
			CygnusClient.mouseScrollX = 0;
			CygnusClient.mouseScrollY = 0;
		}

		@SubscribeEvent
		static void prePlayerRender(RenderPlayerEvent.Pre event)
		{
			LocalPlayer player = Minecraft.getInstance().player;
			if (player != null && player.getVehicle() instanceof EntityLandingBeam)
				event.setCanceled(true);
		}

		@SubscribeEvent
		static void postMouseButton(InputEvent.MouseButton.Post event)
		{
			if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT)
			{
				CygnusClient.isLeftMouseButtonDown = event.getAction() == InputConstants.PRESS;
			}
		}

		@SubscribeEvent
		static void onMouseScroll(InputEvent.MouseScrollingEvent event)
		{
			CygnusClient.mouseScrollX = event.getScrollDeltaX();
			CygnusClient.mouseScrollY = event.getScrollDeltaY();
		}

		// I use this instead of handling it in the screen so that I can access scroll values in `ITerminalState`s.
		// Technically I could expose a hook for it but that would imply other input hooks too, which I don't really want to implement /shrug.
		@SubscribeEvent
		static void onMouseScrollInScreen(ScreenEvent.MouseScrolled.Pre event)
		{
			CygnusClient.mouseScrollX = event.getScrollDeltaX();
			CygnusClient.mouseScrollY = event.getScrollDeltaY();
		}
	}
}
