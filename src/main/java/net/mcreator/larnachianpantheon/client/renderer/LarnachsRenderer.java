package net.mcreator.larnachianpantheon.client.renderer;

import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.Minecraft;

import net.mcreator.larnachianpantheon.entity.LarnachsEntity;
import net.mcreator.larnachianpantheon.client.model.animations.LarnachsAnimation;
import net.mcreator.larnachianpantheon.client.model.ModelLarnachs;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

import com.crimsonsteve.crimsonsteveapi.utils.animations.AnimUtil;

public class LarnachsRenderer extends MobRenderer<LarnachsEntity, ModelLarnachs<LarnachsEntity>> {
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
				VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.eyes(LAYER_TEXTURE[(entity.tickCount / 2) % 3]));
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
				if (entity.walkAmplitude + entity.walkAmplitudeO != 0)
					AnimUtil.animateAmplitude(this, entity.animationState0, LarnachsAnimation.idle, ageInTicks, 1f, idleAmplitude);
				if (entity.walkAmplitude + entity.walkAmplitudeO != 10)
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
			neck.yRot = (cameraYRot - f) * Mth.DEG_TO_RAD - lowerBodyRot.yRot - lowerBody.yRot;
			lowerBodyRot.xRot = Mth.rotLerp(partialTicks, entity.bodyXRotO, entity.bodyXRot) * Mth.DEG_TO_RAD;
			super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		}
	}
}