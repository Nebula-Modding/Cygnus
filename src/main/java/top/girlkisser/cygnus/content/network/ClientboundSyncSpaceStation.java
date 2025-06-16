package top.girlkisser.cygnus.content.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.client.CygnusClient;
import top.girlkisser.cygnus.client.skybox.SpaceSpecialEffects;
import top.girlkisser.cygnus.content.CygnusResourceKeys;
import top.girlkisser.cygnus.content.menu.ContainerTerminal;
import top.girlkisser.cygnus.foundation.space.SpaceStation;

public record ClientboundSyncSpaceStation(SpaceStation spaceStation) implements CustomPacketPayload
{
	public static final CustomPacketPayload.Type<ClientboundSyncSpaceStation> TYPE = new CustomPacketPayload.Type<>(Cygnus.id("sync_space_station"));

	public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncSpaceStation> STREAM_CODEC = StreamCodec.composite(
		SpaceStation.STREAM_CODEC, ClientboundSyncSpaceStation::spaceStation,
		ClientboundSyncSpaceStation::new
	);

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}

	public static void handle(ClientboundSyncSpaceStation packet, IPayloadContext context)
	{
		context.enqueueWork(() -> {
			CygnusClient.mySpaceStation = packet.spaceStation;

			if (context.player().containerMenu instanceof ContainerTerminal menu)
			{
				menu.setSpaceStation(packet.spaceStation);
			}

			if (context.player().level().dimension().equals(CygnusResourceKeys.SPACE))
			{
				SpaceSpecialEffects.invalidatePerDimensionCaches();
			}
		});
	}
}
