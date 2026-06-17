package com.github.fictology.gensokyoontology.client.model;

import com.github.fictology.gensokyoontology.client.renderer.state.ReimuState;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class HakureiReimuModel extends HumanoidModel<ReimuState> {

	public static final ModelLayerLocation LAYER =
			new ModelLayerLocation(GSKOUtil.key("reimu"), "main");

	private final ModelPart root;
	private final ModelPart hand;
	private final ModelPart jewelry;
	private final ModelPart hat;
	private final ModelPart skirt;

	public HakureiReimuModel(ModelPart root) {
		super(root);

		this.root = root.getChild("reimu");
		this.hand = this.root.getChild("hand");
		this.jewelry = hand.getChild("jewelry");
		this.hat = hand.getChild("hat");
		this.skirt = this.root.getChild("body").getChild("skirt");

		// ===== 饰品初始旋转 =====
		setRotationAngle(jewelry.getChild("j2_r1"), 0.0873F, 0.0F, 0.0873F);
		setRotationAngle(jewelry.getChild("j1_r1"), 0.0873F, 0.0F, -0.0873F);

		setRotationAngle(jewelry.getChild("left_up"), 0.0F, 0.0F, 0.0873F);
		setRotationAngle(jewelry.getChild("right_up"), 0.0F, (float) Math.PI, -0.0873F);
		setRotationAngle(jewelry.getChild("left_down"), 0.0F, -0.0873F, 0.5236F);
		setRotationAngle(jewelry.getChild("right_down"), 0.0F, -3.0543F, -0.5236F);

		applyJewelRotations(jewelry.getChild("left_up"));
		applyJewelRotations(jewelry.getChild("right_up"));
		applyJewelRotations(jewelry.getChild("left_down"));
		applyJewelRotations(jewelry.getChild("right_down"));

		// ===== 帽子 =====
		setRotationAngle(hat.getChild("hE_r1"), 0.0F, -1.5708F, 0.0F);
		setRotationAngle(hat.getChild("hW_r1"), 0.0F, -1.5708F, 0.0F);

		// ===== 裙子 =====
		setRotationAngle(skirt.getChild("s8_r1"), 0.1309F, 0.0F, -0.1309F);
		setRotationAngle(skirt.getChild("s7_r1"), 0.1309F, 0.0F, 0.1309F);
		setRotationAngle(skirt.getChild("s6_r1"), -0.1309F, 0.0F, -0.1309F);
		setRotationAngle(skirt.getChild("s5_r1"), -0.1309F, 0.0F, 0.1309F);
		setRotationAngle(skirt.getChild("s4_r1"), 0.0F, 0.0F, 0.2618F);
		setRotationAngle(skirt.getChild("s3_r1"), 0.0F, 0.0F, -0.2618F);
		setRotationAngle(skirt.getChild("s2_r1"), 0.2618F, 0.0F, 0.0F);
		setRotationAngle(skirt.getChild("s1_r1"), -0.2618F, 0.0F, 0.0F);
	}

	private void applyJewelRotations(ModelPart part) {
		setRotationAngle(part.getChild("bdown_r1"), 0.0F, 0.0F, 0.0436F);
		setRotationAngle(part.getChild("bup_r1"), 0.0F, 0.0F, -0.3491F);
		setRotationAngle(part.getChild("bb_r1"), 0.0F, 0.0F, -0.0873F);
		setRotationAngle(part.getChild("c_5_r1"), 0.0F, 0.0F, -0.0873F);
		setRotationAngle(part.getChild("d_r1"), 0.0F, 0.0F, 0.3491F);
		setRotationAngle(part.getChild("u_r1"), 0.0F, 0.0F, -0.5236F);
	}

	public static LayerDefinition createLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition rootPart = mesh.getRoot();

		var head = rootPart.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 0).addBox(-4.0F, -31.25F, -3.0F, 8.0F, 8.0F, 8.0F)
						.texOffs(32, 0).addBox(4.1F, -31.35F, -3.3F, 8.2F, 8.2F, 8.2F),
				PartPose.ZERO);
		head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(0f, 0f, 0f, 0f, 0f, 0f, new CubeDeformation(0F)), PartPose.ZERO);


		// === HEAD ===
		PartDefinition hand = rootPart.addOrReplaceChild("hand", CubeListBuilder.create()
						.texOffs(0, 0).addBox(-4F, -31.25F, -3F, 8F, 8F, 8F),
				PartPose.ZERO);

		// === JEWELRY ===
		PartDefinition jewelry = hand.addOrReplaceChild("jewelry", CubeListBuilder.create()
						.texOffs(36, 72).addBox(-1F, -1F, -0.5F, 2F, 2F, 1F),
				PartPose.offset(0F, -30.5F, 5.5F));

		jewelry.addOrReplaceChild("j2_r1", CubeListBuilder.create()
						.texOffs(36, 70).addBox(-0.8F, -2.6F, -0.25F, 1F, 5F, 0.5F),
				PartPose.offset(-0.397F, 3.3697F, 0.25F));

		jewelry.addOrReplaceChild("j1_r1", CubeListBuilder.create()
						.texOffs(33, 68).addBox(-0.3F, -2.5F, -0.25F, 1F, 5F, 0.5F),
				PartPose.offset(0.4F, 3.3F, 0.25F));

		addJewelSide(jewelry, "left_up", 4.6387F, 0.0443F, -0.2575F);
		addJewelSide(jewelry, "right_up", -3.8153F, -0.0072F, -0.25F);
		addJewelSide(jewelry, "left_down", 4.3565F, 2.256F, 0.3425F);
		addJewelSide(jewelry, "right_down", -4.3337F, 2.3275F, 0.2575F);

		// === HAT ===
		PartDefinition hat = hand.addOrReplaceChild("hat", CubeListBuilder.create()
						.texOffs(64, 18).addBox(-4F, -31.3F, -3.2F, 8F, 8F, 0F)
						.texOffs(56, 9).addBox(-4F, -31.5F, -3F, 8F, 0F, 8F)
						.texOffs(64, 36).addBox(-4F, -31F, 5.25F, 8F, 8F, 0F),
				PartPose.ZERO);

		hat.addOrReplaceChild("hE_r1", CubeListBuilder.create()
						.texOffs(64, 0).addBox(-2F, -5.2F, -7.15F, 8F, 8F, 0F),
				PartPose.offset(-3F, -26F, -1F));

		hat.addOrReplaceChild("hW_r1", CubeListBuilder.create()
						.texOffs(64, 27).addBox(-4F, -4F, 0F, 8F, 8F, 0F),
				PartPose.offset(-4.2F, -27.1F, 0.9F));

		// === BODY ===
		PartDefinition body = rootPart.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(16, 16).addBox(-4F, -23.2F, -1F, 8F, 11.6F, 4F),
				PartPose.ZERO);

		// === SKIRT ===
		PartDefinition skirt = body.addOrReplaceChild("skirt", CubeListBuilder.create(),
				PartPose.offset(-0.0024F, -11.1567F, 1.0186F));

		addSkirtPiece(skirt, "s8_r1", 3.9965F, 0.4301F, 2.0763F);
		addSkirtPiece(skirt, "s7_r1", -4.0291F, 0.429F, 2.0848F);
		addSkirtPiece(skirt, "s6_r1", 3.9073F, 0.2412F, -1.9132F);
		addSkirtPiece(skirt, "s5_r1", -4.0121F, 0.2996F, -1.9237F);
		addSkirtPiece(skirt, "s4_r1", -4.4938F, 0.2324F, -0.0186F, 0, 68);
		addSkirtPiece(skirt, "s3_r1", 3.8882F, 1.2482F, -0.0186F, 0, 68);
		addSkirtPiece(skirt, "s2_r1", 0.0024F, 2.8126F, -2.6366F, 0, 70);
		addSkirtPiece(skirt, "s1_r1", 0.0024F, 0.0941F, -2.7364F, 0, 70);

		// === ARMS & LEGS ===
		rootPart.addOrReplaceChild("left_arm", CubeListBuilder.create()
						.texOffs(40, 32).addBox(-1.6F, -6F, -2.1F, 3.2F, 12F, 4.2F)
						.texOffs(40, 16).addBox(-1.5F, -5.8F, -2F, 3F, 11.6F, 4F),
				PartPose.offset(5.4F, -17.4F, 1F));

		rootPart.addOrReplaceChild("right_arm", CubeListBuilder.create()
						.texOffs(48, 48).addBox(-1.6F, -6F, -2.1F, 3.2F, 12F, 4.2F)
						.texOffs(32, 48).addBox(-1.5F, -5.8F, -2F, 3F, 11.6F, 4F),
				PartPose.offset(-5.6F, -17.4F, 1F));

		rootPart.addOrReplaceChild("left_leg", CubeListBuilder.create()
						.texOffs(16, 48).addBox(-2F, 0.2F, -2F, 4F, 11.6F, 4F),
				PartPose.offset(2F, -11.8F, 1F));

		rootPart.addOrReplaceChild("right_leg", CubeListBuilder.create()
						.texOffs(0, 16).addBox(-2F, 0.2F, -2F, 4F, 11.6F, 4F),
				PartPose.offset(-2F, -11.8F, 1F));

		return LayerDefinition.create(mesh, 80, 80);
	}

	/* ================== 辅助方法 ================== */

	private static void addJewelSide(PartDefinition parent, String name, float x, float y, float z) {
		PartDefinition side = parent.addOrReplaceChild(name, CubeListBuilder.create(),
				PartPose.offset(x, y, z));

		side.addOrReplaceChild("bdown_r1", CubeListBuilder.create()
						.texOffs(26, 76).addBox(-0.85F, -0.5F, -0.125F, 1F, 1F, 0.25F),
				PartPose.offset(2.3374F, 2.5857F, -0.0175F));

		side.addOrReplaceChild("bup_r1", CubeListBuilder.create()
						.texOffs(26, 76).addBox(-0.4F, -0.6F, -0.125F, 0.8F, 1F, 0.25F),
				PartPose.offset(1.608F, -2.8933F, -0.0175F));

		side.addOrReplaceChild("bb_r1", CubeListBuilder.create()
						.texOffs(26, 73).addBox(-0.5F, -2.5F, -0.125F, 0.5F, 5F, 0.25F),
				PartPose.offset(2.312F, -0.1465F, -0.0175F));

		side.addOrReplaceChild("c_5_r1", CubeListBuilder.create()
						.texOffs(36, 69).addBox(-4.5F, -2.5F, -0.25F, 1F, 2F, 0.5F)
						.texOffs(36, 69).addBox(-3.5F, -3F, -0.25F, 1F, 3F, 0.5F)
						.texOffs(36, 69).addBox(-2.5F, -3F, -0.25F, 1F, 3F, 0.5F)
						.texOffs(36, 69).addBox(-1.5F, -3.65F, -0.25F, 1F, 4F, 0.5F)
						.texOffs(32, 68).addBox(-0.5F, -4.1F, -0.25F, 1F, 5F, 0.5F),
				PartPose.offset(1.4466F, 1.435F, 0.0075F));

		side.addOrReplaceChild("d_r1", CubeListBuilder.create()
						.texOffs(32, 73).addBox(-3.1F, -0.5F, -0.35F, 6F, 1F, 0.7F),
				PartPose.offset(-0.8754F, 1.6966F, 0.0075F));

		side.addOrReplaceChild("u_r1", CubeListBuilder.create()
						.texOffs(32, 73).addBox(-3.3F, -0.9F, -0.35F, 6F, 1F, 0.7F),
				PartPose.offset(-0.7908F, -1.2503F, 0.0075F));
	}

	private static void addSkirtPiece(PartDefinition parent, String name, float x, float y, float z) {
		addSkirtPiece(parent, name, x, y, z, 0, 71);
	}

	private static void addSkirtPiece(PartDefinition parent, String name, float x, float y, float z, int u, int v) {
		parent.addOrReplaceChild(name, CubeListBuilder.create()
						.texOffs(u, v).addBox(-0.5F, -4.5F, -0.5F, 1F, 8F, 1F),
				PartPose.offset(x, y, z));
	}

	public void setRotationAngle(ModelPart part, float x, float y, float z) {
		part.xRot = x;
		part.yRot = y;
		part.zRot = z;
	}
}