package top.girlkisser.cygnus.foundation.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;

public record CountedIngredient(Ingredient ingredient, int count)
{
	public static final Codec<CountedIngredient> CODEC = RecordCodecBuilder.create(it -> it.group(
		Ingredient.CODEC.fieldOf("ingredient").forGetter(CountedIngredient::ingredient),
		Codec.INT.fieldOf("count").forGetter(CountedIngredient::count)
	).apply(it, CountedIngredient::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, CountedIngredient> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);
}
