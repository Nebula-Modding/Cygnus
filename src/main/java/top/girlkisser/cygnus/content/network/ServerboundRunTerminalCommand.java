package top.girlkisser.cygnus.content.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.content.CygnusResourceKeys;
import top.girlkisser.cygnus.content.block.BlockTerminalBE;
import top.girlkisser.cygnus.content.registry.CygnusBlockEntities;
import top.girlkisser.cygnus.content.registry.CygnusBlocks;
import top.girlkisser.cygnus.content.terminal.ITerminalCommand;
import top.girlkisser.cygnus.foundation.space.SpaceStation;
import top.girlkisser.cygnus.management.SpaceStationManager;

import java.util.Optional;

public record ServerboundRunTerminalCommand(BlockPos terminalPos, String command) implements CustomPacketPayload
{
	public static final CustomPacketPayload.Type<ServerboundRunTerminalCommand> TYPE = new CustomPacketPayload.Type<>(Cygnus.id("run_terminal_command"));

	public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundRunTerminalCommand> STREAM_CODEC = StreamCodec.composite(
		BlockPos.STREAM_CODEC, ServerboundRunTerminalCommand::terminalPos,
		ByteBufCodecs.STRING_UTF8, ServerboundRunTerminalCommand::command,
		ServerboundRunTerminalCommand::new
	);

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}

	public static void handle(ServerboundRunTerminalCommand packet, IPayloadContext context)
	{
		context.enqueueWork(() ->
		{
			ServerPlayer player = (ServerPlayer) context.player();
			MinecraftServer server = player.server;
			ServerLevel level = server.getLevel(CygnusResourceKeys.SPACE);

			if (level == null || !level.getBlockState(packet.terminalPos).is(CygnusBlocks.TERMINAL))
				return;

			Optional<BlockTerminalBE> terminal = level.getBlockEntity(packet.terminalPos, CygnusBlockEntities.TERMINAL.get());
			if (terminal.isEmpty())
				return;

			Optional<SpaceStation> spaceStation = SpaceStationManager.get(server).getClosestSpaceStationTo(level, packet.terminalPos);
			if (spaceStation.isEmpty())
				return;

			// Don't let players run commands on terminals other than their own
			if (!spaceStation.get().player().equals(player.getUUID()))
				return;

			ITerminalCommand.execute(packet.command, terminal.get(), player, spaceStation.get());

			// Send a clientbound packet to sync the terminal
			PacketDistributor.sendToPlayer(player, new ClientboundSyncTerminal(spaceStation.get()));
		});
	}
}
