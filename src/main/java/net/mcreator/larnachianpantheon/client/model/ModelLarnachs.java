package net.mcreator.larnachianpantheon.client.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.EntityModel;

// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class ModelLarnachs extends EntityModel<LivingEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("larnachian_pantheon", "model_larnachs"), "main");
	public final ModelPart whole;
	public final ModelPart wholeCenter;
	public final ModelPart rightThigh;
	public final ModelPart rightShank;
	public final ModelPart rightShin;
	public final ModelPart rightFoot;
	public final ModelPart leftThigh;
	public final ModelPart leftShank;
	public final ModelPart leftShin;
	public final ModelPart leftFoot;
	public final ModelPart lowerBodyRot;
	public final ModelPart lowerBody;
	public final ModelPart Armor1;
	public final ModelPart Armor2;
	public final ModelPart Armor3;
	public final ModelPart Armor4;
	public final ModelPart upperBody;
	public final ModelPart trueHead;
	public final ModelPart head;
	public final ModelPart neck;
	public final ModelPart upperBodyOnly;
	public final ModelPart bone5;
	public final ModelPart leftShoulderArmor;
	public final ModelPart rightShuolderArmor;
	public final ModelPart leftShoulder;
	public final ModelPart leftForearm;
	public final ModelPart leftWrist;
	public final ModelPart leftHand;
	public final ModelPart leftFinger11;
	public final ModelPart leftFinger12;
	public final ModelPart leftFinger21;
	public final ModelPart leftFinger22;
	public final ModelPart leftFinger31;
	public final ModelPart leftFinger32;
	public final ModelPart leftFinger41;
	public final ModelPart leftFinger42;
	public final ModelPart rightShoulder;
	public final ModelPart rightForearm;
	public final ModelPart rightWrist;
	public final ModelPart rightHand;
	public final ModelPart rightFinger11;
	public final ModelPart rightFinger12;
	public final ModelPart rightFinger21;
	public final ModelPart rightFinger22;
	public final ModelPart rightFinger31;
	public final ModelPart rightFinger32;
	public final ModelPart rightFinger41;
	public final ModelPart rightFinger42;
	public final ModelPart sword;
	public final ModelPart swordBlade;
	public final ModelPart bladeStart;
	public final ModelPart bladeEnd;

	public ModelLarnachs(ModelPart root) {
		super(root);
		this.whole = root.getChild("whole");
		this.wholeCenter = this.whole.getChild("wholeCenter");
		this.rightThigh = this.wholeCenter.getChild("rightThigh");
		this.rightShank = this.rightThigh.getChild("rightShank");
		this.rightShin = this.rightShank.getChild("rightShin");
		this.rightFoot = this.rightShin.getChild("rightFoot");
		this.leftThigh = this.wholeCenter.getChild("leftThigh");
		this.leftShank = this.leftThigh.getChild("leftShank");
		this.leftShin = this.leftShank.getChild("leftShin");
		this.leftFoot = this.leftShin.getChild("leftFoot");
		this.lowerBodyRot = this.wholeCenter.getChild("lowerBodyRot");
		this.lowerBody = this.lowerBodyRot.getChild("lowerBody");
		this.Armor1 = this.lowerBody.getChild("Armor1");
		this.Armor2 = this.lowerBody.getChild("Armor2");
		this.Armor3 = this.lowerBody.getChild("Armor3");
		this.Armor4 = this.lowerBody.getChild("Armor4");
		this.upperBody = this.lowerBody.getChild("upperBody");
		this.trueHead = this.upperBody.getChild("trueHead");
		this.head = this.trueHead.getChild("head");
		this.neck = this.upperBody.getChild("neck");
		this.upperBodyOnly = this.upperBody.getChild("upperBodyOnly");
		this.bone5 = this.upperBodyOnly.getChild("bone5");
		this.leftShoulderArmor = this.upperBodyOnly.getChild("leftShoulderArmor");
		this.rightShuolderArmor = this.upperBodyOnly.getChild("rightShuolderArmor");
		this.leftShoulder = this.upperBodyOnly.getChild("leftShoulder");
		this.leftForearm = this.leftShoulder.getChild("leftForearm");
		this.leftWrist = this.leftForearm.getChild("leftWrist");
		this.leftHand = this.leftWrist.getChild("leftHand");
		this.leftFinger11 = this.leftHand.getChild("leftFinger11");
		this.leftFinger12 = this.leftFinger11.getChild("leftFinger12");
		this.leftFinger21 = this.leftHand.getChild("leftFinger21");
		this.leftFinger22 = this.leftFinger21.getChild("leftFinger22");
		this.leftFinger31 = this.leftHand.getChild("leftFinger31");
		this.leftFinger32 = this.leftFinger31.getChild("leftFinger32");
		this.leftFinger41 = this.leftHand.getChild("leftFinger41");
		this.leftFinger42 = this.leftFinger41.getChild("leftFinger42");
		this.rightShoulder = this.upperBodyOnly.getChild("rightShoulder");
		this.rightForearm = this.rightShoulder.getChild("rightForearm");
		this.rightWrist = this.rightForearm.getChild("rightWrist");
		this.rightHand = this.rightWrist.getChild("rightHand");
		this.rightFinger11 = this.rightHand.getChild("rightFinger11");
		this.rightFinger12 = this.rightFinger11.getChild("rightFinger12");
		this.rightFinger21 = this.rightHand.getChild("rightFinger21");
		this.rightFinger22 = this.rightFinger21.getChild("rightFinger22");
		this.rightFinger31 = this.rightHand.getChild("rightFinger31");
		this.rightFinger32 = this.rightFinger31.getChild("rightFinger32");
		this.rightFinger41 = this.rightHand.getChild("rightFinger41");
		this.rightFinger42 = this.rightFinger41.getChild("rightFinger42");
		this.sword = this.rightHand.getChild("sword");
		this.swordBlade = this.sword.getChild("swordBlade");
		this.bladeStart = this.swordBlade.getChild("bladeStart");
		this.bladeEnd = this.swordBlade.getChild("bladeEnd");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition whole = partdefinition.addOrReplaceChild("whole", CubeListBuilder.create(), PartPose.offset(0.0F, -21.6F, 0.0F));
		PartDefinition wholeCenter = whole.addOrReplaceChild("wholeCenter", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition rightThigh = wholeCenter.addOrReplaceChild("rightThigh",
				CubeListBuilder.create().texOffs(116, 35).addBox(-3.0F, -1.1F, -2.0F, 6.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(42, 76).mirror().addBox(-5.0F, 0.9F, -4.0F, 10.0F, 13.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(-5.0F, 6.7F, 1.0F));
		PartDefinition rightShank = rightThigh.addOrReplaceChild("rightShank",
				CubeListBuilder.create().texOffs(66, 42).addBox(-3.0F, -0.1F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(118, 74).mirror().addBox(-3.0F, -0.1F, -5.0F, 6.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(0, 94).addBox(-4.0F, 1.9F, -3.0F, 8.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(66, 42).mirror().addBox(-3.0F, 11.9F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(0.0F, 14.0F, 0.0F));
		PartDefinition rightShin = rightShank.addOrReplaceChild("rightShin", CubeListBuilder.create().texOffs(30, 98).mirror().addBox(-4.0F, 0.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(106, 114).mirror()
				.addBox(-3.0F, 4.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 13.9F, 0.0F));
		PartDefinition rightFoot = rightShin.addOrReplaceChild("rightFoot", CubeListBuilder.create().texOffs(80, 0).mirror().addBox(-4.0F, 0.0F, -8.0F, 8.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 8.0F, 0.0F));
		PartDefinition leftThigh = wholeCenter.addOrReplaceChild("leftThigh",
				CubeListBuilder.create().texOffs(42, 76).mirror().addBox(-5.0F, 0.9F, -4.0F, 10.0F, 13.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(116, 35).addBox(-3.0F, -1.1F, -2.0F, 6.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offset(5.0F, 6.7F, 1.0F));
		PartDefinition leftShank = leftThigh
				.addOrReplaceChild("leftShank",
						CubeListBuilder.create().texOffs(66, 42).addBox(-3.0F, 11.9F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 94).addBox(-4.0F, 1.9F, -3.0F, 8.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(118, 74)
								.mirror().addBox(-3.0F, -0.1F, -5.0F, 6.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(66, 42).addBox(-3.0F, -0.1F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)),
						PartPose.offset(0.0F, 14.0F, 0.0F));
		PartDefinition leftShin = leftShank.addOrReplaceChild("leftShin",
				CubeListBuilder.create().texOffs(30, 98).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(106, 114).addBox(-3.0F, 4.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 13.9F, 0.0F));
		PartDefinition leftFoot = leftShin.addOrReplaceChild("leftFoot", CubeListBuilder.create().texOffs(80, 0).addBox(-4.0F, 0.0F, -8.0F, 8.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));
		PartDefinition lowerBodyRot = wholeCenter.addOrReplaceChild("lowerBodyRot", CubeListBuilder.create(), PartPose.offset(0.0F, 6.6F, 0.0F));
		PartDefinition lowerBody = lowerBodyRot.addOrReplaceChild("lowerBody",
				CubeListBuilder.create().texOffs(66, 30).addBox(-9.0F, -4.9F, -2.0F, 18.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(0, 30).addBox(-11.0F, -13.9F, -4.0F, 22.0F, 9.0F, 11.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition Armor1 = lowerBody.addOrReplaceChild("Armor1", CubeListBuilder.create(), PartPose.offset(11.0F, -5.9F, 1.0F));
		PartDefinition cube_r1 = Armor1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(170, 24).addBox(8.0F, -10.0F, -4.0F, 1.0F, 16.0F, 11.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-6.0F, 11.0F, -1.0F, 0.0F, 0.0F, -0.1745F));
		PartDefinition Armor2 = lowerBody.addOrReplaceChild("Armor2", CubeListBuilder.create(), PartPose.offset(5.0F, -5.9F, 7.0F));
		PartDefinition cube_r2 = Armor2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(170, 24).mirror().addBox(8.0F, -10.0F, -4.0F, 1.0F, 16.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 9.0F, 10.0F, 1.8696F, 1.4366F, 1.8627F));
		PartDefinition Armor3 = lowerBody.addOrReplaceChild("Armor3", CubeListBuilder.create(), PartPose.offset(-7.0F, -4.9F, 7.0F));
		PartDefinition cube_r3 = Armor3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(170, 24).mirror().addBox(8.0F, -10.0F, -4.0F, 1.0F, 16.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 8.0F, 10.0F, 1.8696F, 1.4366F, 1.8627F));
		PartDefinition Armor4 = lowerBody.addOrReplaceChild("Armor4", CubeListBuilder.create(), PartPose.offset(-11.0F, -4.9F, 1.0F));
		PartDefinition cube_r4 = Armor4.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(170, 24).mirror().addBox(8.0F, -10.0F, -4.0F, 1.0F, 16.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(-10.0F, 8.0F, -1.0F, 0.0F, 0.0F, 0.1309F));
		PartDefinition upperBody = lowerBody.addOrReplaceChild("upperBody", CubeListBuilder.create(), PartPose.offset(0.0F, -14.0F, 0.0F));
		PartDefinition trueHead = upperBody.addOrReplaceChild("trueHead", CubeListBuilder.create(), PartPose.offset(-1.0F, -30.0F, 2.0F));
		PartDefinition head = trueHead.addOrReplaceChild("head", CubeListBuilder.create().texOffs(102, 62).addBox(-3.0F, -2.9F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition cube_r5 = head.addOrReplaceChild("cube_r5",
				CubeListBuilder.create().texOffs(68, 118).addBox(-5.0F, -35.0F, -6.0F, 7.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(120, 0).addBox(2.0F, -35.0F, -6.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-9.0F, 27.1F, -10.0F, 3.0686F, -0.6949F, -3.0279F));
		PartDefinition cube_r6 = head.addOrReplaceChild("cube_r6",
				CubeListBuilder.create().texOffs(20, 111).addBox(2.0F, -35.0F, -6.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(50, 118).addBox(-5.0F, -35.0F, -6.0F, 7.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-1.0F, 27.1F, -3.0F, -0.0948F, -0.8249F, 0.1288F));
		PartDefinition neck = upperBody.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(50, 110).addBox(-3.0F, -8.0F, 0.0F, 6.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -18.9F, 2.0F));
		PartDefinition upperBodyOnly = upperBody
				.addOrReplaceChild(
						"upperBodyOnly", CubeListBuilder.create().texOffs(50, 66).addBox(-9.0F, -1.9F, -2.0F, 18.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-13.0F, -16.9F, -6.0F, 25.0F, 15.0F, 15.0F, new CubeDeformation(0.0F))
								.texOffs(80, 15).addBox(-5.5F, -18.9F, -3.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(50, 50).addBox(-11.0F, -16.9F, -9.0F, 21.0F, 13.0F, 3.0F, new CubeDeformation(0.0F)),
						PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition bone5 = upperBodyOnly.addOrReplaceChild("bone5", CubeListBuilder.create(), PartPose.offset(-1.0F, -16.9F, 9.0F));
		PartDefinition leftShoulderArmor = upperBodyOnly.addOrReplaceChild("leftShoulderArmor", CubeListBuilder.create().texOffs(138, 227).addBox(-2.0F, -5.0F, -6.0F, 18.0F, 5.0F, 15.0F, new CubeDeformation(0.0F)),
				PartPose.offset(8.0F, -16.9F, 0.0F));
		PartDefinition rightShuolderArmor = upperBodyOnly.addOrReplaceChild("rightShuolderArmor", CubeListBuilder.create().texOffs(138, 227).mirror().addBox(-15.0F, -5.0F, -6.0F, 18.0F, 5.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(-10.0F, -16.9F, 0.0F));
		PartDefinition leftShoulder = upperBodyOnly.addOrReplaceChild("leftShoulder", CubeListBuilder.create().texOffs(108, 98).mirror().addBox(0.0F, -3.0F, -2.0F, 2.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(0, 50).mirror()
				.addBox(2.0F, -5.0F, -4.0F, 13.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(12.0F, -10.9F, 0.0F));
		PartDefinition leftForearm = leftShoulder.addOrReplaceChild("leftForearm", CubeListBuilder.create().texOffs(62, 98).mirror().addBox(2.0F, -5.0F, -3.0F, 2.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(30, 110).mirror()
				.addBox(0.0F, -4.0F, -2.0F, 2.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(15.0F, 1.0F, 0.0F));
		PartDefinition leftWrist = leftForearm.addOrReplaceChild("leftWrist",
				CubeListBuilder.create().texOffs(30, 110).mirror().addBox(0.0F, -4.0F, -4.0F, 2.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(98, 42).mirror()
						.addBox(2.0F, -5.0F, -5.0F, 2.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(0, 74).mirror().addBox(6.0F, -5.0F, -5.0F, 11.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(30, 110)
						.mirror().addBox(4.0F, -4.0F, -4.0F, 2.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(80, 87).mirror().addBox(5.0F, 5.0F, -4.0F, 11.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(80, 76)
						.mirror().addBox(5.0F, -8.0F, -4.0F, 11.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(4.0F, 0.0F, 2.0F));
		PartDefinition leftHand = leftWrist.addOrReplaceChild("leftHand", CubeListBuilder.create().texOffs(86, 98).mirror().addBox(-1.0F, -4.0F, -4.0F, 3.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(18.0F, 0.0F, 0.0F));
		PartDefinition leftFinger11 = leftHand.addOrReplaceChild("leftFinger11", CubeListBuilder.create().texOffs(146, 55).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(1.8756F, -2.8432F, 1.8695F, 0.0F, 3.1416F, 0.0F));
		PartDefinition leftFinger12 = leftFinger11.addOrReplaceChild("leftFinger12",
				CubeListBuilder.create().texOffs(145, 75).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(161, 98).addBox(-0.4F, -0.7568F, -1.7695F, 2.4F, 1.6F, 2.7F, new CubeDeformation(0.0F)),
				PartPose.offset(-4.0F, 0.0F, 0.0F));
		PartDefinition leftFinger21 = leftHand.addOrReplaceChild("leftFinger21", CubeListBuilder.create().texOffs(146, 55).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(1.8756F, -0.8432F, 1.8695F, 0.0F, 3.1416F, 0.0F));
		PartDefinition leftFinger22 = leftFinger21.addOrReplaceChild("leftFinger22",
				CubeListBuilder.create().texOffs(145, 75).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(161, 98).addBox(-0.4F, -0.7568F, -1.7695F, 2.4F, 1.6F, 2.7F, new CubeDeformation(0.0F)),
				PartPose.offset(-4.0F, 0.0F, 0.0F));
		PartDefinition leftFinger31 = leftHand.addOrReplaceChild("leftFinger31", CubeListBuilder.create().texOffs(146, 55).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(1.8756F, 1.1568F, 1.8695F, 0.0F, 3.1416F, 0.0F));
		PartDefinition leftFinger32 = leftFinger31.addOrReplaceChild("leftFinger32",
				CubeListBuilder.create().texOffs(145, 75).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(161, 98).addBox(-0.4F, -0.7568F, -1.7695F, 2.4F, 1.6F, 2.7F, new CubeDeformation(0.0F)),
				PartPose.offset(-4.0F, 0.0F, 0.0F));
		PartDefinition leftFinger41 = leftHand.addOrReplaceChild("leftFinger41", CubeListBuilder.create().texOffs(146, 55).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(1.8756F, 3.1568F, 1.8695F, 0.0F, 3.1416F, 0.0F));
		PartDefinition leftFinger42 = leftFinger41.addOrReplaceChild("leftFinger42",
				CubeListBuilder.create().texOffs(145, 75).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(161, 98).addBox(-0.4F, -0.7568F, -1.7695F, 2.4F, 1.6F, 2.7F, new CubeDeformation(0.0F)),
				PartPose.offset(-4.0F, 0.0F, 0.0F));
		PartDefinition rightShoulder = upperBodyOnly.addOrReplaceChild("rightShoulder", CubeListBuilder.create().texOffs(108, 98).addBox(-2.1244F, -1.8518F, -2.0F, 2.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(0, 50).mirror()
				.addBox(-15.1244F, -3.8518F, -4.0F, 13.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-13.0F, -11.9F, 0.0F));
		PartDefinition rightForearm = rightShoulder.addOrReplaceChild("rightForearm",
				CubeListBuilder.create().texOffs(98, 42).addBox(-4.0F, -4.9914F, -3.1305F, 2.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(30, 110).addBox(-2.0F, -3.9914F, -2.1305F, 2.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-15.1244F, 2.1482F, 0.0F));
		PartDefinition rightWrist = rightForearm.addOrReplaceChild("rightWrist",
				CubeListBuilder.create().texOffs(30, 110).addBox(-2.0F, -4.0F, -4.0F, 2.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(62, 98).addBox(-4.0F, -5.0F, -5.0F, 2.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(30, 110)
						.addBox(-6.0F, -4.0F, -4.0F, 2.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(80, 76).addBox(-16.0F, -8.0F, -4.0F, 11.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(80, 87)
						.addBox(-16.0F, 5.0F, -4.0F, 11.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(0, 74).addBox(-17.0F, -5.0F, -5.0F, 11.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-4.0F, 0.0086F, 1.8695F));
		PartDefinition rightHand = rightWrist.addOrReplaceChild("rightHand", CubeListBuilder.create().texOffs(86, 98).addBox(-3.0F, -4.0F, -4.0F, 3.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-17.0F, 0.0F, 0.0F));
		PartDefinition rightFinger11 = rightHand.addOrReplaceChild("rightFinger11", CubeListBuilder.create().texOffs(146, 55).addBox(-4.0F, -1.0F, -1.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -3.0F, 2.0F));
		PartDefinition rightFinger12 = rightFinger11.addOrReplaceChild("rightFinger12",
				CubeListBuilder.create().texOffs(145, 75).addBox(-4.0F, -1.0F, -1.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(161, 98).addBox(-0.4F, -0.7568F, -0.7695F, 2.4F, 1.6F, 2.7F, new CubeDeformation(0.0F)),
				PartPose.offset(-4.0F, 0.0F, 0.0F));
		PartDefinition rightFinger21 = rightHand.addOrReplaceChild("rightFinger21", CubeListBuilder.create().texOffs(146, 55).addBox(-4.0F, -1.0F, -1.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -1.0F, 2.0F));
		PartDefinition rightFinger22 = rightFinger21.addOrReplaceChild("rightFinger22",
				CubeListBuilder.create().texOffs(145, 75).addBox(-4.0F, -1.0F, -1.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(161, 98).addBox(-0.4F, -0.7568F, -0.7695F, 2.4F, 1.6F, 2.7F, new CubeDeformation(0.0F)),
				PartPose.offset(-4.0F, 0.0F, 0.0F));
		PartDefinition rightFinger31 = rightHand.addOrReplaceChild("rightFinger31", CubeListBuilder.create().texOffs(146, 55).addBox(-4.0F, -1.0F, -1.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 1.0F, 2.0F));
		PartDefinition rightFinger32 = rightFinger31.addOrReplaceChild("rightFinger32",
				CubeListBuilder.create().texOffs(145, 75).addBox(-4.0F, -1.0F, -1.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(161, 98).addBox(-0.4F, -0.7568F, -0.7695F, 2.4F, 1.6F, 2.7F, new CubeDeformation(0.0F)),
				PartPose.offset(-4.0F, 0.0F, 0.0F));
		PartDefinition rightFinger41 = rightHand.addOrReplaceChild("rightFinger41", CubeListBuilder.create().texOffs(146, 55).addBox(-4.0F, -1.0F, -1.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 3.0F, 2.0F));
		PartDefinition rightFinger42 = rightFinger41.addOrReplaceChild("rightFinger42",
				CubeListBuilder.create().texOffs(145, 75).addBox(-4.0F, -1.0F, -1.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(161, 98).addBox(-0.4F, -0.7568F, -0.7695F, 2.4F, 1.6F, 2.7F, new CubeDeformation(0.0F)),
				PartPose.offset(-4.0F, 0.0F, 0.0F));
		PartDefinition sword = rightHand
				.addOrReplaceChild(
						"sword", CubeListBuilder.create().texOffs(204, 180).addBox(-1.0F, -9.0F, 0.0F, 3.0F, 23.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(193, 134).addBox(-5.0F, -15.0F, 0.0F, 12.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
								.texOffs(62, 187).addBox(3.0F, -67.0F, 0.0F, 4.0F, 52.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(147, 177).addBox(-5.0F, -30.0F, 0.0F, 2.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)),
						PartPose.offset(-5.0F, -1.0F, -1.0F));
		PartDefinition swordBlade = sword.addOrReplaceChild("swordBlade", CubeListBuilder.create().texOffs(40, 186).addBox(-5.0F, -68.0F, 1.0F, 8.0F, 53.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition bladeStart = swordBlade.addOrReplaceChild("bladeStart", CubeListBuilder.create(), PartPose.offset(0.0F, -15.0F, 0.0F));
		PartDefinition bladeEnd = swordBlade.addOrReplaceChild("bladeEnd", CubeListBuilder.create(), PartPose.offset(0.0F, -67.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	public void setupAnim(LivingEntityRenderState state) {
		float limbSwing = state.walkAnimationPos;
		float limbSwingAmount = state.walkAnimationSpeed;
		float ageInTicks = state.ageInTicks;
		float netHeadYaw = state.yRot;
		float headPitch = state.xRot;

		this.trueHead.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.trueHead.xRot = headPitch / (180F / (float) Math.PI);
	}
}