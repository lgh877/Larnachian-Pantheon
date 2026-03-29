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
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.Mob;

public class LarnachsPathNavigation extends MMPathNavigateGround {
    public LarnachsPathNavigation(Mob p_26448_, Level p_26449_) {
        super(p_26448_, p_26449_);
    }

    public void recomputePath() {
        if (((LarnachsMoveControl) mob.getMoveControl()).remainingMoveTick == 0)
            super.recomputePath();
    }

    protected boolean canMoveDirectly(Vec3 p_186133_, Vec3 p_186134_) {
        return PathNavigation.isClearForMovementBetween(mob, p_186133_, p_186134_, false);
    }
}