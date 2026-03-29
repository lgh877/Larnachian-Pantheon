package net.mcreator.larnachianpantheon.procedures;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.arguments.EntityAnchorArgument;

import net.mcreator.larnachianpantheon.entity.LarnachsEntity;
import net.mcreator.larnachianpantheon.client.model.animations.LarnachsAnimation;
import net.mcreator.larnachianpantheon.LarnachsMoveControl;
import net.mcreator.larnachianpantheon.CameraShakeSegment;
import net.mcreator.larnachianpantheon.CameraShake;

import java.util.Comparator;

import com.crimsonsteve.crimsonsteveapi.utils.animations.CustomFadeInOutAnimation;
import com.crimsonsteve.crimsonsteveapi.utils.VectorHelper;

public class LarnachsOnEntityTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		Entity target = null;
		LarnachsEntity mob = (LarnachsEntity) entity;
		LarnachsMoveControl movecontrol = mob.larnMoveControl;
		if (world.isClientSide()) {
			int bodyXRot = mob.getEntityData().get(LarnachsEntity.DATA_bodyXRotation);
			boolean shouldPlayLandingAnim = !mob.wasOnGround && mob.onGround() && mob.prevYDeltaMovement < -0.6;
			mob.tickAnims();
			mob.walkAmplitudeO = mob.walkAmplitude;
			if (!mob.isInAction() && mob.getEntityData().get(LarnachsEntity.DATA_walkState) == 0) {
				mob.walkAmplitude = Math.min(5f, mob.walkAmplitude + 1f);
			} else {
				mob.walkAmplitude = Math.max(0f, mob.walkAmplitude - 1f);
			}
			mob.bodyXRotO = mob.bodyXRot;
			if (bodyXRot != mob.bodyXRot) {
				mob.bodyXRot += Mth.clamp(bodyXRot - mob.bodyXRot, -mob.bodyXRotSpeed, mob.bodyXRotSpeed);
			}
			if (shouldPlayLandingAnim) {
				float amplitudeLand = (float) Math.min(-mob.prevYDeltaMovement * 1.3, 1);
				CameraShake.addShake(new CameraShakeSegment(mob.position(), (int) (1000 * amplitudeLand), 5 * amplitudeLand, mob.getBbWidth() * 15 * amplitudeLand));
				mob.animList.add(new CustomFadeInOutAnimation(LarnachsAnimation.landing, mob.tickCount, amplitudeLand, mob.animList, 15, 16) {
					{
						isInstant = true;
					}

					public float getFadeInFactor(float fadeFactor) {
						float amp = 1 - fadeFactor;
						amp = Mth.sin(amp * amp * Mth.PI * 2);
						return amp;
					}

					public float getFadeOutFactor(float fadeFactor) {
						return 0;
					}
				});
			}
			mob.wasOnGround = mob.onGround();
			mob.prevYDeltaMovement = mob.getDeltaMovement().y();
		}
		if (!world.isClientSide()) {
			if (movecontrol.remainingMoveTick > 0)
				movecontrol.processWalk();
			if (mob.getActionState() < 5) {
				if ((entity.tickCount & 7) == 0) {
					target = entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null;
					if (target != null) {
						if (!mob.decidedToUseAttack) {
							mob.decidedToUseAttack = ++mob.attackDecisionTicks > 15;
						}
						double dist = Math.sqrt(Math.pow(target.getX() - x, 2) + Math.pow(target.getZ() - z, 2));
						double yDiff = target.getY() - y;
						double width = entity.getBbWidth();
						int rand = Mth.nextInt(mob.getRandom(), 1, 11);
						double yDiffAbs = Math.abs(yDiff);
						if (mob.decidedToUseAttack && rand < (dist + yDiffAbs) / (width * 2) && mob.onGround() && (dist > width * 4 + target.getBbWidth() * 0.5 || yDiffAbs > width * 2)) {
							mob.setActionState(43);
							mob.nextJumpDist = 0.16197791 * dist;
							mob.nextJumpHeight = Math.max(0.4, 0.06139787 * yDiff + 0.89359318);
						} else if (rand < 5 && dist < width * 3 + target.getBbWidth() * 0.5 && (yDiff < width * 3.25 && yDiff > -target.getBbHeight() - 1)) {//entity.lookAt(EntityAnchorArgument.Anchor.EYES, target.position());
							mob.setActionState(Mth.nextInt(mob.getRandom(), 5, 6));
							mob.nextComboDelay = Mth.nextInt(mob.getRandom(), 1, 7);
						} else if (rand < 8) {
							Vec3 directionBetTarget = target.position().add(0, target.getBbHeight() / 2, 0).subtract(mob.position().add(0, width * 0.95, 0)).normalize();
							double horizontalDist = Math.sqrt(Math.pow(directionBetTarget.x(), 2) + Math.pow(directionBetTarget.z(), 2)) * width;
							if (dist < target.getBbWidth() * 0.5 + 4 * horizontalDist && dist > target.getBbWidth() * 0.5 + 2 * horizontalDist && (yDiff < width * 5 && yDiff > -target.getBbHeight() - width * 2.6)) {
								mob.setActionState(42);
							}
						} else if (dist < width * 2.5 + target.getBbWidth() * 0.5 && (yDiff < width && yDiff > -target.getBbHeight())) {
							mob.setActionState(41);
						}
					}
				}
			} else {
				int actionTicks = mob.actionTicks++;
				switch (mob.getActionState()) {
					case 5 :
						if (actionTicks == 0) {
							target = mob.getTarget();
							if (target != null) {
								Vec3 targetVelosity = target.getDeltaMovement();
								Vec3 mobVelosity = mob.getDeltaMovement();
								if (entity instanceof LarnachsEntity _datEntSetI)
									_datEntSetI.getEntityData().set(LarnachsEntity.DATA_bodyXRotation,
											(int) Mth.clamp((int) (Math.toDegrees(Math.atan(((y + entity.getBbWidth() * 1.75 + mobVelosity.y()) - (target.getY() + target.getBbHeight() * 0.5 + -targetVelosity.y()))
													/ Math.sqrt(Math.pow(target.getX() + targetVelosity.x() - x - mobVelosity.x(), 2) + Math.pow(target.getZ() + targetVelosity.z() - z - mobVelosity.z(), 2))))), -50, 60));
								entity.lookAt(EntityAnchorArgument.Anchor.EYES, target.position());
							} else {
								if (entity instanceof LarnachsEntity _datEntSetI)
									_datEntSetI.getEntityData().set(LarnachsEntity.DATA_bodyXRotation, 0);
							}
						} else if (actionTicks == 12) {
							if (world instanceof Level _level) {
								if (!_level.isClientSide()) {
									_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.trident.throw")), SoundSource.HOSTILE, 2, (float) 0.5);
								} else {
									_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.trident.throw")), SoundSource.HOSTILE, 2, (float) 0.5, false);
								}
							}
						} else if (actionTicks == 13) {
							Vec3 lookVec = VectorHelper.calculateViewVector(mob.getEntityData().get(LarnachsEntity.DATA_bodyXRotation) + 27, mob.yBodyRot);
							double width = entity.getBbWidth();
							float attackDamage = (float) mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
							double knockback = mob.getAttribute(Attributes.ATTACK_KNOCKBACK).getValue();
							{
								final Vec3 _center = new Vec3(x + lookVec.x() * width * 1.687, y + (1.3 + lookVec.y() * 1.687) * width, z + lookVec.z() * width * 1.687);
								for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(width * 1.5 / 2d), e -> true).stream()
										.sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
									if (entityiterator != entity && entityiterator.isAttackable()) {
										if (entityiterator instanceof LivingEntity le)
											le.knockback(knockback, -lookVec.x(), -lookVec.z());
										else if (entityiterator instanceof TraceableEntity)
											entityiterator.setDeltaMovement(entityiterator.position().subtract(entity.position()).normalize().scale(entityiterator.getDeltaMovement().length() + 0.1));
										entityiterator.invulnerableTime = 0;
										mob.isHit = true;
										entityiterator.hurt(mob.damageSources().mobAttack(mob), (float) attackDamage);
									}
								}
							}
						} else if (actionTicks == 14) {
							Vec3 lookVec = VectorHelper.calculateViewVector(mob.getEntityData().get(LarnachsEntity.DATA_bodyXRotation) + 23, mob.yBodyRot + 56);
							double width = entity.getBbWidth();
							float attackDamage = (float) mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
							double knockback = mob.getAttribute(Attributes.ATTACK_KNOCKBACK).getValue();
							if (mob.getTarget() != null && Math.random() < 0.7) {
								mob.nextComboDelay = Mth.nextInt(mob.getRandom(), 7, 17) * (Math.random() < 0.5 ? 1 : -1);
								mob.getEntityData().set(LarnachsEntity.DATA_bodyRotationState, mob.nextComboDelay);
								mob.canUseNextAttack = true;
							} else {
								mob.getEntityData().set(LarnachsEntity.DATA_bodyRotationState, 0);
								mob.getEntityData().set(LarnachsEntity.DATA_bodyXRotation, 0);
							}
							{
								final Vec3 _center = new Vec3(x + lookVec.x() * width * 1.9375, y + (1.3 + lookVec.y() * 1.9375) * width, z + lookVec.z() * width * 1.9375);
								for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(width * 1.5 / 2d), e -> true).stream()
										.sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
									if (entityiterator != entity && entityiterator.isAttackable()) {
										if (entityiterator instanceof LivingEntity le)
											le.knockback(knockback, -lookVec.x(), -lookVec.z());
										else if (entityiterator instanceof TraceableEntity)
											entityiterator.setDeltaMovement(entityiterator.position().subtract(entity.position()).normalize().scale(entityiterator.getDeltaMovement().length() + 0.1));
										mob.isHit = true;
										entityiterator.hurt(mob.damageSources().mobAttack(mob), (float) attackDamage);
									}
								}
							}
							if (mob.isHit) {
								mob.setShakePosition(x + lookVec.x() * width * 1.9375, y + (1.3 + lookVec.y() * 1.9375) * width, z + lookVec.z() * width * 1.9375);
								mob.isHit = false;
								mob.getEntityData().set(LarnachsEntity.DATA_shakeOptions, 2);
							}
						} else if (actionTicks == 15 && mob.canUseNextAttack) {
							mob.actionTicks = 0;
							mob.setActionState(mob.nextComboDelay < 0 ? 30 : 31);
							mob.nextComboDelay = Math.abs(mob.nextComboDelay);
							mob.canUseNextAttack = false;
							mob.getEntityData().set(LarnachsEntity.DATA_shakeOptions, 0);
						} else if (actionTicks == 28) {
							mob.actionTicks = 0;
							mob.setActionState(0);
							mob.getEntityData().set(LarnachsEntity.DATA_shakeOptions, 0);
							mob.attackDecisionTicks = 0;
							mob.decidedToUseAttack = false;
						}
						break;
					case 6 :
						if (actionTicks == 0) {
							target = mob.getTarget();
							if (target != null) {
								Vec3 targetVelosity = target.getDeltaMovement();
								Vec3 mobVelosity = mob.getDeltaMovement();
								if (entity instanceof LarnachsEntity _datEntSetI)
									_datEntSetI.getEntityData().set(LarnachsEntity.DATA_bodyXRotation,
											(int) Mth.clamp((int) (Math.toDegrees(Math.atan(((y + entity.getBbWidth() * 1.75 + mobVelosity.y()) - (target.getY() + target.getBbHeight() * 0.5 + -targetVelosity.y()))
													/ Math.sqrt(Math.pow(target.getX() + targetVelosity.x() - x - mobVelosity.x(), 2) + Math.pow(target.getZ() + targetVelosity.z() - z - mobVelosity.z(), 2))))), -50, 60));
								entity.lookAt(EntityAnchorArgument.Anchor.EYES, target.position());
							} else {
								if (entity instanceof LarnachsEntity _datEntSetI)
									_datEntSetI.getEntityData().set(LarnachsEntity.DATA_bodyXRotation, 0);
							}
						} else if (actionTicks == 11) {
							if (world instanceof Level _level) {
								if (!_level.isClientSide()) {
									_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.trident.throw")), SoundSource.HOSTILE, 2, (float) 0.5);
								} else {
									_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.trident.throw")), SoundSource.HOSTILE, 2, (float) 0.5, false);
								}
							}
						} else if (actionTicks == 12) {
							Vec3 lookVec = VectorHelper.calculateViewVector(mob.getEntityData().get(LarnachsEntity.DATA_bodyXRotation), mob.yBodyRot + 45);
							double width = entity.getBbWidth();
							float attackDamage = (float) mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
							double knockback = mob.getAttribute(Attributes.ATTACK_KNOCKBACK).getValue();
							{
								final Vec3 _center = new Vec3(x + lookVec.x() * width * 2.09375, y + (1.3 + lookVec.y() * 2.09375) * width, z + lookVec.z() * width * 2.09375);
								for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(width * 1.5 / 2d), e -> true).stream()
										.sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
									if (entityiterator != entity && entityiterator.isAttackable()) {
										if (entityiterator instanceof LivingEntity le)
											le.knockback(knockback, -lookVec.x(), -lookVec.z());
										else if (entityiterator instanceof TraceableEntity)
											entityiterator.setDeltaMovement(entityiterator.position().subtract(entity.position()).normalize().scale(entityiterator.getDeltaMovement().length() + 0.1));
										entityiterator.invulnerableTime = 0;
										mob.isHit = true;
										entityiterator.hurt(mob.damageSources().mobAttack(mob), (float) attackDamage);
									}
								}
							}
						} else if (actionTicks == 13) {
							Vec3 lookVec = VectorHelper.calculateViewVector(mob.getEntityData().get(LarnachsEntity.DATA_bodyXRotation) + 27, mob.yBodyRot);
							double width = entity.getBbWidth();
							float attackDamage = (float) mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
							double knockback = mob.getAttribute(Attributes.ATTACK_KNOCKBACK).getValue();
							if (mob.getTarget() != null && Math.random() < 0.7) {
								mob.nextComboDelay = Mth.nextInt(mob.getRandom(), 7, 17) * (Math.random() < 0.5 ? 1 : -1);
								mob.getEntityData().set(LarnachsEntity.DATA_bodyRotationState, mob.nextComboDelay);
								mob.canUseNextAttack = true;
							} else {
								mob.getEntityData().set(LarnachsEntity.DATA_bodyRotationState, 0);
								mob.getEntityData().set(LarnachsEntity.DATA_bodyXRotation, 0);
							}
							{
								final Vec3 _center = new Vec3(x + lookVec.x() * width * 2, y + (1.3 + lookVec.y() * 2) * width, z + lookVec.z() * width * 2);
								for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(width * 1.5 / 2d), e -> true).stream()
										.sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
									if (entityiterator != entity && entityiterator.isAttackable()) {
										if (entityiterator instanceof LivingEntity le)
											le.knockback(knockback, -lookVec.x(), -lookVec.z());
										else if (entityiterator instanceof TraceableEntity)
											entityiterator.setDeltaMovement(entityiterator.position().subtract(entity.position()).normalize().scale(entityiterator.getDeltaMovement().length() + 0.1));
										mob.isHit = true;
										entityiterator.hurt(mob.damageSources().mobAttack(mob), (float) attackDamage);
									}
								}
							}
							if (mob.isHit) {
								mob.setShakePosition(x + lookVec.x() * width * 2, y + (1.3 + lookVec.y() * 2) * width, z + lookVec.z() * width * 2);
								mob.isHit = false;
								mob.getEntityData().set(LarnachsEntity.DATA_shakeOptions, 2);
							}
						} else if (actionTicks == 14 && mob.canUseNextAttack) {
							mob.actionTicks = 0;
							mob.setActionState(mob.nextComboDelay < 0 ? 30 : 31);
							mob.nextComboDelay = Math.abs(mob.nextComboDelay);
							mob.canUseNextAttack = false;
							mob.getEntityData().set(LarnachsEntity.DATA_shakeOptions, 0);
						} else if (actionTicks == 28) {
							mob.actionTicks = 0;
							mob.setActionState(0);
							mob.getEntityData().set(LarnachsEntity.DATA_shakeOptions, 0);
							mob.attackDecisionTicks = 0;
							mob.decidedToUseAttack = false;
						}
						break;
					case 7 :
						if (actionTicks == 0) {
							if (world instanceof Level _level) {
								if (!_level.isClientSide()) {
									_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.trident.throw")), SoundSource.HOSTILE, 2, (float) 0.5);
								} else {
									_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.trident.throw")), SoundSource.HOSTILE, 2, (float) 0.5, false);
								}
							}
						} else if (actionTicks == 1) {
							Vec3 lookVec = VectorHelper.calculateViewVector(mob.getEntityData().get(LarnachsEntity.DATA_bodyXRotation) + 27, mob.yBodyRot);
							double width = entity.getBbWidth();
							float attackDamage = (float) mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
							double knockback = mob.getAttribute(Attributes.ATTACK_KNOCKBACK).getValue();
							{
								final Vec3 _center = new Vec3(x + lookVec.x() * width * 1.687, y + (1.3 + lookVec.y() * 1.687) * width, z + lookVec.z() * width * 1.687);
								for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(width * 1.5 / 2d), e -> true).stream()
										.sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
									if (entityiterator != entity && entityiterator.isAttackable()) {
										if (entityiterator instanceof LivingEntity le)
											le.knockback(knockback, -lookVec.x(), -lookVec.z());
										else if (entityiterator instanceof TraceableEntity)
											entityiterator.setDeltaMovement(entityiterator.position().subtract(entity.position()).normalize().scale(entityiterator.getDeltaMovement().length() + 0.1));
										entityiterator.invulnerableTime = 0;
										mob.isHit = true;
										entityiterator.hurt(mob.damageSources().mobAttack(mob), (float) attackDamage);
									}
								}
							}
						} else if (actionTicks == 2) {
							Vec3 lookVec = VectorHelper.calculateViewVector(mob.getEntityData().get(LarnachsEntity.DATA_bodyXRotation) + 23, mob.yBodyRot + 56);
							double width = entity.getBbWidth();
							float attackDamage = (float) mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
							double knockback = mob.getAttribute(Attributes.ATTACK_KNOCKBACK).getValue();
							if (mob.getTarget() != null && Math.random() < 0.6) {
								mob.nextComboDelay = Mth.nextInt(mob.getRandom(), 7, 17) * (Math.random() < 0.5 ? 1 : -1);
								mob.getEntityData().set(LarnachsEntity.DATA_bodyRotationState, mob.nextComboDelay);
								mob.canUseNextAttack = true;
							} else {
								mob.getEntityData().set(LarnachsEntity.DATA_bodyRotationState, 0);
								mob.getEntityData().set(LarnachsEntity.DATA_bodyXRotation, 0);
							}
							{
								final Vec3 _center = new Vec3(x + lookVec.x() * width * 1.9375, y + (1.3 + lookVec.y() * 1.9375) * width, z + lookVec.z() * width * 1.9375);
								for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(width * 1.5 / 2d), e -> true).stream()
										.sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
									if (entityiterator != entity && entityiterator.isAttackable()) {
										if (entityiterator instanceof LivingEntity le)
											le.knockback(knockback, -lookVec.x(), -lookVec.z());
										else if (entityiterator instanceof TraceableEntity)
											entityiterator.setDeltaMovement(entityiterator.position().subtract(entity.position()).normalize().scale(entityiterator.getDeltaMovement().length() + 0.1));
										mob.isHit = true;
										entityiterator.hurt(mob.damageSources().mobAttack(mob), (float) attackDamage);
									}
								}
							}
							if (mob.isHit) {
								mob.setShakePosition(x + lookVec.x() * width * 1.9375, y + (1.3 + lookVec.y() * 1.9375) * width, z + lookVec.z() * width * 1.9375);
								mob.isHit = false;
								mob.getEntityData().set(LarnachsEntity.DATA_shakeOptions, 2);
							}
						} else if (actionTicks == 3 && mob.canUseNextAttack) {
							mob.actionTicks = 0;
							mob.setActionState(mob.nextComboDelay < 0 ? 30 : 31);
							mob.nextComboDelay = Math.abs(mob.nextComboDelay);
							mob.canUseNextAttack = false;
							mob.getEntityData().set(LarnachsEntity.DATA_shakeOptions, 0);
						} else if (actionTicks == 23) {
							mob.actionTicks = 0;
							mob.setActionState(0);
							mob.getEntityData().set(LarnachsEntity.DATA_shakeOptions, 0);
							mob.attackDecisionTicks = 0;
							mob.decidedToUseAttack = false;
						}
						break;
					case 8 :
						if (actionTicks == 0) {
							if (world instanceof Level _level) {
								if (!_level.isClientSide()) {
									_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.trident.throw")), SoundSource.HOSTILE, 2, (float) 0.5);
								} else {
									_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.trident.throw")), SoundSource.HOSTILE, 2, (float) 0.5, false);
								}
							}
						} else if (actionTicks == 1) {
							Vec3 lookVec = VectorHelper.calculateViewVector(mob.getEntityData().get(LarnachsEntity.DATA_bodyXRotation), mob.yBodyRot + 45);
							double width = entity.getBbWidth();
							float attackDamage = (float) mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
							double knockback = mob.getAttribute(Attributes.ATTACK_KNOCKBACK).getValue();
							{
								final Vec3 _center = new Vec3(x + lookVec.x() * width * 2.09375, y + (1.3 + lookVec.y() * 2.09375) * width, z + lookVec.z() * width * 2.09375);
								for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(width * 1.5 / 2d), e -> true).stream()
										.sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
									if (entityiterator != entity && entityiterator.isAttackable()) {
										if (entityiterator instanceof LivingEntity le)
											le.knockback(knockback, -lookVec.x(), -lookVec.z());
										else if (entityiterator instanceof TraceableEntity)
											entityiterator.setDeltaMovement(entityiterator.position().subtract(entity.position()).normalize().scale(entityiterator.getDeltaMovement().length() + 0.1));
										entityiterator.invulnerableTime = 0;
										mob.isHit = true;
										entityiterator.hurt(mob.damageSources().mobAttack(mob), (float) attackDamage);
									}
								}
							}
						} else if (actionTicks == 2) {
							Vec3 lookVec = VectorHelper.calculateViewVector(mob.getEntityData().get(LarnachsEntity.DATA_bodyXRotation) + 27, mob.yBodyRot);
							double width = entity.getBbWidth();
							float attackDamage = (float) mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
							double knockback = mob.getAttribute(Attributes.ATTACK_KNOCKBACK).getValue();
							if (mob.getTarget() != null && Math.random() < 0.6) {
								mob.nextComboDelay = Mth.nextInt(mob.getRandom(), 7, 17) * (Math.random() < 0.5 ? 1 : -1);
								mob.getEntityData().set(LarnachsEntity.DATA_bodyRotationState, mob.nextComboDelay);
								mob.canUseNextAttack = true;
							} else {
								mob.getEntityData().set(LarnachsEntity.DATA_bodyRotationState, 0);
								mob.getEntityData().set(LarnachsEntity.DATA_bodyXRotation, 0);
							}
							{
								final Vec3 _center = new Vec3(x + lookVec.x() * width * 2, y + (1.3 + lookVec.y() * 2) * width, z + lookVec.z() * width * 2);
								for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(width * 1.5 / 2d), e -> true).stream()
										.sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
									if (entityiterator != entity && entityiterator.isAttackable()) {
										if (entityiterator instanceof LivingEntity le)
											le.knockback(knockback, -lookVec.x(), -lookVec.z());
										else if (entityiterator instanceof TraceableEntity)
											entityiterator.setDeltaMovement(entityiterator.position().subtract(entity.position()).normalize().scale(entityiterator.getDeltaMovement().length() + 0.1));
										mob.isHit = true;
										entityiterator.hurt(mob.damageSources().mobAttack(mob), (float) attackDamage);
									}
								}
							}
							if (mob.isHit) {
								mob.setShakePosition(x + lookVec.x() * width * 2, y + (1.3 + lookVec.y() * 2) * width, z + lookVec.z() * width * 2);
								mob.isHit = false;
								mob.getEntityData().set(LarnachsEntity.DATA_shakeOptions, 2);
							}
						} else if (actionTicks == 3 && mob.canUseNextAttack) {
							mob.actionTicks = 0;
							mob.setActionState(mob.nextComboDelay < 0 ? 30 : 31);
							mob.nextComboDelay = Math.abs(mob.nextComboDelay);
							mob.canUseNextAttack = false;
							mob.getEntityData().set(LarnachsEntity.DATA_shakeOptions, 0);
						} else if (actionTicks == 23) {
							mob.actionTicks = 0;
							mob.setActionState(0);
							mob.getEntityData().set(LarnachsEntity.DATA_shakeOptions, 0);
							mob.attackDecisionTicks = 0;
							mob.decidedToUseAttack = false;
						}
						break;
					case 30 :
						if (actionTicks == 0) {
							if (mob.nextComboDelay != 0) {
								target = mob.getTarget();
								if (entity instanceof LarnachsEntity _datEntSetI)
									_datEntSetI.getEntityData().set(LarnachsEntity.DATA_bodyRotationState, 0);
								if (target != null) {
									Vec3 targetVelosity = target.getDeltaMovement();
									Vec3 mobVelosity = mob.getDeltaMovement();
									if (entity instanceof LarnachsEntity _datEntSetI)
										_datEntSetI.getEntityData().set(LarnachsEntity.DATA_bodyXRotation,
												(int) Mth.clamp((int) (Math.toDegrees(Math.atan(((y + entity.getBbWidth() * 1.75 + mobVelosity.y()) - (target.getY() + target.getBbHeight() * 0.5 + -targetVelosity.y()))
														/ Math.sqrt(Math.pow(target.getX() + targetVelosity.x() - x - mobVelosity.x(), 2) + Math.pow(target.getZ() + targetVelosity.z() - z - mobVelosity.z(), 2))))), -50, 60));
									entity.lookAt(EntityAnchorArgument.Anchor.EYES, target.position());
								} else {
									if (entity instanceof LarnachsEntity _datEntSetI)
										_datEntSetI.getEntityData().set(LarnachsEntity.DATA_bodyXRotation, 0);
								}
							} else {
								mob.actionTicks = 0;
								mob.setActionState(0);
							}
						} else if (actionTicks == mob.nextComboDelay - 6) {
							mob.actionTicks = 0;
							mob.setActionState(7);
							if (mob.getTarget() != null)
								entity.lookAt(EntityAnchorArgument.Anchor.EYES, mob.getTarget().position());
						}
						break;
					case 31 :
						if (actionTicks == 0) {
							if (mob.nextComboDelay != 0) {
								target = mob.getTarget();
								if (entity instanceof LarnachsEntity _datEntSetI)
									_datEntSetI.getEntityData().set(LarnachsEntity.DATA_bodyRotationState, 0);
								if (target != null) {
									Vec3 targetVelosity = target.getDeltaMovement();
									Vec3 mobVelosity = mob.getDeltaMovement();
									if (entity instanceof LarnachsEntity _datEntSetI)
										_datEntSetI.getEntityData().set(LarnachsEntity.DATA_bodyXRotation,
												(int) Mth.clamp((int) (Math.toDegrees(Math.atan(((y + entity.getBbWidth() * 1.75 + mobVelosity.y()) - (target.getY() + target.getBbHeight() * 0.5 + -targetVelosity.y()))
														/ Math.sqrt(Math.pow(target.getX() + targetVelosity.x() - x - mobVelosity.x(), 2) + Math.pow(target.getZ() + targetVelosity.z() - z - mobVelosity.z(), 2))))), -50, 60));
									entity.lookAt(EntityAnchorArgument.Anchor.EYES, target.position());
								} else {
									if (entity instanceof LarnachsEntity _datEntSetI)
										_datEntSetI.getEntityData().set(LarnachsEntity.DATA_bodyXRotation, 0);
								}
							} else {
								mob.actionTicks = 0;
								mob.setActionState(0);
							}
						} else if (actionTicks == mob.nextComboDelay - 6) {
							mob.actionTicks = 0;
							mob.setActionState(8);
							if (mob.getTarget() != null)
								entity.lookAt(EntityAnchorArgument.Anchor.EYES, mob.getTarget().position());
						}
						break;
					case 41 :
						if (actionTicks == 0) {
							if (mob.getTarget() != null)
								entity.lookAt(EntityAnchorArgument.Anchor.EYES, mob.getTarget().position());
						} else if (actionTicks == 12) {
							if (world instanceof Level _level) {
								if (!_level.isClientSide()) {
									_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.trident.throw")), SoundSource.HOSTILE, 2, (float) 0.5);
								} else {
									_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.trident.throw")), SoundSource.HOSTILE, 2, (float) 0.5, false);
								}
							}
						} else if (actionTicks == 14) {
							Vec3 lookVec = VectorHelper.calculateFlatViewVector(mob.yBodyRot);
							double width = entity.getBbWidth();
							float attackDamage = (float) mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * 1.5f;
							double knockback = mob.getAttribute(Attributes.ATTACK_KNOCKBACK).getValue();
							mob.setShakePosition(x + (lookVec.x() * 1.5) * width, y, z + (lookVec.z() * 1.5) * width);
							if (mob.getTarget() != null && Math.random() < 0.5) {
								mob.nextComboDelay = Mth.nextInt(mob.getRandom(), 7, 17);
								mob.getEntityData().set(LarnachsEntity.DATA_bodyRotationState, mob.nextComboDelay);
								mob.canUseNextAttack = true;
							}
							mob.getEntityData().set(LarnachsEntity.DATA_shakeOptions, 1);
							for (Entity entityiterator : world.getEntities(null, new AABB(x + (0.5 + lookVec.x()) * width, y, z + (0.5 + lookVec.z()) * width, x + (-0.5 + lookVec.x()) * width, y + width * 1.5, z + (-0.5 + lookVec.z()) * width))) {
								if (entityiterator != entity && entityiterator.isAttackable()) {
									if (entityiterator instanceof LivingEntity le)
										le.knockback(knockback, -lookVec.x(), -lookVec.z());
									else if (entityiterator instanceof TraceableEntity)
										entityiterator.setDeltaMovement(entityiterator.position().subtract(entity.position()).normalize().scale(entityiterator.getDeltaMovement().length() + 0.1));
									entityiterator.push(0, -0.5, 0);
									entityiterator.invulnerableTime = 0;
									entityiterator.hurt(mob.damageSources().mobAttack(mob), (float) attackDamage);
								}
							}
							for (Entity entityiterator : world.getEntities(null,
									new AABB(x + (0.5 + lookVec.x() * 2) * width, y, z + (0.5 + lookVec.z() * 2) * width, x + (-0.5 + lookVec.x() * 2) * width, y + width * 1.5, z + (-0.5 + lookVec.z() * 2) * width))) {
								if (entityiterator != entity && entityiterator.isAttackable()) {
									if (entityiterator instanceof LivingEntity le)
										le.knockback(knockback, -lookVec.x(), -lookVec.z());
									else if (entityiterator instanceof TraceableEntity)
										entityiterator.setDeltaMovement(entityiterator.position().subtract(entity.position()).normalize().scale(entityiterator.getDeltaMovement().length() + 0.1));
									entityiterator.push(0, -0.5, 0);
									entityiterator.hurt(mob.damageSources().mobAttack(mob), (float) attackDamage);
								}
							}
						} else if (actionTicks == 15 && mob.canUseNextAttack) {
							mob.actionTicks = 0;
							mob.setActionState(31);
							mob.nextComboDelay = Math.abs(mob.nextComboDelay);
							mob.canUseNextAttack = false;
							mob.getEntityData().set(LarnachsEntity.DATA_shakeOptions, 0);
						} else if (actionTicks == 23) {
							mob.actionTicks = 0;
							mob.setActionState(0);
							mob.getEntityData().set(LarnachsEntity.DATA_shakeOptions, 0);
							mob.attackDecisionTicks = 0;
							mob.decidedToUseAttack = false;
						}
						break;
					case 42 :
						if (actionTicks == 0) {
							target = mob.getTarget();
							if (target != null) {
								entity.lookAt(EntityAnchorArgument.Anchor.EYES, target.position());
								Vec3 targetVelosity = target.getDeltaMovement();
								Vec3 mobVelosity = mob.getDeltaMovement();
								if (entity instanceof LarnachsEntity _datEntSetI)
									_datEntSetI.getEntityData().set(LarnachsEntity.DATA_bodyXRotation,
											(int) Mth.clamp((int) (Math.toDegrees(Math.atan(((y + entity.getBbWidth() * 0.95 + mobVelosity.y()) - (target.getY() + target.getBbHeight() * 0.5 + targetVelosity.y()))
													/ Math.sqrt(Math.pow(target.getX() + targetVelosity.x() - x - mobVelosity.x(), 2) + Math.pow(target.getZ() + targetVelosity.z() - z - mobVelosity.z(), 2))))), -70, 70));
							} else {
								if (entity instanceof LarnachsEntity _datEntSetI)
									_datEntSetI.getEntityData().set(LarnachsEntity.DATA_bodyXRotation, 0);
							}
						} else if (actionTicks == 12) {
							if (world instanceof Level _level) {
								if (!_level.isClientSide()) {
									_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.trident.throw")), SoundSource.HOSTILE, 2, (float) 0.5);
								} else {
									_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.trident.throw")), SoundSource.HOSTILE, 2, (float) 0.5, false);
								}
							}
						} else if (actionTicks == 14) {
							Vec3 lookVec = VectorHelper.calculateViewVector(mob.getEntityData().get(LarnachsEntity.DATA_bodyXRotation) + 4, mob.yBodyRot);
							double width = entity.getBbWidth();
							float attackDamage = (float) mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * 2;
							double knockback = mob.getAttribute(Attributes.ATTACK_KNOCKBACK).getValue();
							mob.getEntityData().set(LarnachsEntity.DATA_bodyXRotation, 0);
							{
								final Vec3 _center = new Vec3(x + lookVec.x() * width * 3.54295, y + (0.95 + lookVec.y() * 3.54295) * width, z + lookVec.z() * width * 3.54295);
								for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(width / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
										.toList()) {
									if (entityiterator != entity && entityiterator.isAttackable()) {
										if (entityiterator instanceof LivingEntity le)
											le.knockback(knockback, -lookVec.x(), -lookVec.z());
										else if (entityiterator instanceof TraceableEntity)
											entityiterator.setDeltaMovement(entityiterator.position().subtract(entity.position()).normalize().scale(entityiterator.getDeltaMovement().length() + 0.1));
										entityiterator.invulnerableTime = 0;
										mob.isHit = true;
										entityiterator.hurt(mob.damageSources().mobAttack(mob), (float) attackDamage);
									}
								}
							}
							{
								final Vec3 _center = new Vec3(x + lookVec.x() * width * 2.54, y + (0.95 + lookVec.y() * 2.54) * width, z + lookVec.z() * width * 2.54);
								for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(width / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
										.toList()) {
									if (entityiterator != entity && entityiterator.isAttackable()) {
										if (entityiterator instanceof LivingEntity le)
											le.knockback(knockback, -lookVec.x(), -lookVec.z());
										else if (entityiterator instanceof TraceableEntity)
											entityiterator.setDeltaMovement(entityiterator.position().subtract(entity.position()).normalize().scale(entityiterator.getDeltaMovement().length() + 0.1));
										mob.isHit = true;
										entityiterator.hurt(mob.damageSources().mobAttack(mob), (float) attackDamage);
									}
								}
							}
							if (mob.isHit) {
								mob.setShakePosition(x + lookVec.x() * width * 2, y + (0.95 + lookVec.y() * 2) * width, z + lookVec.z() * width * 2);
								mob.isHit = false;
								mob.getEntityData().set(LarnachsEntity.DATA_shakeOptions, 2);
							}
						} else if (actionTicks == 28) {
							mob.actionTicks = 0;
							mob.setActionState(0);
							mob.getEntityData().set(LarnachsEntity.DATA_shakeOptions, 0);
						}
						break;
					case 43 :
						if (actionTicks == 0) {
							if (mob.getTarget() != null)
								entity.lookAt(EntityAnchorArgument.Anchor.EYES, mob.getTarget().position());
						} else if (actionTicks == 15) {
							if (entity.onGround()) {
								if (world instanceof Level _level) {
									if (!_level.isClientSide()) {
										_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.zombie.attack_iron_door")), SoundSource.HOSTILE, 2, (float) 0.5);
									} else {
										_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.zombie.attack_iron_door")), SoundSource.HOSTILE, 2, (float) 0.5, false);
									}
								}
							} else {
								mob.actionTicks = 0;
								mob.setActionState(0);
							}
						} else if (actionTicks == 16) {
							Vec3 lookVec = VectorHelper.calculateFlatViewVector(mob.yBodyRot);
							mob.setDeltaMovement(lookVec.x() * mob.nextJumpDist, mob.nextJumpHeight, lookVec.z() * mob.nextJumpDist);
						} else if (actionTicks > 16 && (entity.onGround() || entity.isInWaterOrBubble())) {
							mob.actionTicks = 0;
							mob.setActionState(0);
						} else if (actionTicks == 40) {
							mob.actionTicks = 0;
							mob.setActionState(0);
						}
						break;
				}
			}
		}
	}
}