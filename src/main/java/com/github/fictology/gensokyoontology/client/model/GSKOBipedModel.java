package com.github.fictology.gensokyoontology.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public abstract class GSKOBipedModel<T extends HumanoidRenderState> extends HumanoidModel<T> {
    public ModelPart body;
    public ModelPart leftArm;
    // bipedLeftArm
    public ModelPart rightArm;
    // private final ModelPart body;

    public ModelPart rightLeg;
    public ModelPart leftLeg;

    public GSKOBipedModel(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
        this.leftArm = this.body.getChild("left_arm");
        this.rightArm = this.body.getChild("right_arm");
        this.leftLeg = this.body.getChild("left_leg");
        this.rightLeg = this.body.getChild("right_leg");
    }


    protected float getArmAngleSq(float limbSwing) {
        return -65.0F * limbSwing + limbSwing * limbSwing;
    }
}
