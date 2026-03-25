package net.mcreator.larnachianpantheon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

public class IsLookingAtMeProcedure {
	public static boolean execute(Entity entity, Entity entityToCheck) {
		if (entity == null || entityToCheck == null)
			return false;
		/*
		entityToCheck.startRiding(entity);*/
		LivingEntity mob = (LivingEntity) entityToCheck;
		if (!mob.canBeSeenAsEnemy())
			return false;
		Vec3 vec3 = mob.getViewVector(1.0F).normalize();
		Vec3 vec31 = new Vec3(entity.getX() - mob.getX(), entity.getEyeY() - mob.getEyeY(), entity.getZ() - mob.getZ());
		double d0 = vec31.length();
		vec31 = vec31.normalize();
		double d1 = vec3.dot(vec31);
		return d1 > 1.0D - 0.025D / d0 ? mob.hasLineOfSight(entity) : false;
	}
}