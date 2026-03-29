package net.mcreator.larnachianpantheon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleTypes;

import net.mcreator.larnachianpantheon.ServerLevelRelatedUtils;

import java.util.Comparator;

public class TestProjectileWhileProjectileFlyingTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity immediatesourceentity) {
		if (entity == null || immediatesourceentity == null)
			return;
		/*
		if (entity instanceof Mob _entity)
		_entity.getNavigation().moveTo(x, y, z, 1);*/
		if (!immediatesourceentity.level().isClientSide()) {
			double speed = immediatesourceentity.getDeltaMovement().length();
			Vec3 deltaMovement = immediatesourceentity.getDeltaMovement();
			Vec3 spread = new Vec3(0.5, 0.5, 0.5);
			ServerLevelRelatedUtils.sendRandomSpreadDirectedSpeed(//
					(ServerLevel) immediatesourceentity.level()//
					, ParticleTypes.FLAME//
					, immediatesourceentity.position()//
					, spread//
					, deltaMovement//
					, speed//
					, 30//
			);
			double currentCount = -0.5;
			while (currentCount < speed - 0.5) {
				{
					final Vec3 _center = new Vec3(x + deltaMovement.x() * currentCount, y + deltaMovement.y() * currentCount, z + deltaMovement.z() * currentCount);
					for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
						entityiterator.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_PROJECTILE), immediatesourceentity, entity), 1);
					}
				}
				currentCount += 0.5;
			}
			if (immediatesourceentity.tickCount > 100) {
				if (!immediatesourceentity.level().isClientSide())
					immediatesourceentity.discard();
			}
		}
	}
}