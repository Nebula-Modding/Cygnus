package top.girlkisser.cygnus.api.space;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.api.CygnusRegistries;

import java.util.List;

public record Galaxy(
	ResourceLocation terminalIcon,
	ResourceLocation terminalIconHover,
	ResourceLocation mapTexture,
	List<ResourceLocation> stars
)
{
	public static final Codec<Galaxy> CODEC = RecordCodecBuilder.create(it -> it.group(
		ResourceLocation.CODEC.optionalFieldOf("terminal_icon", Cygnus.id("terminal/galaxy")).forGetter(Galaxy::terminalIcon),
		ResourceLocation.CODEC.optionalFieldOf("terminal_icon_hover", Cygnus.id("terminal/galaxy_selected")).forGetter(Galaxy::terminalIconHover),
		ResourceLocation.CODEC.optionalFieldOf("map_texture", Cygnus.id("galaxy")).forGetter(Galaxy::mapTexture),
		ResourceLocation.CODEC.listOf().fieldOf("stars").forGetter(Galaxy::stars)
	).apply(it, Galaxy::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, Galaxy> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

	public static Component getName(ResourceLocation galaxyId)
	{
		return Component.translatable(galaxyId.toLanguageKey("galaxy"));
	}

	public static List<ResourceLocation> getGalaxyIds(RegistryAccess registryAccess)
	{
		return registryAccess.lookupOrThrow(CygnusRegistries.GALAXY).listElementIds().map(ResourceKey::location).toList();
	}

	public static List<Galaxy> getGalaxies(RegistryAccess registryAccess)
	{
		return registryAccess.lookupOrThrow(CygnusRegistries.GALAXY).listElements().map(Holder.Reference::value).toList();
	}

	public static Galaxy getGalaxyById(RegistryAccess registryAccess, ResourceKey<Galaxy> id)
	{
		return registryAccess.lookupOrThrow(CygnusRegistries.GALAXY).get(id).orElseThrow().value();
	}

	public static Galaxy getGalaxyById(RegistryAccess registryAccess, ResourceLocation id)
	{
		return getGalaxyById(registryAccess, ResourceKey.create(CygnusRegistries.GALAXY, id));
	}
}
