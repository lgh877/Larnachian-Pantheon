package net.mcreator.larnachianpantheon.client.renderer;

import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.Minecraft;

import net.mcreator.larnachianpantheon.entity.LarnachsEntity;
import net.mcreator.larnachianpantheon.client.model.animations.LarnachsAnimation;
import net.mcreator.larnachianpantheon.client.model.ModelLarnachs;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

import com.crimsonsteve.crimsonsteveapi.utils.animations.AnimUtil;

public class LarnachsRenderer extends MobRenderer<LarnachsEntity, LivingEntityRenderState, ModelLarnachs> {
	private LarnachsEntity entity = null;

	public LarnachsRenderer(EntityRendererProvider.Context context) {
		super(context, new AnimatedModel(context.bakeLayer(ModelLarnachs.LAYER_LOCATION)), 1.5f);
		this.addLayer(new RenderLayer<>(this) {
			static final ResourceLocation LAYER_TEXTURE[] = new ResourceLocation[]{//
					ResourceLocation.parse("larnachian_pantheon:textures/entities/larnachs_layer1.png")//
					, ResourceLocation.parse("larnachian_pantheon:textures/entities/larnachs_layer2.png")//
					, ResourceLocation.parse("larnachian_pantheon:textures/entities/larnachs_layer3.png")//
			};

			//final ResourceLocation LAYER_TEXTURE = ResourceLocation.parse("larnachian_pantheon:textures/entities/larnachs_layer1.png");
			@Override
			public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, LivingEntityRenderState state, float headYaw, float headPitch) {
				VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.eyes(LAYER_TEXTURE[(entity.tickCount / 2) % 3]));
				this.getParentModel().renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
			}
		});
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(LarnachsEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		this.entity = entity;
		if (this.model instanceof AnimatedModel) {
			((AnimatedModel) this.model).setEntity(entity);
		}
	}

	@Override
	public ResourceLocation getTextureLocation(LivingEntityRenderState state) {
		return ResourceLocation.parse("larnachian_pantheon:textures/entities/larnachs_no_layer.png");
	}

	private static final class AnimatedModel extends ModelLarnachs {
		private LarnachsEntity entity = null;
		private final KeyframeAnimation keyframeAnimation0, keyframeAnimation1;
		private float partialTicks;

		public AnimatedModel(ModelPart root) {
			super(root);
			this.keyframeAnimation0 = LarnachsAnimation.idle.bake(root);
			this.keyframeAnimation1 = LarnachsAnimation.basePose2.bake(root);
		}

		public void setEntity(LarnachsEntity entity) {
			this.entity = entity;
		}

		@Override
		public void setupAnim(LivingEntityRenderState state) {
			this.root().getAllParts().forEach(ModelPart::resetPose);
			partialTicks = state.ageInTicks - entity.tickCount;
			float idleAmplitude = Mth.lerp(partialTicks, entity.walkAmplitudeO, entity.walkAmplitude) / 5f;
			if (entity.walkAmplitude + entity.walkAmplitudeO != 0)
				AnimUtil.animateAmplitude(keyframeAnimation0, entity.animationState0, state.ageInTicks, 1f, idleAmplitude);
			if (entity.walkAmplitude + entity.walkAmplitudeO != 10)
				AnimUtil.animateAmplitude(keyframeAnimation1, entity.animationState0, state.ageInTicks, 1f, 1 - idleAmplitude);
			entity.animateMob(this, state.ageInTicks, partialTicks);
			//
			float cameraYRot = Minecraft.getInstance().gameRenderer.getMainCamera().getYRot();
			//float f = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
			neck.yRot = (cameraYRot - state.bodyRot) * Mth.DEG_TO_RAD - lowerBodyRot.yRot - lowerBody.yRot;
			lowerBodyRot.xRot = Mth.rotLerp(partialTicks, entity.bodyXRotO, entity.bodyXRot) * Mth.DEG_TO_RAD;
			//
			super.setupAnim(state);
		}
	}
}