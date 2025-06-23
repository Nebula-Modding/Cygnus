package top.girlkisser.cygnus.content.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import top.girlkisser.cygnus.content.crafting.ChroniteBlastingInput;
import top.girlkisser.cygnus.content.crafting.RecipeChroniteBlasting;
import top.girlkisser.cygnus.content.registry.CygnusBlockEntityTypes;
import top.girlkisser.cygnus.content.registry.CygnusDataMaps;
import top.girlkisser.cygnus.content.registry.CygnusRecipeTypes;
import top.girlkisser.lazuli.api.block.AbstractInventoryBE;
import top.girlkisser.lazuli.api.block.ITickableBE;
import top.girlkisser.lazuli.api.collections.ArrayHelpers;
import top.girlkisser.lazuli.api.inventory.ContainerUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockChroniteBlastFurnaceBE extends AbstractInventoryBE implements ITickableBE
{
	public static final int
		SLOT_INPUT_A = 0,
		SLOT_INPUT_B = 1,
		SLOT_INPUT_C = 2,
		SLOT_FUEL = 3,
		SLOT_RESULT = 4,
		NUM_SLOTS = 5;
	public static final int[]
		INPUT_SLOT_INDEXES = new int[]{ SLOT_INPUT_A, SLOT_INPUT_B, SLOT_INPUT_C },
		FUEL_SLOT_INDEXES = new int[]{ SLOT_FUEL },
		OUTPUT_SLOT_INDEXES = new int[]{ SLOT_RESULT };
	public static final int
		DATA_LIT_TICKS_REMAINING = 0,
		DATA_TOTAL_LIT_TICKS_FOR_FUEL = 1,
		DATA_CRAFT_PROGRESS = 2,
		DATA_CRAFT_DURATION = 3,
		NUM_DATA_VALUES = 4;
	public static final int
		BURN_TIME_DEFAULT = 200,
		BURN_COOL_SPEED = 2;

	public int litTicksRemaining, totalLitTicksForFuel, craftProgress, craftDuration;
	public @Nullable RecipeHolder<RecipeChroniteBlasting> crafting = null;

	protected final ContainerData dataAccess;
	protected final RecipeManager.CachedCheck<ChroniteBlastingInput, RecipeChroniteBlasting> quickCheck;

	public BlockChroniteBlastFurnaceBE(BlockPos pos, BlockState blockState)
	{
		super(CygnusBlockEntityTypes.CHRONITE_BLAST_FURNACE.get(), NUM_SLOTS, pos, blockState);

		this.dataAccess = new ContainerData()
		{
			@Override
			public int get(int index)
			{
				return switch (index)
				{
					case DATA_LIT_TICKS_REMAINING -> litTicksRemaining;
					case DATA_TOTAL_LIT_TICKS_FOR_FUEL -> totalLitTicksForFuel;
					case DATA_CRAFT_PROGRESS -> craftProgress;
					case DATA_CRAFT_DURATION -> craftDuration;
					default -> 0;
				};
			}

			@Override
			public void set(int index, int value)
			{
				switch (index)
				{
					case DATA_LIT_TICKS_REMAINING:
						litTicksRemaining = value;
					case DATA_TOTAL_LIT_TICKS_FOR_FUEL:
						totalLitTicksForFuel = value;
					case DATA_CRAFT_PROGRESS:
						craftProgress = value;
					case DATA_CRAFT_DURATION:
						craftDuration = value;
				}
			}

			@Override
			public int getCount()
			{
				return NUM_DATA_VALUES;
			}
		};

		this.quickCheck = RecipeManager.createCheck(CygnusRecipeTypes.CHRONITE_BLASTING.get());
	}

	public ItemStack getFirstInputStack()
	{
		return this.getItem(SLOT_INPUT_A);
	}

	public ItemStack getSecondInputStack()
	{
		return this.getItem(SLOT_INPUT_B);
	}

	public ItemStack getThirdInputStack()
	{
		return this.getItem(SLOT_INPUT_C);
	}

	public ChroniteBlastingInput getRecipeInput()
	{
		return new ChroniteBlastingInput(List.of(getFirstInputStack(), getSecondInputStack(), getThirdInputStack()));
	}

	public ItemStack getFuelStack()
	{
		return this.getItem(SLOT_FUEL);
	}

	public ItemStack getResultStack()
	{
		return this.getItem(SLOT_RESULT);
	}

	@Override
	public void serverTick(ServerLevel level)
	{
		boolean changed = false;

		if (litTicksRemaining > 0)
		{
			litTicksRemaining--;
			if (crafting != null && ++craftProgress >= craftDuration)
				craft();
			changed = true;
		}
		else if (isValidFuel(getFuelStack()) && getRecipeOrNull() != null && attemptToConsumeFuel())
			changed = true;

		if (crafting == null && checkForRecipe())
			changed = true;

		if (litTicksRemaining <= 0 && craftProgress > 0)
		{
			craftProgress = Mth.clamp(BURN_COOL_SPEED, 0, craftDuration);
			if (craftProgress <= 0 && craftDuration != 0)
			{
				craftDuration = 0;
				crafting = null;
			}
			changed = true;
		}

		boolean shouldBeLit = litTicksRemaining > 0;
		if (getBlockState().getValue(BlockChroniteBlastFurnace.LIT) != shouldBeLit)
			level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BlockChroniteBlastFurnace.LIT, shouldBeLit));

		if (changed)
			setChanged();
	}

	protected @Nullable RecipeHolder<RecipeChroniteBlasting> getRecipeOrNull()
	{
		assert level != null;
		ChroniteBlastingInput input = getRecipeInput();
		return input.isEmpty() ? null : quickCheck.getRecipeFor(input, level).orElse(null);
	}

	protected boolean checkForRecipe()
	{
		assert level != null;

		ItemStack fuel = getFuelStack();

		// If the CBF isn't lit and has no fuel then we can't craft anything, so we'll return now
		if (litTicksRemaining <= 0 && fuel.isEmpty())
			return false;

		var recipe = getRecipeOrNull();

		if (recipe == null)
			return false;

		if (litTicksRemaining <= 0 && !attemptToConsumeFuel())
			return false;

		this.crafting = recipe;
		this.craftProgress = 0;
		this.craftDuration = recipe.value().cookingTime();
		return true;
	}

	protected boolean attemptToConsumeFuel()
	{
		ItemStack fuel = getFuelStack();
		if (fuel.isEmpty())
			return false;

		this.litTicksRemaining = getBurnTimeOrZero(fuel);
		this.totalLitTicksForFuel = litTicksRemaining;
		boolean wasFuelValid = litTicksRemaining > 0;
		if (wasFuelValid)
			fuel.shrink(1);
		return wasFuelValid;
	}

	protected void craft()
	{
		if (this.crafting == null)
			return;

		for (Ingredient ingredient : this.crafting.value().ingredients())
			ContainerUtils.extractItemsForCrafting(this, ingredient, 1);

		ItemStack recipeResult = this.crafting.value().result().copy();
		ItemStack currentResultStack = getResultStack();

		if (currentResultStack.isEmpty())
			setItem(SLOT_RESULT, recipeResult);
		else if (ItemStack.isSameItemSameComponents(recipeResult, currentResultStack))
			currentResultStack.grow(recipeResult.getCount());

		this.craftDuration = 0;
		this.craftProgress = 0;
		this.crafting = null;

		// Immediately check for another recipe so that we don't waste a tick of fuel in between crafts finishing and starting
		checkForRecipe();
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
	{
		super.saveAdditional(tag, registries);
		tag.putInt("LitTicks", litTicksRemaining);
		tag.putInt("TotalLitTicks", totalLitTicksForFuel);
		tag.putInt("CraftProgress", craftProgress);
		tag.putInt("CraftDuration", craftDuration);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
	{
		super.loadAdditional(tag, registries);
		checkForRecipe();
		this.litTicksRemaining = tag.getInt("LitTicks");
		this.totalLitTicksForFuel = tag.getInt("TotalLitTicks");
		this.craftProgress = tag.getInt("CraftProgress");
		this.craftDuration = tag.getInt("CraftDuration");
	}

	@Override
	public ItemStackHandler makeItemStackHandler()
	{
		return new ItemStackHandler(slots)
		{
			@Override
			public void onContentsChanged(int slot)
			{
				BlockChroniteBlastFurnaceBE.this.setChanged();
				if (slot == SLOT_INPUT_A || slot == SLOT_INPUT_B || slot == SLOT_INPUT_C)
				{
					// If the recipe is no longer valid, nullify it
					if (!BlockChroniteBlastFurnaceBE.this.checkForRecipe())
					{
						assert level != null;
						BlockChroniteBlastFurnaceBE.this.craftDuration = getTotalCookTime(level, BlockChroniteBlastFurnaceBE.this);
						BlockChroniteBlastFurnaceBE.this.craftProgress = 0;
						BlockChroniteBlastFurnaceBE.this.crafting = null;
					}
				}
			}


		};
	}

	@Override
	public int[] getSlotsForFace(Direction side)
	{
		return switch (side)
		{
			case Direction.UP -> INPUT_SLOT_INDEXES;
			case Direction.DOWN -> OUTPUT_SLOT_INDEXES;
			default -> FUEL_SLOT_INDEXES;
		};
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction)
	{
		return direction == null || ArrayHelpers.contains(getSlotsForFace(direction), index);
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
	{
		return ArrayHelpers.contains(getSlotsForFace(direction), index);
	}

	protected static boolean isValidFuel(ItemStack stack)
	{
		return getBurnTime(stack) != null;
	}

	protected static @Nullable Integer getBurnTime(ItemStack stack)
	{
		return stack.getItemHolder().getData(CygnusDataMaps.CHRONITE_BLAST_FURNACE_FUELS);
	}

	protected static int getBurnTimeOrZero(ItemStack stack)
	{
		return Objects.requireNonNullElse(getBurnTime(stack), 0);
	}

	private static int getTotalCookTime(Level level, BlockChroniteBlastFurnaceBE be)
	{
		return be.quickCheck
			.getRecipeFor(be.getRecipeInput(), level)
			.map(it -> it.value().cookingTime())
			.orElse(BURN_TIME_DEFAULT);
	}
}
