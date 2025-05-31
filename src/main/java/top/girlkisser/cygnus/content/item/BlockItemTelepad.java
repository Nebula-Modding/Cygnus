package top.girlkisser.cygnus.content.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import top.girlkisser.cygnus.content.CygnusResourceKeys;
import top.girlkisser.cygnus.content.block.BlockTelepadBE;
import top.girlkisser.cygnus.content.registry.CygnusBlocks;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockItemTelepad extends BlockItem
{
	public BlockItemTelepad(Properties properties)
	{
		super(CygnusBlocks.TELEPAD.get(), properties);
	}

	@Override
	@ParametersAreNonnullByDefault
	protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state)
	{
		if (player != null && !level.isClientSide && !level.dimension().equals(CygnusResourceKeys.SPACE))
		{
			BlockTelepadBE.setTelepadDestination((ServerLevel) level, pos, player.getUUID());
		}

		return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
	}
}
