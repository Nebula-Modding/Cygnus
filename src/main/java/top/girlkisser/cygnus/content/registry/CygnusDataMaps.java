package top.girlkisser.cygnus.content.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import org.jetbrains.annotations.ApiStatus;
import top.girlkisser.cygnus.Cygnus;

@SuppressWarnings("unused")
@ApiStatus.NonExtendable
public interface CygnusDataMaps
{
	DataMapType<Item, Integer> CHRONITE_BLAST_FURNACE_FUELS = DataMapType
		.builder(
			Cygnus.id("chronite_blast_furnace_fuels"),
			Registries.ITEM,
			Codec.INT
		).build();
}
