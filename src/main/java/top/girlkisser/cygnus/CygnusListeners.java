package top.girlkisser.cygnus;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import top.girlkisser.cygnus.content.CygnusResourceKeys;
import top.girlkisser.cygnus.content.network.ClientboundSyncSpaceStation;
import top.girlkisser.cygnus.content.network.ServerboundAttemptSpaceStationConstruction;
import top.girlkisser.cygnus.content.network.ServerboundRunTerminalCommand;
import top.girlkisser.cygnus.content.registry.CygnusBlocks;
import top.girlkisser.cygnus.content.registry.CygnusDataMaps;
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
			event.registerItem(Capabilities.FluidHandler.ITEM, (stack, _void) -> ((CygnusItem) stack.getItem()).makeFluidHandler(stack), CygnusItems.OXYGEN_DRILL);
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
			if (planet.isPresent())
			{
				double gravity = planet.get().getGravityInVanillaUnits();
				// Prevent fall damage on planets with no gravity
				if (gravity == 0)
				{
					event.setCanceled(true);
					return;
				}
				// Otherwise, we multiply the fall damage by the gravity
				// The base gravity is 0.08, so to scale it properly we need to add 0.92 (that way gravity of 0.08 does not affect fall damage)
				event.setDamageMultiplier((float) gravity + 0.92f);
				// The vanilla game will ceil the damage, meaning you'll take 1 damage even when the damage is something tiny like 0.001 (which you'd see on the moon)
				// To fix this, I'll just cancel fall damage if it's low enough. Hacky? Sure, but it's probably fine :3
//				if (!event.getEntity().getType().is(EntityTypeTags.FALL_DAMAGE_IMMUNE))
//				{
//					float safeFallDistance = (float)event.getEntity().getAttributeValue(Attributes.SAFE_FALL_DISTANCE);
//					float distance = event.getDistance() - safeFallDistance;
//					double damage = (double)(distance * event.getDamageMultiplier()) * event.getEntity().getAttributeValue(Attributes.FALL_DAMAGE_MULTIPLIER);
//					if (damage < 1d)
//					{
//						event.setCanceled(true);
//					}
//				}
			}
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
