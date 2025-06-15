package top.girlkisser.cygnus.content.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.content.block.BlockChroniteBlastFurnaceBE;
import top.girlkisser.cygnus.content.block.BlockCommandCentreBE;
import top.girlkisser.cygnus.content.block.BlockTelepadBE;
import top.girlkisser.cygnus.content.block.BlockTerminalBE;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@ApiStatus.NonExtendable
public interface CygnusBlockEntityTypes
{
	DeferredRegister<BlockEntityType<?>> R = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Cygnus.MODID);

	static <T extends BlockEntity> DeferredBE<T> reg(String id, Supplier<BlockEntityType<T>> blockEntity)
	{
		return DeferredBE.fromDeferredHolder(R.register(id, blockEntity));
	}

	static <T extends BlockEntity> DeferredBE<T> reg(String id, BlockEntityType.BlockEntitySupplier<? extends T> factory, DeferredBlock<?>... validBlocks)
	{
		return reg(id, () -> new BlockEntityType<>(factory, Arrays.stream(validBlocks).map(DeferredBlock::get).collect(Collectors.toSet()), null));
	}

	DeferredBE<BlockCommandCentreBE> COMMAND_CENTRE = reg("command_centre", BlockCommandCentreBE::new, CygnusBlocks.COMMAND_CENTRE);
	DeferredBE<BlockTerminalBE> TERMINAL = reg("terminal", BlockTerminalBE::new, CygnusBlocks.TERMINAL);
	DeferredBE<BlockTelepadBE> TELEPAD = reg("telepad", BlockTelepadBE::new, CygnusBlocks.TELEPAD);
	DeferredBE<BlockChroniteBlastFurnaceBE> CHRONITE_BLAST_FURNACE = reg("chronite_blast_furnace", BlockChroniteBlastFurnaceBE::new, CygnusBlocks.CHRONITE_BLAST_FURNACE);

	// Shorthand
	class DeferredBE<T extends BlockEntity> extends DeferredHolder<BlockEntityType<?>, BlockEntityType<T>>
	{
		protected DeferredBE(ResourceKey<BlockEntityType<?>> key)
		{
			super(key);
		}

		public static <T extends BlockEntity> DeferredBE<T> fromDeferredHolder(DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> holder)
		{
			return new DeferredBE<>(Objects.requireNonNull(holder.getKey()));
		}
	}
}
