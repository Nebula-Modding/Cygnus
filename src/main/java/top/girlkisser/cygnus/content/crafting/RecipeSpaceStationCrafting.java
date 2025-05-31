package top.girlkisser.cygnus.content.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;
import top.girlkisser.cygnus.Cygnus;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record RecipeSpaceStationCrafting(
	List<Ingredient> ingredients,
	ResourceLocation structure
) implements Recipe<IngredientListRecipeInput>
{
	public static final RecipeType<RecipeSpaceStationCrafting> TYPE = RecipeType.simple(Cygnus.id("space_station_crafting"));
	public static final Serializer SERIALIZER = new Serializer();

	@Override
	public boolean matches(IngredientListRecipeInput input, Level level)
	{
		for (int i = 0 ; i < ingredients.size() ; i++)
			if (!ingredients.get(i).test(input.getItem(i)))
				return false;
		return true;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height)
	{
		return true;
	}

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

	@ApiStatus.Obsolete
	@Override
	public ItemStack assemble(IngredientListRecipeInput input, HolderLookup.Provider registries)
	{
		return ItemStack.EMPTY;
	}

	@ApiStatus.Obsolete
	@Override
	public ItemStack getResultItem(HolderLookup.Provider registries)
	{
		return ItemStack.EMPTY;
	}

	public static class Serializer implements RecipeSerializer<RecipeSpaceStationCrafting>
	{
		public static final MapCodec<RecipeSpaceStationCrafting> CODEC = RecordCodecBuilder.mapCodec(it -> it.group(
			Ingredient.LIST_CODEC.fieldOf("ingredients").forGetter(RecipeSpaceStationCrafting::ingredients),
			ResourceLocation.CODEC.fieldOf("structure").forGetter(RecipeSpaceStationCrafting::structure)
		).apply(it, RecipeSpaceStationCrafting::new));

		public static final StreamCodec<RegistryFriendlyByteBuf, RecipeSpaceStationCrafting> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

		@Override
		public MapCodec<RecipeSpaceStationCrafting> codec()
		{
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, RecipeSpaceStationCrafting> streamCodec()
		{
			return STREAM_CODEC;
		}
	}
}
