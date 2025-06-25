package top.girlkisser.cygnus.content.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import top.girlkisser.cygnus.content.menu.ContainerChroniteBlastFurnace;
import top.girlkisser.lazuli.api.block.AbstractBlockWithEntity;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockChroniteBlastFurnace extends AbstractBlockWithEntity<BlockChroniteBlastFurnaceBE>
{
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public BlockChroniteBlastFurnace(Properties properties)
	{
		super(BlockChroniteBlastFurnaceBE::new, properties);
		this.registerDefaultState(defaultBlockState()
			.setValue(FACING, Direction.NORTH)
			.setValue(LIT, false));
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if (!level.isClientSide && level.getBlockEntity(pos) instanceof BlockChroniteBlastFurnaceBE)
		{
			player.openMenu(
				new MenuProvider()
				{
					@Override
					public Component getDisplayName()
					{
						return state.getBlock().getName();
					}

					@Override
					public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player)
					{
						return new ContainerChroniteBlastFurnace(windowId, inventory, pos);
					}
				},
				buf -> buf.writeBlockPos(pos)
			);
		}

		return ItemInteractionResult.SUCCESS;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston)
	{
		if (!state.is(newState.getBlock()))
		{
			if (level.getBlockEntity(pos) instanceof BlockChroniteBlastFurnaceBE be)
			{
				if (level instanceof ServerLevel)
				{
					Containers.dropContents(level, pos, be);
				}

				super.onRemove(state, level, pos, newState, movedByPiston);
				level.updateNeighbourForOutputSignal(pos, this);
			}
			else
			{
				super.onRemove(state, level, pos, newState, movedByPiston);
			}
		}
	}

	@Override
	protected boolean hasAnalogOutputSignal(BlockState state)
	{
		return true;
	}

	@Override
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos)
	{
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rotation)
	{
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror)
	{
		return rotate(state, mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, LIT);
	}

	@Override
	@ParametersAreNonnullByDefault
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
	{
		tooltipComponents.add(Component.translatable("block.cygnus.chronite_blast_furnace.tooltip"));
	}
}
