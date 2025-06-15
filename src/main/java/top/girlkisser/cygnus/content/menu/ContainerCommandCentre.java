package top.girlkisser.cygnus.content.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;
import top.girlkisser.cygnus.content.block.BlockCommandCentreBE;
import top.girlkisser.cygnus.content.registry.CygnusBlocks;
import top.girlkisser.cygnus.content.registry.CygnusMenuTypes;
import top.girlkisser.cygnus.foundation.menu.AbstractTerminalContainer;
import top.girlkisser.cygnus.foundation.space.SpaceStation;

public class ContainerCommandCentre extends AbstractTerminalContainer<BlockCommandCentreBE>
{
	public ContainerCommandCentre(int containerId, Inventory playerInventory, BlockPos pos, @Nullable SpaceStation spaceStation)
	{
		super(CygnusMenuTypes.COMMAND_CENTRE.get(), CygnusBlocks.COMMAND_CENTRE.get(), containerId, playerInventory, pos, spaceStation);
	}

	// Override to mark the function as nullable
	@Override
	public @Nullable SpaceStation getSpaceStation()
	{
		return spaceStation;
	}
}
