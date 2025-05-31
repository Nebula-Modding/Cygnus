package top.girlkisser.cygnus.content.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.content.block.BlockCommandCentre;
import top.girlkisser.cygnus.content.block.BlockTelepad;
import top.girlkisser.cygnus.content.block.BlockTerminal;
import top.girlkisser.cygnus.content.item.BlockItemTelepad;

import java.util.function.Supplier;

@SuppressWarnings("unused")
@ApiStatus.NonExtendable
public interface CygnusBlocks
{
	DeferredRegister.Blocks R = DeferredRegister.createBlocks(Cygnus.MODID);

	static <T extends Block> DeferredBlock<T> reg(String id, Supplier<T> block, Supplier<BlockItem> blockItem)
	{
		DeferredBlock<T> entry = R.register(id, block);
		CygnusItems.R.register(id, blockItem);
		return entry;
	}

	static <T extends Block> DeferredBlock<T> reg(String id, Supplier<T> block)
	{
		DeferredBlock<T> entry = R.register(id, block);
		CygnusItems.R.register(id, () -> new BlockItem(entry.get(), new Item.Properties()));
		return entry;
	}

	static DeferredBlock<Block> reg(String id, BlockBehaviour.Properties properties)
	{
		return reg(id, () -> new Block(properties));
	}

	static DeferredBlock<Block> reg(String id, BlockBehaviour copyPropertiesFrom)
	{
		return reg(id, BlockBehaviour.Properties.ofFullCopy(copyPropertiesFrom));
	}

	DeferredBlock<?>
		// Decorational blocks
		STEEL_BLOCK = reg("steel_block", Blocks.IRON_BLOCK),
		STEEL_TILES = reg("steel_tiles", Blocks.IRON_BLOCK),
		STEEL_SHEET_METAL = reg("steel_sheet_metal", Blocks.IRON_BLOCK),

		// Functional blocks
		COMMAND_CENTRE = reg("command_centre", () -> new BlockCommandCentre(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK))),
		TERMINAL = reg("terminal", () -> new BlockTerminal(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(-1.0F, 3600000.0F).noLootTable())),
		TELEPAD = reg("telepad",
			() -> new BlockTelepad(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)),
			() -> new BlockItemTelepad(new Item.Properties().fireResistant().rarity(Rarity.RARE)));
}
