package top.girlkisser.cygnus.content.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockSteelDoor extends BlockLockingDoor
{
	public BlockSteelDoor(BlockSetType type, Properties properties)
	{
		super(type, properties);
	}

	protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return Shapes.empty();
	}

	protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos)
	{
		return 1.0F;
	}

	protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos)
	{
		return true;
	}
}
