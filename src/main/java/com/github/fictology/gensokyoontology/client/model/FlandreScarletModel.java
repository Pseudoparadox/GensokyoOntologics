package com.github.fictology.gensokyoontology.client.model;

import com.github.fictology.gensokyoontology.client.renderer.state.FlandreState;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

import java.util.List;

public class FlandreScarletModel extends HumanoidModel<FlandreState> {

	private List<ModelPart> modelParts = Lists.newArrayList();
	public static final ModelLayerLocation ID = new ModelLayerLocation(GSKOUtil.key("flandre"), "main");

	public static LayerDefinition createLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		// === HEAD ===
		var head = root.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 0).addBox(-4.0F, -31.25F, -3.0F, 8.0F, 8.0F, 8.0F)
						.texOffs(32, 0).addBox(4.1F, -31.35F, -3.3F, 8.2F, 8.2F, 8.2F),
				PartPose.ZERO);
		head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(0f, 0f, 0f, 0f, 0f, 0f, new CubeDeformation(0F)), PartPose.ZERO);

		// === ARMS ===
		root.addOrReplaceChild("left_arm", CubeListBuilder.create()
						.texOffs(51, 31).addBox(-1.6F, -6.0F, -2.1F, 3.2F, 12.0F, 4.2F)
						.texOffs(40, 17).addBox(-1.5F, -5.8F, -2.0F, 3.0F, 11.6F, 4.0F),
				PartPose.offset(5.4F, -17.4F, 1.0F));

		root.addOrReplaceChild("right_arm", CubeListBuilder.create()
						.texOffs(51, 31).addBox(-1.6F, -6.0F, -2.1F, 3.2F, 12.0F, 4.2F)
						.texOffs(33, 48).addBox(-1.5F, -5.8F, -2.0F, 3.0F, 11.6F, 4.0F),
				PartPose.offset(-5.6F, -17.4F, 1.0F));

		// === BODY ===
		var body = root.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(16, 17).addBox(-4.0F, -23.2F, -1.0F, 8F, 11.6F, 4F)
						.texOffs(20, 36).addBox(-4.0F, -23.0F, -1.2F, 8F, 5F, 0F),
				PartPose.ZERO);

		// === SKIRT ===
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

		// === LEGS ===
		root.addOrReplaceChild("left_leg", CubeListBuilder.create()
						.texOffs(0, 51).addBox(-1.95F, 3.95F, -2.1F, 4F, 8F, 4.2F)
						.texOffs(17, 48).addBox(-2.05F, 0.25F, -2.0F, 4F, 11.6F, 4F),
				PartPose.offset(2.05F, -11.85F, 1.0F));

		root.addOrReplaceChild("right_leg", CubeListBuilder.create()
						.texOffs(0, 36).addBox(-2.05F, 3.95F, -2.1F, 4F, 8F, 4.2F)
						.texOffs(0, 17).addBox(-1.95F, 0.25F, -2.0F, 4F, 11.6F, 4F),
				PartPose.offset(-2.05F, -11.85F, 1.0F));

		// === LEFT WING ===
		var wingLeft = root.addOrReplaceChild("wing_left", CubeListBuilder.create(),
				PartPose.offset(2.25F, 1.0F, 0.0F));

		addCrystal(wingLeft, "crystal", -18.3489F, -18.0429F, 3.5071F, 0, 68);
		addRod(wingLeft, "l_rod", -18.3489F, -18.0429F, 3.5071F, 0.0626F, -0.4571F, 0.0636F, 0, 64);

		addCrystal(wingLeft, "crystal2", -18.6489F, -18.0429F, 3.5071F, 4, 68);
		addRod(wingLeft, "l2_rod", -18.6489F, -18.0429F, 3.5071F, 3.0626F, -1.4571F, 0.0636F, 4, 64);

		addCrystal(wingLeft, "crystal3", -18.9489F, -17.6429F, 3.5071F, 8, 68);
		addRod(wingLeft, "l3_rod", -18.9489F, -17.6429F, 3.5071F, 6.0626F, -2.7571F, 0.0636F, 8, 64);

		addCrystal(wingLeft, "crystal4", -18.9489F, -18.9429F, 3.5071F, 12, 68);
		addRod(wingLeft, "l4_rod", -18.9489F, -18.9429F, 3.5071F, 8.8626F, -2.3571F, 0.0636F, 12, 64);

		addCrystal(wingLeft, "crystal5", -18.7489F, -18.9429F, 3.5071F, 16, 68);
		addRod(wingLeft, "l5_rod", -18.7489F, -18.9429F, 3.5071F, 11.3626F, -1.4571F, 0.0636F, 16, 64);

		addCrystal(wingLeft, "crystal6", -17.3489F, -18.4429F, 3.5071F, 20, 68);
		addRod(wingLeft, "l6_rod", -17.3489F, -18.4429F, 3.5071F, 12.6626F, -1.0571F, 0.0636F, 20, 64);

		// Wing sticks
		var wingStick = wingLeft.addOrReplaceChild("wing_stick_left", CubeListBuilder.create(), PartPose.ZERO);
		wingStick.addOrReplaceChild("stick_left_p1", CubeListBuilder.create()
						.texOffs(0, 71).addBox(-4.5F, -0.5F, -0.5F, 9F, 1F, 1F),
				PartPose.offset(-14.5F, -22.5F, 3.5F));
		wingStick.addOrReplaceChild("stick_left_p2", CubeListBuilder.create()
						.texOffs(0, 71).addBox(-4.5F, -0.5F, -0.5F, 9F, 1F, 1F),
				PartPose.offset(-6.5F, -22.5F, 3.5F));

		// === RIGHT WING ===
		var wingRight = root.addOrReplaceChild("wing_right", CubeListBuilder.create(),
				PartPose.offset(18.75F, 1.0F, 7.0F));

		addCrystal(wingRight, "crystal7", 2.7068F, -19.4F, 3.5604F, 0, 68);
		addRod(wingRight, "l7_rod", 2.7068F, -19.4F, 3.5604F, 0.0068F, 0.9F, 0.0104F, 0, 64);

		addCrystal(wingRight, "crystal8", 5.4068F, -20.4F, 3.5604F, 4, 68);
		addRod(wingRight, "l8_rod", 5.4068F, -20.4F, 3.5604F, 0.0068F, 0.9F, 0.0104F, 4, 64);

		addCrystal(wingRight, "crystal9", 8.1068F, -21.3F, 3.5604F, 8, 68);
		addRod(wingRight, "l9_rod", 8.1068F, -21.3F, 3.5604F, 0.0068F, 0.9F, 0.0104F, 8, 64);

		addCrystal(wingRight, "crystal10", 10.9068F, -22.2F, 3.5604F, 12, 68);
		addRod(wingRight, "l10_rod", 10.9068F, -22.2F, 3.5604F, 0.0068F, 0.9F, 0.0104F, 12, 64);

		addCrystal(wingRight, "crystal11", 13.6068F, -21.3F, 3.5604F, 16, 68);
		addRod(wingRight, "l11_rod", 13.6068F, -21.3F, 3.5604F, 0.0068F, 0.9F, 0.0104F, 16, 64);

		addCrystal(wingRight, "crystal12", 16.3068F, -20.4F, 3.5604F, 20, 68);
		addRod(wingRight, "l12_rod", 16.3068F, -20.4F, 3.5604F, 0.0068F, 0.9F, 0.0104F, 20, 64);

		PartDefinition wingMainStick2 = wingRight.addOrReplaceChild("wing_stick_right", CubeListBuilder.create(),
				PartPose.offset(10.5F, -22.5F, 3.5F));
		wingMainStick2.addOrReplaceChild("stick_right_p1", CubeListBuilder.create()
						.texOffs(0, 71).addBox(-4.5F, -0.5F, -0.5F, 9F, 1F, 1F),
				PartPose.offset(-4F, 0F, 0F));
		wingMainStick2.addOrReplaceChild("stick_right_p2", CubeListBuilder.create()
						.texOffs(0, 71).addBox(-4.5F, -0.5F, -0.5F, 9F, 1F, 1F),
				PartPose.offset(4F, 0F, 0F));

		return LayerDefinition.create(mesh, 80, 80);
	}

	/* ===================== 辅助方法 ===================== */
	private static void addCrystal(PartDefinition parent, String name, float ox, float oy, float oz, int texX, int texY) {
		parent.addOrReplaceChild(name, CubeListBuilder.create()
						.texOffs(texX, texY).addBox(-0.25F, -0.25F, -0.25F, 0.6F, 0.6F, 0.6F),
				PartPose.offset(ox, oy, oz));
	}

	private static void addRod(PartDefinition parent, String name, float ox, float oy, float oz,
	                           float rx, float ry, float rz, int texX, int texY) {
		PartDefinition crystal = parent.getChild(
				name.replace("l", "crystal").replace("_rod", ""));
		crystal.addOrReplaceChild(name, CubeListBuilder.create()
						.texOffs(texX, texY).addBox(-0.5F, -1.5F, -0.5F, 1F, 3F, 1F),
				PartPose.offset(rx, ry, rz));
	}


	public FlandreScarletModel(ModelPart root) {
		super(root);

		var body = root.getChild("body");
		var skirt = body.getChild("skirt");
		var wingLeft = root.getChild("wing_left");
		var wingRight = root.getChild("wing_right");
		var wingSL = wingLeft.getChild("wing_stick_left");
		var wingSR = wingRight.getChild("wing_stick_right");

		this.setRotationAngle(skirt.getChild("skirt_piece_8"), 0.1309F, 0.0F, -0.1309F);
		this.setRotationAngle(skirt.getChild("skirt_piece_7"), 0.1309F, 0.0F, 0.1309F);
		this.setRotationAngle(skirt.getChild("skirt_piece_6"),-0.1309F, 0F, -0.1309F);
		this.setRotationAngle(skirt.getChild("skirt_piece_5"), -0.1309F, 0F, 0.1309F);

		this.setRotationAngle(skirt.getChild("skirt_piece_4"), 0F, 0F, 0.2618F);
		this.setRotationAngle(skirt.getChild("skirt_piece_3"), 0F, 0F, -0.2618F);
		this.setRotationAngle(skirt.getChild("skirt_piece_2"), 0.2618F, 0F, 0F);
		this.setRotationAngle(skirt.getChild("skirt_piece_1"), -0.2618F, 0F, 0F);

		this.setRotationAngle(wingLeft.getChild("wing_stick_left"), 0F, 0F, -0.3491F);
		this.setRotationAngle(wingSL.getChild("stick_left_p1"), 0F, 0F, 0.3491F);
		this.setRotationAngle(wingSL.getChild("stick_left_p2"), 0F, 0F, 0.3491F);

		this.setRotationAngle(root.getChild("wing_right"), 0F, (float)Math.PI, 0F);
		this.setRotationAngle(wingRight.getChild("crystal7").getChild("l7_rod"), 0F, -0.7854F, 0F);
		this.setRotationAngle(wingRight.getChild("crystal8").getChild("l8_rod"), 0F, -0.7854F, 0F);
		this.setRotationAngle(wingRight.getChild("crystal9").getChild("l9_rod"), 0F, -0.7854F, 0F);
		this.setRotationAngle(wingRight.getChild("crystal10").getChild("l10_rod"), 0F, -0.7854F, 0F);
		this.setRotationAngle(wingRight.getChild("crystal11").getChild("l11_rod"), 0F, -0.7854F, 0F);
		this.setRotationAngle(wingRight.getChild("crystal12").getChild("l12_rod"), 0F, -0.7854F, 0F);

		this.setRotationAngle(wingSR.getChild("stick_right_p1"), 0F, 0F, -0.3491F);
		this.setRotationAngle(wingSR.getChild("stick_right_p2"), 0F, 0F, 0.3491F);

	}

	public List<ModelPart> getBodyParts() {
		return ImmutableList.of(this.leftLeg, this.rightLeg, this.leftArm, this.rightArm, this.body);
	}

	@Override
	public void setupAnim(FlandreState state) {
		super.setupAnim(state);
	}


	public void setRotationAngle(ModelPart modelPart, float x, float y, float z) {
		modelPart.xRot = x;
		modelPart.yRot = y;
		modelPart.zRot = z;
	}

}

