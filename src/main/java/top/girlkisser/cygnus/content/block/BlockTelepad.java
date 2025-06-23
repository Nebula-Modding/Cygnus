package top.girlkisser.cygnus.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.cygnus.api.space.SpaceStation;
import top.girlkisser.cygnus.content.CygnusResourceKeys;
import top.girlkisser.cygnus.content.registry.CygnusBlocks;
import top.girlkisser.cygnus.management.SpaceStationManager;
import top.girlkisser.lazuli.api.block.AbstractBlockWithEntity;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

public class BlockTelepad extends AbstractBlockWithEntity<BlockTelepadBE>
{
	public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 8, 16);

	public BlockTelepad(Properties properties)
	{
		super(BlockTelepadBE::new, properties);
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston)
	{
		super.onPlace(state, level, pos, oldState, movedByPiston);

		if (level instanceof ServerLevel serverLevel && level.dimension().equals(CygnusResourceKeys.SPACE))
		{
			SpaceStationManager manager = SpaceStationManager.get(serverLevel.getServer());
			Optional<SpaceStation> closestSpaceStation = manager.getClosestSpaceStationTo(serverLevel, pos);
			closestSpaceStation.ifPresent(spaceStation ->
			{
				spaceStation.telepads().add(pos);
				manager.setDirty();
			});
		}
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston)
	{
		super.onRemove(state, level, pos, newState, movedByPiston);

		if (level instanceof ServerLevel serverLevel && level.dimension().equals(CygnusResourceKeys.SPACE))
		{
			SpaceStationManager manager = SpaceStationManager.get(serverLevel.getServer());
			Optional<SpaceStation> closestSpaceStation = manager.getClosestSpaceStationTo(serverLevel, pos);
			closestSpaceStation.ifPresent(spaceStation ->
			{
				spaceStation.telepads().removeIf(it -> it.equals(pos));
				manager.setDirty();
			});
		}
	}

	@ParametersAreNonnullByDefault
	protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE;
	}

	public static void placeTelepadWithSpaceStationDestination(Level level, BlockPos pos, UUID spaceStationOwner)
	{
		level.setBlock(pos, CygnusBlocks.TELEPAD.get().defaultBlockState(), Block.UPDATE_CLIENTS);
		var be = level.getBlockEntity(pos);
		assert be != null;
		((BlockTelepadBE) be).setDestination(spaceStationOwner);
	}
}
