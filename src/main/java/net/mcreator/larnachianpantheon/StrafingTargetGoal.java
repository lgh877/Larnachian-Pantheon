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

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

//Based on AvoidTargetGoal
public class StrafingTargetGoal extends Goal {
    protected final PathfinderMob mob;
    protected LivingEntity toAvoid;
    protected final double walkSpeedModifier;
    protected final float maxDist, minDist, defaultStraight, defaultSide;
    protected double stopDist;
    private int evalChooseDirection;
    private boolean goRight, isCollided;

    //protected Path path;
    //protected final PathNavigation pathNav;
    public StrafingTargetGoal(PathfinderMob mob, float maxDistance, float minDistance, float defaultStraight, double walkSpeed) {
        this.mob = mob;
        this.maxDist = maxDistance;
        this.minDist = minDistance;
        this.walkSpeedModifier = walkSpeed;
        this.defaultStraight = defaultStraight;
        this.defaultSide = (float) Math.sqrt(1 - defaultStraight * defaultStraight);
        goRight = mob.getRandom().nextBoolean();
        isCollided = false;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public StrafingTargetGoal(PathfinderMob mob, float maxDistance, double walkSpeed) {
        this(mob, maxDistance, maxDistance * 0.5f, 0, walkSpeed);
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }
        if (this.mob.distanceToSqr(target) > (float) Math.pow(maxDist + (mob.getBbWidth() + target.getBbWidth()) / 2d, 2)) {
            return false;
        }
        this.toAvoid = target;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.toAvoid == null || !this.toAvoid.isAlive())
            return false;
        return this.mob.distanceToSqr(toAvoid) > Math.pow(maxDist + (mob.getBbWidth() + toAvoid.getBbWidth()) / 2d, 2);
    }

    @Override
    public void tick() {
        double distSqr = mob.distanceToSqr(toAvoid);
        double minDistModified = minDist + (mob.getBbWidth() + toAvoid.getBbWidth()) / 2d;
        if (!isCollided) {
            int collisionState = (mob.horizontalCollision ? 1 : 0) //
                    | (mob.verticalCollision ? 2 : 0) //
                    | (mob.verticalCollisionBelow ? 4 : 0);
            isCollided = ((collisionState & 1) != 0) || ((collisionState & 6) == 2);
        }
        if (!isCollided && distSqr < minDistModified * minDistModified) {
            mob.getMoveControl().strafe(-0.8f, goRight ? 0.6f : -0.6f);
        } else {
            if (++evalChooseDirection > 12) {
                evalChooseDirection = 0;
                goRight = mob.getRandom().nextBoolean();
                isCollided = false;
            }
            mob.getMoveControl().strafe(defaultStraight, goRight ? this.defaultSide : -this.defaultSide);
        }
        mob.getLookControl().setLookAt(toAvoid);
    }

    @Override
    public void stop() {
        //pathNav.stop();
        this.toAvoid = null;
    }
}