package top.girlkisser.cygnus.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
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
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.client.particle.ChroniteParticleProvider;
import top.girlkisser.cygnus.client.particle2d.Particles2D;
import top.girlkisser.cygnus.client.screen.ScreenChroniteBlastFurnace;
import top.girlkisser.cygnus.client.screen.command_centre.CommandCentreStateMainMenu;
import top.girlkisser.cygnus.client.screen.terminal.ScreenTerminal;
import top.girlkisser.cygnus.client.skybox.PlanetRendererLoader;
import top.girlkisser.cygnus.client.skybox.SpaceSpecialEffects;
import top.girlkisser.cygnus.client.starmap.StarmapGalaxyConfigLoader;
import top.girlkisser.cygnus.client.starmap.StarmapPlanetConfigLoader;
import top.girlkisser.cygnus.client.starmap.StarmapStarConfigLoader;
import top.girlkisser.cygnus.config.CygnusClientConfig;
import top.girlkisser.cygnus.content.entity.EntityLandingBeam;
import top.girlkisser.cygnus.content.menu.ContainerChroniteBlastFurnace;
import top.girlkisser.cygnus.content.menu.ContainerCommandCentre;
import top.girlkisser.cygnus.content.menu.ContainerTerminal;
import top.girlkisser.cygnus.content.registry.*;
import top.girlkisser.cygnus.foundation.client.particle2d.ScreenParticleEngine2D;
import top.girlkisser.cygnus.foundation.fluid.BasicFluidType;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ApiStatus.Internal
public class CygnusClientListeners
{
	@EventBusSubscriber(modid = Cygnus.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ModEventListeners
	{
		@SubscribeEvent
		static void onClientSetup(FMLClientSetupEvent event)
		{
		}

		@SubscribeEvent
		static void registerMenus(RegisterMenuScreensEvent event)
		{
			event.register(CygnusMenuTypes.TERMINAL.get(), new MenuScreens.ScreenConstructor<ContainerTerminal, ScreenTerminal<ContainerTerminal>>()
			{
				@Override
				@ParametersAreNonnullByDefault
				public @NotNull ScreenTerminal<ContainerTerminal> create(ContainerTerminal menu, Inventory playerInventory, Component title)
				{
					return new ScreenTerminal<>(menu, title);
				}
			});

			event.register(CygnusMenuTypes.COMMAND_CENTRE.get(), new MenuScreens.ScreenConstructor<ContainerCommandCentre, ScreenTerminal<ContainerCommandCentre>>()
			{
				@Override
				@ParametersAreNonnullByDefault
				public @NotNull ScreenTerminal<ContainerCommandCentre> create(ContainerCommandCentre menu, Inventory playerInventory, Component title)
				{
					return new ScreenTerminal<>(menu, title, CommandCentreStateMainMenu::new);
				}
			});

			event.register(CygnusMenuTypes.CHRONITE_BLAST_FURNACE.get(), new MenuScreens.ScreenConstructor<ContainerChroniteBlastFurnace, ScreenChroniteBlastFurnace>()
			{
				@Override
				@ParametersAreNonnullByDefault
				public @NotNull ScreenChroniteBlastFurnace create(ContainerChroniteBlastFurnace menu, Inventory playerInventory, Component title)
				{
					return new ScreenChroniteBlastFurnace(menu, playerInventory, title);
				}
			});
		}

		@SubscribeEvent
		static void registerClientExtensions(RegisterClientExtensionsEvent event)
		{
			event.registerFluidType(BasicFluidType.getClientExtensionsFor(CygnusFluidTypes.OXYGEN_GAS.get()), CygnusFluidTypes.OXYGEN_GAS);
		}

		@SubscribeEvent
		static void registerParticleProviders(RegisterParticleProvidersEvent event)
		{
			event.registerSpriteSet(CygnusParticleTypes.CHRONITE.get(), ChroniteParticleProvider::new);
		}

		@SubscribeEvent
		static void registerDimensionSpecialEffects(RegisterDimensionSpecialEffectsEvent event)
		{
			event.register(Cygnus.id("space"), new SpaceSpecialEffects());
		}

		@SubscribeEvent
		static void onRegisterResourcePackRegistries(RegisterClientReloadListenersEvent event)
		{
			event.registerReloadListener(new PlanetRendererLoader());
			event.registerReloadListener(new StarmapGalaxyConfigLoader());
			event.registerReloadListener(new StarmapStarConfigLoader());
			event.registerReloadListener(new StarmapPlanetConfigLoader());
		}

		@SubscribeEvent
		static void setup(final FMLClientSetupEvent event)
		{
			//noinspection CodeBlock2Expr
			event.enqueueWork(() ->
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
			});
		}
	}

	@EventBusSubscriber(modid = Cygnus.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
	public static class GameEventListeners
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

		@SubscribeEvent
		static void postScreenRender(ScreenEvent.Render.Post event)
		{
			if (!CygnusClientConfig.enable2dParticles)
			{
				return;
			}

			if (
				event.getScreen() instanceof MenuAccess<?> menuAccess &&
				menuAccess.getMenu().getCarried().is(CygnusItems.CHRONITE) &&
				CygnusClient.RANDOM.nextFloat() >= .5f
			)
			{
				ScreenParticleEngine2D.INSTANCE.addParticle(Particles2D.makeChroniteParticle(
					event.getMouseX() - 4,
					event.getMouseY() - 4,
					(CygnusClient.RANDOM.nextFloat() - .5f) / 1.5f,
					CygnusClient.RANDOM.nextFloat() - .5f,
					0f,
					.0175f,
					CygnusClient.RANDOM.nextIntBetweenInclusive(5, 10) / 10f,
					60
				));
			}

			ScreenParticleEngine2D.INSTANCE.render(event.getGuiGraphics(), event.getPartialTick());
		}

		@SubscribeEvent
		static void onScreenClose(ScreenEvent.Closing event)
		{
			if (!CygnusClientConfig.enable2dParticles)
			{
				ScreenParticleEngine2D.INSTANCE.clear();
			}
		}

		@SubscribeEvent
		static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event)
		{
			SpaceSpecialEffects.invalidatePerDimensionCaches();
		}
	}
}
