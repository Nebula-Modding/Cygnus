package top.girlkisser.cygnus.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.girlkisser.cygnus.api.space.SpaceStation;
import top.girlkisser.cygnus.content.menu.ContainerTerminal;
import top.girlkisser.cygnus.management.SpaceStationManager;
import top.girlkisser.lazuli.api.block.AbstractBlockWithEntity;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

public class BlockTerminal extends AbstractBlockWithEntity<BlockTerminalBE>
{
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public static final VoxelShape SHAPE_LOWER = Block.box(0, 0, 0, 16, 16, 16);
	public static final VoxelShape[] SHAPES_UPPER = {
		Block.box(0, 0, 8, 16, 16, 16),
		Block.box(0, 0, 0, 16, 16, 8),
		Block.box(8, 0, 0, 16, 16, 16),
		Block.box(0, 0, 0, 8, 16, 16)
	};

	public BlockTerminal(Properties properties)
	{
		super(BlockTerminalBE::new, properties);
		this.registerDefaultState(this.stateDefinition.any()
			.setValue(HALF, DoubleBlockHalf.LOWER)
			.setValue(FACING, Direction.NORTH));
	}

	@Override
	@ParametersAreNonnullByDefault
	public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if (!level.isClientSide && level.getBlockEntity(pos) instanceof BlockTerminalBE)
		{
			ServerLevel serverLevel = (ServerLevel) level;

			Optional<SpaceStation> maybeSpaceStation = SpaceStationManager
				.get(serverLevel.getServer())
				.getClosestSpaceStationTo(serverLevel, pos);

			if (maybeSpaceStation.isEmpty())
				return ItemInteractionResult.FAIL;

			var spaceStation = maybeSpaceStation.get();

			player.openMenu(
				new MenuProvider()
				{
					@Override
					public @NotNull Component getDisplayName()
					{
						return state.getBlock().getName();
					}

					@Override
					public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player)
					{
						return new ContainerTerminal(windowId, inventory, pos, spaceStation);
					}
				},
				(RegistryFriendlyByteBuf buf) ->
				{
					buf.writeBlockPos(pos);
					buf.writeBoolean(true);
					SpaceStation.STREAM_CODEC.encode(buf, spaceStation);
				}
			);
		}

		return ItemInteractionResult.SUCCESS;
	}

	@ParametersAreNonnullByDefault
	protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return state.getValue(HALF) == DoubleBlockHalf.UPPER ? SHAPES_UPPER[state.getValue(FACING).ordinal() - 2] : SHAPE_LOWER;
	}

	@ParametersAreNonnullByDefault
	protected @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos)
	{
		DoubleBlockHalf half = state.getValue(HALF);
		if (direction.getAxis() != Direction.Axis.Y || half == DoubleBlockHalf.LOWER != (direction == Direction.UP) || neighborState.is(this) && neighborState.getValue(HALF) != half)
		{
			return half == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canSurvive(level, pos) ?
				Blocks.AIR.defaultBlockState() :
				super.updateShape(state, direction, neighborState, level, pos, neighborPos);
		}
		else
		{
			return Blocks.AIR.defaultBlockState();
		}
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		BlockPos pos = context.getClickedPos();
		Level level = context.getLevel();
		return pos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(pos.above()).canBeReplaced(context) ?
			defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()) :
			null;
	}

	@Override
	protected @NotNull BlockState rotate(BlockState state, Rotation rotation)
	{
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	protected @NotNull BlockState mirror(@NotNull BlockState state, Mirror mirror)
	{
		return rotate(state, mirror.getRotation(state.getValue(FACING)));
	}

	public void setPlacedBy(Level level, BlockPos pos, @NotNull BlockState state, LivingEntity placer, @NotNull ItemStack stack)
	{
		BlockPos above = pos.above();
		level.setBlock(above, copyWaterloggedFrom(level, above, this.defaultBlockState()
			.setValue(FACING, state.getValue(FACING))
			.setValue(HALF, DoubleBlockHalf.UPPER)), Block.UPDATE_ALL);
	}

	protected boolean canSurvive(BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos)
	{
		if (state.getValue(HALF) != DoubleBlockHalf.UPPER)
		{
			return super.canSurvive(state, level, pos);
		}
		else
		{
			BlockState belowState = level.getBlockState(pos.below());
			if (state.getBlock() != this)
				return super.canSurvive(state, level, pos);
			else
				return belowState.is(this) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER;
		}
	}

	public static void placeAt(LevelAccessor level, BlockState state, BlockPos pos, int flags)
	{
		level.setBlock(pos, copyWaterloggedFrom(level, pos.above(), state.setValue(HALF, DoubleBlockHalf.LOWER)), flags);
		level.setBlock(pos.above(), copyWaterloggedFrom(level, pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER)), flags);
	}

	public static BlockState copyWaterloggedFrom(LevelReader level, BlockPos pos, BlockState state)
	{
		return state.hasProperty(BlockStateProperties.WATERLOGGED) ? state.setValue(BlockStateProperties.WATERLOGGED, level.isWaterAt(pos)) : state;
	}

	public @NotNull BlockState playerWillDestroy(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player)
	{
		if (!level.isClientSide)
		{
			if (player.isCreative())
				preventDropFromBottomPart(level, pos, state, player);
			else
				dropResources(state, level, pos, null, player, player.getMainHandItem());
		}

		return super.playerWillDestroy(level, pos, state, player);
	}

	@ParametersAreNonnullByDefault
	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool)
	{
		super.playerDestroy(level, player, pos, Blocks.AIR.defaultBlockState(), blockEntity, tool);
	}

	protected static void preventDropFromBottomPart(Level level, BlockPos pos, BlockState state, Player player)
	{
		if (state.getValue(HALF) == DoubleBlockHalf.UPPER)
		{
			BlockPos below = pos.below();
			BlockState belowState = level.getBlockState(below);
			if (belowState.is(state.getBlock()) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER)
			{
				BlockState blockstate1 = belowState.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
				level.setBlock(below, blockstate1, 35);
				level.levelEvent(player, 2001, below, Block.getId(belowState));
			}
		}
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(HALF, FACING);
	}
}
