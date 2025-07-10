package top.girlkisser.cygnus.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.cygnus.api.space.SpaceStation;
import top.girlkisser.cygnus.content.CygnusResourceKeys;
import top.girlkisser.cygnus.content.menu.ContainerCommandCentre;
import top.girlkisser.cygnus.management.SpaceStationManager;
import top.girlkisser.lazuli.api.block.AbstractBlockWithEntity;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class BlockCommandCentre extends AbstractBlockWithEntity<BlockCommandCentreBE>
{
	public BlockCommandCentre(Properties properties)
	{
		super(BlockCommandCentreBE::new, properties);
	}

	@Override
	@ParametersAreNonnullByDefault
	public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if (!level.isClientSide && level.getBlockEntity(pos) instanceof BlockCommandCentreBE)
		{
			ServerLevel serverLevel = (ServerLevel) level;

			ServerLevel space = serverLevel.getServer().getLevel(CygnusResourceKeys.SPACE);
			assert space != null;

			SpaceStation spaceStation = SpaceStationManager
				.get(serverLevel.getServer())
				.getClosestSpaceStationTo(space, pos)
				.orElse(null);

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
						return new ContainerCommandCentre(windowId, inventory, pos, spaceStation);
					}
				},
				(RegistryFriendlyByteBuf buf) ->
				{
					buf.writeBlockPos(pos);
					buf.writeBoolean(spaceStation != null);
					if (spaceStation != null)
					{
						SpaceStation.STREAM_CODEC.encode(buf, spaceStation);
					}
				}
			);
		}

		return ItemInteractionResult.SUCCESS;
	}

	@Override
	@ParametersAreNonnullByDefault
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
	{
		tooltipComponents.add(Component.translatable("block.cygnus.command_center.tooltip"));
	}
}
