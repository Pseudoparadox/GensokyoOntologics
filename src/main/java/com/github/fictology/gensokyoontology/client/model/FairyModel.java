package com.github.fictology.gensokyoontology.client.model;
// Made create Blockbench 4.7.0
// Exported for Minecraft version 1.15 - 1.16 create Mojang mappings
// Paste this class into your mod and generate all required imports


import com.github.fictology.gensokyoontology.client.renderer.state.FairyRenderState;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

public class FairyModel extends HumanoidModel<FairyRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(GSKOUtil.key("fairy_layer"), "main");
    private final ModelPart bone;
    private final ModelPart head;
    private final ModelPart blink;
    private final ModelPart armRight;
    private final ModelPart armLeft;
    private final ModelPart body;
    private final ModelPart sittingRotationSkirt;
    private final ModelPart wingLeft;
    private final ModelPart wingRight;
    private final ModelPart legLeft;
    private final ModelPart legRight;

    public FairyModel(ModelPart root) {
        super(root);
        this.bone = root.getChild("bone");
        this.head = this.bone.getChild("head");
        this.blink = this.head.getChild("blink");
        this.armRight = this.bone.getChild("armRight");
        this.armLeft = this.bone.getChild("armLeft");
        this.body = this.bone.getChild("body");
        this.sittingRotationSkirt = this.body.getChild("sittingRotationSkirt");
        this.wingLeft = this.bone.getChild("wingLeft");
        this.wingRight = this.bone.getChild("wingRight");
        this.legLeft = this.bone.getChild("legLeft");
        this.legRight = this.bone.getChild("legRight");
    }


    public static LayerDefinition createBodyLayer() {
        var meshdefinition = new MeshDefinition();
        var partdefinition = meshdefinition.getRoot();

        var bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        var head = bone.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.0F, 0.0F));

        var blink = head.addOrReplaceChild("blink", CubeListBuilder.create().texOffs(24, 0).addBox(-4.0F, -8.0F, -4.001F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        var armRight = bone.addOrReplaceChild("armRight", CubeListBuilder.create().texOffs(46, 22).addBox(-2.0F, 1.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 52).addBox(-2.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -17.5F, 0.0F, 0.0F, 0.0F, 0.4363F));

        var armLeft = bone.addOrReplaceChild("armLeft", CubeListBuilder.create().texOffs(12, 52).addBox(-0.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(54, 22).addBox(0.0F, 1.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -17.5F, 0.0F, 0.0F, 0.0F, -0.4363F));

        var body = bone.addOrReplaceChild("body", CubeListBuilder.create().texOffs(5, 35).addBox(-3.0F, -7.499F, -3.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.5F, 0.0F));

        var sittingRotationSkirt = body.addOrReplaceChild("sittingRotationSkirt", CubeListBuilder.create().texOffs(0, 21).addBox(-4.5F, 2.5F, -4.5F, 9.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 21).addBox(-4.0F, 0.5F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(-3.5F, -1.5F, -3.5F, 7.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        var wingLeft = bone.addOrReplaceChild("wingLeft", CubeListBuilder.create().texOffs(32, 34).addBox(0.0F, -6.0F, 0.25F, 0.0F, 11.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -15.0F, 3.25F, 0.0F, 1.0472F, 0.0F));

        var wingRight = bone.addOrReplaceChild("wingRight", CubeListBuilder.create().texOffs(32, 34).addBox(0.0F, -6.0F, -0.25F, 0.0F, 11.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -15.0F, 3.75F, 0.0F, -1.0472F, 0.0F));

        var legLeft = bone.addOrReplaceChild("legLeft", CubeListBuilder.create().texOffs(32, 6).addBox(-1.501F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -9.0F, 0.0F));

        var legRight = bone.addOrReplaceChild("legRight", CubeListBuilder.create().texOffs(32, 18).addBox(-1.499F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -9.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(FairyRenderState p_361833_) {
        super.setupAnim(p_361833_);
        // this.animate(new AnimationState(), Animation.wingFlapping, 0.1f, 0.5f);
    }


    public class Animation {
        public static final AnimationDefinition wingFlapping = AnimationDefinition.Builder.withLength(3.0F)
                .looping()
                .addAnimation("wingLeft", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, -45.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("wingRight", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 45.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .build();

        public static final AnimationDefinition armAnimation = AnimationDefinition.Builder.withLength(3.0F)
                .looping()
                .addAnimation("armRight", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -7.5F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("armLeft", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 7.5F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .build();
    }
}