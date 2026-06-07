package com.github.fictology.gensokyoontology.client.model;
// Made create Blockbench 4.8.3
// Exported for Minecraft version 1.15 - 1.16 create Mojang mappings
// Paste this class into your mod and generate all required imports

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class LilyWhiteModel extends HumanoidModel<HumanoidRenderState> {
    private final ModelPart lilywhite;
    private final ModelPart hand;
    private final ModelPart wing;
    private final ModelPart wingRight;
    private final ModelPart wingRightEndR1;
    private final ModelPart wingRightUpR1;
    private final ModelPart wingLeft;
    private final ModelPart wingLeftEndR1;
    private final ModelPart wingLeftUpR1;

    public LilyWhiteModel(ModelPart root) {
        super(root);

        this.lilywhite = root.getChild("lilyWhite");
        this.hand = this.lilywhite.getChild("hand");
        this.wing = this.lilywhite.getChild("wing");
        this.wingRight = this.wing.getChild("wingR2");
        this.wingRightEndR1 = this.wing.getChild("wingREnd");
        this.wingRightUpR1 = this.wing.getChild("wingRUp");

        this.wingLeft = this.wing.getChild("wingLeft");
        this.wingLeftEndR1 = this.wing.getChild("wingLEnd");
        this.wingLeftUpR1 = this.wing.getChild("wingLUp");
    }

    public static MeshDefinition createMesh(CubeDeformation cube) {

        var meshdefinition = HumanoidModel.createMesh(cube, 0);
        var partdefinition = meshdefinition.getRoot();

        var bone = partdefinition.addOrReplaceChild("lilyWhite", CubeListBuilder.create(), PartPose.offset(0F, 24F, 0F));
        var hand = bone.addOrReplaceChild("hand", CubeListBuilder.create()
                .texOffs(0,0).addBox(-3.8F, -31.05F, -3.0F, 7.8F, 7.8F, 7.8F)
                .texOffs(0, 24).addBox(-3.9F, -30.75F, -3.4F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);

        var wing = bone.addOrReplaceChild("wing", CubeListBuilder.create(), PartPose.ZERO);
        var wingRight = wing.addOrReplaceChild("wingRight", CubeListBuilder.create()
                        .texOffs(0,48).addBox(-6.1571F, -3.1868F, -0.0102F, 13.0F, 7.0F, 0.0F),
                PartPose.offsetAndRotation(-5.5929F, -16.8132F, 4.0102F, 0.0F, -2.9671F, 0.0F));

        var wingRightEnd = wing.addOrReplaceChild("wingREnd", CubeListBuilder.create()
                        .texOffs(0, 48).addBox(-6.4991F, -2.5F, -0.0102F, 13.0F, 7.0F, 0.0F),
                PartPose.offsetAndRotation(0.342F, 4.3132F, 0.0F, 0.0F, 0.0F, 0.3491F));

        var wingRUp = wing.addOrReplaceChild("wingRUp", CubeListBuilder.create()
                        .texOffs(0, 48).addBox(-6.4991F, -5.5F, -0.0102F, 13.0F, 7.0F, 0.0F),
                PartPose.offsetAndRotation(0.342F, -3.6868F, 0.0F, 0.0F, 0.0F, -0.3491F));

        var wingLeft = wing.addOrReplaceChild("wingLeft", CubeListBuilder.create()
                        .texOffs(0, 48).addBox(-6.0994F, -3.5175F, 0.0F, 13.0F, 7.0F, 0.0F),
                PartPose.offsetAndRotation(5.8494F, -16.4825F, 4.0F, 0.0F, -0.1745F, 0.0F));

        var wingLEnd = wing.addOrReplaceChild("wingLEnd", CubeListBuilder.create()
                        .texOffs(0, 48).addBox(-6.5F, -2.5F, 0.0F, 13.0F, 7.0F, 0.0F),
                PartPose.offsetAndRotation(0.4006F, 3.9825F, 0.0F, 0.0F, 0.0F, 0.3491F));

        var wingLUp = wing.addOrReplaceChild("wingLUp", CubeListBuilder.create()
                        .texOffs(0, 48).addBox(-6.5F, -5.5F, 0.0F, 13.0F, 7.0F, 0.0F),
                PartPose.offsetAndRotation(0.4006F, -4.0175F, 0.0F, 0.0F, 0.0F, -0.3491F));
/*
        rightLeg = new ModelRenderer(this);
        rightLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
        lilywhite.addChild(rightLeg);
        rightLeg.setTextureOffset(46, 0).addBox(-4.1F, -7.9F, -1.1F, 4.0F, 8.0F, 4.2F, 0.0F, false);
        rightLeg.setTextureOffset(24, 16).addBox(-4.0F, -11.6F, -1.0F, 4.0F, 11.6F, 4.0F, 0.0F, false);

        leftLeg = new ModelRenderer(this);
        leftLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
        lilywhite.addChild(leftLeg);
        leftLeg.setTextureOffset(46, 12).addBox(0.1F, -7.9F, -1.1F, 4.0F, 8.0F, 4.2F, 0.0F, false);
        leftLeg.setTextureOffset(0, 32).addBox(0.0F, -11.6F, -1.0F, 4.0F, 11.6F, 4.0F, 0.0F, false);


        leftArm = new ModelRenderer(this);
        leftArm.setRotationPoint(0.0F, 0.0F, 0.0F);
        lilywhite.addChild(leftArm);
        leftArm.setTextureOffset(45, 40).addBox(3.9F, -16.6F, -1.1F, 3.2F, 5.0F, 4.2F, 0.0F, false);
        leftArm.setTextureOffset(32, 0).addBox(4.0F, -23.2F, -1.0F, 3.0F, 11.6F, 4.0F, 0.0F, false);

        rightArm = new ModelRenderer(this);
        rightArm.setRotationPoint(0.0F, 0.0F, 0.0F);
        lilywhite.addChild(rightArm);
        rightArm.setTextureOffset(29, 39).addBox(-7.1F, -16.6F, -1.1F, 3.2F, 5.0F, 4.2F, 0.0F, false);
        rightArm.setTextureOffset(16, 32).addBox(-7.0F, -23.2F, -1.0F, 3.0F, 11.6F, 4.0F, 0.0F, false);

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 0.0F, 0.0F);
        lilywhite.addChild(body);
        body.setTextureOffset(33, 49).addBox(-3.0F, -21.6F, -1.2F, 6.0F, 3.0F, 0.0F, 0.0F, false);
        body.setTextureOffset(0, 16).addBox(-4.0F, -23.2F, -1.0F, 8.0F, 11.6F, 4.0F, 0.0F, false);

         */

        return meshdefinition;
    }

}
