package top.girlkisser.cygnus.datagen.client

import com.google.gson.JsonObject
import martian.dapper.api.client.CubeModel
import martian.dapper.api.client.CubeModel.Companion.all
import martian.dapper.api.client.CubeModel.Companion.down
import martian.dapper.api.client.CubeModel.Companion.up
import martian.dapper.api.client.DapperBlockStateProvider
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.IGeneratedBlockState
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.data.event.GatherDataEvent
import net.neoforged.neoforge.registries.DeferredBlock
import top.girlkisser.cygnus.Cygnus
import top.girlkisser.cygnus.Cygnus.id
import top.girlkisser.cygnus.content.block.BlockTerminal
import top.girlkisser.cygnus.content.registry.CygnusBlocks.*
import top.girlkisser.cygnus.content.registry.CygnusFluids

class CygnusBlockStateProvider(event: GatherDataEvent) : DapperBlockStateProvider(event, Cygnus.MODID)
{
	override fun registerStatesAndModels()
	{
		setOf(
//			IRON_SHEET_METAL,
//			IRON_CUT,
			STEEL_SHEET_METAL,
			STEEL_BLOCK,
			STEEL_CUT,
//			ALUMINIUM_SHEET_METAL,
//			ALUMINIUM_BLOCK,
//			ALUMINIUM_CUT,
//			TITANIUM_SHEET_METAL,
			TITANIUM_BLOCK,
//			TITANIUM_PLATING,
			TITANIUM_CUT,
			LUNAR_REGOLITH,
			LUNAR_STONE,
			LUNAR_COBBLESTONE,
			SMOOTH_LUNAR_STONE,
			CHISELED_LUNAR_STONE,
			LUNAR_STONE_BRICKS,
			CRACKED_LUNAR_STONE_BRICKS,
			LUNAR_DEEPSLATE,
			COBBLED_LUNAR_DEEPSLATE,
			SMOOTH_LUNAR_DEEPSLATE,
			CHISELED_LUNAR_DEEPSLATE,
			LUNAR_DEEPSLATE_BRICKS,
			CRACKED_LUNAR_DEEPSLATE_BRICKS,
//			ALUMINIUM_ORE,
//			DEEPSLATE_ALUMINIUM_ORE,
//			TITANIUM_ORE,
//			DEEPSLATE_TITANIUM_ORE,
			CHRONITE_BLOCK,
			BUDDING_CHRONITE,
		).forEach { it.addModel(CubeModel() all it.id.withPrefix("block/")) }

		setOf(
//			IRON_CUT_STAIRS,
			STEEL_CUT_STAIRS,
//			ALUMINIUM_CUT_STAIRS,
			TITANIUM_CUT_STAIRS,
		).forEach {
			stairsBlock(it.get() as StairBlock, it.id.withPath(it.id.withPrefix("block/").path.replace("_stairs", "")))
			simpleBlockItem(it.get(), ModelFile.UncheckedModelFile(it.id.withPrefix("block/")))
		}

		setOf(
//			IRON_CUT_SLAB,
			STEEL_CUT_SLAB,
//			ALUMINIUM_CUT_SLAB,
			TITANIUM_CUT_SLAB,
		).forEach {
			val slabTexture = it.id.withPath(it.id.withPrefix("block/").path.replace("_slab", ""))
			slabBlock(it.get() as SlabBlock, slabTexture, slabTexture)
			simpleBlockItem(it.get(), ModelFile.UncheckedModelFile(it.id.withPrefix("block/")))
		}

		setOf(
//			IRON_CUT_PRESSURE_PLATE,
			STEEL_CUT_PRESSURE_PLATE,
//			ALUMINIUM_CUT_PRESSURE_PLATE,
			TITANIUM_CUT_PRESSURE_PLATE,
		).forEach {
			pressurePlateBlock(it.get() as PressurePlateBlock, it.id.withPath(it.id.withPrefix("block/").path.replace("_pressure_plate", "")))
			simpleBlockItem(it.get(), ModelFile.UncheckedModelFile(it.id.withPrefix("block/")))
		}

		setOf(
//			IRON_CUT_BUTTON,
			STEEL_CUT_BUTTON,
//			ALUMINIUM_CUT_BUTTON,
			TITANIUM_CUT_BUTTON,
		).forEach {
			val buttonTexture = it.id.withPath(it.id.withPrefix("block/").path.replace("_button", ""))
			buttonBlock(it.get() as ButtonBlock, buttonTexture)
			models().buttonInventory(it.id.path + "_inventory", buttonTexture)
			simpleBlockItem(it.get(), ModelFile.UncheckedModelFile(it.id.withPrefix("block/").withSuffix("_inventory")))
		}

		setOf(
//			IRON_WINDOW,
			STEEL_WINDOW,
//			ALUMINIUM_WINDOW,
			TITANIUM_WINDOW,
		).forEach {
			it.addModel(CubeModel().all(it.id.withPrefix("block/")).renderType(mcLoc("translucent")))
		}

		setOf(
//			IRON_GRATE,
			STEEL_GRATE,
//			ALUMINIUM_GRATE,
//			TITANIUM_GRATE,
		).forEach {
			it.addModel(CubeModel().all(it.id.withPrefix("block/")).renderType(mcLoc("cutout")))
		}

		setOf(
//			IRON_PILLAR
			STEEL_PILLAR,
//			ALUMINIUM_PILLAR,
//			TITANIUM_PILLAR,
			LUNAR_STONE_PILLAR,
			LUNAR_DEEPSLATE_PILLAR,
		).forEach { it.addAxisModel(it.id.withPrefix("block/")) }

		setOf(
			STEEL_BARS,
//			ALUMINIUM_BARS,
//			TITANIUM_BARS,
		).forEach {
			paneBlockWithRenderType(it.get() as IronBarsBlock, it.id.withPrefix("block/"), it.id.withPrefix("block/"), "translucent")
		}

		setOf(
			CHRONITE_CLUSTER,
			LARGE_CHRONITE_BUD,
			MEDIUM_CHRONITE_BUD,
			SMALL_CHRONITE_BUD,
		).forEach {
			it.addDirectionalModel(CubeModel.crossModel(it.id.withPrefix("block/")), makeItem = false)
		}

		setOf(
//			IRON_AIRTIGHT_DOOR,
			STEEL_DOOR,
//			ALUMINIUM_DOOR,
//			TITANIUM_DOOR,
		).forEach {
			doorBlockWithRenderType(it.get() as DoorBlock, it.id.withPrefix("block/").withSuffix("_bottom"), it.id.withPrefix("block/").withSuffix("_top"), "translucent")
		}

		setOf(
//			IRON_AIRTIGHT_TRAPDOOR,
			STEEL_TRAPDOOR,
//			ALUMINIUM_TRAPDOOR,
//			TITANIUM_TRAPDOOR,
		).forEach {
			trapdoorBlockWithRenderType(it.get() as TrapDoorBlock, it.id.withPrefix("block/"), false, "translucent")
			simpleBlockItem(it.get(), ModelFile.UncheckedModelFile(it.id.withPrefix("block/").withSuffix("_bottom")))
		}

		setOf(
//			IRON_BULB,
			STEEL_BULB,
//			ALUMINIUM_BULB,
//			TITANIUM_BULB,
		).forEach {
			val idLitPowered = it.id.withPrefix("block/").withSuffix("_lit_powered")
			val idLit = it.id.withPrefix("block/").withSuffix("_lit")
			val idPowered = it.id.withPrefix("block/").withSuffix("_powered")
			val idNormal = it.id.withPrefix("block/")

			CubeModel().all(idLitPowered).build(idLitPowered.path, this.models())
			CubeModel().all(idLit).build(idLit.path, this.models())
			CubeModel().all(idPowered).build(idPowered.path, this.models())
			CubeModel().all(idNormal).build(idNormal.path, this.models())

			getVariantBuilder(it.get()).forAllStates { state ->
				val powered = state.getValue(CopperBulbBlock.POWERED)
				val lit = state.getValue(CopperBulbBlock.LIT)
				ConfiguredModel.builder()
					.modelFile(when (lit) {
						true -> when (powered) {
							true -> ModelFile.UncheckedModelFile(idLitPowered)
							false -> ModelFile.UncheckedModelFile(idLit)
						}
						false -> when (powered) {
							true -> ModelFile.UncheckedModelFile(idPowered)
							false -> ModelFile.UncheckedModelFile(idNormal)
						}
					})
					.build()
			}

			simpleBlockItem(it.get(), ModelFile.UncheckedModelFile(it.id.withPrefix("block/")))
		}

		COMMAND_CENTRE.addModel(CubeModel()
			all id("block/command_center_side")
			up id("block/command_center_top")
			down id("block/steel_block"))

		TELEPAD.addModel(ModelFile.UncheckedModelFile(id("block/telepad")))

		getVariantBuilder(TERMINAL.get()).forAllStates {
			val dir = it.getValue(HorizontalDirectionalBlock.FACING)
			val half = it.getValue(BlockTerminal.HALF)
			ConfiguredModel.builder()
				.modelFile(when (half) {
					DoubleBlockHalf.UPPER -> ModelFile.UncheckedModelFile(id("block/terminal_upper"))
					DoubleBlockHalf.LOWER -> ModelFile.UncheckedModelFile(id("block/terminal_lower"))
				})
				.rotationY((dir.toYRot().toInt() + 180) % 360)
				.build()
		}
		simpleBlockItem(TERMINAL.get(), ModelFile.UncheckedModelFile(id("block/terminal")))

		// Fluids
		blankState(CygnusFluids.OXYGEN_GAS_BLOCK)
	}

	private fun blankState(block: DeferredBlock<*>)
	{
		this.registeredBlocks[block.get()] = IGeneratedBlockState { JsonObject() }
	}
}
