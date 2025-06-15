package top.girlkisser.cygnus.datagen.server

import martian.dapper.api.server.DapperLootTableProvider
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
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
import top.girlkisser.cygnus.content.registry.CygnusBlocks
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
			setOf(
				CygnusBlocks.STEEL_BLOCK,
				CygnusBlocks.STEEL_TILES,
				CygnusBlocks.STEEL_SHEET_METAL,
				CygnusBlocks.CHRONITE_BLOCK,
				CygnusBlocks.COMMAND_CENTRE,
				CygnusBlocks.TELEPAD,
				CygnusBlocks.CHRONITE_BLAST_FURNACE,
			).forEach {
				dropSelf(it.get())
			}

			setOf(
				CygnusBlocks.BUDDING_CHRONITE,
			).forEach {
				add(it.get(), noDrop())
			}

			setOf(
				CygnusBlocks.SMALL_CHRONITE_BUD,
				CygnusBlocks.MEDIUM_CHRONITE_BUD,
				CygnusBlocks.LARGE_CHRONITE_BUD,
			).forEach {
				dropWhenSilkTouch(it.get())
			}

			add(CygnusBlocks.CHRONITE_CLUSTER.get(), DapperLootTableBuilder().apply {
				addPool {
					setRolls(1)
					alternatives {
						item(CygnusBlocks.CHRONITE_CLUSTER) {
							condition(hasSilkTouch())
						}
						alternatives {
							item(CygnusItems.CHRONITE) {
								condition(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES)))
								applyFunction(SetItemCountFunction.setCount(ConstantValue.exactly(1f), false))
								applyFunction(ApplyBonusCount.addOreBonusCount(registries.lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.FORTUNE).get()))
							}
							item(CygnusItems.CHRONITE) {
								applyFunction(SetItemCountFunction.setCount(ConstantValue.exactly(1f), false))
								applyFunction(ApplyExplosionDecay.explosionDecay())
							}
						}
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
