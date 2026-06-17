package com.github.fictology.gensokyoontology.client.model;// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.github.fictology.gensokyoontology.client.renderer.state.SimpleState;
import com.github.fictology.gensokyoontology.common.entiy.monster.RumiaEntity;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class RumiaModel extends EntityModel<SimpleState<RumiaEntity>> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(GSKOUtil.key("rumiamodel"), "main");
	private final ModelPart head;
	private final ModelPart blink;
	private final ModelPart armRight;
	private final ModelPart armLeft;
	private final ModelPart body;
	private final ModelPart sittingRotationSkirt;
	private final ModelPart legLeft;
	private final ModelPart legRight;
	private final ModelPart hairtie;

	public RumiaModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
		this.blink = this.head.getChild("blink");
		this.armRight = root.getChild("armRight");
		this.armLeft = root.getChild("armLeft");
		this.body = root.getChild("body");
		this.sittingRotationSkirt = this.body.getChild("sittingRotationSkirt");
		this.legLeft = root.getChild("legLeft");
		this.legRight = root.getChild("legRight");
		this.hairtie = root.getChild("hairtie");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 37).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition blink = head.addOrReplaceChild("blink", CubeListBuilder.create().texOffs(52, 24).addBox(-4.0F, -8.0F, -4.001F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition armRight = partdefinition.addOrReplaceChild("armRight", CubeListBuilder.create().texOffs(60, 58).addBox(-2.0F, 1.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 58).addBox(-2.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 8.5F, 0.0F, 0.0F, 0.0F, 1.6144F));

		PartDefinition armLeft = partdefinition.addOrReplaceChild("armLeft", CubeListBuilder.create().texOffs(36, 58).addBox(-0.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(48, 64).addBox(0.0F, 1.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 8.5F, 0.0F, 0.0F, 0.0F, -1.6144F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(52, 0).addBox(-3.5F, -7.5F, -3.0F, 7.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.5F, 0.0F));

		PartDefinition sittingRotationSkirt = body.addOrReplaceChild("sittingRotationSkirt", CubeListBuilder.create().texOffs(32, 48).addBox(-4.0F, 0.5F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(32, 37).addBox(-4.5F, 2.5F, -4.5F, 9.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition legLeft = partdefinition.addOrReplaceChild("legLeft", CubeListBuilder.create().texOffs(0, 53).addBox(-1.501F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 15.0F, 0.0F));

		PartDefinition legRight = partdefinition.addOrReplaceChild("legRight", CubeListBuilder.create().texOffs(12, 53).addBox(-1.499F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 15.0F, 0.0F));

		PartDefinition hairtie = partdefinition.addOrReplaceChild("hairtie", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition tie2_r1 = hairtie.addOrReplaceChild("tie2_r1", CubeListBuilder.create().texOffs(2, 16).addBox(-0.0152F, -1.0F, -0.1736F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -24.0F, 0.5F, 0.0879F, -0.151F, -0.5303F));

		PartDefinition tie1_r1 = hairtie.addOrReplaceChild("tie1_r1", CubeListBuilder.create().texOffs(2, 16).addBox(-0.2929F, -1.0F, 0.2247F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -24.0F, 0.0F, 0.0692F, 0.2527F, 0.2706F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(SimpleState<RumiaEntity> state) {
		super.setupAnim(state);
	}

}