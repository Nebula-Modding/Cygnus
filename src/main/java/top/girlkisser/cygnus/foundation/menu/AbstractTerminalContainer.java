package top.girlkisser.cygnus.foundation.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import top.girlkisser.cygnus.foundation.space.SpaceStation;

public class AbstractTerminalContainer<T extends BlockEntity> extends AbstractBlockEntityContainer<T>
{
	protected SpaceStation spaceStation;

	public AbstractTerminalContainer(MenuType<?> menuType, Block block, int containerId, Inventory playerInventory, BlockPos pos, SpaceStation spaceStation)
	{
		super(menuType, block, 0, containerId, playerInventory, pos);
		this.spaceStation = spaceStation;
	}

	public SpaceStation getSpaceStation()
	{
		return spaceStation;
	}

	public void setSpaceStation(SpaceStation value)
	{
		spaceStation = value;
	}
}
