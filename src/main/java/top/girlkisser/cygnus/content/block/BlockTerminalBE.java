package top.girlkisser.cygnus.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import top.girlkisser.cygnus.content.registry.CygnusBlockEntities;

public class BlockTerminalBE extends BlockEntity
{
	public BlockTerminalBE(BlockPos pos, BlockState blockState)
	{
		super(CygnusBlockEntities.TERMINAL.get(), pos, blockState);
	}
}
