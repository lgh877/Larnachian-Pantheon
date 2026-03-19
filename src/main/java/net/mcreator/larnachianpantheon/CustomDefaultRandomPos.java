/**
 * The code of this mod element is always locked.
 *
 * You can register new events in this class too.
 *
 * If you want to make a plain independent class, create it using
 * Project Browser -> New... and make sure to make the class
 * outside net.mcreator.larnachianpantheon as this package is managed by MCreator.
 *
 * If you change workspace package, modid or prefix, you will need
 * to manually adapt this file to these changes or remake it.
 *
 * This class will be added in the mod root package.
*/
package net.mcreator.larnachianpantheon;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

public class CustomDefaultRandomPos {
	@Nullable
	private static BlockPos generateRandomPosTowardDirection(PathfinderMob p_148437_, int p_148438_, boolean p_148439_, BlockPos p_148440_) {
		BlockPos blockpos = RandomPos.generateRandomPosTowardDirection(p_148437_, p_148438_, p_148437_.getRandom(), p_148440_);
		return !GoalUtils.isOutsideLimits(blockpos, p_148437_) && !GoalUtils.isRestricted(p_148439_, p_148437_, blockpos) && !GoalUtils.isNotStable(p_148437_.getNavigation(), blockpos) && !GoalUtils.hasMalus(p_148437_, blockpos) ? blockpos : null;
	}

	@Nullable
	public static Vec3 getPosStrafe(PathfinderMob mob, int horizontalRange, int verticalRange, Vec3 targetPos) {
		Vec3 directionVec = mob.position().subtract(targetPos);
		boolean goRight = mob.getRandom().nextBoolean();
		double strafeX = goRight ? -directionVec.z : directionVec.z;
		double strafeZ = goRight ? directionVec.x : -directionVec.x;
		boolean checkRestriction = GoalUtils.mobRestricted(mob, horizontalRange);
		return RandomPos.generateRandomPos(mob, () -> {
			BlockPos randomDirPos = RandomPos.generateRandomDirectionWithinRadians(mob.getRandom(), horizontalRange, verticalRange, 0, strafeX, strafeZ, (double) ((float) Math.PI / 4F));
			return randomDirPos == null ? null : generateRandomPosTowardDirection(mob, horizontalRange, checkRestriction, randomDirPos);
		});
	}
}