package net.mcreator.larnachianpantheon.client.renderer;

import com.crimsonsteve.crimsonsteveapi.utils.animations.AnimUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.larnachianpantheon.client.model.ModelLarnachs;
import net.mcreator.larnachianpantheon.client.model.animations.LarnachsAnimation;
import net.mcreator.larnachianpantheon.entity.LarnachsEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class LarnachsRenderer extends MobRenderer<LarnachsEntity, LarnachsRenderState, ModelLarnachs> {
	private LarnachsEntity entity = null;

	public LarnachsRenderer(EntityRendererProvider.Context context) {
		super(context, new AnimatedModel(context.bakeLayer(ModelLarnachs.LAYER_LOCATION)), 1.5f);
		this.addLayer(new RenderLayer<>(this) {
			static final Identifier LAYER_TEXTURE[] = new Identifier[]{//
                    Identifier.parse("larnachian_pantheon:textures/entities/larnachs_layer1.png")//
					, Identifier.parse("larnachian_pantheon:textures/entities/larnachs_layer2.png")//
					, Identifier.parse("larnachian_pantheon:textures/entities/larnachs_layer3.png")//
			};

			@Override
            public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, LarnachsRenderState state, float yRot, float xRot) {
                RenderType BLADE_LAYER = RenderTypes.eyes(LAYER_TEXTURE[(state.tickCount / 2) % 3]);
                submitNodeCollector.order(1).submitModel(this.getParentModel(), state, poseStack, BLADE_LAYER, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
            }
		});
	}

    @Override
    public LarnachsRenderState createRenderState() {
        return new LarnachsRenderState();
    }

	@Override
    public void extractRenderState(LarnachsEntity entity, LarnachsRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);

        state.entity = entity;

        state.tickCount = entity.tickCount;
        state.walkAmplitude = entity.walkAmplitude;
        state.walkAmplitudeO = entity.walkAmplitudeO;
        state.bodyXRot = entity.bodyXRot;
        state.bodyXRotO = entity.bodyXRotO;

        state.cameraYRot = Minecraft.getInstance().gameRenderer.getMainCamera().yRot();

        state.animationState0.copyFrom(entity.animationState0);
    }

	@Override
	public Identifier getTextureLocation(LarnachsRenderState state) {
		return Identifier.parse("larnachian_pantheon:textures/entities/larnachs_no_layer.png");
	}

	private static final class AnimatedModel extends ModelLarnachs {
		private final KeyframeAnimation keyframeAnimation0, keyframeAnimation1;

		public AnimatedModel(ModelPart root) {
			super(root);
			this.keyframeAnimation0 = LarnachsAnimation.idle.bake(root);
			this.keyframeAnimation1 = LarnachsAnimation.basePose2.bake(root);
		}


		@Override
		public void setupAnim(LarnachsRenderState state) {
            super.setupAnim(state);
            //this.root().getAllParts().forEach(ModelPart::resetPose);
            float idleAmplitude = Mth.lerp(state.partialTick, state.walkAmplitudeO, state.walkAmplitude) / 5f;

            if (state.walkAmplitude + state.walkAmplitudeO != 0)
                AnimUtil.animateAmplitude(keyframeAnimation0, state.animationState0, state.ageInTicks, 1f, idleAmplitude);
            if (state.walkAmplitude + state.walkAmplitudeO != 10)
                AnimUtil.animateAmplitude(keyframeAnimation1, state.animationState0, state.ageInTicks, 1f, 1 - idleAmplitude);
            state.entity.animateMob(this, state.ageInTicks, state.partialTick);
			//
			//float cameraYRot = Minecraft.getInstance().gameRenderer.getMainCamera().yRot();
			neck.yRot = (state.cameraYRot - state.bodyRot) * Mth.DEG_TO_RAD - lowerBodyRot.yRot - lowerBody.yRot;
			lowerBodyRot.xRot = Mth.rotLerp(state.partialTick, state.bodyXRotO, state.bodyXRot) * Mth.DEG_TO_RAD;
			//
		}
	}
}