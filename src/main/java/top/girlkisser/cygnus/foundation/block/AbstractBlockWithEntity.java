package top.girlkisser.cygnus.foundation.block;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public abstract class AbstractBlockWithEntity<T extends BlockEntity> extends Block implements EntityBlock
{
	private final BlockEntityType.BlockEntitySupplier<T> blockEntityFactory;
	protected boolean hasTicker = true;

	public AbstractBlockWithEntity(
		BlockEntityType.BlockEntitySupplier<T> blockEntityFactory,
		Properties properties
	)
	{
		super(properties);
		this.blockEntityFactory = blockEntityFactory;
	}

	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState)
	{
		return blockEntityFactory.create(blockPos, blockState);
	}

	@Override
	@ParametersAreNonnullByDefault
	public @Nullable <U extends BlockEntity> BlockEntityTicker<U> getTicker(Level level, BlockState state, BlockEntityType<U> blockEntityType)
	{
		if (!hasTicker)
			return null;

		if (level.isClientSide)
			return (level_, pos, state_, be) ->
			{
				if (be instanceof ITickableBE tickableBE)
					tickableBE.clientTick((ClientLevel) level);
			};
		else
			return (level_, pos, state_, be) ->
			{
				if (be instanceof ITickableBE tickableBE)
					tickableBE.serverTick((ServerLevel) level);
			};
	}
}
