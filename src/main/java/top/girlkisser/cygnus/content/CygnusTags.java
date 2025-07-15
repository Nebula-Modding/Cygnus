package top.girlkisser.cygnus.content;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.ApiStatus;
import top.girlkisser.cygnus.Cygnus;

@ApiStatus.NonExtendable
public interface CygnusTags
{
	@ApiStatus.NonExtendable
	interface Items
	{
		static TagKey<Item> c(String path)
		{
			return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path));
		}

		static TagKey<Item> mod(String path)
		{
			return TagKey.create(Registries.ITEM, Cygnus.id(path));
		}

		TagKey<Item>
			NUGGETS_STEEL = c("nuggets/steel"),
			INGOTS_STEEL = c("ingots/steel"),
			PLATES_STEEL = c("plates/steel"),
			RODS_STEEL = c("rods/steel"),
			STORAGE_BLOCKS_STEEL = c("storage_blocks/steel"),
			INGOTS_ALUMINIUM = c("ingots/aluminum"),
			NUGGETS_ALUMINIUM = c("nuggets/aluminum"),
			PLATES_ALUMINIUM = c("plates/aluminum"),
			RODS_ALUMINIUM = c("rods/aluminum"),
			STORAGE_BLOCKS_RAW_ALUMINIUM = c("storage_blocks/raw_aluminum"),
			STORAGE_BLOCKS_ALUMINIUM = c("storage_blocks/aluminum"),
			NUGGETS_TITANIUM = c("nuggets/titanium"),
			INGOTS_TITANIUM = c("ingots/titanium"),
			PLATES_TITANIUM = c("plates/titanium"),
			RODS_TITANIUM = c("rods/titanium"),
			STORAGE_BLOCKS_RAW_TITANIUM = c("storage_blocks/raw_titanium"),
			STORAGE_BLOCKS_TITANIUM = c("storage_blocks/titanium");

		TagKey<Item>
			CAN_WALK_ON_CHRONITE = mod("can_walk_on_chronite");
	}

	@ApiStatus.NonExtendable
	interface Blocks
	{
		static TagKey<Block> c(String path)
		{
			return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", path));
		}

		TagKey<Block> INCORRECT_FOR_OXYGEN_DRILl = TagKey.create(Registries.BLOCK, Cygnus.id("incorrect_for_oxygen_drill"));
	}

	@ApiStatus.NonExtendable
	interface Fluids
	{
		TagKey<Fluid> OXYGEN = TagKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath("c", "gasses/oxygen"));
	}
}
