package top.girlkisser.cygnus.content.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import top.girlkisser.cygnus.content.registry.CygnusItems;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockLockingDoor extends DoorBlock
{
	public static final BlockSetType IRON_UNLOCKED = new BlockSetType(
		BlockSetType.IRON.name(),
		true,
		BlockSetType.IRON.canOpenByWindCharge(),
		BlockSetType.IRON.canButtonBeActivatedByArrows(),
		BlockSetType.IRON.pressurePlateSensitivity(),
		BlockSetType.IRON.soundType(),
		BlockSetType.IRON.doorClose(),
		BlockSetType.IRON.doorOpen(),
		BlockSetType.IRON.trapdoorClose(),
		BlockSetType.IRON.trapdoorOpen(),
		BlockSetType.IRON.pressurePlateClickOff(),
		BlockSetType.IRON.pressurePlateClickOn(),
		BlockSetType.IRON.buttonClickOff(),
		BlockSetType.IRON.buttonClickOn()
	);

	public static final BooleanProperty LOCKED = BooleanProperty.create("door_locked");

	public BlockLockingDoor(BlockSetType type, Properties properties)
	{
		super(type, properties);
		this.registerDefaultState(defaultBlockState().setValue(LOCKED, false));
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
	{
		return state.getValue(LOCKED) ? InteractionResult.PASS : super.useWithoutItem(state, level, pos, player, hitResult);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
	{
		return stack.is(CygnusItems.KEY) ?
			ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION :
			ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(LOCKED);
	}

	@Override
	@ParametersAreNonnullByDefault
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
	{
		tooltipComponents.add(Component.translatable("messages.cygnus.door_lockable"));
	}
}
