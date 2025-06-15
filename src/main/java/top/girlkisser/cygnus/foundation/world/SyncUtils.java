package top.girlkisser.cygnus.foundation.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SyncUtils
{
	public static void sync(Level level, BlockPos pos)
	{
		BlockState state = level.getBlockState(pos);
		level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
	}

	public static void sync(BlockEntity be)
	{
		if (be.getLevel() != null)
		{
			sync(be.getLevel(), be.getBlockPos());
			be.setChanged();
		}
	}
}
