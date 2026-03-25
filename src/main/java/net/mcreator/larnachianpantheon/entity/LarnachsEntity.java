package net.mcreator.larnachianpantheon.entity;

import org.joml.Vector3f;

import org.checkerframework.checker.units.qual.s;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.client.animation.AnimationDefinition;

import net.mcreator.larnachianpantheon.procedures.LarnachsOnEntityTickUpdateProcedure;
import net.mcreator.larnachianpantheon.init.LarnachianPantheonModEntities;
import net.mcreator.larnachianpantheon.configuration.LarnachsModConfigurationConfiguration;
import net.mcreator.larnachianpantheon.client.model.animations.LarnachsAnimation;
import net.mcreator.larnachianpantheon.*;

import java.util.function.Predicate;
import java.util.concurrent.CopyOnWriteArrayList;

import com.crimsonsteve.crimsonsteveapi.utils.animations.UsualAnimation;
import com.crimsonsteve.crimsonsteveapi.utils.animations.CustomFadeInOutAnimation;
import com.crimsonsteve.crimsonsteveapi.utils.animations.ContinuousAnimation;
import com.crimsonsteve.crimsonsteveapi.utils.animations.BaseAnimation;
import com.crimsonsteve.crimsonsteveapi.interfaces.IStackableAnimationMob;
import com.crimsonsteve.crimsonsteveapi.interfaces.IActionStateMob;

public class LarnachsEntity extends Monster implements IActionStateMob, IStackableAnimationMob, IForceBodyRotation {
	public static final EntityDataAccessor<Integer> DATA_actionState = SynchedEntityData.defineId(LarnachsEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> DATA_walkState = SynchedEntityData.defineId(LarnachsEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> DATA_bodyRotationState = SynchedEntityData.defineId(LarnachsEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> DATA_bodyXRotation = SynchedEntityData.defineId(LarnachsEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> DATA_forcedRotation = SynchedEntityData.defineId(LarnachsEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> DATA_shakeOptions = SynchedEntityData.defineId(LarnachsEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Vector3f> DATA_shakePosition = SynchedEntityData.defineId(LarnachsEntity.class, EntityDataSerializers.VECTOR3);
	public static final EntityDataAccessor<Float> DATA_movingForward = SynchedEntityData.defineId(LarnachsEntity.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> DATA_movingSide = SynchedEntityData.defineId(LarnachsEntity.class, EntityDataSerializers.FLOAT);
	public final AnimationState animationState0 = new AnimationState();

	public LarnachsEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(LarnachianPantheonModEntities.LARNACHS.get(), world);
	}

	public LarnachsEntity(EntityType<LarnachsEntity> type, Level world) {
		super(type, world);
		setMaxUpStep(2.1f);
		xpReward = 700;
		setNoAi(false);
		setPersistenceRequired();
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_actionState, 0);
		this.entityData.define(DATA_walkState, 0);
		this.entityData.define(DATA_bodyRotationState, 0);
		this.entityData.define(DATA_bodyXRotation, 0);
		this.entityData.define(DATA_forcedRotation, 0);
		this.entityData.define(DATA_shakeOptions, 0);
		this.entityData.define(DATA_shakePosition, new Vector3f(0, 0, 0));
		this.entityData.define(DATA_movingForward, 0f);
		this.entityData.define(DATA_movingSide, 0f);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		goalSelector.addGoal(0, new DoNothingGoal(this));
		larnMoveControl = new LarnachsMoveControl(this);
		moveControl = larnMoveControl;
		goalSelector.addGoal(1, new StrafingTargetGoal(this, getBbWidth() * 6, getBbWidth() * 8, 0.1f, 1) {
			public boolean canUse() {
				return !LarnachsEntity.this.decidedToUseAttack && super.canUse();
			}

			public boolean canContinueToUse() {
				return !LarnachsEntity.this.decidedToUseAttack && super.canContinueToUse();
			}
		});
		goalSelector.addGoal(1, new StrafingTargetGoal(this, 0, getBbWidth() * 0.5f, 0.3f, 1) {
			public boolean canUse() {
				return LarnachsEntity.this.decidedToUseAttack && super.canUse();
			}

			public boolean canContinueToUse() {
				return LarnachsEntity.this.decidedToUseAttack && super.canContinueToUse();
			}
		});
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1, false) {
			@Override
			protected double getAttackReachSqr(LivingEntity entity) {
				return 0;
			}
		});
		this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1));
		this.targetSelector.addGoal(5, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(6, new FloatGoal(this));
		if (LarnachsModConfigurationConfiguration.RAMPAGEMODE.get())
			targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, false));
		else
			targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, false));
	}

	public void setShakePosition(double x, double y, double z) {
		entityData.set(DATA_shakePosition, new Vector3f((float) x, (float) y, (float) z));
	}

	public void setMoveForwardAndSide(float f, float s) {
		int forwardAmp = f > 0 ? 1 : -1, sideAmp = s > 0 ? 1 : -1;
		Vec2 md = new Vec2(f * f, s * s).normalized();
		entityData.set(DATA_movingForward, md.x * forwardAmp);
		entityData.set(DATA_movingSide, md.y * sideAmp);
	}

	public float getVoicePitch() {
		return 0.5f;
	}

	public float walkAmplitude, walkAmplitudeO, forcedRotation;
	public double nextJumpDist, //
			nextJumpHeight, //
			prevYDeltaMovement;
	public LarnachsMoveControl larnMoveControl;
	public int bodyXRot, //
			bodyXRotO, //
			timeTookForRot = 10, //
			bodyXRotSpeed, //
			actionTicks, //
			nextComboDelay, //
			forcedRotationSpeed, //
			attackDecisionTicks;
	public CopyOnWriteArrayList<BaseAnimation> animList = new CopyOnWriteArrayList<>();
	public boolean canUseNextAttack = false, isHit = false, decidedToUseAttack = false, wasOnGround;

	public CopyOnWriteArrayList<? extends BaseAnimation> getAnimList() {
		return animList;
	}

	public int getActionState() {
		return entityData.get(DATA_actionState);
	}

	public void setActionState(int input) {
		entityData.set(DATA_actionState, input);
	}

	public boolean isInAction() {
		return entityData.get(DATA_actionState) > 40;
	}

	public boolean isInDeathAction() {
		return entityData.get(DATA_actionState) == 100;
	}

	private void addActionAwareWalkAnimation(AnimationDefinition animationType, float amplitude, int walkState) {
		UsualAnimation anim = new UsualAnimation(animationType, this.tickCount, amplitude, this.animList, 10, 15) {
			{
				this.remainingFadeTicks = 5;
			}

			@Override
			protected boolean evaluateActive() {
				return super.evaluateActive() && walkState != 0 && !isInAction();
			}
		};
		this.animList.add(anim);
	}

	private void strongKnockback(Entity p_33340_) {
		double d0 = p_33340_.getX() - this.getX();
		double d1 = p_33340_.getZ() - this.getZ();
		double d2 = Math.max(d0 * d0 + d1 * d1, 0.1D);
		p_33340_.push(d0 / d2, 0.5D, d1 / d2);
	}

	private static final Predicate<Entity> IS_ON_GROUND = (p_33346_) -> {
		return p_33346_.isAlive() && (p_33346_.onGround() || p_33346_.isInWaterOrBubble());
	};

	protected int calculateFallDamage(float p_21237_, float p_21238_) {
		if (!level().isClientSide()) {
			if (p_21237_ > 3) {
				float attackDamage = (float) getAttribute(Attributes.ATTACK_DAMAGE).getValue() * 0.5f;
				playSound(SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR, 2, 0.5f);
				for (LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D), IS_ON_GROUND)) {
					if (livingentity != this) {
						livingentity.hurt(this.damageSources().mobAttack(this), attackDamage);
					}
					this.strongKnockback(livingentity);
				}
			}
		}
		return super.calculateFallDamage(p_21237_ - 5, p_21238_);
	}

	public void onSyncedDataUpdated(EntityDataAccessor<?> p_219422_) {
		super.onSyncedDataUpdated(p_219422_);
		if (level().isClientSide()) {
			if (DATA_walkState.equals(p_219422_)) {
				float forward = this.entityData.get(DATA_movingForward);
				float side = this.entityData.get(DATA_movingSide);
				float absForward = Math.abs(forward);
				float absSide = Math.abs(side);
				float wholeAxisAmp = Math.max(absForward, absSide);
				int walkState = entityData.get(DATA_walkState);
				boolean isRight = (walkState & 1) == 1;
				if (absForward > 0.001F) {
					AnimationDefinition fwdAnim = forward > 0 ? (isRight ? LarnachsAnimation.walkForwardRight : LarnachsAnimation.walkForwardLeft) : (isRight ? LarnachsAnimation.walkBackRight : LarnachsAnimation.walkBackLeft);
					this.addActionAwareWalkAnimation(fwdAnim, absForward, walkState);
				}
				if (absSide > 0.001F) {
					AnimationDefinition sideAnim = side > 0 ? (isRight ? LarnachsAnimation.walkLeftRight : LarnachsAnimation.walkLeftLeft) : (isRight ? LarnachsAnimation.walkRightRight : LarnachsAnimation.walkRightLeft);
					this.addActionAwareWalkAnimation(sideAnim, absSide, walkState);
				}
				if (wholeAxisAmp > 0.001F) {
					this.addActionAwareWalkAnimation(LarnachsAnimation.walkWholeAxisOnly, wholeAxisAmp, walkState);
				}
			} else if (DATA_actionState.equals(p_219422_)) {
				BaseAnimation anim;
				switch (this.entityData.get(DATA_actionState)) {
					case 0 :
						timeTookForRot = 10;
						forcedRotationSpeed = (int) Math.abs(Mth.wrapDegrees(forcedRotation - yBodyRot) * 0.1f) + 1;
						break;
					case 5 :
						timeTookForRot = 10;
						forcedRotationSpeed = (int) Math.abs(Mth.wrapDegrees(forcedRotation - yBodyRot) * 0.1f) + 1;
						anim = new ContinuousAnimation(LarnachsAnimation.SlashLR, tickCount, 1, animList, 8, () -> getActionState() == 5) {
							{
								remainingFadeTicks = 8;
							}
						};
						anim.fullyActive = true;
						animList.add(anim);
						break;
					case 6 :
						timeTookForRot = 10;
						forcedRotationSpeed = (int) Math.abs(Mth.wrapDegrees(forcedRotation - yBodyRot) * 0.1f) + 1;
						animList.add(new ContinuousAnimation(LarnachsAnimation.SlashRL, tickCount, 1, animList, 8, () -> getActionState() == 6));
						break;
					case 7 :
						timeTookForRot = 10;
						forcedRotationSpeed = (int) Math.abs(Mth.wrapDegrees(forcedRotation - yBodyRot) * 0.1f) + 1;
						animList.add(new ContinuousAnimation(LarnachsAnimation.SlashLRStart, tickCount, 1, animList, 4, () -> getActionState() == 7) {
							{
								remainingFadeTicks = 4;
								fullyActive = true;
							}
						});
						break;
					case 8 :
						timeTookForRot = 10;
						forcedRotationSpeed = (int) Math.abs(Mth.wrapDegrees(forcedRotation - yBodyRot) * 0.1f) + 1;
						animList.add(new ContinuousAnimation(LarnachsAnimation.SlashRLStart, tickCount, 1, animList, 4, () -> getActionState() == 8) {
							{
								remainingFadeTicks = 4;
								fullyActive = true;
							}
						});
						break;
					case 30 :
						timeTookForRot = Math.abs(entityData.get(DATA_bodyRotationState)) - 4;
						forcedRotationSpeed = (int) (Math.abs(Mth.wrapDegrees(forcedRotation - entityData.get(DATA_forcedRotation)) / (float) timeTookForRot)) + 1;
						animList.add(new ContinuousAnimation(LarnachsAnimation.SlashLRReady, tickCount, 1, animList, timeTookForRot, () -> getActionState() == 30) {
							{
								isInstant = true;
							}
						});
						break;
					case 31 :
						timeTookForRot = Math.abs(entityData.get(DATA_bodyRotationState)) - 4;
						forcedRotationSpeed = (int) (Math.abs(Mth.wrapDegrees(forcedRotation - entityData.get(DATA_forcedRotation)) / (float) timeTookForRot)) + 1;
						animList.add(new ContinuousAnimation(LarnachsAnimation.SlashRLReady, tickCount, 1, animList, timeTookForRot, () -> getActionState() == 31) {
							{
								isInstant = true;
							}
						});
						break;
					case 41 :
						timeTookForRot = 10;
						forcedRotationSpeed = (int) Math.abs(Mth.wrapDegrees(forcedRotation - yBodyRot) * 0.1f) + 1;
						animList.add(new ContinuousAnimation(LarnachsAnimation.Chop2, tickCount, 1, animList, 5, () -> getActionState() == 41) {
							{
								remainingFadeTicks = 5;
								fullyActive = true;
							}
						});
						break;
					case 42 :
						timeTookForRot = 10;
						forcedRotationSpeed = (int) Math.abs(Mth.wrapDegrees(forcedRotation - yBodyRot) * 0.1f) + 1;
						animList.add(new ContinuousAnimation(LarnachsAnimation.impale, tickCount, 1, animList, 5, () -> getActionState() == 42) {
							{
								remainingFadeTicks = 5;
								fullyActive = true;
							}
						});
						break;
					case 43 :
						timeTookForRot = 10;
						forcedRotationSpeed = (int) Math.abs(Mth.wrapDegrees(forcedRotation - yBodyRot) * 0.1f) + 1;
						animList.add(new ContinuousAnimation(LarnachsAnimation.jumpFront, tickCount, 1.3f, animList, 5, () -> getActionState() == 43) {
							{
								remainingFadeTicks = 5;
								fullyActive = true;
							}
						});
						break;
					case 100 :
						break;
				}
				forcedRotation = yBodyRot;
			} else if (DATA_forcedRotation.equals(p_219422_)) {
				forcedRotationSpeed = (int) (Math.abs(Mth.wrapDegrees(forcedRotation - entityData.get(DATA_forcedRotation)) / (float) timeTookForRot)) + 1;
				forcedRotation = entityData.get(DATA_forcedRotation);
			} else if (DATA_bodyXRotation.equals(p_219422_)) {
				bodyXRotSpeed = Math.max(Math.abs(entityData.get(DATA_bodyXRotation) - bodyXRot) / timeTookForRot, 1);
			} else if (DATA_bodyRotationState.equals(p_219422_)) {
				int state = entityData.get(DATA_bodyRotationState);
				if (state != 0) {
					int rot = state > 0 ? 1 : -1;
					state = Math.abs(state);
					BaseAnimation anim;
					anim = new CustomFadeInOutAnimation(LarnachsAnimation.upperBodyRotation, tickCount, rot, animList, state, state + 1) {
						public float getFadeInFactor(float fadeFactor) {
							float amp = Mth.sin(fadeFactor * Mth.PI / 2);
							return amp * amp;
						}

						public float getFadeOutFactor(float fadeFactor) {
							return 0;
						}
					};
					anim.isInstant = true;
					animList.add(anim);
				}
			} else if (DATA_shakeOptions.equals(p_219422_)) {
				Vec3 shakePos = new Vec3(entityData.get(DATA_shakePosition));
				switch (this.entityData.get(DATA_shakeOptions)) {
					case 1 :
						CameraShake.addShake(new CameraShakeSegment(shakePos, 1000, 5, getBbWidth() * 15));
						break;
					case 2 :
						CameraShake.addShake(new CameraShakeSegment(shakePos, 350, 5, getBbWidth() * 7));
						break;
				}
			}
		}
	}

	@Override
	public void die(DamageSource source) {
		super.die(source);
		if (!level().isClientSide())
			setActionState(100);
	}

	protected float getDamageAfterArmorAbsorb(DamageSource source, float amount) {
		float finalAmount = super.getDamageAfterArmorAbsorb(source, amount);
		if (!level().isClientSide()) {
			decidedToUseAttack = true;
			if (finalAmount > 0 && source.getEntity() != null && getTarget() != null && !isAlliedTo(source.getEntity()) && source.getEntity() instanceof LivingEntity se) {
				if (se.canBeSeenAsEnemy()) {
					LivingEntity target = getTarget();
					if (distanceToSqr(target) > distanceToSqr(se))
						setTarget(se);
				}
			}
		}
		return finalAmount;
	}

	public void lookAt(EntityAnchorArgument.Anchor p_20033_, Vec3 p_20034_) {
		super.lookAt(p_20033_, p_20034_);
		if (entityData.get(DATA_actionState) > 4)
			entityData.set(DATA_forcedRotation, (int) this.getYRot());
	}

	public void lookAt(Entity p_21392_, float p_21393_, float p_21394_) {
		super.lookAt(p_21392_, p_21393_, p_21394_);
		if (entityData.get(DATA_actionState) > 4)
			entityData.set(DATA_forcedRotation, (int) this.getYRot());
	}

	public int getHeadRotSpeed() {
		return 10;
	}

	public PathNavigation createNavigation(Level p21480) {
		return new LarnachsPathNavigation(this, p21480);
	}

	public int getRotationSpeed() {
		return forcedRotationSpeed;
	}

	public float getSupposedRotation() {
		return forcedRotation;
	}

	protected BodyRotationControl createBodyControl() {
		return new LarnachsBodyRotationControl(this);
	}

	/*
	implements IActionState, IStackableAnimationMob, IForceBodyRotation
		public LarnachsRenderer(EntityRendererProvider.Context context) {
			super(context, new AnimatedModel(context.bakeLayer(ModelLarnachs.LAYER_LOCATION)), 1.5f);
			this.addLayer(new RenderLayer<LarnachsEntity, ModelLarnachs<LarnachsEntity>>(this) {
				static final ResourceLocation LAYER_TEXTURE[] = new ResourceLocation[]{//
						new ResourceLocation("larnachian_pantheon:textures/entities/larnachs_layer1.png")//
						, new ResourceLocation("larnachian_pantheon:textures/entities/larnachs_layer2.png")//
						, new ResourceLocation("larnachian_pantheon:textures/entities/larnachs_layer3.png")//
				};
				@Override
				public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, LarnachsEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
					VertexConsumer vertexConsumer = bufferSource.getBuffer(CustomRenderTypes.eyes(LAYER_TEXTURE[(entity.tickCount / 2) % 3]));
					this.getParentModel().renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
				}
			});
		}
		@Override
		public ResourceLocation getTextureLocation(LarnachsEntity entity) {
			return new ResourceLocation("larnachian_pantheon:textures/entities/larnachs_no_layer.png");
		}
		private static final class AnimatedModel extends ModelLarnachs<LarnachsEntity> {
			private final ModelPart root;
			private float partialTicks;
			private final HierarchicalModel animator = new HierarchicalModel<LarnachsEntity>() {
				@Override
				public ModelPart root() {
					return root;
				}
				@Override
				public void setupAnim(LarnachsEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
					this.root().getAllParts().forEach(ModelPart::resetPose);
					partialTicks = ageInTicks - entity.tickCount;
					float idleAmplitude = Mth.lerp(partialTicks, entity.walkAmplitudeO, entity.walkAmplitude) / 5f;
					AnimUtil.animateAmplitude(this, entity.animationState0, LarnachsAnimation.idle, ageInTicks, 1f, idleAmplitude);
					AnimUtil.animateAmplitude(this, entity.animationState0, LarnachsAnimation.basePose2, ageInTicks, 1f, 1 - idleAmplitude);
					entity.animateMob(this, ageInTicks, partialTicks);
				}
			};
			public AnimatedModel(ModelPart root) {
				super(root);
				this.root = root;
			}
			@Override
			public void setupAnim(LarnachsEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
				animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
				float cameraYRot = Minecraft.getInstance().gameRenderer.getMainCamera().getYRot();
				float f = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
				neck.yRot = (cameraYRot - f) * Mth.DEG_TO_RAD;
				lowerBodyRot.xRot = Mth.rotLerp(partialTicks, entity.bodyXRotO, entity.bodyXRot) * Mth.DEG_TO_RAD;
				super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			}
		}
	*/
	{
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEFINED;
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.axe.scrape"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.axe.scrape"));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("DataactionState", this.entityData.get(DATA_actionState));
		compound.putInt("DatawalkState", this.entityData.get(DATA_walkState));
		compound.putInt("DatabodyRotationState", this.entityData.get(DATA_bodyRotationState));
		compound.putInt("DatabodyXRotation", this.entityData.get(DATA_bodyXRotation));
		compound.putInt("DataforcedRotation", this.entityData.get(DATA_forcedRotation));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("DataactionState"))
			this.entityData.set(DATA_actionState, compound.getInt("DataactionState"));
		if (compound.contains("DatawalkState"))
			this.entityData.set(DATA_walkState, compound.getInt("DatawalkState"));
		if (compound.contains("DatabodyRotationState"))
			this.entityData.set(DATA_bodyRotationState, compound.getInt("DatabodyRotationState"));
		if (compound.contains("DatabodyXRotation"))
			this.entityData.set(DATA_bodyXRotation, compound.getInt("DatabodyXRotation"));
		if (compound.contains("DataforcedRotation"))
			this.entityData.set(DATA_forcedRotation, compound.getInt("DataforcedRotation"));
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide()) {
			this.animationState0.animateWhen(true, this.tickCount);
		}
	}

	@Override
	public void baseTick() {
		super.baseTick();
		LarnachsOnEntityTickUpdateProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
	}

	public static void init() {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.4);
		builder = builder.add(Attributes.MAX_HEALTH, 500);
		builder = builder.add(Attributes.ARMOR, 15);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 10);
		builder = builder.add(Attributes.FOLLOW_RANGE, 64);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.9);
		builder = builder.add(Attributes.ATTACK_KNOCKBACK, 2);
		return builder;
	}
}