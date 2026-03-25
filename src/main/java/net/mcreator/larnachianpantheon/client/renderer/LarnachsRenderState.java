package net.mcreator.larnachianpantheon.client.renderer;

import net.mcreator.larnachianpantheon.entity.LarnachsEntity;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class LarnachsRenderState extends LivingEntityRenderState {
    public LarnachsEntity entity;

    public float walkAmplitude;
    public float walkAmplitudeO;
    public float bodyXRot;
    public float bodyXRotO;
    public int tickCount; // 텍스처 레이어 깜빡임 애니메이션 용도

    public float cameraYRot;

    public final AnimationState animationState0 = new AnimationState();
}
