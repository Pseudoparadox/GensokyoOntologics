package com.github.fictology.gensokyoontology.client.model;

import com.github.fictology.gensokyoontology.client.renderer.state.FlandreState;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;

import java.util.Set;

public class FlandreScarletModel extends HumanoidModel<FlandreState> {

	private final ModelPart wingLeft;
	private final ModelPart wingRight;
	private final ModelPart skirt;

	public static final ModelLayerLocation ID =
			new ModelLayerLocation(GSKOUtil.key("flandre"), "main");

	public static LayerDefinition createLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		// ── HEAD ────────────────────────────────────────────────
		// head 本身（blockbench 导出的主体头盒）
		var head = root.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 0).addBox(-4.0F, -31.25F, -3.0F, 8.0F, 8.0F, 8.0F),
				PartPose.offset(0F, 24F, 0F));

		head.addOrReplaceChild("hat", CubeListBuilder.create()
						.texOffs(32, 0).addBox(4.1F, -31.35F, -3.3F, 8.2F, 8.2F, 8.2F),
				PartPose.offset(-8F, 0F,0F));


		// ── ARMS ───────────────────────────────────────────────
		root.addOrReplaceChild("left_arm", CubeListBuilder.create()
						.texOffs(51, 31).addBox(-1.6F, -6.0F, -2.1F, 3.2F, 12.0F, 4.2F)
						.texOffs(40, 17).addBox(-1.5F, -5.8F, -2.0F, 3.0F, 11.6F, 4.0F),
				PartPose.offset(5.4F, -17.4F + 24, 1.0F));

		root.addOrReplaceChild("right_arm", CubeListBuilder.create()
						.texOffs(51, 31).addBox(-1.6F, -6.0F, -2.1F, 3.2F, 12.0F, 4.2F)
						.texOffs(33, 48).addBox(-1.5F, -5.8F, -2.0F, 3.0F, 11.6F, 4.0F),
				PartPose.offset(-5.6F, -17.4F + 24, 1.0F));

		var body = root.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(16, 17).addBox(-4.0F, -23.2F, -1.0F, 8F, 11.6F, 4F)
						.texOffs(20, 36).addBox(-4.0F, -23.0F, -1.2F, 8F, 5F, 0F),
				PartPose.offset(0, 24, 0));

		var skirt = body.addOrReplaceChild("skirt", CubeListBuilder.create(),
				PartPose.offset(-0.0024F, -11.1567F, 1.0186F));

		skirt.addOrReplaceChild("skirt_table", CubeListBuilder.create()
						.texOffs(56, 17).addBox(-3.5F, -1.8F, -0.4F, 7F, 5F, 0F),
				PartPose.offset(0F, -2.5F, 3F));

		skirt.addOrReplaceChild("skirt_piece_8", CubeListBuilder.create()
						.texOffs(50, 66).addBox(-0.4F, -4.5F, -0.5F, 1F, 8F, 1F),
				PartPose.offset(3.9965F, 0.43F, 2.0763F));
		skirt.addOrReplaceChild("skirt_piece_7", CubeListBuilder.create()
						.texOffs(50, 66).addBox(-0.5F, -4.5F, -0.5F, 1F, 8F, 1F),
				PartPose.offset(-4.0291F, 0.43F, 2.0848F));
		skirt.addOrReplaceChild("skirt_piece_6", CubeListBuilder.create()
						.texOffs(50, 66).addBox(-0.4F, -4.2F, -0.6F, 1F, 8F, 1F),
				PartPose.offset(3.9073F, 0.2412F, -1.9132F));
		skirt.addOrReplaceChild("skirt_piece_5", CubeListBuilder.create()
						.texOffs(50, 66).addBox(-0.5F, -4.2F, -0.6F, 1F, 8F, 1F),
				PartPose.offset(-4.0121F, 0.2996F, -1.9237F));
		skirt.addOrReplaceChild("skirt_piece_4", CubeListBuilder.create()
						.texOffs(46, 63).addBox(-0.7F, -4.3F, -2.0F, 2F, 8F, 4F),
				PartPose.offset(-4.4938F, 0.2324F, -0.0186F));
		skirt.addOrReplaceChild("skirt_piece_3", CubeListBuilder.create()
						.texOffs(46, 63).addBox(-0.5F, -5.2F, -2.0F, 2F, 8F, 4F),
				PartPose.offset(3.8882F, 1.2482F, -0.0186F));
		skirt.addOrReplaceChild("skirt_piece_2", CubeListBuilder.create()
						.texOffs(25, 64).addBox(-4.0F, -5.5F, 4.3F, 8F, 8F, 2F),
				PartPose.offset(0.0024F, 2.8126F, -2.6366F));
		skirt.addOrReplaceChild("skirt_piece_1", CubeListBuilder.create()
						.texOffs(25, 64).addBox(-4.0F, -4.2F, -0.4F, 8F, 8F, 2F),
				PartPose.offset(0.0024F, 0.0941F, -2.7364F));

		// ── LEGS ────────────────────────────────────────────────
		root.addOrReplaceChild("left_leg", CubeListBuilder.create()
						.texOffs(0, 51).addBox(-1.95F, 3.95F, -2.1F, 4F, 8F, 4.2F)
						.texOffs(17, 48).addBox(-2.05F, 0.25F, -2.0F, 4F, 11.6F, 4F),
				PartPose.offset(2.05F, -11.85F + 24, 1.0F));

		root.addOrReplaceChild("right_leg", CubeListBuilder.create()
						.texOffs(0, 36).addBox(-2.05F, 3.95F, -2.1F, 4F, 8F, 4.2F)
						.texOffs(0, 17).addBox(-1.95F, 0.25F, -2.0F, 4F, 11.6F, 4F),
				PartPose.offset(-2.05F, -11.85F + 24, 1.0F));

		var wingLeft = body.addOrReplaceChild("wing_left", CubeListBuilder.create(),
				PartPose.offset(2.25F, 1.0F, 0.0F));

		addCrystalWithRod(wingLeft, "crystal",  "l_rod",
				-18.3489F, -18.0429F, 3.5071F,
				0.0626F, -0.4571F, 0.0636F,
				0, 68, 0, 64);
		addCrystalWithRod(wingLeft, "crystal2", "l2_rod",
				-18.6489F, -18.0429F, 3.5071F,
				3.0626F, -1.4571F, 0.0636F,
				4, 68, 4, 64);
		addCrystalWithRod(wingLeft, "crystal3", "l3_rod",
				-18.9489F, -17.6429F, 3.5071F,
				6.0626F, -2.7571F, 0.0636F,
				8, 68, 8, 64);
		addCrystalWithRod(wingLeft, "crystal4", "l4_rod",
				-18.9489F, -18.9429F, 3.5071F,
				8.8626F, -2.3571F, 0.0636F,
				12, 68, 12, 64);
		addCrystalWithRod(wingLeft, "crystal5", "l5_rod",
				-18.7489F, -18.9429F, 3.5071F,
				11.3626F, -1.4571F, 0.0636F,
				16, 68, 16, 64);
		addCrystalWithRod(wingLeft, "crystal6", "l6_rod",
				-17.3489F, -18.4429F, 3.5071F,
				12.6626F, -1.0571F, 0.0636F,
				20, 68, 20, 64);

		var wingStick = wingLeft.addOrReplaceChild("wing_stick_left", CubeListBuilder.create(), PartPose.ZERO);
		wingStick.addOrReplaceChild("stick_left_p1", CubeListBuilder.create()
						.texOffs(0, 71).addBox(-4.5F, -0.5F, -0.5F, 9F, 1F, 1F),
				PartPose.offset(-14.5F, -22.5F, 3.5F));
		wingStick.addOrReplaceChild("stick_left_p2", CubeListBuilder.create()
						.texOffs(0, 71).addBox(-4.5F, -0.5F, -0.5F, 9F, 1F, 1F),
				PartPose.offset(-6.5F, -22.5F, 3.5F));

		var wingRight = body.addOrReplaceChild("wing_right", CubeListBuilder.create(),
				PartPose.offset(18.75F - 16, 1.0F, 0.0F));

		addCrystalWithRod(wingRight, "crystal7",  "l7_rod",
				2.7068F, -19.4F, 3.5604F,
				0.0068F, 0.9F, 0.0104F,
				0, 68, 0, 64);
		addCrystalWithRod(wingRight, "crystal8",  "l8_rod",
				5.4068F, -20.4F, 3.5604F,
				0.0068F, 0.9F, 0.0104F,
				4, 68, 4, 64);
		addCrystalWithRod(wingRight, "crystal9",  "l9_rod",
				8.1068F, -21.3F, 3.5604F,
				0.0068F, 0.9F, 0.0104F,
				8, 68, 8, 64);
		addCrystalWithRod(wingRight, "crystal10", "l10_rod",
				10.9068F, -22.2F, 3.5604F,
				0.0068F, 0.9F, 0.0104F,
				12, 68, 12, 64);
		addCrystalWithRod(wingRight, "crystal11", "l11_rod",
				13.6068F, -21.3F, 3.5604F,
				0.0068F, 0.9F, 0.0104F,
				16, 68, 16, 64);
		addCrystalWithRod(wingRight, "crystal12", "l12_rod",
				16.3068F, -20.4F, 3.5604F,
				0.0068F, 0.9F, 0.0104F,
				20, 68, 20, 64);

		PartDefinition wingMainStick2 = wingRight.addOrReplaceChild("wing_stick_right",
				CubeListBuilder.create(),
				PartPose.offset(10.5F, -22.5F, 3.5F));
		wingMainStick2.addOrReplaceChild("stick_right_p1", CubeListBuilder.create()
						.texOffs(0, 71).addBox(-4.5F, -0.5F, -0.5F, 9F, 1F, 1F),
				PartPose.offset(-4F, 0F, 0F));
		wingMainStick2.addOrReplaceChild("stick_right_p2", CubeListBuilder.create()
						.texOffs(0, 71).addBox(-4.5F, -0.5F, -0.5F, 9F, 1F, 1F),
				PartPose.offset(4F, 0F, 0F));

		return LayerDefinition.create(mesh, 80, 80);
	}

	private static void addCrystalWithRod(
			PartDefinition parent,
			String crystalName, String rodName,
			float ox, float oy, float oz,
			float rodRx, float rodRy, float rodRz,
			int crystalTexX, int crystalTexY,
			int rodTexX, int rodTexY) {

		PartDefinition crystal = parent.addOrReplaceChild(crystalName,
				CubeListBuilder.create()
						.texOffs(crystalTexX, crystalTexY)
						.addBox(-0.25F, -0.25F, -0.25F, 0.6F, 0.6F, 0.6F),
				PartPose.offset(ox, oy, oz));

		crystal.addOrReplaceChild(rodName,
				CubeListBuilder.create()
						.texOffs(rodTexX, rodTexY)
						.addBox(-0.5F, -1.5F, -0.5F, 1F, 3F, 1F),
				PartPose.offset(rodRx, rodRy, rodRz));
	}

	// ============================================================
	//  构造函数 —— super(root) 活下来了因为 hat 现在在 root 上
	// ============================================================
	public FlandreScarletModel(ModelPart root) {
		super(root);  // ← 现在 root 上有 head/hat/body/left_arm/right_arm/left_leg/right_leg ✓

		// 保存自定义部件引用
		this.skirt     = root.getChild("body").getChild("skirt");
		this.wingLeft  = root.getChild("body").getChild("wing_left");
		this.wingRight = root.getChild("body").getChild("wing_right");

		setRot(skirt.getChild("skirt_piece_8"),  0.1309F, 0F, -0.1309F);
		setRot(skirt.getChild("skirt_piece_7"),   0.1309F, 0F,  0.1309F);
		setRot(skirt.getChild("skirt_piece_6"),  -0.1309F, 0F, -0.1309F);
		setRot(skirt.getChild("skirt_piece_5"),  -0.1309F, 0F,  0.1309F);
		setRot(skirt.getChild("skirt_piece_4"),   0F,      0F,  0.2618F);
		setRot(skirt.getChild("skirt_piece_3"),   0F,      0F, -0.2618F);
		setRot(skirt.getChild("skirt_piece_2"),   0.2618F, 0F,  0F);
		setRot(skirt.getChild("skirt_piece_1"),  -0.2618F, 0F,  0F);

		var wingSL = wingLeft.getChild("wing_stick_left");
		setRot(wingLeft,                            0F, 0F, -0.3491F);
		setRot(wingSL.getChild("stick_left_p1"),    0F, 0F,  0.3491F);
		setRot(wingSL.getChild("stick_left_p2"),    0F, 0F,  0.3491F);


		setRot(wingRight, 0F, (float) Math.PI, 0F);
		// 右翼各杆子的额外Y旋转
		setRot(wingRight.getChild("crystal7").getChild("l7_rod"),  0F, -0.7854F, 0F);
		setRot(wingRight.getChild("crystal8").getChild("l8_rod"),  0F, -0.7854F, 0F);
		setRot(wingRight.getChild("crystal9").getChild("l9_rod"),  0F, -0.7854F, 0F);
		setRot(wingRight.getChild("crystal10").getChild("l10_rod"),0F, -0.7854F, 0F);
		setRot(wingRight.getChild("crystal11").getChild("l11_rod"), 0F, -0.7854F, 0F);
		setRot(wingRight.getChild("crystal12").getChild("l12_rod"), 0F, -0.7854F, 0F);

		var wingSR = wingRight.getChild("wing_stick_right");
		setRot(wingSR.getChild("stick_right_p1"), 0F, 0F, -0.3491F);
		setRot(wingSR.getChild("stick_right_p2"), 0F, 0F,  0.3491F);
	}

	// ============================================================
	//  小工具
	// ============================================================
	private void setRot(ModelPart p, float x, float y, float z) {
		p.xRot = x;
		p.yRot = y;
		p.zRot = z;
	}

	// 保留你原来暴露给外部的方法（如果渲染器用到的话）
	public ModelPart getSkirt()      { return skirt; }
	public ModelPart getWingLeft()   { return wingLeft; }
	public ModelPart getWingRight()  { return wingRight; }
}