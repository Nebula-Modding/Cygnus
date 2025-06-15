package top.girlkisser.cygnus.content.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.neoforged.neoforge.items.SlotItemHandler;
import top.girlkisser.cygnus.content.block.BlockChroniteBlastFurnaceBE;
import top.girlkisser.cygnus.content.registry.CygnusBlocks;
import top.girlkisser.cygnus.content.registry.CygnusMenuTypes;
import top.girlkisser.cygnus.foundation.menu.AbstractBlockEntityContainer;
import top.girlkisser.cygnus.foundation.menu.SlotOutputOnly;

public class ContainerChroniteBlastFurnace extends AbstractBlockEntityContainer<BlockChroniteBlastFurnaceBE>
{
	protected int litTicksRemaining, totalLitTicksForFuel, craftProgress, craftDuration;

	public ContainerChroniteBlastFurnace(int containerId, Inventory playerInventory, BlockPos pos)
	{
		super(
			CygnusMenuTypes.CHRONITE_BLAST_FURNACE.get(),
			CygnusBlocks.CHRONITE_BLAST_FURNACE.get(),
			BlockChroniteBlastFurnaceBE.NUM_SLOTS,
			containerId,
			playerInventory,
			pos
		);

		addDataSlot(new DataSlot()
		{
			@Override
			public int get()
			{
				return blockEntity.litTicksRemaining;
			}

			@Override
			public void set(int value)
			{
				litTicksRemaining = value;
			}
		});

		addDataSlot(new DataSlot()
		{
			@Override
			public int get()
			{
				return blockEntity.totalLitTicksForFuel;
			}

			@Override
			public void set(int value)
			{
				totalLitTicksForFuel = value;
			}
		});

		addDataSlot(new DataSlot()
		{
			@Override
			public int get()
			{
				return blockEntity.craftProgress;
			}

			@Override
			public void set(int value)
			{
				craftProgress = value;
			}
		});

		addDataSlot(new DataSlot()
		{
			@Override
			public int get()
			{
				return blockEntity.craftDuration;
			}

			@Override
			public void set(int value)
			{
				craftDuration = value;
			}
		});

		addSlotRange(blockEntity, 0, 28, 17, 3, 18);
		addSlot(new SlotItemHandler(blockEntity.getInventory(), BlockChroniteBlastFurnaceBE.SLOT_FUEL, 46, 53));
		addSlot(new SlotOutputOnly(blockEntity, BlockChroniteBlastFurnaceBE.SLOT_RESULT, 124, 35));

		addPlayerInventorySlots(playerInventory, 8, 84);
	}

	public int getLitTicksRemaining()
	{
		return litTicksRemaining;
	}

	public int getTotalLitTicksForFuel()
	{
		return totalLitTicksForFuel;
	}

	public int getCraftProgress()
	{
		return craftProgress;
	}

	public int getCraftDuration()
	{
		return craftDuration;
	}
}
