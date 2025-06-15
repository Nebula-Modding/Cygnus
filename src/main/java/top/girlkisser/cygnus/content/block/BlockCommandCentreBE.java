package top.girlkisser.cygnus.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import top.girlkisser.cygnus.content.registry.CygnusBlockEntityTypes;

public class BlockCommandCentreBE extends BlockEntity
{
	public BlockCommandCentreBE(BlockPos pos, BlockState blockState)
	{
		super(CygnusBlockEntityTypes.COMMAND_CENTRE.get(), pos, blockState);
	}
}
