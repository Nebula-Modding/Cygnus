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
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.foundation.container.ContainerUtils;
import top.girlkisser.cygnus.foundation.crafting.CountedIngredient;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record RecipeSpaceStationCrafting(
	List<CountedIngredient> ingredients,
	ResourceLocation structure
) implements Recipe<InventoryRecipeInput>
{
	public static final RecipeType<RecipeSpaceStationCrafting> TYPE = RecipeType.simple(Cygnus.id("space_station_crafting"));
	public static final Serializer SERIALIZER = new Serializer();

	public boolean assemble(InventoryRecipeInput input)
	{
		for (CountedIngredient ingredient : ingredients)
		{
			if (ContainerUtils.countItems(input.inventory(), ingredient.ingredient()) >= ingredient.count())
				ContainerUtils.extractItems(input.inventory(), ingredient.ingredient(), ingredient.count());
			else
				return false;
		}
		return true;
	}

	public boolean matches(InventoryRecipeInput input)
	{
		for (CountedIngredient ingredient : ingredients)
		{
			if (ContainerUtils.countItems(input.inventory(), ingredient.ingredient()) < ingredient.count())
				return false;
		}
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
	public boolean matches(InventoryRecipeInput input, Level level)
	{
		return matches(input);
	}

	@ApiStatus.Obsolete
	@Override
	public boolean canCraftInDimensions(int width, int height)
	{
		return true;
	}

	@ApiStatus.Obsolete
	@Override
	public ItemStack assemble(InventoryRecipeInput input, HolderLookup.Provider registries)
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
			CountedIngredient.CODEC.listOf().fieldOf("ingredients").forGetter(RecipeSpaceStationCrafting::ingredients),
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
