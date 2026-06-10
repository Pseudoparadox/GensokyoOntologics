package com.github.fictology.gensokyoontology.client.model.no;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.List;
/*
public class FlandreScarletModel extends GSKOBipedModel<FlandreScarletEntity> {

	private List<ModelPart> modelParts = Lists.newArrayList();

	private final ModelPart flandre;
	private final ModelPart bipedHead;
	private final ModelPart skirt;
	private final ModelPart skirtTable;
	private final ModelPart skirtPiece8;
	private final ModelPart skirtPiece7;
	private final ModelPart skirtPiece6;
	private final ModelPart skirt_piece_5;
	private final ModelPart skirt_piece_4;
	private final ModelPart skirt_piece_3;
	private final ModelPart skirt_piece_2;
	private final ModelPart skirt_piece_1;
	public final ModelPart wingLeft;
	private final ModelPart crystal;
	private final ModelPart l_2_r1;
	private final ModelPart crystal2;
	private final ModelPart l2_2_r1;
	private final ModelPart crystal3;
	private final ModelPart l3_2_r1;
	private final ModelPart crystal4;
	private final ModelPart l4_2_r1;
	private final ModelPart crystal5;
	private final ModelPart l5_2_r1;
	private final ModelPart crystal6;
	private final ModelPart l6_2_r1;
	private final ModelPart gan;
	private final ModelPart g2_r1;
	private final ModelPart g1_r1;
	private final ModelPart wingRight;
	private final ModelPart crystal7;
	private final ModelPart l7_2_r1;
	private final ModelPart crystal8;
	private final ModelPart l8_2_r1;
	private final ModelPart crystal9;
	private final ModelPart l9_2_r1;
	private final ModelPart crystal10;
	private final ModelPart l10_2_r1;
	private final ModelPart crystal11;
	private final ModelPart l11_2_r1;
	private final ModelPart crystal12;
	private final ModelPart l12_2_r1;
	private final ModelPart wingMainStick2;
	private final ModelPart gan2_2_r1;
	private final ModelPart gam2_1_r1;


	public static LayerDefinition createMesh(){

		var mesh = new MeshDefinition();
		var part = mesh.getRoot();

		var bone = part.addOrReplaceChild("flandre", CubeListBuilder.create(), PartPose.offset(0F, 24F, 0F));
		var head = part.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-4.0F, -31.25F, -3.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0))
				.texOffs(32, 0).addBox(4.1F, -31.35F, -3.3F, 8.2F, 8.2F, 8.2F, new CubeDeformation(0)), PartPose.ZERO);
		var leftArm = part.addOrReplaceChild("leftArm", CubeListBuilder.create()
				.texOffs(51, 31).addBox(-1.6F, -6.0F, -2.1F, 3.2F, 12.0F, 4.2F, new CubeDeformation(0))
				.texOffs(40, 17).addBox(-1.5F, -5.8F, -2.0F, 3.0F, 11.6F, 4.0F, new CubeDeformation(0)), PartPose.offset(5.4F, -17.4F, 1.0F));

		return LayerDefinition.create(mesh, 80, 80);
	}


	public FlandreScarletModel(ModelPart root) {
		super(root);
		// textureWidth = 80;
		// textureHeight = 80;

		var mesh = new MeshDefinition();
		var part = mesh.getRoot();

		this.flandre = root.getChild("flandre");
		this.bipedHead = this.flandre.getChild("head");
		this.leftArm = this.flandre.getChild("leftArm");
		this.rightArm = this.flandre.getChild("rightArm");

		this.rightArm = new ModelPart(this);
		this.rightArm.setRotationPoint(-5.6F, -17.4F, 1.0F);
		this.flandre.addChild(rightArm);
		this.rightArm.setTextureOffset(51, 31).addBox(-1.6F, -6.0F, -2.1F, 3.2F, 12.0F, 4.2F, 0.0F, false);
		this.rightArm.setTextureOffset(33, 48).addBox(-1.5F, -5.8F, -2.0F, 3.0F, 11.6F, 4.0F, 0.0F, false);
		this.body = new ModelPart(this);
		this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.flandre.addChild(body);
		this.body.setTextureOffset(20, 36).addBox(-4.0F, -23.0F, -1.2F, 8.0F, 5.0F, 0.0F, 0.0F, false);
		this.body.setTextureOffset(16, 17).addBox(-4.0F, -23.2F, -1.0F, 8.0F, 11.6F, 4.0F, 0.0F, false);
		this.skirt = new ModelPart(this);
		this.skirt.setRotationPoint(-0.0024F, -11.1567F, 1.0186F);
		this.body.addChild(skirt);
		this.skirtTable = new ModelPart(this);
		this.skirtTable.setRotationPoint(0.0F, -2.5F, 3.0F);
		this.skirt.addChild(skirtTable);
		this.setRotationAngle(skirtTable, 0.2618F, 0.0F, 0.0F);
		this.skirtTable.setTextureOffset(56, 17).addBox(-3.5F, -1.8F, -0.4F, 7.0F, 5.0F, 0.0F, 0.0F, false);
		this.skirtPiece8 = new ModelPart(this);
		this.skirtPiece8.setRotationPoint(3.9965F, 0.4301F, 2.0763F);
		this.skirt.addChild(skirtPiece8);
		this.setRotationAngle(skirtPiece8, 0.1309F, 0.0F, -0.1309F);
		this.skirtPiece8.setTextureOffset(50, 66).addBox(-0.4F, -4.5F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);
		this.skirtPiece7 = new ModelPart(this);
		this.skirtPiece7.setRotationPoint(-4.0291F, 0.429F, 2.0848F);
		this.skirt.addChild(skirtPiece7);
		this.setRotationAngle(skirtPiece7, 0.1309F, 0.0F, 0.1309F);
		this.skirtPiece7.setTextureOffset(50, 66).addBox(-0.5F, -4.5F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);
		this.skirtPiece6 = new ModelPart(this);
		this.skirtPiece6.setRotationPoint(3.9073F, 0.2412F, -1.9132F);
		this.skirt.addChild(skirtPiece6);
		this.setRotationAngle(skirtPiece6, -0.1309F, 0.0F, -0.1309F);
		this.skirtPiece6.setTextureOffset(50, 66).addBox(-0.4F, -4.2F, -0.6F, 1.0F, 8.0F, 1.0F, 0.0F, false);
		this.skirt_piece_5 = new ModelPart(this);
		this.skirt_piece_5.setRotationPoint(-4.0121F, 0.2996F, -1.9237F);
		this.skirt.addChild(skirt_piece_5);
		this.setRotationAngle(skirt_piece_5, -0.1309F, 0.0F, 0.1309F);
		this.skirt_piece_5.setTextureOffset(50, 66).addBox(-0.5F, -4.2F, -0.6F, 1.0F, 8.0F, 1.0F, 0.0F, false);
		this.skirt_piece_4 = new ModelPart(this);
		this.skirt_piece_4.setRotationPoint(-4.4938F, 0.2324F, -0.0186F);
		this.skirt.addChild(skirt_piece_4);
		this.setRotationAngle(skirt_piece_4, 0.0F, 0.0F, 0.2618F);
		this.skirt_piece_4.setTextureOffset(46, 63).addBox(-0.7F, -4.3F, -2.0F, 2.0F, 8.0F, 4.0F, 0.0F, false);
		this.skirt_piece_3 = new ModelPart(this);
		this.skirt_piece_3.setRotationPoint(3.8882F, 1.2482F, -0.0186F);
		this.skirt.addChild(skirt_piece_3);
		this.setRotationAngle(skirt_piece_3, 0.0F, 0.0F, -0.2618F);
		this.skirt_piece_3.setTextureOffset(46, 63).addBox(-0.5F, -5.2F, -2.0F, 2.0F, 8.0F, 4.0F, 0.0F, false);
		this.skirt_piece_2 = new ModelPart(this);
		this.skirt_piece_2.setRotationPoint(0.0024F, 2.8126F, -2.6366F);
		this.skirt.addChild(skirt_piece_2);
		this.setRotationAngle(skirt_piece_2, 0.2618F, 0.0F, 0.0F);
		this.skirt_piece_2.setTextureOffset(25, 64).addBox(-4.0F, -5.5F, 4.3F, 8.0F, 8.0F, 2.0F, 0.0F, false);

		skirt_piece_1 = new ModelPart(this);
		skirt_piece_1.setRotationPoint(0.0024F, 0.0941F, -2.7364F);
		skirt.addChild(skirt_piece_1);
		setRotationAngle(skirt_piece_1, -0.2618F, 0.0F, 0.0F);
		skirt_piece_1.setTextureOffset(25, 64).addBox(-4.0F, -4.2F, -0.4F, 8.0F, 8.0F, 2.0F, 0.0F, false);

		rightLeg = new ModelPart(this);
		rightLeg.setRotationPoint(-2.05F, -11.85F, 1.0F);
		flandre.addChild(rightLeg);
		rightLeg.setTextureOffset(0, 36).addBox(-2.05F, 3.95F, -2.1F, 4.0F, 8.0F, 4.2F, 0.0F, false);
		rightLeg.setTextureOffset(0, 17).addBox(-1.95F, 0.25F, -2.0F, 4.0F, 11.6F, 4.0F, 0.0F, false);

		leftLeg = new ModelPart(this);
		leftLeg.setRotationPoint(2.05F, -11.85F, 1.0F);
		flandre.addChild(leftLeg);
		leftLeg.setTextureOffset(0, 51).addBox(-1.95F, 3.95F, -2.1F, 4.0F, 8.0F, 4.2F, 0.0F, false);
		leftLeg.setTextureOffset(17, 48).addBox(-2.05F, 0.25F, -2.0F, 4.0F, 11.6F, 4.0F, 0.0F, false);

		wingLeft = new ModelPart(this);
		wingLeft.setRotationPoint(2.25F, 1.0F, 0.0F);
		flandre.addChild(wingLeft);

		crystal = new ModelPart(this);
		crystal.setRotationPoint(-18.3489F, -18.0429F, 3.5071F);
		wingLeft.addChild(crystal);
		crystal.setTextureOffset(0, 68).addBox(-0.2511F, -2.5571F, -0.2571F, 0.6F, 0.6F, 0.6F, 0.0F, false);

		l_2_r1 = new ModelPart(this);
		l_2_r1.setRotationPoint(0.0626F, -0.4571F, 0.0636F);
		crystal.addChild(l_2_r1);
		setRotationAngle(l_2_r1, 0.0F, -0.7854F, 0.0F);
		l_2_r1.setTextureOffset(0, 64).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

		crystal2 = new ModelPart(this);
		crystal2.setRotationPoint(-18.6489F, -18.0429F, 3.5071F);
		wingLeft.addChild(crystal2);
		crystal2.setTextureOffset(4, 68).addBox(2.7489F, -3.5571F, -0.2571F, 0.6F, 0.6F, 0.6F, 0.0F, false);

		l2_2_r1 = new ModelPart(this);
		l2_2_r1.setRotationPoint(3.0626F, -1.4571F, 0.0636F);
		crystal2.addChild(l2_2_r1);
		setRotationAngle(l2_2_r1, 0.0F, -0.7854F, 0.0F);
		l2_2_r1.setTextureOffset(4, 64).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

		crystal3 = new ModelPart(this);
		crystal3.setRotationPoint(-18.9489F, -17.6429F, 3.5071F);
		wingLeft.addChild(crystal3);
		crystal3.setTextureOffset(8, 68).addBox(5.7489F, -4.8571F, -0.2571F, 0.6F, 0.6F, 0.6F, 0.0F, false);

		l3_2_r1 = new ModelPart(this);
		l3_2_r1.setRotationPoint(6.0626F, -2.7571F, 0.0636F);
		crystal3.addChild(l3_2_r1);
		setRotationAngle(l3_2_r1, 0.0F, -0.7854F, 0.0F);
		l3_2_r1.setTextureOffset(8, 64).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

		crystal4 = new ModelPart(this);
		crystal4.setRotationPoint(-18.9489F, -18.9429F, 3.5071F);
		wingLeft.addChild(crystal4);
		crystal4.setTextureOffset(12, 68).addBox(8.5489F, -4.4571F, -0.2571F, 0.6F, 0.6F, 0.6F, 0.0F, false);

		l4_2_r1 = new ModelPart(this);
		l4_2_r1.setRotationPoint(8.8626F, -2.3571F, 0.0636F);
		crystal4.addChild(l4_2_r1);
		setRotationAngle(l4_2_r1, 0.0F, -0.7854F, 0.0F);
		l4_2_r1.setTextureOffset(12, 64).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

		crystal5 = new ModelPart(this);
		crystal5.setRotationPoint(-18.7489F, -18.9429F, 3.5071F);
		wingLeft.addChild(crystal5);
		crystal5.setTextureOffset(16, 68).addBox(11.0489F, -3.5571F, -0.2571F, 0.6F, 0.6F, 0.6F, 0.0F, false);

		l5_2_r1 = new ModelPart(this);
		l5_2_r1.setRotationPoint(11.3626F, -1.4571F, 0.0636F);
		crystal5.addChild(l5_2_r1);
		setRotationAngle(l5_2_r1, 0.0F, -0.7854F, 0.0F);
		l5_2_r1.setTextureOffset(16, 64).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

		crystal6 = new ModelPart(this);
		crystal6.setRotationPoint(-17.3489F, -18.4429F, 3.5071F);
		wingLeft.addChild(crystal6);
		crystal6.setTextureOffset(20, 68).addBox(12.3489F, -3.1571F, -0.2571F, 0.6F, 0.6F, 0.6F, 0.0F, false);

		l6_2_r1 = new ModelPart(this);
		l6_2_r1.setRotationPoint(12.6626F, -1.0571F, 0.0636F);
		crystal6.addChild(l6_2_r1);
		setRotationAngle(l6_2_r1, 0.0F, -0.7854F, 0.0F);
		l6_2_r1.setTextureOffset(20, 64).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

		gan = new ModelPart(this);
		gan.setRotationPoint(0.0F, 0.0F, 0.0F);
		wingLeft.addChild(gan);

		g2_r1 = new ModelPart(this);
		g2_r1.setRotationPoint(-14.5F, -22.5F, 3.5F);
		gan.addChild(g2_r1);
		setRotationAngle(g2_r1, 0.0F, 0.0F, -0.3491F);
		g2_r1.setTextureOffset(0, 71).addBox(-4.5F, -0.5F, -0.5F, 9.0F, 1.0F, 1.0F, 0.0F, false);

		g1_r1 = new ModelPart(this);
		g1_r1.setRotationPoint(-6.5F, -22.5F, 3.5F);
		gan.addChild(g1_r1);
		setRotationAngle(g1_r1, 0.0F, 0.0F, 0.3491F);
		g1_r1.setTextureOffset(0, 71).addBox(-4.5F, -0.5F, -0.5F, 9.0F, 1.0F, 1.0F, 0.0F, false);

		wingRight = new ModelPart(this);
		wingRight.setRotationPoint(18.75F, 1.0F, 7.0F);
		flandre.addChild(wingRight);
		setRotationAngle(wingRight, 0.0F, 3.1416F, 0.0F);


		crystal7 = new ModelPart(this);
		crystal7.setRotationPoint(2.7068F, -19.4F, 3.5604F);
		wingRight.addChild(crystal7);
		crystal7.setTextureOffset(0, 68).addBox(-0.3068F, -1.2F, -0.3104F, 0.6F, 0.6F, 0.6F, 0.0F, false);

		l7_2_r1 = new ModelPart(this);
		l7_2_r1.setRotationPoint(0.0068F, 0.9F, 0.0104F);
		crystal7.addChild(l7_2_r1);
		setRotationAngle(l7_2_r1, 0.0F, -0.7854F, 0.0F);
		l7_2_r1.setTextureOffset(0, 64).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

		crystal8 = new ModelPart(this);
		crystal8.setRotationPoint(5.4068F, -20.4F, 3.5604F);
		wingRight.addChild(crystal8);
		crystal8.setTextureOffset(4, 68).addBox(-0.3068F, -1.2F, -0.3104F, 0.6F, 0.6F, 0.6F, 0.0F, false);

		l8_2_r1 = new ModelPart(this);
		l8_2_r1.setRotationPoint(0.0068F, 0.9F, 0.0104F);
		crystal8.addChild(l8_2_r1);
		setRotationAngle(l8_2_r1, 0.0F, -0.7854F, 0.0F);
		l8_2_r1.setTextureOffset(4, 64).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

		crystal9 = new ModelPart(this);
		crystal9.setRotationPoint(8.1068F, -21.3F, 3.5604F);
		wingRight.addChild(crystal9);
		crystal9.setTextureOffset(8, 68).addBox(-0.3068F, -1.2F, -0.3104F, 0.6F, 0.6F, 0.6F, 0.0F, false);

		l9_2_r1 = new ModelPart(this);
		l9_2_r1.setRotationPoint(0.0068F, 0.9F, 0.0104F);
		crystal9.addChild(l9_2_r1);
		setRotationAngle(l9_2_r1, 0.0F, -0.7854F, 0.0F);
		l9_2_r1.setTextureOffset(8, 64).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

		crystal10 = new ModelPart(this);
		crystal10.setRotationPoint(10.9068F, -22.2F, 3.5604F);
		wingRight.addChild(crystal10);
		crystal10.setTextureOffset(12, 68).addBox(-0.3068F, -1.2F, -0.3104F, 0.6F, 0.6F, 0.6F, 0.0F, false);

		l10_2_r1 = new ModelPart(this);
		l10_2_r1.setRotationPoint(0.0068F, 0.9F, 0.0104F);
		crystal10.addChild(l10_2_r1);
		setRotationAngle(l10_2_r1, 0.0F, -0.7854F, 0.0F);
		l10_2_r1.setTextureOffset(12, 64).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

		crystal11 = new ModelPart(this);
		crystal11.setRotationPoint(13.6068F, -21.3F, 3.5604F);
		wingRight.addChild(crystal11);
		crystal11.setTextureOffset(16, 68).addBox(-0.3068F, -1.2F, -0.3104F, 0.6F, 0.6F, 0.6F, 0.0F, false);

		l11_2_r1 = new ModelPart(this);
		l11_2_r1.setRotationPoint(0.0068F, 0.9F, 0.0104F);
		crystal11.addChild(l11_2_r1);
		setRotationAngle(l11_2_r1, 0.0F, -0.7854F, 0.0F);
		l11_2_r1.setTextureOffset(16, 64).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

		crystal12 = new ModelPart(this);
		crystal12.setRotationPoint(16.3068F, -20.4F, 3.5604F);
		wingRight.addChild(crystal12);
		crystal12.setTextureOffset(20, 68).addBox(-0.3068F, -1.2F, -0.3104F, 0.6F, 0.6F, 0.6F, 0.0F, false);

		l12_2_r1 = new ModelPart(this);
		l12_2_r1.setRotationPoint(0.0068F, 0.9F, 0.0104F);
		crystal12.addChild(l12_2_r1);
		setRotationAngle(l12_2_r1, 0.0F, -0.7854F, 0.0F);
		l12_2_r1.setTextureOffset(20, 64).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

		wingMainStick2 = new ModelPart(this);
		wingMainStick2.setRotationPoint(10.5F, -22.5F, 3.5F);
		wingRight.addChild(wingMainStick2);


		gan2_2_r1 = new ModelPart(this);
		gan2_2_r1.setRotationPoint(-4.0F, 0.0F, 0.0F);
		wingMainStick2.addChild(gan2_2_r1);
		setRotationAngle(gan2_2_r1, 0.0F, 0.0F, -0.3491F);
		gan2_2_r1.setTextureOffset(0, 71).addBox(-4.5F, -0.5F, -0.5F, 9.0F, 1.0F, 1.0F, 0.0F, false);

		gam2_1_r1 = new ModelPart(this);
		gam2_1_r1.setRotationPoint(4.0F, 0.0F, 0.0F);
		wingMainStick2.addChild(gam2_1_r1);
		setRotationAngle(gam2_1_r1, 0.0F, 0.0F, 0.3491F);
		gam2_1_r1.setTextureOffset(0, 71).addBox(-4.5F, -0.5F, -0.5F, 9.0F, 1.0F, 1.0F, 0.0F, false);
	}

	public List<ModelPart> getBodyParts() {
		return ImmutableList.of(this.leftLeg, this.rightLeg, this.leftArm, this.rightArm, this.bipedBody);
	}

	@Override
	public void setLivingAnimations(@NotNull FlandreScarletEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		super.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);
	}

	@Override
	public void render(@NotNull MatrixStack matrixStackIn, @NotNull IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		flandre.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
	}

	@Override
	public void setRotationAngles(@NotNull FlandreScarletEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.wingLeft.rotateAngleZ = MathHelper.lerp(this.swimAnimation, this.wingLeft.rotateAngleZ, 0.3F * MathHelper.cos(limbSwing * 0.33333334F));
		this.wingRight.rotateAngleZ = MathHelper.lerp(this.swimAnimation, this.wingRight.rotateAngleZ, 0.3F * MathHelper.cos(limbSwing * 0.33333334F));
	}

	public void setRotationAngle(ModelPart modelPart, float x, float y, float z) {
		modelPart.rotateAngleX = x;
		modelPart.rotateAngleY = y;
		modelPart.rotateAngleZ = z;
	}



}

 */
