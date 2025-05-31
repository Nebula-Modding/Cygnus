package top.girlkisser.cygnus.datagen.client

import com.google.gson.JsonObject
import martian.dapper.api.client.CubeModel
import martian.dapper.api.client.CubeModel.Companion.all
import martian.dapper.api.client.CubeModel.Companion.down
import martian.dapper.api.client.CubeModel.Companion.up
import martian.dapper.api.client.DapperBlockStateProvider
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.IGeneratedBlockState
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.data.event.GatherDataEvent
import net.neoforged.neoforge.registries.DeferredBlock
import top.girlkisser.cygnus.Cygnus
import top.girlkisser.cygnus.Cygnus.id
import top.girlkisser.cygnus.content.block.BlockTerminal
import top.girlkisser.cygnus.content.registry.CygnusBlocks
import top.girlkisser.cygnus.content.registry.CygnusFluids

class CygnusBlockStateProvider(event: GatherDataEvent) : DapperBlockStateProvider(event, Cygnus.MODID)
{
	override fun registerStatesAndModels()
	{
		CygnusBlocks.STEEL_BLOCK.addModel(CubeModel() all id("block/steel_block"))
		CygnusBlocks.STEEL_TILES.addModel(CubeModel() all id("block/steel_tiles"))
		CygnusBlocks.STEEL_SHEET_METAL.addModel(CubeModel() all id("block/steel_sheet_metal"))

		CygnusBlocks.COMMAND_CENTRE.addModel(CubeModel()
			all id("block/command_centre_side")
			up id("block/command_centre_top")
			down id("block/steel_block"))
		CygnusBlocks.TELEPAD.addModel(ModelFile.UncheckedModelFile(id("block/telepad")))

		getVariantBuilder(CygnusBlocks.TERMINAL.get()).forAllStates {
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
		simpleBlockItem(CygnusBlocks.TERMINAL.get(), ModelFile.UncheckedModelFile(id("block/terminal")))

		blankState(CygnusFluids.OXYGEN_GAS_BLOCK)
	}

	private fun blankState(block: DeferredBlock<*>)
	{
		this.registeredBlocks[block.get()] = IGeneratedBlockState { JsonObject() }
	}
}
