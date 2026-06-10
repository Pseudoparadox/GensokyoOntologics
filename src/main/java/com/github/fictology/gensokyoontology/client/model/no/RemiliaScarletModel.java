package com.github.fictology.gensokyoontology.client.model.no;
/*
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import github.thelawf.gensokyoontology.common.entity.monster.RemiliaScarletEntity;
import net.minecraft.client.renderer.model.ModelRenderer;
import org.jetbrains.annotations.NotNull;

public class RemiliaScarletModel extends GSKOBipedModel<RemiliaScarletEntity> {
	private final ModelRenderer remilia;
	public final ModelRenderer body;
	private final ModelRenderer hand;
	private final ModelRenderer hat;
	private final ModelRenderer hE_r1;
	private final ModelRenderer hW_r1;
	// public final ModelRenderer leftArm;
	// // bipedLeftArm
	// public final ModelRenderer rightArm;
	// private final ModelRenderer body;
	private final ModelRenderer skirt;
	private final ModelRenderer skirt_table_h_r1;
	private final ModelRenderer s8_r1;
	private final ModelRenderer s7_r1;
	private final ModelRenderer s6_r1;
	private final ModelRenderer s5_r1;
	private final ModelRenderer s4_r1;
	private final ModelRenderer s3_r1;
	private final ModelRenderer s2_r1;
	private final ModelRenderer s1_r1;
	// public final ModelRenderer rightLeg;
	// public final ModelRenderer leftLeg;
	private final ModelRenderer fly_left;
	private final ModelRenderer gan2;
	private final ModelRenderer g4_r1;
	private final ModelRenderer g3_r1;
	private final ModelRenderer g2_r1;
	private final ModelRenderer g1_r1;
	private final ModelRenderer membrane2;
	private final ModelRenderer m1_1_r1;
	private final ModelRenderer m3_1_r1;
	private final ModelRenderer m3_r1;
	private final ModelRenderer m2_r1;
	private final ModelRenderer m1_r1;
	private final ModelRenderer fly_right;
	private final ModelRenderer gan3;
	private final ModelRenderer g4_r2;
	private final ModelRenderer g3_r2;
	private final ModelRenderer g2_r2;
	private final ModelRenderer g1_r2;
	private final ModelRenderer membrane3;
	private final ModelRenderer m1_1_r2;
	private final ModelRenderer m3_1_r2;
	private final ModelRenderer m3_r2;
	private final ModelRenderer m2_r2;
	private final ModelRenderer m1_r2;

	public RemiliaScarletModel(float modelSize) {
		super(modelSize);
		textureWidth = 80;
		textureHeight = 80;

		remilia = new ModelRenderer(this);
		remilia.setRotationPoint(0.0F, 24.0F, 0.0F);
		

		hand = new ModelRenderer(this);
		hand.setRotationPoint(0.0F, 0.0F, 0.0F);
		remilia.addChild(hand);
		hand.setTextureOffset(0, 0).addBox(-4.0F, -31.25F, -3.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

		hat = new ModelRenderer(this);
		hat.setRotationPoint(0.0F, 0.0F, 0.0F);
		hand.addChild(hat);
		hat.setTextureOffset(63, 17).addBox(-4.0F, -31.3F, -3.2F, 8.0F, 8.0F, 0.0F, 0.0F, false);
		hat.setTextureOffset(56, 9).addBox(-4.0F, -31.5F, -3.0F, 8.0F, 0.0F, 8.0F, 0.0F, false);
		hat.setTextureOffset(63, 35).addBox(-4.0F, -31.0F, 5.25F, 8.0F, 8.0F, 0.0F, 0.0F, false);

		hE_r1 = new ModelRenderer(this);
		hE_r1.setRotationPoint(-3.0F, -26.0F, -1.0F);
		hat.addChild(hE_r1);
		setRotationAngle(hE_r1, 0.0F, -1.5708F, 0.0F);
		hE_r1.setTextureOffset(63, 1).addBox(-2.0F, -5.2F, -7.15F, 8.0F, 8.0F, 0.0F, 0.0F, false);

		hW_r1 = new ModelRenderer(this);
		hW_r1.setRotationPoint(-4.2F, -27.1F, 0.9F);
		hat.addChild(hW_r1);
		setRotationAngle(hW_r1, 0.0F, -1.5708F, 0.0F);
		hW_r1.setTextureOffset(63, 26).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, 0.0F, false);

		this.leftArm = new ModelRenderer(this);
		this.leftArm.setRotationPoint(5.4F, -23.4F, 1.0F);
		remilia.addChild(leftArm);
		this.leftArm.setTextureOffset(49, 38).addBox(-1.6F, -0.0F, -2.1F, 3.2F, 12.0F, 4.2F, 0.0F, false);
		this.leftArm.setTextureOffset(40, 16).addBox(-1.5F, -0.2F, -2.0F, 3.0F, 11.6F, 4.0F, 0.0F, false);

		this.rightArm = new ModelRenderer(this);
		this.rightArm.setRotationPoint(-5.6F, -23.4F, 1.0F);
		remilia.addChild(rightArm);
		this.rightArm.setTextureOffset(49, 35).addBox(-1.6F, -0.0F, -2.1F, 3.2F, 12.0F, 4.2F, 0.0F, false);
		this.rightArm.setTextureOffset(33, 48).addBox(-1.5F, -0.2F, -2.0F, 3.0F, 11.6F, 4.0F, 0.0F, false);

		this.body = new ModelRenderer(this);
		this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
		remilia.addChild(this.body);
		this.body.setTextureOffset(64, 53).addBox(-4.0F, -23.0F, -1.2F, 8.0F, 5.0F, 0.0F, 0.0F, false);
		this.body.setTextureOffset(16, 16).addBox(-4.0F, -23.2F, -1.0F, 8.0F, 11.6F, 4.0F, 0.0F, false);

		skirt = new ModelRenderer(this);
		skirt.setRotationPoint(-0.0024F, -11.1567F, 1.0186F);
		this.body.addChild(skirt);

		skirt_table_h_r1 = new ModelRenderer(this);
		skirt_table_h_r1.setRotationPoint(0.0F, -2.5F, 3.0F);
		skirt.addChild(skirt_table_h_r1);
		setRotationAngle(skirt_table_h_r1, 0.2618F, 0.0F, 0.0F);
		skirt_table_h_r1.setTextureOffset(61, 58).addBox(-3.5F, -1.8F, -0.4F, 7.0F, 5.0F, 0.0F, 0.0F, false);

		s8_r1 = new ModelRenderer(this);
		s8_r1.setRotationPoint(3.9965F, 0.4301F, 2.0763F);
		skirt.addChild(s8_r1);
		setRotationAngle(s8_r1, 0.2182F, 0.0F, -0.2182F);
		s8_r1.setTextureOffset(61, 68).addBox(-1.1F, -4.5F, -1.3F, 2.0F, 9.0F, 2.0F, 0.0F, false);

		s7_r1 = new ModelRenderer(this);
		s7_r1.setRotationPoint(-4.0291F, 0.429F, 2.0848F);
		skirt.addChild(s7_r1);
		setRotationAngle(s7_r1, 0.2182F, 0.0F, 0.2182F);
		s7_r1.setTextureOffset(61, 68).addBox(-0.8F, -4.5F, -1.2F, 2.0F, 9.0F, 2.0F, 0.0F, false);

		s6_r1 = new ModelRenderer(this);
		s6_r1.setRotationPoint(3.9073F, 0.2412F, -1.9132F);
		skirt.addChild(s6_r1);
		setRotationAngle(s6_r1, -0.2182F, 0.0F, -0.2182F);
		s6_r1.setTextureOffset(61, 68).addBox(-1.05F, -4.2F, -1.0F, 2.0F, 9.0F, 2.0F, 0.0F, false);

		s5_r1 = new ModelRenderer(this);
		s5_r1.setRotationPoint(-4.0121F, 0.2996F, -1.9237F);
		skirt.addChild(s5_r1);
		setRotationAngle(s5_r1, -0.2182F, 0.0F, 0.2182F);
		s5_r1.setTextureOffset(61, 68).addBox(-0.85F, -4.3F, -0.9F, 2.0F, 9.0F, 2.0F, 0.0F, false);

		s4_r1 = new ModelRenderer(this);
		s4_r1.setRotationPoint(-4.4938F, 0.2324F, -0.0186F);
		skirt.addChild(s4_r1);
		setRotationAngle(s4_r1, 0.0F, 0.0F, 0.2618F);
		s4_r1.setTextureOffset(59, 66).addBox(-0.7F, -4.3F, -2.0F, 2.0F, 9.0F, 4.0F, 0.0F, false);

		s3_r1 = new ModelRenderer(this);
		s3_r1.setRotationPoint(3.8882F, 1.2482F, -0.0186F);
		skirt.addChild(s3_r1);
		setRotationAngle(s3_r1, 0.0F, 0.0F, -0.2618F);
		s3_r1.setTextureOffset(59, 66).addBox(-0.5F, -5.2F, -2.0F, 2.0F, 9.0F, 4.0F, 0.0F, false);

		s2_r1 = new ModelRenderer(this);
		s2_r1.setRotationPoint(0.0024F, 2.8126F, -2.6366F);
		skirt.addChild(s2_r1);
		setRotationAngle(s2_r1, 0.3054F, 0.0F, 0.0F);
		s2_r1.setTextureOffset(59, 68).addBox(-4.0F, -5.3F, 4.5F, 8.0F, 9.0F, 2.0F, 0.0F, false);

		s1_r1 = new ModelRenderer(this);
		s1_r1.setRotationPoint(0.0024F, 0.5392F, -2.2345F);
		skirt.addChild(s1_r1);
		setRotationAngle(s1_r1, -0.3054F, 0.0F, 0.0F);
		s1_r1.setTextureOffset(59, 68).addBox(-4.0F, -4.5F, -1.2F, 8.0F, 9.0F, 2.0F, 0.0F, false);

		this.rightLeg = new ModelRenderer(this);
		this.rightLeg.setRotationPoint(-2.05F, -11.85F, 1.0F);
		remilia.addChild(rightLeg);
		this.rightLeg.setTextureOffset(0, 48).addBox(-2.05F, -0.05F, -2.1F, 4.0F, 12.0F, 4.2F, 0.0F, false);
		this.rightLeg.setTextureOffset(0, 16).addBox(-1.95F, 0.25F, -2.0F, 4.0F, 11.6F, 4.0F, 0.0F, false);

		this.leftLeg = new ModelRenderer(this);
		this.leftLeg.setRotationPoint(2.05F, -11.85F, 1.0F);
		remilia.addChild(leftLeg);
		this.leftLeg.setTextureOffset(0, 32).addBox(-1.95F, -0.05F, -2.1F, 4.0F, 12.0F, 4.2F, 0.0F, false);
		this.leftLeg.setTextureOffset(17, 48).addBox(-2.05F, 0.25F, -2.0F, 4.0F, 11.6F, 4.0F, 0.0F, false);

		fly_left = new ModelRenderer(this);
		fly_left.setRotationPoint(17.0F, 0.0F, 7.0F);
		remilia.addChild(fly_left);
		setRotationAngle(fly_left, 0.0F, 3.1416F, 0.0F);

		gan2 = new ModelRenderer(this);
		gan2.setRotationPoint(19.25F, 1.0F, 0.0F);
		fly_left.addChild(gan2);

		g4_r1 = new ModelRenderer(this);
		g4_r1.setRotationPoint(-11.5F, -21.0F, 3.5F);
		gan2.addChild(g4_r1);
		setRotationAngle(g4_r1, 0.0F, 0.0F, 0.6109F);
		g4_r1.setTextureOffset(0, 70).addBox(-1.3F, -2.4F, -0.5F, 1.0F, 9.0F, 1.0F, 0.0F, false);

		g3_r1 = new ModelRenderer(this);
		g3_r1.setRotationPoint(-10.5F, -20.5F, 3.5F);
		gan2.addChild(g3_r1);
		setRotationAngle(g3_r1, 0.0F, 0.0F, -0.3491F);
		g3_r1.setTextureOffset(0, 70).addBox(0.75F, -4.5F, -0.5F, 1.0F, 10.5F, 1.0F, 0.0F, false);

		g2_r1 = new ModelRenderer(this);
		g2_r1.setRotationPoint(-14.5F, -22.5F, 3.5F);
		gan2.addChild(g2_r1);
		setRotationAngle(g2_r1, 0.0F, 0.0F, -0.3927F);
		g2_r1.setTextureOffset(0, 66).addBox(-8.5F, -0.5F, -0.5F, 13.0F, 1.0F, 1.0F, 0.0F, false);

		g1_r1 = new ModelRenderer(this);
		g1_r1.setRotationPoint(-6.5F, -22.5F, 3.5F);
		gan2.addChild(g1_r1);
		setRotationAngle(g1_r1, 0.0F, 0.0F, 0.3054F);
		g1_r1.setTextureOffset(0, 66).addBox(-4.3F, -0.5F, -0.5F, 9.0F, 1.0F, 1.0F, 0.0F, false);

		membrane2 = new ModelRenderer(this);
		membrane2.setRotationPoint(17.0F, 0.0F, 0.0F);
		fly_left.addChild(membrane2);

		m1_1_r1 = new ModelRenderer(this);
		m1_1_r1.setRotationPoint(-3.5F, -17.5F, 3.5F);
		membrane2.addChild(m1_1_r1);
		setRotationAngle(m1_1_r1, 0.0F, 0.0F, -1.2654F);
		m1_1_r1.setTextureOffset(14, 75).addBox(2.5F, -4.7F, 0.0F, 1.0F, 5.0F, 0.0F, 0.0F, false);

		m3_1_r1 = new ModelRenderer(this);
		m3_1_r1.setRotationPoint(-13.3758F, -20.195F, 3.5F);
		membrane2.addChild(m3_1_r1);
		setRotationAngle(m3_1_r1, 0.0F, 0.0F, 1.1345F);
		m3_1_r1.setTextureOffset(40, 73).addBox(-0.5F, -3.4F, 0.0F, 1.0F, 7.0F, 0.0F, 0.0F, false);

		m3_r1 = new ModelRenderer(this);
		m3_r1.setRotationPoint(-13.75F, -18.0F, 3.5F);
		membrane2.addChild(m3_r1);
		setRotationAngle(m3_r1, 0.0F, 0.0F, 0.48F);
		m3_r1.setTextureOffset(32, 72).addBox(-6.1F, -5.6F, 0.0F, 8.0F, 8.0F, 0.0F, 0.0F, false);

		m2_r1 = new ModelRenderer(this);
		m2_r1.setRotationPoint(-9.25F, -19.0F, 3.75F);
		membrane2.addChild(m2_r1);
		setRotationAngle(m2_r1, 0.0F, 0.0F, 0.0873F);
		m2_r1.setTextureOffset(24, 72).addBox(-3.8F, -3.7F, -0.25F, 8.0F, 8.0F, 0.0F, 0.0F, false);

		m1_r1 = new ModelRenderer(this);
		m1_r1.setRotationPoint(-3.8297F, -19.7493F, 3.5F);
		membrane2.addChild(m1_r1);
		setRotationAngle(m1_r1, 0.0F, 0.0F, -0.829F);
		m1_r1.setTextureOffset(5, 73).addBox(-4.6F, -4.3F, 0.0F, 7.0F, 7.0F, 0.0F, 0.0F, false);

		fly_right = new ModelRenderer(this);
		fly_right.setRotationPoint(-17.0F, 0.0F, 0.0F);
		remilia.addChild(fly_right);

		gan3 = new ModelRenderer(this);
		gan3.setRotationPoint(19.25F, 1.0F, 0.0F);
		fly_right.addChild(gan3);

		g4_r2 = new ModelRenderer(this);
		g4_r2.setRotationPoint(-11.5F, -21.0F, 3.5F);
		gan3.addChild(g4_r2);
		setRotationAngle(g4_r2, 0.0F, 0.0F, 0.6109F);
		g4_r2.setTextureOffset(0, 70).addBox(-1.3F, -2.4F, -0.5F, 1.0F, 9.0F, 1.0F, 0.0F, false);

		g3_r2 = new ModelRenderer(this);
		g3_r2.setRotationPoint(-10.5F, -20.5F, 3.5F);
		gan3.addChild(g3_r2);
		setRotationAngle(g3_r2, 0.0F, 0.0F, -0.3491F);
		g3_r2.setTextureOffset(0, 70).addBox(0.75F, -4.5F, -0.5F, 1.0F, 10.5F, 1.0F, 0.0F, false);

		g2_r2 = new ModelRenderer(this);
		g2_r2.setRotationPoint(-14.5F, -22.5F, 3.5F);
		gan3.addChild(g2_r2);
		setRotationAngle(g2_r2, 0.0F, 0.0F, -0.3927F);
		g2_r2.setTextureOffset(0, 66).addBox(-8.5F, -0.5F, -0.5F, 13.0F, 1.0F, 1.0F, 0.0F, false);

		g1_r2 = new ModelRenderer(this);
		g1_r2.setRotationPoint(-6.5F, -22.5F, 3.5F);
		gan3.addChild(g1_r2);
		setRotationAngle(g1_r2, 0.0F, 0.0F, 0.3054F);
		g1_r2.setTextureOffset(0, 66).addBox(-4.3F, -0.5F, -0.5F, 9.0F, 1.0F, 1.0F, 0.0F, false);

		membrane3 = new ModelRenderer(this);
		membrane3.setRotationPoint(17.0F, 0.0F, 0.0F);
		fly_right.addChild(membrane3);
		

		m1_1_r2 = new ModelRenderer(this);
		m1_1_r2.setRotationPoint(-3.5F, -17.5F, 3.5F);
		membrane3.addChild(m1_1_r2);
		setRotationAngle(m1_1_r2, 0.0F, 0.0F, -1.2654F);
		m1_1_r2.setTextureOffset(14, 75).addBox(2.5F, -4.7F, 0.0F, 1.0F, 5.0F, 0.0F, 0.0F, false);

		m3_1_r2 = new ModelRenderer(this);
		m3_1_r2.setRotationPoint(-13.3758F, -20.195F, 3.5F);
		membrane3.addChild(m3_1_r2);
		setRotationAngle(m3_1_r2, 0.0F, 0.0F, 1.1345F);
		m3_1_r2.setTextureOffset(40, 73).addBox(-0.5F, -3.4F, 0.0F, 1.0F, 7.0F, 0.0F, 0.0F, false);

		m3_r2 = new ModelRenderer(this);
		m3_r2.setRotationPoint(-13.75F, -18.0F, 3.5F);
		membrane3.addChild(m3_r2);
		setRotationAngle(m3_r2, 0.0F, 0.0F, 0.48F);
		m3_r2.setTextureOffset(32, 72).addBox(-6.1F, -5.6F, 0.0F, 8.0F, 8.0F, 0.0F, 0.0F, false);

		m2_r2 = new ModelRenderer(this);
		m2_r2.setRotationPoint(-9.25F, -19.0F, 3.75F);
		membrane3.addChild(m2_r2);
		setRotationAngle(m2_r2, 0.0F, 0.0F, 0.0873F);
		m2_r2.setTextureOffset(24, 72).addBox(-3.8F, -3.7F, -0.25F, 8.0F, 8.0F, 0.0F, 0.0F, false);

		m1_r2 = new ModelRenderer(this);
		m1_r2.setRotationPoint(-3.8297F, -19.7493F, 3.5F);
		membrane3.addChild(m1_r2);
		setRotationAngle(m1_r2, 0.0F, 0.0F, -0.829F);
		m1_r2.setTextureOffset(5, 73).addBox(-4.6F, -4.3F, 0.0F, 7.0F, 7.0F, 0.0F, 0.0F, false);
	}

	public Iterable<ModelRenderer> getBodyParts() {
		return Iterables.concat(super.getBodyParts(), ImmutableList.of(this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg));
	}

	@Override
	public void setLivingAnimations(@NotNull RemiliaScarletEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		super.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(@NotNull MatrixStack matrixStack, @NotNull IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		remilia.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}

 */