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
import net.minecraft.world.level.block.state.properties.BlockSetType;
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
import top.girlkisser.lazuli.api.colour.UnpackedColour;

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

	// Order: Sheet Metal, Block, Chiselled, Grate, Cut, Stairs, Slabs, Pressure Plate, Button, Window, Pillar, Bars, Door, Trapdoor, Bulb

	// @formatter:off
	DeferredBlock<?>
	// Raw Blocks
		RAW_ALUMINIUM_BLOCK = reg("raw_aluminum_block", copy(Blocks.RAW_IRON_BLOCK).mapColor(MapColor.QUARTZ)),
		RAW_TITANIUM_BLOCK = reg("raw_titanium_block", copy(Blocks.RAW_IRON_BLOCK).mapColor(MapColor.CLAY)),

	// Decorational blocks
		// Iron
		IRON_SHEET_METAL = reg("iron_sheet_metal", Blocks.IRON_BLOCK),
		IRON_CHISELLED = reg("chiseled_iron", Blocks.IRON_BLOCK),
		IRON_GRATE = reg("iron_grate", WaterloggedTransparentBlock::new, basicGlassProperties(copy(Blocks.COPPER_GRATE).sound(CygnusSoundTypes.METAL_GRATE))),
		IRON_CUT = reg("cut_iron", Blocks.IRON_BLOCK),
		IRON_CUT_STAIRS = reg("cut_iron_stairs", () -> new StairBlock(CygnusBlocks.IRON_CUT.get().defaultBlockState(), copy(Blocks.IRON_BLOCK))),
		IRON_CUT_SLAB = reg("cut_iron_slab", () -> new SlabBlock(copy(Blocks.IRON_BLOCK))),
		IRON_CUT_PRESSURE_PLATE = reg("cut_iron_pressure_plate", () -> new PressurePlateBlock(BlockSetType.IRON, copy(Blocks.IRON_BLOCK).noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY))),
		IRON_CUT_BUTTON = reg("cut_iron_button", () -> new ButtonBlock(BlockSetType.IRON, 20, copy(Blocks.IRON_BLOCK).noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY))),
		IRON_WINDOW = reg("iron_window", TransparentBlock::new, basicGlassProperties(copy(Blocks.IRON_BLOCK).sound(CygnusSoundTypes.METAL_WINDOW))),
		IRON_PILLAR = reg("iron_pillar", RotatedPillarBlock::new, copy(Blocks.IRON_BLOCK)),
		IRON_AIRTIGHT_DOOR = reg("airtight_iron_door", () -> new BlockLockingDoor(BlockLockingDoor.IRON_UNLOCKED, copy(Blocks.IRON_DOOR))),
		IRON_AIRTIGHT_TRAPDOOR = reg("airtight_iron_trapdoor", () -> new BlockLockingTrapdoor(BlockLockingDoor.IRON_UNLOCKED, copy(Blocks.IRON_TRAPDOOR))),
		IRON_BULB = reg("iron_bulb", () -> new CopperBulbBlock(copy(Blocks.IRON_BLOCK)
			.isRedstoneConductor(CygnusBlocks::never)
			.lightLevel(litBlockEmission(15))
			.sound(CygnusSoundTypes.METAL_BULB))),
		// Steel
		STEEL_SHEET_METAL = reg("steel_sheet_metal", copy(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_BLACK)),
		STEEL_BLOCK = reg("steel_block", copy(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_BLACK)),
		STEEL_CHISELLED = reg("chiseled_steel", copy(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_BLACK)),
		STEEL_GRATE = reg("steel_grate", WaterloggedTransparentBlock::new, basicGlassProperties(copy(Blocks.COPPER_GRATE).sound(CygnusSoundTypes.METAL_GRATE).mapColor(MapColor.COLOR_BLACK))),
		STEEL_CUT = reg("cut_steel", copy(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_BLACK)),
		STEEL_CUT_STAIRS = reg("cut_steel_stairs", () -> new StairBlock(CygnusBlocks.STEEL_CUT.get().defaultBlockState(), copy(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_BLACK))),
		STEEL_CUT_SLAB = reg("cut_steel_slab", () -> new SlabBlock(copy(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_BLACK))),
		STEEL_CUT_PRESSURE_PLATE = reg("cut_steel_pressure_plate", () -> new PressurePlateBlock(BlockSetType.IRON, copy(Blocks.IRON_BLOCK).noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY).mapColor(MapColor.COLOR_BLACK))),
		STEEL_CUT_BUTTON = reg("cut_steel_button", () -> new ButtonBlock(BlockSetType.IRON, 20, copy(Blocks.IRON_BLOCK).noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY).mapColor(MapColor.COLOR_BLACK))),
		STEEL_WINDOW = reg("steel_window", TransparentBlock::new, basicGlassProperties(copy(Blocks.IRON_BLOCK).sound(CygnusSoundTypes.METAL_WINDOW).mapColor(MapColor.COLOR_BLACK))),
		STEEL_PILLAR = reg("steel_pillar", RotatedPillarBlock::new, copy(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_BLACK)),
		STEEL_BARS = reg("steel_bars", () -> new IronBarsBlock(copy(Blocks.IRON_BARS).mapColor(MapColor.COLOR_BLACK))),
		STEEL_DOOR = reg("steel_door", () -> new BlockLockingDoor(BlockLockingDoor.IRON_UNLOCKED, copy(Blocks.IRON_DOOR).mapColor(MapColor.COLOR_BLACK))),
		STEEL_TRAPDOOR = reg("steel_trapdoor", () -> new BlockLockingTrapdoor(BlockLockingDoor.IRON_UNLOCKED, copy(Blocks.IRON_TRAPDOOR).mapColor(MapColor.COLOR_BLACK))),
		STEEL_BULB = reg("steel_bulb", () -> new CopperBulbBlock(copy(Blocks.IRON_BLOCK)
			.isRedstoneConductor(CygnusBlocks::never)
			.lightLevel(litBlockEmission(15))
			.sound(CygnusSoundTypes.METAL_BULB)
			.mapColor(MapColor.COLOR_BLACK))),
		// Aluminium
		ALUMINIUM_SHEET_METAL = reg("aluminum_sheet_metal", copy(Blocks.IRON_BLOCK).mapColor(MapColor.QUARTZ)),
		ALUMINIUM_BLOCK = reg("aluminum_block", copy(Blocks.IRON_BLOCK).mapColor(MapColor.QUARTZ)),
		ALUMINIUM_CHISELLED = reg("chiseled_aluminum", copy(Blocks.IRON_BLOCK).mapColor(MapColor.QUARTZ)),
		ALUMINIUM_GRATE = reg("aluminum_grate", WaterloggedTransparentBlock::new, basicGlassProperties(copy(Blocks.COPPER_GRATE).sound(CygnusSoundTypes.METAL_GRATE).mapColor(MapColor.QUARTZ))),
		ALUMINIUM_CUT = reg("cut_aluminum", copy(Blocks.IRON_BLOCK).mapColor(MapColor.QUARTZ)),
		ALUMINIUM_CUT_STAIRS = reg("cut_aluminum_stairs", () -> new StairBlock(CygnusBlocks.ALUMINIUM_CUT.get().defaultBlockState(), copy(Blocks.IRON_BLOCK).mapColor(MapColor.QUARTZ))),
		ALUMINIUM_CUT_SLAB = reg("cut_aluminum_slab", () -> new SlabBlock(copy(Blocks.IRON_BLOCK).mapColor(MapColor.QUARTZ))),
		ALUMINIUM_CUT_PRESSURE_PLATE = reg("cut_aluminum_pressure_plate", () -> new PressurePlateBlock(BlockSetType.IRON, copy(Blocks.IRON_BLOCK).noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY).mapColor(MapColor.QUARTZ))),
		ALUMINIUM_CUT_BUTTON = reg("cut_aluminum_button", () -> new ButtonBlock(BlockSetType.IRON, 20, copy(Blocks.IRON_BLOCK).noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY).mapColor(MapColor.QUARTZ))),
		ALUMINIUM_WINDOW = reg("aluminum_window", TransparentBlock::new, basicGlassProperties(copy(Blocks.IRON_BLOCK).sound(CygnusSoundTypes.METAL_WINDOW).mapColor(MapColor.QUARTZ))),
		ALUMINIUM_PILLAR = reg("aluminum_pillar", RotatedPillarBlock::new, copy(Blocks.IRON_BLOCK).mapColor(MapColor.QUARTZ)),
		ALUMINIUM_BARS = reg("aluminum_bars", () -> new IronBarsBlock(copy(Blocks.IRON_BARS).mapColor(MapColor.QUARTZ))),
		ALUMINIUM_DOOR = reg("aluminum_door", () -> new BlockLockingDoor(BlockLockingDoor.IRON_UNLOCKED, copy(Blocks.IRON_DOOR).mapColor(MapColor.QUARTZ))),
		ALUMINIUM_TRAPDOOR = reg("aluminum_trapdoor", () -> new BlockLockingTrapdoor(BlockLockingDoor.IRON_UNLOCKED, copy(Blocks.IRON_TRAPDOOR).mapColor(MapColor.QUARTZ))),
		ALUMINIUM_BULB = reg("aluminum_bulb", () -> new CopperBulbBlock(copy(Blocks.IRON_BLOCK)
			.isRedstoneConductor(CygnusBlocks::never)
			.lightLevel(litBlockEmission(15))
			.sound(CygnusSoundTypes.METAL_BULB)
			.mapColor(MapColor.QUARTZ))),
		// Titanium
		TITANIUM_SHEET_METAL = reg("titanium_sheet_metal", copy(Blocks.IRON_BLOCK).mapColor(MapColor.CLAY)),
		TITANIUM_BLOCK = reg("titanium_block", copy(Blocks.IRON_BLOCK).mapColor(MapColor.CLAY)),
		TITANIUM_CHISELLED = reg("chiseled_titanium", copy(Blocks.IRON_BLOCK).mapColor(MapColor.CLAY)),
		TITANIUM_GRATE = reg("titanium_grate", WaterloggedTransparentBlock::new, basicGlassProperties(copy(Blocks.COPPER_GRATE).sound(CygnusSoundTypes.METAL_GRATE).mapColor(MapColor.CLAY))),
		TITANIUM_CUT = reg("cut_titanium", copy(Blocks.IRON_BLOCK).mapColor(MapColor.CLAY)),
		TITANIUM_CUT_STAIRS = reg("cut_titanium_stairs", () -> new StairBlock(CygnusBlocks.TITANIUM_CUT.get().defaultBlockState(), copy(Blocks.IRON_BLOCK).mapColor(MapColor.CLAY))),
		TITANIUM_CUT_SLAB = reg("cut_titanium_slab", () -> new SlabBlock(copy(Blocks.IRON_BLOCK).mapColor(MapColor.CLAY))),
		TITANIUM_CUT_PRESSURE_PLATE = reg("cut_titanium_pressure_plate", () -> new PressurePlateBlock(BlockSetType.IRON, copy(Blocks.IRON_BLOCK).noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY).mapColor(MapColor.CLAY))),
		TITANIUM_CUT_BUTTON = reg("cut_titanium_button", () -> new ButtonBlock(BlockSetType.IRON, 20, copy(Blocks.IRON_BLOCK).noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY).mapColor(MapColor.CLAY))),
		TITANIUM_WINDOW = reg("titanium_window", TransparentBlock::new, basicGlassProperties(copy(Blocks.IRON_BLOCK).sound(CygnusSoundTypes.METAL_WINDOW).mapColor(MapColor.CLAY))),
		TITANIUM_PILLAR = reg("titanium_pillar", RotatedPillarBlock::new, copy(Blocks.IRON_BLOCK).mapColor(MapColor.CLAY)),
		TITANIUM_BARS = reg("titanium_bars", () -> new IronBarsBlock(copy(Blocks.IRON_BARS).mapColor(MapColor.CLAY))),
		TITANIUM_DOOR = reg("titanium_door", () -> new BlockLockingDoor(BlockLockingDoor.IRON_UNLOCKED, copy(Blocks.IRON_DOOR).mapColor(MapColor.CLAY))),
		TITANIUM_TRAPDOOR = reg("titanium_trapdoor", () -> new BlockLockingTrapdoor(BlockLockingDoor.IRON_UNLOCKED, copy(Blocks.IRON_TRAPDOOR).mapColor(MapColor.CLAY))),
		TITANIUM_BULB = reg("titanium_bulb", () -> new CopperBulbBlock(copy(Blocks.IRON_BLOCK)
			.isRedstoneConductor(CygnusBlocks::never)
			.lightLevel(litBlockEmission(15))
			.sound(CygnusSoundTypes.METAL_BULB)
			.mapColor(MapColor.CLAY))),

	// Lunar Blocks
		// Lunar Regolith
		LUNAR_REGOLITH = reg("lunar_regolith", () -> new ColoredFallingBlock(new UnpackedColour(85, 88, 96).toColorRGBA(), copy(Blocks.SAND).mapColor(MapColor.STONE))),
		// Lunar Stone
		LUNAR_STONE = reg("lunar_stone", copy(Blocks.STONE).mapColor(MapColor.TERRACOTTA_CYAN)),
		LUNAR_COBBLESTONE = reg("lunar_cobblestone", copy(Blocks.COBBLESTONE).mapColor(MapColor.TERRACOTTA_CYAN)),
		POLISHED_LUNAR_STONE = reg("polished_lunar_stone", copy(Blocks.SMOOTH_STONE).mapColor(MapColor.TERRACOTTA_CYAN)),
		CHISELED_LUNAR_STONE = reg("chiseled_lunar_stone", copy(Blocks.CHISELED_STONE_BRICKS).mapColor(MapColor.TERRACOTTA_CYAN)),
		LUNAR_STONE_BRICKS = reg("lunar_stone_bricks", copy(Blocks.STONE_BRICKS).mapColor(MapColor.TERRACOTTA_CYAN)),
		CRACKED_LUNAR_STONE_BRICKS = reg("cracked_lunar_stone_bricks", copy(Blocks.CRACKED_STONE_BRICKS).mapColor(MapColor.TERRACOTTA_CYAN)),
		LUNAR_STONE_PILLAR = reg("lunar_stone_pillar", RotatedPillarBlock::new, copy(Blocks.STONE_BRICKS).mapColor(MapColor.TERRACOTTA_CYAN)),
		// Lunar Deepslate
		LUNAR_DEEPSLATE = reg("lunar_deepslate", copy(Blocks.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)),
		COBBLED_LUNAR_DEEPSLATE = reg("cobbled_lunar_deepslate", copy(Blocks.COBBLED_DEEPSLATE).mapColor(MapColor.COLOR_GRAY)),
		POLISHED_LUNAR_DEEPSLATE = reg("polished_lunar_deepslate", copy(Blocks.POLISHED_DEEPSLATE).mapColor(MapColor.COLOR_GRAY)),
		CHISELED_LUNAR_DEEPSLATE = reg("chiseled_lunar_deepslate", copy(Blocks.CHISELED_DEEPSLATE).mapColor(MapColor.COLOR_GRAY)),
		LUNAR_DEEPSLATE_BRICKS = reg("lunar_deepslate_bricks", copy(Blocks.DEEPSLATE_BRICKS).mapColor(MapColor.COLOR_GRAY)),
		CRACKED_LUNAR_DEEPSLATE_BRICKS = reg("cracked_lunar_deepslate_bricks", copy(Blocks.CRACKED_DEEPSLATE_BRICKS).mapColor(MapColor.COLOR_GRAY)),
		LUNAR_DEEPSLATE_PILLAR = reg("lunar_deepslate_pillar", RotatedPillarBlock::new, copy(Blocks.DEEPSLATE_BRICKS).mapColor(MapColor.COLOR_GRAY)),

	// Environmental/nature blocks
		// Ores
			// Coal Ores
		LUNAR_COAL_ORE = reg("lunar_coal_ore", Blocks.COAL_ORE),
		LUNAR_DEEPSLATE_COAL_ORE = reg("lunar_deepslate_coal_ore", Blocks.COAL_ORE),
			// Iron Ores
		LUNAR_IRON_ORE = reg("lunar_iron_ore", Blocks.IRON_ORE),
		LUNAR_DEEPSLATE_IRON_ORE = reg("lunar_deepslate_iron_ore", Blocks.DEEPSLATE_IRON_ORE),
			// Copper Ores
		LUNAR_COPPER_ORE = reg("lunar_copper_ore", Blocks.COPPER_ORE),
		LUNAR_DEEPSLATE_COPPER_ORE = reg("lunar_deepslate_copper_ore", Blocks.DEEPSLATE_COPPER_ORE),
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
		ALUMINIUM_ORE = reg("aluminum_ore", Blocks.IRON_ORE),
		DEEPSLATE_ALUMINIUM_ORE = reg("deepslate_aluminum_ore", Blocks.IRON_ORE),
		LUNAR_ALUMINIUM_ORE = reg("lunar_aluminum_ore", Blocks.IRON_ORE),
		LUNAR_DEEPSLATE_ALUMINIUM_ORE = reg("lunar_deepslate_aluminum_ore", Blocks.IRON_ORE),
			// Titanium Ores
		TITANIUM_ORE = reg("titanium_ore", Blocks.GOLD_ORE),
		DEEPSLATE_TITANIUM_ORE = reg("deepslate_titanium_ore", Blocks.GOLD_ORE),
		LUNAR_TITANIUM_ORE = reg("lunar_titanium_ore", Blocks.GOLD_ORE),
		LUNAR_DEEPSLATE_TITANIUM_ORE = reg("lunar_deepslate_titanium_ore", Blocks.GOLD_ORE),
		// Chronite
		CHRONITE_BLOCK = reg("chronite_block", () -> new BlockChronite(of()
			.mapColor(MapColor.GRASS)
			.strength(1.5F)
			.lightLevel(s -> 9)
			.sound(SoundType.AMETHYST)
			.requiresCorrectToolForDrops())),
		BUDDING_CHRONITE = reg("budding_chronite", () -> new BlockBuddingChroniteCrystal(of()
			.mapColor(MapColor.GRASS)
			.randomTicks()
			.strength(1.5F)
			.lightLevel(s -> 9)
			.sound(SoundType.AMETHYST)
			.requiresCorrectToolForDrops()
			.pushReaction(PushReaction.DESTROY))),
		CHRONITE_CLUSTER = reg("chronite_cluster", () -> new BlockChroniteCluster(7.0F, 3.0F, of()
			.mapColor(MapColor.GRASS)
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
		COMMAND_CENTRE = reg("command_center", () -> new BlockCommandCentre(copy(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_BLACK))),
		TERMINAL = reg("terminal", () -> new BlockTerminal(copy(Blocks.IRON_BLOCK).strength(1.0F, 3600000.0F).mapColor(MapColor.COLOR_BLACK))),
		TELEPAD = reg("telepad",
			() -> new BlockTelepad(copy(Blocks.IRON_BLOCK).mapColor(MapColor.EMERALD)),
			() -> new BlockItemTelepad(new Item.Properties().fireResistant().rarity(Rarity.RARE))),
		CHRONITE_BLAST_FURNACE = reg("chronite_blast_furnace", () -> new BlockChroniteBlastFurnace(copy(Blocks.IRON_BLOCK).mapColor(MapColor.CLAY)));
	// @formatter:on
}
