package com.github.fictology.gensokyoontology.client.model.no;
/*
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import github.thelawf.gensokyoontology.common.entity.HaniwaEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class HaniwaModel extends EntityModel<HaniwaEntity> {
    private final ModelRenderer haniwa;
    private final ModelRenderer leftArm;
    private final ModelRenderer main;
    private final ModelRenderer body;
    private final ModelRenderer leftBase;
    private final ModelRenderer rightFromEyes;
    private final ModelRenderer rightArmHorizon;
    private final ModelRenderer rightArmVertical;

    public HaniwaModel() {
        textureWidth = 64;
        textureHeight = 64;

        haniwa = new ModelRenderer(this);
        haniwa.setRotationPoint(0.0F, 24.0F, 0.0F);


        leftArm = new ModelRenderer(this);
        leftArm.setRotationPoint(0.0F, -13.0F, 0.0F);
        haniwa.addChild(leftArm);
        leftArm.setTextureOffset(0, 0).addBox(-8.0F, 5.0F, 0.0F, 2.0F, 5.0F, 2.0F, 0.0F, false);

        main = new ModelRenderer(this);
        main.setRotationPoint(-8.0F, -6.0F, 8.0F);
        haniwa.addChild(main);
        main.setTextureOffset(0, 0).addBox(3.0F, -6.0F, -13.0F, 10.0F, 12.0F, 10.0F, 0.0F, false);

        body = new ModelRenderer(this);
        body.setRotationPoint(-8.0F, -6.0F, 8.0F);
        haniwa.addChild(body);
        body.setTextureOffset(0, 28).addBox(4.0F, -8.0F, -12.0F, 8.0F, 2.0F, 8.0F, 0.0F, false);

        leftBase = new ModelRenderer(this);
        leftBase.setRotationPoint(-0.4718F, -7.1164F, 8.25F);
        haniwa.addChild(leftBase);
        leftBase.setTextureOffset(30, 0).addBox(-5.5282F, -0.8836F, -9.25F, 1.0F, 3.0F, 3.0F, 0.0F, false);

        rightFromEyes = new ModelRenderer(this);
        rightFromEyes.setRotationPoint(-0.4718F, -7.1164F, 8.25F);
        haniwa.addChild(rightFromEyes);
        rightFromEyes.setTextureOffset(0, 22).addBox(5.4718F, -0.8836F, -9.25F, 1.0F, 3.0F, 3.0F, 0.0F, false);

        rightArmHorizon = new ModelRenderer(this);
        rightArmHorizon.setRotationPoint(0.0F, 0.0F, 0.0F);
        haniwa.addChild(rightArmHorizon);
        rightArmHorizon.setTextureOffset(24, 18).addBox(6.0F, -8.0F, -1.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);

        rightArmVertical = new ModelRenderer(this);
        rightArmVertical.setRotationPoint(0.0F, 0.0F, 0.0F);
        haniwa.addChild(rightArmVertical);
        rightArmVertical.setTextureOffset(0, 22).addBox(8.0F, -12.0F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(HaniwaEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        haniwa.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}

 */