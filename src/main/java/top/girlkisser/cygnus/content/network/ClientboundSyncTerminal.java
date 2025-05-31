package top.girlkisser.cygnus.content.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.content.menu.ContainerTerminal;
import top.girlkisser.cygnus.foundation.space.SpaceStation;

public record ClientboundSyncTerminal(SpaceStation spaceStation) implements CustomPacketPayload
{
	public static final CustomPacketPayload.Type<ClientboundSyncTerminal> TYPE = new CustomPacketPayload.Type<>(Cygnus.id("sync_terminal"));

	public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncTerminal> STREAM_CODEC = StreamCodec.composite(
		SpaceStation.STREAM_CODEC, ClientboundSyncTerminal::spaceStation,
		ClientboundSyncTerminal::new
	);

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}

	public static void handle(ClientboundSyncTerminal packet, IPayloadContext context)
	{
		context.enqueueWork(() ->
		{
			if (context.player().containerMenu instanceof ContainerTerminal menu)
			{
				menu.spaceStation = packet.spaceStation;
			}
		});
	}
}
