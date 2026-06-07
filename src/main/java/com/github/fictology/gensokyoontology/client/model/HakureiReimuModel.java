package com.github.fictology.gensokyoontology.client.model;// Made create Blockbench 4.8.3
// Exported for Minecraft version 1.15 - 1.16 create Mojang mappings
// Paste this class into your mod and generate all required imports
/*

import github.thelawf.gensokyoontology.common.entiy.HakureiReimuEntity;

public class HakureiReimuModel extends GSKOBipedModel<HakureiReimuEntity> {
	private final ModelRenderer reimu;
	private final ModelRenderer hand;
	private final ModelRenderer jewelry;
	private final ModelRenderer j2_r1;
	private final ModelRenderer j1_r1;
	private final ModelRenderer left_up;
	private final ModelRenderer bdown_r1;
	private final ModelRenderer bup_r1;
	private final ModelRenderer bb_r1;
	private final ModelRenderer c_5_r1;
	private final ModelRenderer d_r1;
	private final ModelRenderer u_r1;
	private final ModelRenderer right_up;
	private final ModelRenderer bdown_r2;
	private final ModelRenderer bup_r2;
	private final ModelRenderer bb_r2;
	private final ModelRenderer c_5_r2;
	private final ModelRenderer d_r2;
	private final ModelRenderer u_r2;
	private final ModelRenderer left_down;
	private final ModelRenderer bdown_r3;
	private final ModelRenderer bup_r3;
	private final ModelRenderer bb_r3;
	private final ModelRenderer c_5_r3;
	private final ModelRenderer d_r3;
	private final ModelRenderer u_r3;
	private final ModelRenderer right_down;
	private final ModelRenderer bdown_r4;
	private final ModelRenderer bup_r4;
	private final ModelRenderer bb_r4;
	private final ModelRenderer c_5_r4;
	private final ModelRenderer d_r4;
	private final ModelRenderer u_r4;
	private final ModelRenderer hat;
	private final ModelRenderer hE_r1;
	private final ModelRenderer hW_r1;
	private final ModelRenderer skirt;
	private final ModelRenderer s8_r1;
	private final ModelRenderer s7_r1;
	private final ModelRenderer s6_r1;
	private final ModelRenderer s5_r1;
	private final ModelRenderer s4_r1;
	private final ModelRenderer s3_r1;
	private final ModelRenderer s2_r1;
	private final ModelRenderer s1_r1;

	public HakureiReimuModel(float modelSize) {
		super(modelSize);
		textureWidth = 80;
		textureHeight = 80;

		reimu = new ModelRenderer(this);
		reimu.setRotationPoint(0.0F, 24.0F, 0.0F);

		hand = new ModelRenderer(this);
		hand.setRotationPoint(0.0F, 0.0F, 0.0F);
		reimu.addChild(hand);
		hand.setTextureOffset(0, 0).addBox(-4.0F, -31.25F, -3.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

		jewelry = new ModelRenderer(this);
		jewelry.setRotationPoint(0.0F, -30.5F, 5.5F);
		hand.addChild(jewelry);
		jewelry.setTextureOffset(36, 72).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);

		j2_r1 = new ModelRenderer(this);
		j2_r1.setRotationPoint(-0.397F, 3.3697F, 0.25F);
		jewelry.addChild(j2_r1);
		setRotationAngle(j2_r1, 0.0873F, 0.0F, 0.0873F);
		j2_r1.setTextureOffset(36, 70).addBox(-0.8F, -2.6F, -0.25F, 1.0F, 5.0F, 0.5F, 0.0F, false);

		j1_r1 = new ModelRenderer(this);
		j1_r1.setRotationPoint(0.4F, 3.3F, 0.25F);
		jewelry.addChild(j1_r1);
		setRotationAngle(j1_r1, 0.0873F, 0.0F, -0.0873F);
		j1_r1.setTextureOffset(33, 68).addBox(-0.3F, -2.5F, -0.25F, 1.0F, 5.0F, 0.5F, 0.0F, false);

		left_up = new ModelRenderer(this);
		left_up.setRotationPoint(4.6387F, 0.0443F, -0.2575F);
		jewelry.addChild(left_up);
		setRotationAngle(left_up, 0.0F, 0.0F, 0.0873F);
		

		bdown_r1 = new ModelRenderer(this);
		bdown_r1.setRotationPoint(2.3374F, 2.5857F, -0.0175F);
		left_up.addChild(bdown_r1);
		setRotationAngle(bdown_r1, 0.0F, 0.0F, 0.0436F);
		bdown_r1.setTextureOffset(26, 76).addBox(-0.85F, -0.5F, -0.125F, 1.0F, 1.0F, 0.25F, 0.0F, false);

		bup_r1 = new ModelRenderer(this);
		bup_r1.setRotationPoint(1.608F, -2.8933F, -0.0175F);
		left_up.addChild(bup_r1);
		setRotationAngle(bup_r1, 0.0F, 0.0F, -0.3491F);
		bup_r1.setTextureOffset(26, 76).addBox(-0.4F, -0.6F, -0.125F, 0.8F, 1.0F, 0.25F, 0.0F, false);

		bb_r1 = new ModelRenderer(this);
		bb_r1.setRotationPoint(2.312F, -0.1465F, -0.0175F);
		left_up.addChild(bb_r1);
		setRotationAngle(bb_r1, 0.0F, 0.0F, -0.0873F);
		bb_r1.setTextureOffset(26, 73).addBox(-0.5F, -2.5F, -0.125F, 0.5F, 5.0F, 0.25F, 0.0F, false);

		c_5_r1 = new ModelRenderer(this);
		c_5_r1.setRotationPoint(1.4466F, 1.435F, 0.0075F);
		left_up.addChild(c_5_r1);
		setRotationAngle(c_5_r1, 0.0F, 0.0F, -0.0873F);
		c_5_r1.setTextureOffset(36, 69).addBox(-4.5F, -2.5F, -0.25F, 1.0F, 2.0F, 0.5F, 0.0F, false);
		c_5_r1.setTextureOffset(36, 69).addBox(-3.5F, -3.0F, -0.25F, 1.0F, 3.0F, 0.5F, 0.0F, false);
		c_5_r1.setTextureOffset(36, 69).addBox(-2.5F, -3.0F, -0.25F, 1.0F, 3.0F, 0.5F, 0.0F, false);
		c_5_r1.setTextureOffset(36, 69).addBox(-1.5F, -3.65F, -0.25F, 1.0F, 4.0F, 0.5F, 0.0F, false);
		c_5_r1.setTextureOffset(32, 68).addBox(-0.5F, -4.1F, -0.25F, 1.0F, 5.0F, 0.5F, 0.0F, false);

		d_r1 = new ModelRenderer(this);
		d_r1.setRotationPoint(-0.8754F, 1.6966F, 0.0075F);
		left_up.addChild(d_r1);
		setRotationAngle(d_r1, 0.0F, 0.0F, 0.3491F);
		d_r1.setTextureOffset(32, 73).addBox(-3.1F, -0.5F, -0.35F, 6.0F, 1.0F, 0.7F, 0.0F, false);

		u_r1 = new ModelRenderer(this);
		u_r1.setRotationPoint(-0.7908F, -1.2503F, 0.0075F);
		left_up.addChild(u_r1);
		setRotationAngle(u_r1, 0.0F, 0.0F, -0.5236F);
		u_r1.setTextureOffset(32, 73).addBox(-3.3F, -0.9F, -0.35F, 6.0F, 1.0F, 0.7F, 0.0F, false);

		right_up = new ModelRenderer(this);
		right_up.setRotationPoint(-3.8153F, -0.0072F, -0.25F);
		jewelry.addChild(right_up);
		setRotationAngle(right_up, 0.0F, 3.1416F, -0.0873F);
		

		bdown_r2 = new ModelRenderer(this);
		bdown_r2.setRotationPoint(3.1015F, 2.5035F, -0.025F);
		right_up.addChild(bdown_r2);
		setRotationAngle(bdown_r2, 0.0F, 0.0F, 0.0436F);
		bdown_r2.setTextureOffset(26, 76).addBox(-0.85F, -0.5F, -0.125F, 1.0F, 1.0F, 0.25F, 0.0F, false);

		bup_r2 = new ModelRenderer(this);
		bup_r2.setRotationPoint(2.3721F, -2.9755F, -0.025F);
		right_up.addChild(bup_r2);
		setRotationAngle(bup_r2, 0.0F, 0.0F, -0.3491F);
		bup_r2.setTextureOffset(26, 76).addBox(-0.4F, -0.6F, -0.125F, 0.8F, 1.0F, 0.25F, 0.0F, false);

		bb_r2 = new ModelRenderer(this);
		bb_r2.setRotationPoint(3.0761F, -0.2287F, -0.025F);
		right_up.addChild(bb_r2);
		setRotationAngle(bb_r2, 0.0F, 0.0F, -0.0873F);
		bb_r2.setTextureOffset(26, 73).addBox(-0.5F, -2.5F, -0.125F, 0.5F, 5.0F, 0.25F, 0.0F, false);

		c_5_r2 = new ModelRenderer(this);
		c_5_r2.setRotationPoint(2.2107F, 1.3528F, 0.0F);
		right_up.addChild(c_5_r2);
		setRotationAngle(c_5_r2, 0.0F, 0.0F, -0.0873F);
		c_5_r2.setTextureOffset(36, 69).addBox(-4.5F, -2.5F, -0.25F, 1.0F, 2.0F, 0.5F, 0.0F, false);
		c_5_r2.setTextureOffset(36, 69).addBox(-3.5F, -3.0F, -0.25F, 1.0F, 3.0F, 0.5F, 0.0F, false);
		c_5_r2.setTextureOffset(36, 69).addBox(-2.5F, -3.0F, -0.25F, 1.0F, 3.0F, 0.5F, 0.0F, false);
		c_5_r2.setTextureOffset(36, 69).addBox(-1.5F, -3.65F, -0.25F, 1.0F, 4.0F, 0.5F, 0.0F, false);
		c_5_r2.setTextureOffset(32, 68).addBox(-0.5F, -4.1F, -0.25F, 1.0F, 5.0F, 0.5F, 0.0F, false);

		d_r2 = new ModelRenderer(this);
		d_r2.setRotationPoint(-0.1113F, 1.6144F, 0.0F);
		right_up.addChild(d_r2);
		setRotationAngle(d_r2, 0.0F, 0.0F, 0.3491F);
		d_r2.setTextureOffset(32, 73).addBox(-3.1F, -0.5F, -0.35F, 6.0F, 1.0F, 0.7F, 0.0F, false);

		u_r2 = new ModelRenderer(this);
		u_r2.setRotationPoint(-0.0267F, -1.3325F, 0.0F);
		right_up.addChild(u_r2);
		setRotationAngle(u_r2, 0.0F, 0.0F, -0.5236F);
		u_r2.setTextureOffset(32, 73).addBox(-3.3F, -0.9F, -0.35F, 6.0F, 1.0F, 0.7F, 0.0F, false);

		left_down = new ModelRenderer(this);
		left_down.setRotationPoint(4.3565F, 2.256F, 0.3425F);
		jewelry.addChild(left_down);
		setRotationAngle(left_down, 0.0F, -0.0873F, 0.5236F);
		

		bdown_r3 = new ModelRenderer(this);
		bdown_r3.setRotationPoint(2.3844F, 2.6028F, -0.0175F);
		left_down.addChild(bdown_r3);
		setRotationAngle(bdown_r3, 0.0F, 0.0F, 0.0436F);
		bdown_r3.setTextureOffset(26, 76).addBox(-0.85F, -0.5F, -0.125F, 1.0F, 1.0F, 0.25F, 0.0F, false);

		bup_r3 = new ModelRenderer(this);
		bup_r3.setRotationPoint(1.655F, -2.8762F, -0.0175F);
		left_down.addChild(bup_r3);
		setRotationAngle(bup_r3, 0.0F, 0.0F, -0.3491F);
		bup_r3.setTextureOffset(26, 76).addBox(-0.4F, -0.6F, -0.125F, 0.8F, 1.0F, 0.25F, 0.0F, false);

		bb_r3 = new ModelRenderer(this);
		bb_r3.setRotationPoint(2.359F, -0.1294F, -0.0175F);
		left_down.addChild(bb_r3);
		setRotationAngle(bb_r3, 0.0F, 0.0F, -0.0873F);
		bb_r3.setTextureOffset(26, 73).addBox(-0.5F, -2.5F, -0.125F, 0.5F, 5.0F, 0.25F, 0.0F, false);

		c_5_r3 = new ModelRenderer(this);
		c_5_r3.setRotationPoint(1.4935F, 1.4521F, 0.0075F);
		left_down.addChild(c_5_r3);
		setRotationAngle(c_5_r3, 0.0F, 0.0F, -0.0873F);
		c_5_r3.setTextureOffset(36, 69).addBox(-4.5F, -2.5F, -0.25F, 1.0F, 2.0F, 0.5F, 0.0F, false);
		c_5_r3.setTextureOffset(36, 69).addBox(-3.5F, -3.0F, -0.25F, 1.0F, 3.0F, 0.5F, 0.0F, false);
		c_5_r3.setTextureOffset(36, 69).addBox(-2.5F, -3.0F, -0.25F, 1.0F, 3.0F, 0.5F, 0.0F, false);
		c_5_r3.setTextureOffset(36, 69).addBox(-1.5F, -3.65F, -0.25F, 1.0F, 4.0F, 0.5F, 0.0F, false);
		c_5_r3.setTextureOffset(32, 68).addBox(-0.5F, -4.1F, -0.25F, 1.0F, 5.0F, 0.5F, 0.0F, false);

		d_r3 = new ModelRenderer(this);
		d_r3.setRotationPoint(-0.8284F, 1.7137F, 0.0075F);
		left_down.addChild(d_r3);
		setRotationAngle(d_r3, 0.0F, 0.0F, 0.3491F);
		d_r3.setTextureOffset(32, 73).addBox(-4.1F, -0.5F, -0.35F, 7.0F, 1.0F, 0.7F, 0.0F, false);

		u_r3 = new ModelRenderer(this);
		u_r3.setRotationPoint(-0.7438F, -1.2332F, 0.0075F);
		left_down.addChild(u_r3);
		setRotationAngle(u_r3, 0.0F, 0.0F, -0.5236F);
		u_r3.setTextureOffset(32, 69).addBox(-3.3F, -0.9F, -0.35F, 6.0F, 1.0F, 0.7F, 0.0F, false);

		right_down = new ModelRenderer(this);
		right_down.setRotationPoint(-4.3337F, 2.3275F, 0.2575F);
		jewelry.addChild(right_down);
		setRotationAngle(right_down, 0.0F, -3.0543F, -0.5236F);
		

		bdown_r4 = new ModelRenderer(this);
		bdown_r4.setRotationPoint(2.3374F, 2.5857F, -0.0175F);
		right_down.addChild(bdown_r4);
		setRotationAngle(bdown_r4, 0.0F, 0.0F, 0.0436F);
		bdown_r4.setTextureOffset(26, 76).addBox(-0.85F, -0.5F, -0.125F, 1.0F, 1.0F, 0.25F, 0.0F, false);

		bup_r4 = new ModelRenderer(this);
		bup_r4.setRotationPoint(1.608F, -2.8933F, -0.0175F);
		right_down.addChild(bup_r4);
		setRotationAngle(bup_r4, 0.0F, 0.0F, -0.3491F);
		bup_r4.setTextureOffset(26, 76).addBox(-0.4F, -0.6F, -0.125F, 0.8F, 1.0F, 0.25F, 0.0F, false);

		bb_r4 = new ModelRenderer(this);
		bb_r4.setRotationPoint(2.312F, -0.1465F, -0.0175F);
		right_down.addChild(bb_r4);
		setRotationAngle(bb_r4, 0.0F, 0.0F, -0.0873F);
		bb_r4.setTextureOffset(26, 73).addBox(-0.5F, -2.5F, -0.125F, 0.5F, 5.0F, 0.25F, 0.0F, false);

		c_5_r4 = new ModelRenderer(this);
		c_5_r4.setRotationPoint(1.4466F, 1.435F, 0.0075F);
		right_down.addChild(c_5_r4);
		setRotationAngle(c_5_r4, 0.0F, 0.0F, -0.0873F);
		c_5_r4.setTextureOffset(36, 69).addBox(-4.5F, -2.5F, -0.25F, 1.0F, 2.0F, 0.5F, 0.0F, false);
		c_5_r4.setTextureOffset(36, 69).addBox(-3.5F, -3.0F, -0.25F, 1.0F, 3.0F, 0.5F, 0.0F, false);
		c_5_r4.setTextureOffset(36, 69).addBox(-2.5F, -3.0F, -0.25F, 1.0F, 3.0F, 0.5F, 0.0F, false);
		c_5_r4.setTextureOffset(36, 69).addBox(-1.5F, -3.65F, -0.25F, 1.0F, 4.0F, 0.5F, 0.0F, false);
		c_5_r4.setTextureOffset(32, 68).addBox(-0.5F, -4.1F, -0.25F, 1.0F, 5.0F, 0.5F, 0.0F, false);

		d_r4 = new ModelRenderer(this);
		d_r4.setRotationPoint(-0.8754F, 1.6966F, 0.0075F);
		right_down.addChild(d_r4);
		setRotationAngle(d_r4, 0.0F, 0.0F, 0.3491F);
		d_r4.setTextureOffset(32, 73).addBox(-4.1F, -0.5F, -0.35F, 7.0F, 1.0F, 0.7F, 0.0F, false);

		u_r4 = new ModelRenderer(this);
		u_r4.setRotationPoint(-0.7908F, -1.2503F, 0.0075F);
		right_down.addChild(u_r4);
		setRotationAngle(u_r4, 0.0F, 0.0F, -0.5236F);
		u_r4.setTextureOffset(32, 73).addBox(-3.3F, -0.9F, -0.35F, 6.0F, 1.0F, 0.7F, 0.0F, false);

		hat = new ModelRenderer(this);
		hat.setRotationPoint(0.0F, 0.0F, 0.0F);
		hand.addChild(hat);
		hat.setTextureOffset(64, 18).addBox(-4.0F, -31.3F, -3.2F, 8.0F, 8.0F, 0.0F, 0.0F, false);
		hat.setTextureOffset(56, 9).addBox(-4.0F, -31.5F, -3.0F, 8.0F, 0.0F, 8.0F, 0.0F, false);
		hat.setTextureOffset(64, 36).addBox(-4.0F, -31.0F, 5.25F, 8.0F, 8.0F, 0.0F, 0.0F, false);

		hE_r1 = new ModelRenderer(this);
		hE_r1.setRotationPoint(-3.0F, -26.0F, -1.0F);
		hat.addChild(hE_r1);
		setRotationAngle(hE_r1, 0.0F, -1.5708F, 0.0F);
		hE_r1.setTextureOffset(64, 0).addBox(-2.0F, -5.2F, -7.15F, 8.0F, 8.0F, 0.0F, 0.0F, false);

		hW_r1 = new ModelRenderer(this);
		hW_r1.setRotationPoint(-4.2F, -27.1F, 0.9F);
		hat.addChild(hW_r1);
		setRotationAngle(hW_r1, 0.0F, -1.5708F, 0.0F);
		hW_r1.setTextureOffset(64, 27).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, 0.0F, false);

		leftArm = new ModelRenderer(this);
		leftArm.setRotationPoint(5.4F, -17.4F, 1.0F);
		reimu.addChild(leftArm);
		leftArm.setTextureOffset(40, 32).addBox(-1.6F, -6.0F, -2.1F, 3.2F, 12.0F, 4.2F, 0.0F, false);
		leftArm.setTextureOffset(40, 16).addBox(-1.5F, -5.8F, -2.0F, 3.0F, 11.6F, 4.0F, 0.0F, false);

		rightArm = new ModelRenderer(this);
		rightArm.setRotationPoint(-5.6F, -17.4F, 1.0F);
		reimu.addChild(rightArm);
		rightArm.setTextureOffset(48, 48).addBox(-1.6F, -6.0F, -2.1F, 3.2F, 12.0F, 4.2F, 0.0F, false);
		rightArm.setTextureOffset(32, 48).addBox(-1.5F, -5.8F, -2.0F, 3.0F, 11.6F, 4.0F, 0.0F, false);

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 0.0F, 0.0F);
		reimu.addChild(body);
		body.setTextureOffset(16, 16).addBox(-4.0F, -23.2F, -1.0F, 8.0F, 11.6F, 4.0F, 0.0F, false);

		skirt = new ModelRenderer(this);
		skirt.setRotationPoint(-0.0024F, -11.1567F, 1.0186F);
		body.addChild(skirt);
		

		s8_r1 = new ModelRenderer(this);
		s8_r1.setRotationPoint(3.9965F, 0.4301F, 2.0763F);
		skirt.addChild(s8_r1);
		setRotationAngle(s8_r1, 0.1309F, 0.0F, -0.1309F);
		s8_r1.setTextureOffset(3, 71).addBox(-0.4F, -4.5F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

		s7_r1 = new ModelRenderer(this);
		s7_r1.setRotationPoint(-4.0291F, 0.429F, 2.0848F);
		skirt.addChild(s7_r1);
		setRotationAngle(s7_r1, 0.1309F, 0.0F, 0.1309F);
		s7_r1.setTextureOffset(5, 71).addBox(-0.5F, -4.5F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

		s6_r1 = new ModelRenderer(this);
		s6_r1.setRotationPoint(3.9073F, 0.2412F, -1.9132F);
		skirt.addChild(s6_r1);
		setRotationAngle(s6_r1, -0.1309F, 0.0F, -0.1309F);
		s6_r1.setTextureOffset(4, 71).addBox(-0.4F, -4.2F, -0.6F, 1.0F, 8.0F, 1.0F, 0.0F, false);

		s5_r1 = new ModelRenderer(this);
		s5_r1.setRotationPoint(-4.0121F, 0.2996F, -1.9237F);
		skirt.addChild(s5_r1);
		setRotationAngle(s5_r1, -0.1309F, 0.0F, 0.1309F);
		s5_r1.setTextureOffset(2, 71).addBox(-0.5F, -4.2F, -0.6F, 1.0F, 8.0F, 1.0F, 0.0F, false);

		s4_r1 = new ModelRenderer(this);
		s4_r1.setRotationPoint(-4.4938F, 0.2324F, -0.0186F);
		skirt.addChild(s4_r1);
		setRotationAngle(s4_r1, 0.0F, 0.0F, 0.2618F);
		s4_r1.setTextureOffset(0, 68).addBox(-0.7F, -4.3F, -2.0F, 2.0F, 8.0F, 4.0F, 0.0F, false);

		s3_r1 = new ModelRenderer(this);
		s3_r1.setRotationPoint(3.8882F, 1.2482F, -0.0186F);
		skirt.addChild(s3_r1);
		setRotationAngle(s3_r1, 0.0F, 0.0F, -0.2618F);
		s3_r1.setTextureOffset(0, 68).addBox(-0.5F, -5.2F, -2.0F, 2.0F, 8.0F, 4.0F, 0.0F, false);

		s2_r1 = new ModelRenderer(this);
		s2_r1.setRotationPoint(0.0024F, 2.8126F, -2.6366F);
		skirt.addChild(s2_r1);
		setRotationAngle(s2_r1, 0.2618F, 0.0F, 0.0F);
		s2_r1.setTextureOffset(0, 70).addBox(-4.0F, -5.5F, 4.3F, 8.0F, 8.0F, 2.0F, 0.0F, false);

		s1_r1 = new ModelRenderer(this);
		s1_r1.setRotationPoint(0.0024F, 0.0941F, -2.7364F);
		skirt.addChild(s1_r1);
		setRotationAngle(s1_r1, -0.2618F, 0.0F, 0.0F);
		s1_r1.setTextureOffset(0, 70).addBox(-4.0F, -4.2F, -0.4F, 8.0F, 8.0F, 2.0F, 0.0F, false);

		rightLeg = new ModelRenderer(this);
		rightLeg.setRotationPoint(-2.0F, -11.8F, 1.0F);
		reimu.addChild(rightLeg);
		rightLeg.setTextureOffset(0, 16).addBox(-2.0F, 0.2F, -2.0F, 4.0F, 11.6F, 4.0F, 0.0F, false);

		leftLeg = new ModelRenderer(this);
		leftLeg.setRotationPoint(2.0F, -11.8F, 1.0F);
		reimu.addChild(leftLeg);
		leftLeg.setTextureOffset(16, 48).addBox(-2.0F, 0.2F, -2.0F, 4.0F, 11.6F, 4.0F, 0.0F, false);

		// rightLeg = new ModelRenderer(this);
		// rightLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
		// reimu.addChild(rightLeg);
		// rightLeg.setTextureOffset(0, 16).addBox(-4.0F, -11.6F, -1.0F, 4.0F, 11.6F, 4.0F, 0.0F, false);
//
		// leftLeg = new ModelRenderer(this);
		// leftLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
		// reimu.addChild(leftLeg);
		// leftLeg.setTextureOffset(16, 48).addBox(0.0F, -11.6F, -1.0F, 4.0F, 11.6F, 4.0F, 0.0F, false);
	}


	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		reimu.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
	}
}

 */