package top.girlkisser.cygnus.content.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.content.CygnusResourceKeys;
import top.girlkisser.cygnus.content.crafting.InventoryRecipeInput;
import top.girlkisser.cygnus.content.crafting.RecipeSpaceStationCrafting;
import top.girlkisser.cygnus.management.SpaceStationManager;

public record ServerboundAttemptSpaceStationConstruction(ResourceLocation recipeId) implements CustomPacketPayload
{
	public static final CustomPacketPayload.Type<ServerboundAttemptSpaceStationConstruction> TYPE = new CustomPacketPayload.Type<>(Cygnus.id("attempt_space_station_construction"));

	public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundAttemptSpaceStationConstruction> STREAM_CODEC = StreamCodec.composite(
		ResourceLocation.STREAM_CODEC, ServerboundAttemptSpaceStationConstruction::recipeId,
		ServerboundAttemptSpaceStationConstruction::new
	);

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}

	public static void handle(ServerboundAttemptSpaceStationConstruction packet, IPayloadContext context)
	{
		context.enqueueWork(() ->
		{
			ServerPlayer player = (ServerPlayer) context.player();
			MinecraftServer server = player.server;
			ServerLevel level = server.getLevel(CygnusResourceKeys.SPACE);

			if (level == null)
				return;

			SpaceStationManager manager = SpaceStationManager.get(server);

			if (manager.getSpaceStationForPlayer(player).isPresent())
				return;

			var maybeHolder = level.getRecipeManager().byKey(packet.recipeId);
			if (maybeHolder.isEmpty())
				return;

			var recipe = (RecipeSpaceStationCrafting) (maybeHolder.get().value());

			boolean didMakeSpaceStation = false;
			// We can ignore actually crafting the recipe when the crafter is in creative mode.
			if (player.isCreative())
			{
				manager.createSpaceStationForPlayer(player, recipe.structure());
				didMakeSpaceStation = true;
			}
			else
			{
				var input = new InventoryRecipeInput(player.getInventory());
				if (recipe.matches(input) && recipe.assemble(input))
				{
					player.inventoryMenu.broadcastChanges();
					manager.createSpaceStationForPlayer(player, recipe.structure());
					didMakeSpaceStation = true;
				}
			}

			if (didMakeSpaceStation)
			{
				var spaceStation = manager.getSpaceStationForPlayer(player);
				if (spaceStation.isEmpty())
				{
					// Something is wrong
					Cygnus.LOGGER.error("Created space station but failed to get it after creation. This error should never occur, please report it!");
					return;
				}
				// Send a clientbound packet to sync the terminal
				PacketDistributor.sendToPlayer(player, new ClientboundSyncTerminal(spaceStation.get()));
			}
		});
	}
}
