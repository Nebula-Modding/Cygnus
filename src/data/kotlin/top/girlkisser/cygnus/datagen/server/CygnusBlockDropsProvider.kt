package top.girlkisser.cygnus.datagen.server

import martian.dapper.api.server.DapperLootTableProvider
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay
import net.minecraft.world.level.storage.loot.functions.LootItemFunction
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.MatchTool
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider
import net.neoforged.neoforge.data.event.GatherDataEvent
import top.girlkisser.cygnus.content.registry.CygnusBlocks.*
import top.girlkisser.cygnus.content.registry.CygnusItems

class CygnusBlockDropsProvider(event: GatherDataEvent) : DapperLootTableProvider(
	event,
	listOf(
		SubProviderEntry({ BlockLoot(it) }, LootContextParamSets.BLOCK)
	)
)
{
	private class BlockLoot(registries: HolderLookup.Provider) : Companion.BlockProvider(registries)
	{
		override fun generate()
		{
			// Drops self
			setOf(
				RAW_ALUMINIUM_BLOCK,
				RAW_TITANIUM_BLOCK,

				IRON_SHEET_METAL,
				IRON_GRATE,
				IRON_CUT,
				IRON_WINDOW,
				IRON_PILLAR,
				IRON_AIRTIGHT_TRAPDOOR,
				IRON_BULB,

				STEEL_SHEET_METAL,
				STEEL_BLOCK,
				STEEL_GRATE,
				STEEL_CUT,
				STEEL_WINDOW,
				STEEL_PILLAR,
				STEEL_BARS,
				STEEL_TRAPDOOR,
				STEEL_BULB,

				ALUMINIUM_SHEET_METAL,
				ALUMINIUM_BLOCK,
				ALUMINIUM_GRATE,
				ALUMINIUM_CUT,
				ALUMINIUM_WINDOW,
				ALUMINIUM_PILLAR,
				ALUMINIUM_BARS,
				ALUMINIUM_TRAPDOOR,
				ALUMINIUM_BULB,

				TITANIUM_SHEET_METAL,
				TITANIUM_BLOCK,
				TITANIUM_GRATE,
				TITANIUM_CUT,
				TITANIUM_WINDOW,
				TITANIUM_PILLAR,
				TITANIUM_BARS,
				TITANIUM_TRAPDOOR,
				TITANIUM_BULB,

				LUNAR_REGOLITH,
				LUNAR_COBBLESTONE,
				POLISHED_LUNAR_STONE,
				CHISELED_LUNAR_STONE,
				LUNAR_STONE_BRICKS,
				CRACKED_LUNAR_STONE_BRICKS,
				LUNAR_STONE_PILLAR,
				COBBLED_LUNAR_DEEPSLATE,
				POLISHED_LUNAR_DEEPSLATE,
				CHISELED_LUNAR_DEEPSLATE,
				LUNAR_DEEPSLATE_BRICKS,
				CRACKED_LUNAR_DEEPSLATE_BRICKS,
				LUNAR_DEEPSLATE_PILLAR,
				CHRONITE_BLOCK,

				COMMAND_CENTRE,
				TERMINAL,
				TELEPAD,
				CHRONITE_BLAST_FURNACE,
			).forEach {
				dropSelf(it.get())
			}

			// Door drops
			setOf(
				IRON_AIRTIGHT_DOOR,
				STEEL_DOOR,
				ALUMINIUM_DOOR,
				TITANIUM_DOOR,
			).forEach {
				add(it.get(), createDoorTable(it.get()))
			}

			// Stones->Cobblestones
			mapOf(
				LUNAR_STONE to LUNAR_COBBLESTONE,
				LUNAR_DEEPSLATE to COBBLED_LUNAR_DEEPSLATE,
			).forEach {
				createStoneDrops(it.key.get(), it.value.get())
			}

			// Simple ore drops
			mapOf(
				LUNAR_COAL_ORE to Items.COAL,
				LUNAR_DEEPSLATE_COAL_ORE to Items.COAL,
//				MARTIAN_COAL_ORE to Items.COAL,
//				MARTIAN_DEEPSLATE_COAL_ORE to Items.COAL,
//				MERCURIAL_COAL_ORE to Items.COAL,
//				MERCURIAL_DEEPSLATE_COAL_ORE to Items.COAL,
//				VENUSIAN_COAL_ORE to Items.COAL,
//				VENUSIAN_DEEPSLATE_COAL_ORE to Items.COAL,
				LUNAR_IRON_ORE to Items.RAW_IRON,
				LUNAR_DEEPSLATE_IRON_ORE to Items.RAW_IRON,
//				MARTIAN_IRON_ORE to Items.RAW_IRON,
//				MARTIAN_DEEPSLATE_IRON_ORE to Items.RAW_IRON,
//				MERCURIAL_IRON_ORE to Items.RAW_IRON,
//				MERCURIAL_DEEPSLATE_IRON_ORE to Items.RAW_IRON,
//				VENUSIAN_IRON_ORE to Items.RAW_IRON,
//				VENUSIAN_DEEPSLATE_IRON_ORE to Items.RAW_IRON,
				LUNAR_GOLD_ORE to Items.RAW_GOLD,
				LUNAR_DEEPSLATE_GOLD_ORE to Items.RAW_GOLD,
//				MARTIAN_GOLD_ORE to Items.RAW_GOLD,
//				MARTIAN_DEEPSLATE_GOLD_ORE to Items.RAW_GOLD,
//				MERCURIAL_GOLD_ORE to Items.RAW_GOLD,
//				MERCURIAL_DEEPSLATE_GOLD_ORE to Items.RAW_GOLD,
//				VENUSIAN_GOLD_ORE to Items.RAW_GOLD,
//				VENUSIAN_DEEPSLATE_GOLD_ORE to Items.RAW_GOLD,
				LUNAR_EMERALD_ORE to Items.EMERALD,
				LUNAR_DEEPSLATE_EMERALD_ORE to Items.EMERALD,
//				MARTIAN_EMERALD_ORE to Items.EMERALD,
//				MARTIAN_DEEPSLATE_EMERALD_ORE to Items.EMERALD,
//				MERCURIAL_EMERALD_ORE to Items.EMERALD,
//				MERCURIAL_DEEPSLATE_EMERALD_ORE to Items.EMERALD,
//				VENUSIAN_EMERALD_ORE to Items.EMERALD,
//				VENUSIAN_DEEPSLATE_EMERALD_ORE to Items.EMERALD,
				LUNAR_DIAMOND_ORE to Items.DIAMOND,
				LUNAR_DEEPSLATE_DIAMOND_ORE to Items.DIAMOND,
//				MARTIAN_DIAMOND_ORE to Items.DIAMOND,
//				MARTIAN_DEEPSLATE_DIAMOND_ORE to Items.DIAMOND,
//				MERCURIAL_DIAMOND_ORE to Items.DIAMOND,
//				MERCURIAL_DEEPSLATE_DIAMOND_ORE to Items.DIAMOND,
//				VENUSIAN_DIAMOND_ORE to Items.DIAMOND,
//				VENUSIAN_DEEPSLATE_DIAMOND_ORE to Items.DIAMOND,
				ALUMINIUM_ORE to CygnusItems.RAW_ALUMINIUM,
				DEEPSLATE_ALUMINIUM_ORE to CygnusItems.RAW_ALUMINIUM,
				LUNAR_ALUMINIUM_ORE to CygnusItems.RAW_ALUMINIUM,
				LUNAR_DEEPSLATE_ALUMINIUM_ORE to CygnusItems.RAW_ALUMINIUM,
//				MARTIAN_ALUMINIUM_ORE to CygnusItems.RAW_ALUMINIUM,
//				MARTIAN_DEEPSLATE_ALUMINIUM_ORE to CygnusItems.RAW_ALUMINIUM,
//				MERCURIAL_ALUMINIUM_ORE to CygnusItems.RAW_ALUMINIUM,
//				MERCURIAL_DEEPSLATE_ALUMINIUM_ORE to CygnusItems.RAW_ALUMINIUM,
//				VENUSIAN_ALUMINIUM_ORE to CygnusItems.RAW_ALUMINIUM,
//				VENUSIAN_DEEPSLATE_ALUMINIUM_ORE to CygnusItems.RAW_ALUMINIUM,
				TITANIUM_ORE to CygnusItems.RAW_TITANIUM,
				DEEPSLATE_TITANIUM_ORE to CygnusItems.RAW_TITANIUM,
				LUNAR_TITANIUM_ORE to CygnusItems.RAW_TITANIUM,
				LUNAR_DEEPSLATE_TITANIUM_ORE to CygnusItems.RAW_TITANIUM,
//				MARTIAN_TITANIUM_ORE to CygnusItems.RAW_TITANIUM,
//				MARTIAN_DEEPSLATE_TITANIUM_ORE to CygnusItems.RAW_TITANIUM,
//				MERCURIAL_TITANIUM_ORE to CygnusItems.RAW_TITANIUM,
//				MERCURIAL_DEEPSLATE_TITANIUM_ORE to CygnusItems.RAW_TITANIUM,
//				VENUSIAN_TITANIUM_ORE to CygnusItems.RAW_TITANIUM,
//				VENUSIAN_DEEPSLATE_TITANIUM_ORE to CygnusItems.RAW_TITANIUM,
			).forEach {
				this.add(it.key.get(), createOreDrop(it.key.get(), it.value.asItem()))
			}

			// Copper ore drops
			setOf(
				LUNAR_COPPER_ORE,
				LUNAR_DEEPSLATE_COPPER_ORE,
//				MARTIAN_COPPER_ORE,
//				MARTIAN_DEEPSLATE_COPPER_ORE,
//				MERCURIAL_COPPER_ORE,
//				MERCURIAL_DEEPSLATE_COPPER_ORE,
//				VENUSIAN_COPPER_ORE,
//				VENUSIAN_DEEPSLATE_COPPER_ORE,
			).forEach {
				add(it.get(), createCopperOreDrops(it.get()))
			}

			// Redstone ore drops
			setOf(
				LUNAR_REDSTONE_ORE,
				LUNAR_DEEPSLATE_REDSTONE_ORE,
//				MARTIAN_REDSTONE_ORE,
//				MARTIAN_DEEPSLATE_REDSTONE_ORE,
//				MERCURIAL_REDSTONE_ORE,
//				MERCURIAL_DEEPSLATE_REDSTONE_ORE,
//				VENUSIAN_REDSTONE_ORE,
//				VENUSIAN_DEEPSLATE_REDSTONE_ORE,
			).forEach {
				add(it.get(), createRedstoneOreDrops(it.get()))
			}

			// Lapis ore drops
			setOf(
				LUNAR_LAPIS_ORE,
				LUNAR_DEEPSLATE_LAPIS_ORE,
//				MARTIAN_LAPIS_ORE,
//				MARTIAN_DEEPSLATE_LAPIS_ORE,
//				MERCURIAL_LAPIS_ORE,
//				MERCURIAL_DEEPSLATE_LAPIS_ORE,
//				VENUSIAN_LAPIS_ORE,
//				VENUSIAN_DEEPSLATE_LAPIS_ORE,
			).forEach {
				add(it.get(), createLapisOreDrops(it.get()))
			}

			// No drop
			setOf(
				BUDDING_CHRONITE,
			).forEach {
				add(it.get(), noDrop())
			}

			// Drops with silk touch
			setOf(
				SMALL_CHRONITE_BUD,
				MEDIUM_CHRONITE_BUD,
				LARGE_CHRONITE_BUD,
			).forEach {
				dropWhenSilkTouch(it.get())
			}

			// Similar handling as amethyst clusters
			add(CHRONITE_CLUSTER.get(), DapperLootTableBuilder().apply {
				addPool {
					setRolls(1)
					alternatives {
						item(CHRONITE_CLUSTER) {
							condition(hasSilkTouch())
						}
						alternatives {
							item(CygnusItems.CHRONITE_SHARD) {
								condition(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES)))
								applyFunction(SetItemCountFunction.setCount(ConstantValue.exactly(1f), false))
								applyFunction(ApplyBonusCount.addOreBonusCount(registries.lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.FORTUNE).get()))
							}
							item(CygnusItems.CHRONITE_SHARD) {
								applyFunction(SetItemCountFunction.setCount(ConstantValue.exactly(1f), false))
								applyFunction(ApplyExplosionDecay.explosionDecay())
							}
						}
					}
				}
			}.builder)
		}

		fun createStoneDrops(stoneBlock: Block, cobblestoneBlock: Block)
		{
			add(stoneBlock, DapperLootTableBuilder().apply {
				addPool {
					setRolls(1)
					alternatives {
						item(stoneBlock) {
							condition(hasSilkTouch())
						}
						item(cobblestoneBlock) { }
					}
				}
			}.builder)
		}
	}

	//TODO: Move this to Dapper
	@Suppress("Unused")
	companion object
	{
		class DapperLootTableBuilder
		{
			var builder: LootTable.Builder = LootTable.lootTable()

			fun addPool(func: DapperLootPoolBuilder.() -> Unit)
			{
				val b = DapperLootPoolBuilder()
				func.invoke(b)
				builder.withPool(b.builder)
			}
		}

		interface IDapperLootEntriesExtension
		{
			fun entry(b: DapperLootPoolEntryBuilder)

			fun alternatives(func: DapperLootPoolEntryBuilder.Companion.Alternatives.() -> Unit)
			{
				val b = DapperLootPoolEntryBuilder.Companion.Alternatives()
				func.invoke(b)
				entry(b)
			}

			fun item(item: ItemLike, func: DapperLootPoolEntryBuilder.Companion.Item.() -> Unit)
			{
				val b = DapperLootPoolEntryBuilder.Companion.Item(item)
				func.invoke(b)
				entry(b)
			}
		}

		class DapperLootPoolBuilder : IDapperLootEntriesExtension
		{
			var builder: LootPool.Builder = LootPool.lootPool()

			fun setRolls(rolls: NumberProvider) { builder.setRolls(rolls) }
			fun setBonusRolls(rolls: NumberProvider) { builder.setBonusRolls(rolls) }

			fun setRolls(rolls: Int) { builder.setRolls(ConstantValue.exactly(rolls.toFloat())) }
			fun setBonusRolls(rolls: Int) { builder.setBonusRolls(ConstantValue.exactly(rolls.toFloat())) }

			override fun entry(b: DapperLootPoolEntryBuilder) { builder.add(b.build()) }
		}

		interface DapperLootPoolEntryBuilder
		{
			fun build(): LootPoolEntryContainer.Builder<*>

			companion object
			{
				class Alternatives : DapperLootPoolEntryBuilder, IDapperLootEntriesExtension
				{
					var alternatives = mutableListOf<DapperLootPoolEntryBuilder>()

					override fun entry(b: DapperLootPoolEntryBuilder) { alternatives.add(b) }

					override fun build(): AlternativesEntry.Builder = AlternativesEntry.alternatives(alternatives, { it.build() })
				}

				class Item(item: ItemLike) : DapperLootPoolEntryBuilder
				{
					var builder: LootPoolSingletonContainer.Builder<*> = LootItem.lootTableItem(item)

					fun condition(condition: LootItemCondition.Builder) { builder.`when`(condition) }

					fun applyFunction(function: LootItemFunction.Builder) { builder.apply(function) }

					override fun build() = builder
				}
			}
		}
	}
}
