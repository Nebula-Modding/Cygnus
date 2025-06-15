package top.girlkisser.cygnus.content.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.content.block.BlockChroniteBlastFurnaceBE;
import top.girlkisser.cygnus.foundation.container.ContainerUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record RecipeChroniteBlasting(
	List<Ingredient> ingredients,
	int cookingTime,
	ItemStack result
) implements Recipe<ChroniteBlastingInput>
{
	public static final RecipeType<RecipeChroniteBlasting> TYPE = RecipeType.simple(Cygnus.id("chronite_blasting"));
	public static final RecipeChroniteBlasting.Serializer SERIALIZER = new RecipeChroniteBlasting.Serializer();

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return SERIALIZER;
	}

	@Override
	public RecipeType<?> getType()
	{
		return TYPE;
	}

	@Override
	public boolean matches(ChroniteBlastingInput input, Level level)
	{
		for (Ingredient ingredient : ingredients)
		{
			if (!ContainerUtils.doesListContainItemMatchingIngredient(input.stacks(), ingredient))
				return false;
		}
		return true;
	}

	@Override
	public ItemStack assemble(ChroniteBlastingInput input, HolderLookup.Provider registries)
	{
		return result.copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height)
	{
		return true;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider registries)
	{
		return result.copy();
	}

	public static class Serializer implements RecipeSerializer<RecipeChroniteBlasting>
	{
		public static final MapCodec<RecipeChroniteBlasting> CODEC = RecordCodecBuilder.mapCodec(it -> it.group(
			Ingredient.CODEC.listOf(1, 3).fieldOf("ingredients").forGetter(RecipeChroniteBlasting::ingredients),
			Codec.INT.optionalFieldOf("cookingTime", BlockChroniteBlastFurnaceBE.BURN_TIME_DEFAULT).forGetter(RecipeChroniteBlasting::cookingTime),
			ItemStack.STRICT_CODEC.fieldOf("result").forGetter(RecipeChroniteBlasting::result)
		).apply(it, RecipeChroniteBlasting::new));

		public static final StreamCodec<RegistryFriendlyByteBuf, RecipeChroniteBlasting> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

		@Override
		public MapCodec<RecipeChroniteBlasting> codec()
		{
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, RecipeChroniteBlasting> streamCodec()
		{
			return STREAM_CODEC;
		}
	}
}
