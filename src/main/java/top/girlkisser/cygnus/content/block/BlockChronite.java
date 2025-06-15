package top.girlkisser.cygnus.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.state.BlockState;
import top.girlkisser.cygnus.content.registry.CygnusParticleTypes;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockChronite extends AmethystBlock
{
	public BlockChronite(Properties properties)
	{
		super(properties);
	}

	@Override
	@ParametersAreNonnullByDefault
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity)
	{
		if (!canEntityWalkOnChronite(entity))
			entity.hurt(level.damageSources().hotFloor(), 1.0F);

		super.stepOn(level, pos, state, entity);
	}

	@Override
	@ParametersAreNonnullByDefault
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
	{
		if (random.nextInt(5) != 0)
			return;

		Direction direction = Direction.getRandom(random);
		BlockPos blockPos = pos.relative(direction);
		BlockState blockState = level.getBlockState(blockPos);
		if (!state.canOcclude() || !blockState.isFaceSturdy(level, blockPos, direction.getOpposite()))
		{
			double x = direction.getStepX() == 0 ? random.nextDouble() : .5d + direction.getStepX() * .6d;
			double y = direction.getStepY() == 0 ? random.nextDouble() : .5d + direction.getStepY() * .6d;
			double z = direction.getStepZ() == 0 ? random.nextDouble() : .5d + direction.getStepZ() * .6d;
			level.addParticle(CygnusParticleTypes.CHRONITE.get(), pos.getX() + x, pos.getY() + y, pos.getZ() + z, 0, 0, 0);
		}
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public static boolean canEntityWalkOnChronite(Entity entity)
	{
		return entity instanceof LivingEntity le && le.getItemBySlot(EquipmentSlot.FEET).canWalkOnPowderedSnow(le);
	}
}
