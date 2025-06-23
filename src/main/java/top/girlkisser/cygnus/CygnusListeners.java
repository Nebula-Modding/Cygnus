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
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import top.girlkisser.cygnus.api.CygnusRegistries;
import top.girlkisser.cygnus.api.space.*;
import top.girlkisser.cygnus.content.CygnusResourceKeys;
import top.girlkisser.cygnus.content.network.ClientboundSyncSpaceStation;
import top.girlkisser.cygnus.content.network.ServerboundAttemptSpaceStationConstruction;
import top.girlkisser.cygnus.content.network.ServerboundRunTerminalCommand;
import top.girlkisser.cygnus.content.registry.CygnusBlocks;
import top.girlkisser.cygnus.content.registry.CygnusDataMaps;
import top.girlkisser.cygnus.content.registry.CygnusItems;
import top.girlkisser.cygnus.management.SpaceStationManager;
import top.girlkisser.lazuli.api.item.LazuliItem;

import java.util.Optional;

final class CygnusListeners
{
	@EventBusSubscriber(modid = Cygnus.MODID, bus = EventBusSubscriber.Bus.MOD)
	static class ModEventListeners
	{
		@SubscribeEvent
		static void onRegisterComponents(RegisterCapabilitiesEvent event)
		{
			event.registerItem(Capabilities.FluidHandler.ITEM, (stack, _void) -> ((LazuliItem) stack.getItem()).makeFluidHandler(stack), CygnusItems.OXYGEN_DRILL);
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
			registrar.playToServer(ServerboundAttemptSpaceStationConstruction.TYPE, ServerboundAttemptSpaceStationConstruction.STREAM_CODEC, ServerboundAttemptSpaceStationConstruction::handle);

			registrar.playToClient(ClientboundSyncSpaceStation.TYPE, ClientboundSyncSpaceStation.STREAM_CODEC, ClientboundSyncSpaceStation::handle);
		}

		@SubscribeEvent
		static void onRegisterDataMapTypes(RegisterDataMapTypesEvent event)
		{
			event.register(CygnusDataMaps.CHRONITE_BLAST_FURNACE_FUELS);
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
		static void onSyncDatapacks(OnDatapackSyncEvent event)
		{
			PlanetUtils.clearCache();
		}

		@SubscribeEvent
		static void onBlockBreak(BlockEvent.BreakEvent event)
		{
			if (event.getState().is(CygnusBlocks.TELEPAD))
			{
				ServerPlayer serverPlayer = (ServerPlayer) event.getPlayer();
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

		@SubscribeEvent
		static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event)
		{
			if (!event.getEntity().level().isClientSide && event.getTo().equals(CygnusResourceKeys.SPACE))
			{
				ServerPlayer serverPlayer = (ServerPlayer) event.getEntity();
				SpaceStationManager manager = SpaceStationManager.get(serverPlayer.server);
				manager.getSpaceStationForPlayer(serverPlayer)
					.ifPresent(spaceStation -> spaceStation.sync((ServerLevel) serverPlayer.level()));
			}
		}

		@SubscribeEvent
		static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
		{
			if (!event.getEntity().level().isClientSide)
			{
				ServerPlayer serverPlayer = (ServerPlayer) event.getEntity();
				SpaceStationManager manager = SpaceStationManager.get(serverPlayer.server);
				manager.getSpaceStationForPlayer(serverPlayer)
					.ifPresent(spaceStation -> spaceStation.sync((ServerLevel) serverPlayer.level()));
			}
		}
	}
}
