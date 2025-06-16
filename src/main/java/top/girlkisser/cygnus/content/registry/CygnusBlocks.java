package top.girlkisser.cygnus.content.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.content.CygnusSoundTypes;
import top.girlkisser.cygnus.content.block.*;
import top.girlkisser.cygnus.content.item.BlockItemTelepad;
import top.girlkisser.cygnus.foundation.colours.UnpackedColour;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.function.UnaryOperator;

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

	static <T extends Block> DeferredBlock<T> reg(String id, Function<BlockBehaviour.Properties, T> blockConstructor, BlockBehaviour.Properties properties)
	{
		return reg(id, () -> blockConstructor.apply(properties));
	}

	static DeferredBlock<Block> reg(String id, BlockBehaviour copyPropertiesFrom)
	{
		return reg(id, BlockBehaviour.Properties.ofFullCopy(copyPropertiesFrom));
	}

	static DeferredBlock<Block> reg(String id, BlockBehaviour copyPropertiesFrom, UnaryOperator<BlockBehaviour.Properties> propertiesUnaryOperator)
	{
		return reg(id, propertiesUnaryOperator.apply(BlockBehaviour.Properties.ofFullCopy(copyPropertiesFrom)));
	}

	// Helpers
	static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos, EntityType<?> entity)
	{
		return false;
	}

	static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos)
	{
		return false;
	}

	static ToIntFunction<BlockState> litBlockEmission(int lightValue)
	{
		return state -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
	}

	static BlockBehaviour.Properties copy(BlockBehaviour block)
	{
		return BlockBehaviour.Properties.ofFullCopy(block);
	}

	static BlockBehaviour.Properties of()
	{
		return BlockBehaviour.Properties.of();
	}

	static BlockBehaviour.Properties basicGlassProperties(BlockBehaviour.Properties p)
	{
		return p
			.noOcclusion()
			.isValidSpawn(CygnusBlocks::never)
			.isRedstoneConductor(CygnusBlocks::never)
			.isSuffocating(CygnusBlocks::never)
			.isViewBlocking(CygnusBlocks::never);
	}

	static BlockBehaviour.Properties basicGlassProperties()
	{
		return basicGlassProperties(of());
	}

	// @formatter:off
	DeferredBlock<?>
	// Decorational blocks
		// Steel
		STEEL_BLOCK = reg("steel_block", Blocks.IRON_BLOCK),
		STEEL_TILES = reg("steel_tiles", Blocks.IRON_BLOCK),
		STEEL_SHEET_METAL = reg("steel_sheet_metal", Blocks.IRON_BLOCK),
		STEEL_WINDOW = reg("steel_window", TransparentBlock::new, basicGlassProperties(copy(Blocks.IRON_BLOCK).sound(CygnusSoundTypes.METAL_WINDOW))),
		STEEL_DOOR = reg("steel_door", () -> new BlockSteelDoor(BlockLockingDoor.IRON_UNLOCKED, copy(Blocks.IRON_DOOR))),
		STEEL_TRAPDOOR = reg("steel_trapdoor", () -> new BlockLockingTrapdoor(BlockLockingDoor.IRON_UNLOCKED, copy(Blocks.IRON_TRAPDOOR))),
		STEEL_VENT = reg("steel_vent", TransparentBlock::new, basicGlassProperties(copy(Blocks.COPPER_GRATE).sound(CygnusSoundTypes.METAL_GRATE))),
		STEEL_PILLAR = reg("steel_pillar", RotatedPillarBlock::new, copy(Blocks.IRON_BLOCK)),
		STEEL_BULB = reg("steel_bulb", () -> new CopperBulbBlock(copy(Blocks.IRON_BLOCK)
			.isRedstoneConductor(CygnusBlocks::never)
			.lightLevel(litBlockEmission(15))
			.sound(CygnusSoundTypes.METAL_BULB))),
		STEEL_BARS = reg("steel_bars", () -> new IronBarsBlock(copy(Blocks.IRON_BARS))),
		// Aluminium
		ALUMINIUM_BLOCK = reg("aluminium_block", Blocks.IRON_BLOCK),
		ALUMINIUM_TILES = reg("aluminium_tiles", Blocks.IRON_BLOCK),
		ALUMINIUM_SHEET_METAL = reg("aluminium_sheet_metal", Blocks.IRON_BLOCK),
		// Titanium
		TITANIUM_BLOCK = reg("titanium_block", Blocks.IRON_BLOCK),
		TITANIUM_TILES = reg("titanium_tiles", Blocks.IRON_BLOCK),
		TITANIUM_SHEET_METAL = reg("titanium_sheet_metal", Blocks.IRON_BLOCK),
		TITANIUM_PLATING = reg("titanium_plating", Blocks.IRON_BLOCK),
		TITANIUM_WINDOW = reg("titanium_window", TransparentBlock::new, basicGlassProperties(copy(Blocks.IRON_BLOCK).sound(CygnusSoundTypes.METAL_WINDOW))),

	// Lunar Blocks
		// Lunar Regolith
		LUNAR_REGOLITH = reg("lunar_regolith", () -> new ColoredFallingBlock(new UnpackedColour(85, 88, 96).toColorRGBA(), copy(Blocks.SAND))),
		// Lunar Stone
		LUNAR_STONE = reg("lunar_stone", Blocks.STONE),
		LUNAR_COBBLESTONE = reg("lunar_cobblestone", Blocks.COBBLESTONE),
		SMOOTH_LUNAR_STONE = reg("smooth_lunar_stone", Blocks.SMOOTH_STONE),
		CHISELED_LUNAR_STONE = reg("chiseled_lunar_stone", Blocks.CHISELED_STONE_BRICKS),
		LUNAR_STONE_BRICKS = reg("lunar_stone_bricks", Blocks.STONE_BRICKS),
		CRACKED_LUNAR_STONE_BRICKS = reg("cracked_lunar_stone_bricks", Blocks.CRACKED_STONE_BRICKS),
		LUNAR_STONE_PILLAR = reg("lunar_stone_pillar", RotatedPillarBlock::new, copy(Blocks.STONE_BRICKS)),
		// Lunar Deepslate
		LUNAR_DEEPSLATE = reg("lunar_deepslate", Blocks.DEEPSLATE),
		COBBLED_LUNAR_DEEPSLATE = reg("cobbled_lunar_deepslate", Blocks.COBBLED_DEEPSLATE),
		SMOOTH_LUNAR_DEEPSLATE = reg("smooth_lunar_deepslate", Blocks.DEEPSLATE),
		CHISELED_LUNAR_DEEPSLATE = reg("chiseled_lunar_deepslate", Blocks.CHISELED_DEEPSLATE),
		LUNAR_DEEPSLATE_BRICKS = reg("lunar_deepslate_bricks", Blocks.DEEPSLATE_BRICKS),
		CRACKED_LUNAR_DEEPSLATE_BRICKS = reg("cracked_lunar_deepslate_bricks", Blocks.CRACKED_DEEPSLATE_BRICKS),
		LUNAR_DEEPSLATE_PILLAR = reg("lunar_deepslate_pillar", RotatedPillarBlock::new, copy(Blocks.DEEPSLATE_BRICKS)),

	// Environmental/nature blocks
		// Ores
			// Coal Ores
		LUNAR_COAL_ORE = reg("lunar_coal_ore", Blocks.COAL_ORE),
		LUNAR_DEEPSLATE_COAL_ORE = reg("lunar_deepslate_coal_ore", Blocks.DEEPSLATE_COAL_ORE),
			// Iron Ores
		LUNAR_IRON_ORE = reg("lunar_iron_ore", Blocks.IRON_ORE),
		LUNAR_DEEPSLATE_IRON_ORE = reg("lunar_deepslate_iron_ore", Blocks.DEEPSLATE_IRON_ORE),
			// Gold Ores
		LUNAR_GOLD_ORE = reg("lunar_gold_ore", Blocks.GOLD_ORE),
		LUNAR_DEEPSLATE_GOLD_ORE = reg("lunar_deepslate_gold_ore", Blocks.DEEPSLATE_GOLD_ORE),
			// Redstone Ores
		LUNAR_REDSTONE_ORE = reg("lunar_redstone_ore", RedStoneOreBlock::new, copy(Blocks.REDSTONE_ORE)),
		LUNAR_DEEPSLATE_REDSTONE_ORE = reg("lunar_deepslate_redstone_ore", RedStoneOreBlock::new, copy(Blocks.DEEPSLATE_REDSTONE_ORE)),
			// Emerald Ores
		LUNAR_EMERALD_ORE = reg("lunar_emerald_ore", Blocks.EMERALD_ORE),
		LUNAR_DEEPSLATE_EMERALD_ORE = reg("lunar_deepslate_emerald_ore", Blocks.DEEPSLATE_EMERALD_ORE),
			// Lapis Ores
		LUNAR_LAPIS_ORE = reg("lunar_lapis_ore", Blocks.LAPIS_ORE),
		LUNAR_DEEPSLATE_LAPIS_ORE = reg("lunar_deepslate_lapis_ore", Blocks.DEEPSLATE_LAPIS_ORE),
			// Diamond Ores
		LUNAR_DIAMOND_ORE = reg("lunar_diamond_ore", Blocks.DIAMOND_ORE),
		LUNAR_DEEPSLATE_DIAMOND_ORE = reg("lunar_deepslate_diamond_ore", Blocks.DEEPSLATE_DIAMOND_ORE),
			// Aluminium Ores
		ALUMINIUM_ORE = reg("aluminium_ore", Blocks.IRON_ORE),
		DEEPSLATE_ALUMINIUM_ORE = reg("deepslate_aluminium_ore", Blocks.IRON_ORE),
		LUNAR_ALUMINIUM_ORE = reg("lunar_aluminium_ore", Blocks.IRON_ORE),
		LUNAR_DEEPSLATE_ALUMINIUM_ORE = reg("lunar_deepslate_aluminium_ore", Blocks.IRON_ORE),
			// Titanium Ores
		TITANIUM_ORE = reg("titanium_ore", Blocks.GOLD_ORE),
		DEEPSLATE_TITANIUM_ORE = reg("deepslate_titanium_ore", Blocks.GOLD_ORE),
		LUNAR_TITANIUM_ORE = reg("lunar_titanium_ore", Blocks.GOLD_ORE),
		LUNAR_DEEPSLATE_TITANIUM_ORE = reg("lunar_deepslate_titanium_ore", Blocks.GOLD_ORE),
		// Chronite
		CHRONITE_BLOCK = reg("chronite_block", () -> new BlockChronite(of()
			.mapColor(MapColor.COLOR_LIGHT_GREEN)
			.strength(1.5F)
			.lightLevel(s -> 9)
			.sound(SoundType.AMETHYST)
			.requiresCorrectToolForDrops())),
		BUDDING_CHRONITE = reg("budding_chronite", () -> new BlockBuddingChroniteCrystal(of()
			.mapColor(MapColor.COLOR_LIGHT_GREEN)
			.randomTicks()
			.strength(1.5F)
			.lightLevel(s -> 9)
			.sound(SoundType.AMETHYST)
			.requiresCorrectToolForDrops()
			.pushReaction(PushReaction.DESTROY))),
		CHRONITE_CLUSTER = reg("chronite_cluster", () -> new BlockChroniteCluster(7.0F, 3.0F, of()
			.mapColor(MapColor.COLOR_LIGHT_GREEN)
			.forceSolidOn()
			.noOcclusion()
			.sound(SoundType.AMETHYST_CLUSTER)
			.strength(1.5F)
			.lightLevel(s -> 7)
			.pushReaction(PushReaction.DESTROY))),
		LARGE_CHRONITE_BUD = reg("large_chronite_bud", () -> new BlockChroniteCluster(5.0F, 3.0F, copy(CygnusBlocks.CHRONITE_CLUSTER.get())
			.sound(SoundType.MEDIUM_AMETHYST_BUD)
			.lightLevel(s -> 6))),
		MEDIUM_CHRONITE_BUD = reg("medium_chronite_bud", () -> new BlockChroniteCluster(4.0F, 3.0F, copy(CygnusBlocks.CHRONITE_CLUSTER.get())
			.sound(SoundType.LARGE_AMETHYST_BUD)
			.lightLevel(s -> 5))),
		SMALL_CHRONITE_BUD = reg("small_chronite_bud", () -> new BlockChroniteCluster(3.0F, 4.0F, copy(CygnusBlocks.CHRONITE_CLUSTER.get())
			.sound(SoundType.SMALL_AMETHYST_BUD)
			.lightLevel(s -> 4))),

	// Functional blocks
		COMMAND_CENTRE = reg("command_centre", () -> new BlockCommandCentre(copy(Blocks.IRON_BLOCK))),
		TERMINAL = reg("terminal", () -> new BlockTerminal(copy(Blocks.IRON_BLOCK).strength(-1.0F, 3600000.0F).noLootTable())),
		TELEPAD = reg("telepad",
			() -> new BlockTelepad(copy(Blocks.IRON_BLOCK)),
			() -> new BlockItemTelepad(new Item.Properties().fireResistant().rarity(Rarity.RARE))),
		CHRONITE_BLAST_FURNACE = reg("chronite_blast_furnace", () -> new BlockChroniteBlastFurnace(copy(Blocks.IRON_BLOCK)));
	// @formatter:on
}
