package top.girlkisser.cygnus.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import top.girlkisser.cygnus.content.registry.CygnusBlocks;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockBuddingChroniteCrystal extends BlockChronite
{
	public static final int GROWTH_CHANCE = 5;

	public BlockBuddingChroniteCrystal(Properties properties)
	{
		super(properties);
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		if (random.nextInt(GROWTH_CHANCE) == 0)
		{
			Direction direction = Direction.values()[random.nextInt(Direction.values().length)];
			BlockPos budPos = pos.relative(direction);
			BlockState budState = level.getBlockState(budPos);
			Block block = null;

			if (canClusterGrowAtState(budState))
				block = CygnusBlocks.SMALL_CHRONITE_BUD.get();
			else if (budState.is(CygnusBlocks.SMALL_CHRONITE_BUD) && budState.getValue(AmethystClusterBlock.FACING) == direction)
				block = CygnusBlocks.MEDIUM_CHRONITE_BUD.get();
			else if (budState.is(CygnusBlocks.MEDIUM_CHRONITE_BUD) && budState.getValue(AmethystClusterBlock.FACING) == direction)
				block = CygnusBlocks.LARGE_CHRONITE_BUD.get();
			else if (budState.is(CygnusBlocks.LARGE_CHRONITE_BUD) && budState.getValue(AmethystClusterBlock.FACING) == direction)
				block = CygnusBlocks.CHRONITE_CLUSTER.get();

			if (block != null)
			{
				BlockState newBudState = block.defaultBlockState()
					.setValue(AmethystClusterBlock.FACING, direction)
					.setValue(AmethystClusterBlock.WATERLOGGED, budState.getFluidState().getType() == Fluids.WATER);
				level.setBlockAndUpdate(budPos, newBudState);
			}
		}
	}

	public static boolean canClusterGrowAtState(BlockState state)
	{
		return state.isAir() || state.is(Blocks.WATER) && state.getFluidState().getAmount() == 8;
	}
}
