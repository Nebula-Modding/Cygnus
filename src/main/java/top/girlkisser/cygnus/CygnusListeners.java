package top.girlkisser.cygnus;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import top.girlkisser.cygnus.content.CygnusResourceKeys;
import top.girlkisser.cygnus.content.block.BlockTelepadBE;
import top.girlkisser.cygnus.content.network.ClientboundSyncTerminal;
import top.girlkisser.cygnus.content.network.ServerboundRunTerminalCommand;
import top.girlkisser.cygnus.content.registry.CygnusBlockEntities;
import top.girlkisser.cygnus.content.registry.CygnusBlocks;
import top.girlkisser.cygnus.content.registry.CygnusItems;
import top.girlkisser.cygnus.foundation.CygnusRegistries;
import top.girlkisser.cygnus.foundation.TickScheduler;
import top.girlkisser.cygnus.foundation.item.CygnusItem;
import top.girlkisser.cygnus.foundation.space.*;
import top.girlkisser.cygnus.management.SpaceStationManager;

import java.util.Optional;

final class CygnusListeners
{
	@EventBusSubscriber(modid = Cygnus.MODID, bus = EventBusSubscriber.Bus.MOD)
	static class ModEventListeners
	{
		@SubscribeEvent
		static void onRegisterComponents(RegisterCapabilitiesEvent event)
		{
			event.registerItem(Capabilities.FluidHandler.ITEM, (stack, _void) -> ((CygnusItem)stack.getItem()).makeFluidHandler(stack), CygnusItems.OXYGEN_DRILL);
		}

		@SubscribeEvent
		static void onRegisterDataPackRegistries(DataPackRegistryEvent.NewRegistry event)
		{
			event.dataPackRegistry(CygnusRegistries.GALAXY, Galaxy.CODEC, Galaxy.CODEC);
			event.dataPackRegistry(CygnusRegistries.STAR, Star.CODEC, Star.CODEC);
			event.dataPackRegistry(CygnusRegistries.PLANET, Planet.CODEC, Planet.CODEC);
		}

		@SubscribeEvent
		static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event)
		{
			PayloadRegistrar registrar = event.registrar("1");

			registrar.playToServer(ServerboundRunTerminalCommand.TYPE, ServerboundRunTerminalCommand.STREAM_CODEC, ServerboundRunTerminalCommand::handle);

			registrar.playToClient(ClientboundSyncTerminal.TYPE, ClientboundSyncTerminal.STREAM_CODEC, ClientboundSyncTerminal::handle);
		}
	}

	@EventBusSubscriber(modid = Cygnus.MODID, bus = EventBusSubscriber.Bus.GAME)
	static class GameEventListeners
	{
		@SubscribeEvent
		static void onServerStarting(ServerStartingEvent event)
		{
		}

		@SubscribeEvent
		static void onServerTick(ServerTickEvent.Post event)
		{
			TickScheduler.SERVER.tick();
		}

		@SubscribeEvent
		static void onSyncDatapacks(OnDatapackSyncEvent event)
		{
			PlanetUtils.clearCache();
		}

		@SubscribeEvent
		static void onLivingEntityFall(LivingFallEvent event)
		{
			Optional<Planet> planet = PlanetUtils.getPlanetForDimension(event.getEntity().level());
			// Prevent fall damage on planets with no gravity
			if (planet.isPresent() && planet.get().getGravityInVanillaUnits() == 0)
			{
				event.setCanceled(true);
			}
		}

//		@SubscribeEvent
//		static void onPlayerPlaceBlock(BlockEvent.EntityPlaceEvent event)
//		{
//			if (event.getEntity() instanceof ServerPlayer player && !((ServerLevel)event.getLevel()).dimension().equals(CygnusResourceKeys.SPACE))
//			{
//				event.getLevel()
//					.getBlockEntity(event.getPos(), CygnusBlockEntities.TELEPAD.get())
//					.ifPresent(it -> it.setDestination(player.getUUID()));
//			}
//		}

		@SubscribeEvent
		static void onBlockBreak(BlockEvent.BreakEvent event)
		{
			if (event.getState().is(CygnusBlocks.TELEPAD))
			{
				ServerPlayer serverPlayer = (ServerPlayer)event.getPlayer();
				SpaceStationManager manager = SpaceStationManager.get(serverPlayer.server);
				Optional<SpaceStation> maybeSpaceStation = manager.getClosestSpaceStationTo(serverPlayer.serverLevel(), event.getPos());
				// If this is the only telepad left on a space station, don't let players destroy it
				if (maybeSpaceStation.isPresent() && maybeSpaceStation.get().telepads().size() <= 1)
				{
					serverPlayer.sendSystemMessage(Component.translatable("message.cygnus.cannot_remove_last_telepad").withStyle(ChatFormatting.RED));
					event.setCanceled(true);
				}
			}
		}
	}
}
