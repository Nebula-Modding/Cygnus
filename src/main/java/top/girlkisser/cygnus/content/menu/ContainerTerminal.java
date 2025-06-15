package top.girlkisser.cygnus.content.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import top.girlkisser.cygnus.content.block.BlockTerminalBE;
import top.girlkisser.cygnus.content.registry.CygnusBlocks;
import top.girlkisser.cygnus.content.registry.CygnusMenuTypes;
import top.girlkisser.cygnus.foundation.menu.AbstractTerminalContainer;
import top.girlkisser.cygnus.foundation.space.SpaceStation;

public class ContainerTerminal extends AbstractTerminalContainer<BlockTerminalBE>
{
	public ContainerTerminal(int containerId, Inventory playerInventory, BlockPos pos, SpaceStation spaceStation)
	{
		super(CygnusMenuTypes.TERMINAL.get(), CygnusBlocks.TERMINAL.get(), containerId, playerInventory, pos, spaceStation);
	}
}
