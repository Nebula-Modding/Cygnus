package top.girlkisser.cygnus.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.foundation.space.SpaceStation;
import top.girlkisser.cygnus.management.SpaceStationManager;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockCommandCentre extends Block implements EntityBlock
{
	public BlockCommandCentre(Properties properties)
	{
		super(properties);
	}

	@Override
	@ParametersAreNonnullByDefault
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new BlockCommandCentreBE(pos, state);
	}

	@ParametersAreNonnullByDefault
	protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
	{
		if (!level.isClientSide)
		{
			MinecraftServer server = level.getServer();
			assert server != null;
			SpaceStationManager manager = SpaceStationManager.get(server);
			SpaceStation station = manager.getOrCreateSpaceStationForPlayer((ServerPlayer) player, Cygnus.id("space_station"));
			station.teleportPlayerHereViaBeam((ServerPlayer) player, player.getUUID());
		}

		return InteractionResult.SUCCESS_NO_ITEM_USED;
	}
}
