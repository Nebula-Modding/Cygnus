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
import top.girlkisser.cygnus.api.CygnusRegistries;

import java.util.List;

public record Star(
	List<ResourceLocation> planets
)
{
	public static final Codec<Star> CODEC = RecordCodecBuilder.create(it -> it.group(
		ResourceLocation.CODEC.listOf().fieldOf("planets").forGetter(Star::planets)
	).apply(it, Star::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, Star> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

	public static Component getName(ResourceLocation id)
	{
		return Component.translatable(id.toLanguageKey("star"));
	}

	public static List<ResourceLocation> getStarIds(RegistryAccess registryAccess)
	{
		return registryAccess.lookupOrThrow(CygnusRegistries.STAR).listElementIds().map(ResourceKey::location).toList();
	}

	public static List<Star> getStars(RegistryAccess registryAccess)
	{
		return registryAccess.lookupOrThrow(CygnusRegistries.STAR).listElements().map(Holder.Reference::value).toList();
	}

	public static Star getStarById(RegistryAccess registryAccess, ResourceKey<Star> id)
	{
		return registryAccess.lookupOrThrow(CygnusRegistries.STAR).get(id).orElseThrow().value();
	}

	public static Star getStarById(RegistryAccess registryAccess, ResourceLocation id)
	{
		return getStarById(registryAccess, ResourceKey.create(CygnusRegistries.STAR, id));
	}
}
