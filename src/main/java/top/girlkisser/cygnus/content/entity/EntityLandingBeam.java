package top.girlkisser.cygnus.content.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.cygnus.client.CygnusClient;
import top.girlkisser.cygnus.content.block.BlockTelepad;
import top.girlkisser.cygnus.content.registry.CygnusBlocks;
import top.girlkisser.cygnus.content.registry.CygnusEntityTypes;
import top.girlkisser.cygnus.foundation.client.particle.DustParticlePresets;
import top.girlkisser.cygnus.foundation.client.particle.ParticleHelper;
import top.girlkisser.cygnus.foundation.space.SpaceStation;
import top.girlkisser.cygnus.management.SpaceStationManager;

import java.util.UUID;

public class EntityLandingBeam extends VehicleEntity
{
	public boolean isTheDestinationAPlanet;
	public UUID spaceStationUUID;

	public EntityLandingBeam(EntityType<?> entityType, Level level)
	{
		super(entityType, level);
	}

	public EntityLandingBeam(Level level, boolean isTheDestinationAPlanet, UUID spaceStationUUID)
	{
		this(CygnusEntityTypes.LANDING_BEAM.get(), level);
		this.isTheDestinationAPlanet = isTheDestinationAPlanet;
		this.spaceStationUUID = spaceStationUUID;
	}

	@Override
	public void tick()
	{
		super.tick();

		this.setDeltaMovement(0, isTheDestinationAPlanet ? -1 : 1, 0);
		this.move(MoverType.SELF, this.getDeltaMovement());

		Level level = this.level();

		if (level.isClientSide)
		{
			float x = Mth.sin(CygnusClient.clientTicks / 2f) * 0.25f;
			float z = Mth.cos(CygnusClient.clientTicks / 2f) * 0.25f;

			if (isTheDestinationAPlanet)
				ParticleHelper.addDust(DustParticlePresets.TELEPAD, level, position().add(x, 0, z), new Vec3(0, 0.7D, 0));
			else
				ParticleHelper.addDust(DustParticlePresets.TELEPAD, level, position().add(x, 2, z), new Vec3(0, -0.7D, 0));
		}
		else
		{
			if (this.countPlayerPassengers() == 0)
			{
				this.kill();
				level.addFreshEntity(new ItemEntity(level, getX(), getY(), getZ(), CygnusBlocks.TELEPAD.get().asItem().getDefaultInstance()));
				return;
			}

			if (isTheDestinationAPlanet)
			{
				BlockPos pos = this.blockPosition().below();
				BlockState state = level.getBlockState(pos);

				if (!state.isAir() || pos.getY() <= level.getMinBuildHeight() + 1)
				{
					BlockPos telepadPos = pos.above();
					if (this.spaceStationUUID != null)
					{
						BlockTelepad.placeTelepadWithSpaceStationDestination(level, telepadPos, this.spaceStationUUID);
					}
					this.getPassengers().forEach(entity ->
					{
						entity.stopRiding();
						entity.setPos(telepadPos.getCenter());
						entity.fallDistance = 0;
					});
					this.kill();
				}
			}
			else
			{
				if (this.position().y >= 300)
				{
					assert this.getServer() != null;
					SpaceStationManager manager = SpaceStationManager.get(this.getServer());
					SpaceStation spaceStation = manager.getSpaceStationByOwnerUUID(spaceStationUUID).orElseThrow();
					this.getPassengers().forEach(entity ->
					{
						entity.stopRiding();
						entity.fallDistance = 0;
						spaceStation.teleportEntityHere(entity);
					});
					this.kill();
				}
			}
		}
	}

	@Override
	public boolean canCollideWith(@NotNull Entity entity)
	{
		return false;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return false;
	}

	@Override
	public boolean isPushable()
	{
		return false;
	}

	@Override
	public boolean shouldRender(double x, double y, double z)
	{
		return false;
	}

	@Override
	protected @NotNull Item getDropItem()
	{
		return Items.AIR;
	}

	@Override
	protected void readAdditionalSaveData(@NotNull CompoundTag compound)
	{
		if (compound.hasUUID("spaceStationUUID"))
			spaceStationUUID = compound.getUUID("spaceStationUUID");

		isTheDestinationAPlanet = compound.getBoolean("isTheDestinationAPlanet");
	}

	@Override
	protected void addAdditionalSaveData(@NotNull CompoundTag compound)
	{
		if (spaceStationUUID != null)
			compound.putUUID("spaceStationUUID", spaceStationUUID);

		compound.putBoolean("isTheDestinationAPlanet", isTheDestinationAPlanet);
	}
}
