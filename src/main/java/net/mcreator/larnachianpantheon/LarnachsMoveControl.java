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
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.Mob;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;

import net.mcreator.larnachianpantheon.entity.LarnachsEntity;

public class LarnachsMoveControl<T extends LarnachsEntity> extends MoveControl {
	public final PathNavigation navigation;
	public int remainingMoveTick;
	public double targetDirX, targetDirZ;
	public float speedZza, speedXxa, nextRotation, speedMul;
	public boolean hasTarget;
	public T larnE;

	public LarnachsMoveControl(T mob) {
		super(mob);
		larnE = mob;
		navigation = mob.getNavigation();
	}

	public void strafe(float p_24989_, float p_24990_) {
		this.operation = MoveControl.Operation.STRAFE;
		this.strafeForwards = p_24989_;
		this.strafeRight = p_24990_;
		this.speedModifier = 1;
	}

	public void tick() {
		if (remainingMoveTick == 0)
			if (this.operation == MoveControl.Operation.STRAFE) {
				this.operation = MoveControl.Operation.WAIT;
				this.speedZza = strafeForwards;
				this.speedXxa = strafeRight;
				hasTarget = mob.getTarget() != null;
				larnE.setMoveForwardAndSide(speedZza, speedXxa);
				occurWalk();
			} else if (this.operation == MoveControl.Operation.MOVE_TO) {
				this.operation = MoveControl.Operation.WAIT;
				Path path = this.navigation.getPath();
				double dX, dZ;
				if (path != null) {
					while (!path.isDone()) {
						Vec3 current = path.getNextNodePos().getCenter();
						if (this.mob.getBoundingBox().inflate(0.1f).contains(current) && isClearForMovementBetween(mob, mob.position(), current, false)) {
							path.advance();
						} else {
							break;
						}
					}
					if (path.isDone()) {
						stopMovement();
						return;
					}
					while (path.getNextNodeIndex() + 4 < path.getNodeCount()) {
						int nextNode = path.getNextNodeIndex() + 3;
						Vec3 current = path.getNextNodePos().getCenter(), next = path.getNodePos(nextNode).getCenter();
						double curDX = current.x() - this.mob.getX();
						double curDZ = current.z() - this.mob.getZ();
						double distToCurrentSq = curDX * curDX + curDZ * curDZ;
						double nextDX = next.x() - this.mob.getX();
						double nextDZ = next.z() - this.mob.getZ();
						double distToNextSq = nextDX * nextDX + nextDZ * nextDZ;
						if (distToNextSq < distToCurrentSq && isClearForMovementBetween(mob, mob.position(), next, false)) {
							path.setNextNodeIndex(nextNode);
						} else {
							break;
						}
					}
					dX = path.getNextNodePos().getCenter().x() - this.mob.getX();
					dZ = path.getNextNodePos().getCenter().z() - this.mob.getZ();
				} else {
					dX = this.wantedX - this.mob.getX();
					dZ = this.wantedZ - this.mob.getZ();
				}
				double distanceSq = dX * dX + dZ * dZ;
				if (distanceSq > 1.0E-7D) {
					double distance = Math.sqrt(distanceSq);
					this.targetDirX = dX / distance;
					this.targetDirZ = dZ / distance;
					hasTarget = mob.getTarget() != null;
					nextRotation = (float) (Mth.atan2(targetDirZ, targetDirX) * (double) (180F / (float) Math.PI)) - 90.0F;
					if (hasTarget) {
						float rad = (nextRotation - this.mob.getYRot()) * ((float) Math.PI / 180F);
						this.speedZza = Mth.cos(rad);
						this.speedXxa = -Mth.sin(rad);
						larnE.setMoveForwardAndSide(speedZza, speedXxa);
					} else
						larnE.setMoveForwardAndSide(1, 0);
					occurWalk();
				} else {
					this.stopMovement();
				}
			} else {
				this.stopMovement();
			}
	}

	private boolean isWalkable(float p_24997_, float p_24998_) {
		PathNavigation pathnavigation = this.mob.getNavigation();
		if (pathnavigation != null) {
			NodeEvaluator nodeevaluator = pathnavigation.getNodeEvaluator();
			if (nodeevaluator != null && nodeevaluator.getBlockPathType(this.mob.level(), Mth.floor(this.mob.getX() + (double) p_24997_), this.mob.getBlockY(), Mth.floor(this.mob.getZ() + (double) p_24998_)) != BlockPathTypes.WALKABLE) {
				return false;
			}
		}
		return true;
	}

	private void occurWalk() {
		int collisionState = (mob.horizontalCollision ? 1 : 0) //
				| (mob.verticalCollision ? 2 : 0) //
				| (mob.verticalCollisionBelow ? 4 : 0);
		boolean isCollidingObstacle = ((collisionState & 1) != 0) || ((collisionState & 6) == 2);
		this.remainingMoveTick = 16;
		mob.playSound(SoundEvents.IRON_TRAPDOOR_OPEN, 1, 0.5f);
		if (isCollidingObstacle//
				&& net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(mob.level(), mob)) {
			AABB aabb = mob.getBoundingBox().inflate(0.2D);
			for (BlockPos blockpos : BlockPos.betweenClosed(Mth.floor(aabb.minX), Mth.floor(aabb.minY), Mth.floor(aabb.minZ), Mth.floor(aabb.maxX), Mth.floor(aabb.maxY), Mth.floor(aabb.maxZ))) {
				BlockState blockstate = mob.level().getBlockState(blockpos);
				Block block = blockstate.getBlock();
				if (block instanceof LeavesBlock) {
					mob.level().destroyBlock(blockpos, true, mob);
				}
			}
		}
	}

	public void processWalk() {
		if (larnE.isInAction()) {
			stopMovement();
			return;
		}
		remainingMoveTick--;
		if (remainingMoveTick != 15) {
			float moveSpeed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
			if (!hasTarget) {
				this.mob.setYRot(this.rotlerp(this.mob.getYRot(), nextRotation, 15.0F));
			} else if (larnE.getActionState() < 5 && mob.getTarget() != null)
				mob.lookAt(mob.getTarget(), 30, 30);
			if (remainingMoveTick > 5) {
				if (remainingMoveTick == 7)
					mob.playSound(SoundEvents.IRON_TRAPDOOR_CLOSE, 1, 0.5f);
				if (mob.fallDistance == 0)
					if (hasTarget) {
						this.mob.setSpeed(moveSpeed);
						this.mob.setZza(speedZza * moveSpeed);
						this.mob.setXxa(speedXxa * moveSpeed);
					} else
						this.mob.setSpeed(moveSpeed);
			} else {
				mob.setZza(mob.zza * 0.3f);
				mob.setXxa(mob.xxa * 0.3f);
			}
		} else
			this.mob.getEntityData().set(LarnachsEntity.DATA_walkState, //
					(mob.getEntityData().get(LarnachsEntity.DATA_walkState) & 1) + 1);
	}

	private boolean isClearForMovementBetween(Mob p_262599_, Vec3 p_262674_, Vec3 p_262586_, boolean p_262676_) {
		Vec3 vec3 = new Vec3(p_262586_.x, p_262586_.y + (double) p_262599_.getBbHeight() * 0.5D, p_262586_.z);
		return p_262599_.level().clip(new ClipContext(p_262674_, vec3, ClipContext.Block.COLLIDER, p_262676_ ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, p_262599_)).getType() == HitResult.Type.MISS;
	}

	public void stopMovement() {
		this.mob.getEntityData().set(LarnachsEntity.DATA_walkState, 0);
		this.mob.setSpeed(0.0F);
		this.mob.setZza(0.0F);
		this.mob.setXxa(0.0F);
		this.remainingMoveTick = 0;
	}
}