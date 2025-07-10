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
				STEEL_SHEET_METAL,
				STEEL_BLOCK,
				STEEL_GRATE,
				STEEL_PLATING,
				STEEL_WINDOW,
				STEEL_PILLAR,
				STEEL_BARS,
				STEEL_DOOR,
				STEEL_TRAPDOOR,
				STEEL_BULB,

				ALUMINIUM_SHEET_METAL,
				ALUMINIUM_BLOCK,
				ALUMINIUM_PLATING,

				TITANIUM_SHEET_METAL,
				TITANIUM_BLOCK,
				TITANIUM_PLATING,
				TITANIUM_WINDOW,

				LUNAR_REGOLITH,
				LUNAR_COBBLESTONE,
				SMOOTH_LUNAR_STONE,
				CHISELED_LUNAR_STONE,
				LUNAR_STONE_BRICKS,
				CRACKED_LUNAR_STONE_BRICKS,
				LUNAR_STONE_PILLAR,
				COBBLED_LUNAR_DEEPSLATE,
				SMOOTH_LUNAR_DEEPSLATE,
				CHISELED_LUNAR_DEEPSLATE,
				LUNAR_DEEPSLATE_BRICKS,
				CRACKED_LUNAR_DEEPSLATE_BRICKS,
				LUNAR_DEEPSLATE_PILLAR,
				CHRONITE_BLOCK,

				COMMAND_CENTRE,
				TELEPAD,
				CHRONITE_BLAST_FURNACE,
			).forEach {
				dropSelf(it.get())
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
				LUNAR_IRON_ORE to Items.RAW_IRON,
				LUNAR_DEEPSLATE_IRON_ORE to Items.RAW_IRON,
				LUNAR_GOLD_ORE to Items.RAW_GOLD,
				LUNAR_DEEPSLATE_GOLD_ORE to Items.RAW_GOLD,
				LUNAR_EMERALD_ORE to Items.EMERALD,
				LUNAR_DEEPSLATE_EMERALD_ORE to Items.EMERALD,
				LUNAR_DIAMOND_ORE to Items.DIAMOND,
				LUNAR_DEEPSLATE_DIAMOND_ORE to Items.DIAMOND,
				ALUMINIUM_ORE to CygnusItems.RAW_ALUMINIUM,
				DEEPSLATE_ALUMINIUM_ORE to CygnusItems.RAW_ALUMINIUM,
				LUNAR_ALUMINIUM_ORE to CygnusItems.RAW_ALUMINIUM,
				LUNAR_DEEPSLATE_ALUMINIUM_ORE to CygnusItems.RAW_ALUMINIUM,
				TITANIUM_ORE to CygnusItems.RAW_TITANIUM,
				DEEPSLATE_TITANIUM_ORE to CygnusItems.RAW_TITANIUM,
				LUNAR_TITANIUM_ORE to CygnusItems.RAW_TITANIUM,
				LUNAR_DEEPSLATE_TITANIUM_ORE to CygnusItems.RAW_TITANIUM,
			).forEach {
				this.add(it.key.get(), createOreDrop(it.key.get(), it.value.asItem()))
			}

			// Redstone ore drops
			setOf(
				LUNAR_REDSTONE_ORE,
				LUNAR_DEEPSLATE_REDSTONE_ORE,
			).forEach {
				add(it.get(), createRedstoneOreDrops(it.get()))
			}

			// Lapis ore drops
			setOf(
				LUNAR_LAPIS_ORE,
				LUNAR_DEEPSLATE_LAPIS_ORE,
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
