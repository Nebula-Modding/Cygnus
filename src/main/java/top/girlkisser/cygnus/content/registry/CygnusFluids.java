package top.girlkisser.cygnus.content.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;
import top.girlkisser.cygnus.Cygnus;

import java.util.function.Supplier;

@SuppressWarnings("unused")
@ApiStatus.NonExtendable
public interface CygnusFluids
{
	DeferredRegister<Fluid> R = DeferredRegister.create(Registries.FLUID, Cygnus.MODID);

	private static DeferredHolder<Fluid, FlowingFluid> regSource(String id, Supplier<BaseFlowingFluid.Properties> properties)
	{
		return R.register(id, () -> new BaseFlowingFluid.Source(properties.get()));
	}

	private static DeferredHolder<Fluid, FlowingFluid> regFlowing(String id, Supplier<BaseFlowingFluid.Properties> properties)
	{
		return R.register(id, () -> new BaseFlowingFluid.Flowing(properties.get()));
	}

	private static DeferredBlock<LiquidBlock> regBlock(String id, Supplier<FlowingFluid> sourceSupplier)
	{
		return CygnusBlocks.R.register(id, () -> new LiquidBlock(sourceSupplier.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER).noLootTable()));
	}

	private static DeferredItem<BucketItem> regBucket(String id, Supplier<FlowingFluid> sourceSupplier)
	{
		return CygnusItems.R.register(id, () -> new BucketItem(sourceSupplier.get(), new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));
	}

	// Oxygen
	DeferredHolder<Fluid, FlowingFluid> OXYGEN_GAS = regSource("oxygen_gas", () -> CygnusFluids.OXYGEN_GAS_PROPERTIES);
	DeferredHolder<Fluid, FlowingFluid> OXYGEN_GAS_FLOWING = regFlowing("oxygen_gas_flowing", () -> CygnusFluids.OXYGEN_GAS_PROPERTIES);
	DeferredBlock<LiquidBlock> OXYGEN_GAS_BLOCK = regBlock("oxygen_gas", CygnusFluids.OXYGEN_GAS);
	DeferredItem<BucketItem> OXYGEN_GAS_BUCKET = regBucket("oxygen_gas_bucket", CygnusFluids.OXYGEN_GAS);

	BaseFlowingFluid.Properties OXYGEN_GAS_PROPERTIES = new BaseFlowingFluid.Properties(
		CygnusFluidTypes.OXYGEN_GAS,
		CygnusFluids.OXYGEN_GAS,
		CygnusFluids.OXYGEN_GAS_FLOWING
	)
		.slopeFindDistance(2)
		.levelDecreasePerBlock(1)
		.block(CygnusFluids.OXYGEN_GAS_BLOCK)
		.bucket(CygnusFluids.OXYGEN_GAS_BUCKET);
}
