package top.girlkisser.cygnus.content.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.cygnus.content.block.BlockLockingDoor;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemKey extends Item
{
	public ItemKey(Properties properties)
	{
		super(properties);
	}

	@Override
	public @NotNull InteractionResult useOn(@NotNull UseOnContext context)
	{
		BlockState state = context.getLevel().getBlockState(context.getClickedPos());
		if (state.hasProperty(BlockLockingDoor.LOCKED))
		{
			state = state.setValue(BlockLockingDoor.LOCKED, !state.getValue(BlockLockingDoor.LOCKED));
			context.getLevel().setBlock(context.getClickedPos(), state, Block.UPDATE_ALL);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	@ParametersAreNonnullByDefault
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
	{
		tooltipComponents.add(Component.translatable("item.cygnus.key.tooltip.1"));
		tooltipComponents.add(Component.translatable("item.cygnus.key.tooltip.2"));
	}
}
