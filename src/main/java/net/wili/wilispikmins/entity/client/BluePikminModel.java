package net.wili.wilispikmins.entity.client;

// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.wili.wilispikmins.entity.animations.ModAnimationDefinitions;
import net.wili.wilispikmins.entity.custom.BluePikminEntity;
import net.wili.wilispikmins.entity.custom.YellowPikminEntity;

public class BluePikminModel<T extends BluePikminEntity> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart leftleg;
    private final ModelPart rightleg;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart eyes;
    private final ModelPart colita;
    private final ModelPart hoja;
    private final ModelPart rightarm;
    private final ModelPart leftarm;

    public BluePikminModel(ModelPart root) {
        this.root = root.getChild("root");
        this.leftleg = this.root.getChild("leftleg");
        this.rightleg = this.root.getChild("rightleg");
        this.body = this.root.getChild("body");
        this.head = this.body.getChild("head");
        this.eyes = this.head.getChild("eyes");
        this.colita = this.head.getChild("colita");
        this.hoja = this.colita.getChild("hoja");
        this.rightarm = this.body.getChild("rightarm");
        this.leftarm = this.body.getChild("leftarm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(1.5F, 22.0F, -0.5F));

        PartDefinition leftleg = root.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(8, 21).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offset(0.0F, 0.0F, -0.5F));

        PartDefinition rightleg = root.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(8, 18).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offset(-2.0F, 0.0F, -0.5F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 11).addBox(-1.4F, -4.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.1F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(12, 11).addBox(-1.5F, -8.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.5F, -6.0F, -2.5F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, -4.0F, 0.0F));

        PartDefinition eyes = head.addOrReplaceChild("eyes", CubeListBuilder.create().texOffs(20, 4).addBox(-3.5F, -2.0F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 7).addBox(0.5F, -2.0F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -3.5F));

        PartDefinition colita = head.addOrReplaceChild("colita", CubeListBuilder.create().texOffs(20, 0).addBox(-1.0F, -1.9571F, 0.0429F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0429F, -1.0429F));

        PartDefinition cube_r1 = colita.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 18).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, -1.25F, 0.75F, -0.7854F, 0.0F, 0.0F));

        PartDefinition hoja = colita.addOrReplaceChild("hoja", CubeListBuilder.create(), PartPose.offset(0.0F, -3.9571F, 3.0429F));

        PartDefinition cube_r2 = hoja.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(12, 16).addBox(-2.0F, -6.0F, 0.0F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition rightarm = body.addOrReplaceChild("rightarm", CubeListBuilder.create().texOffs(22, 16).addBox(0.0F, 0.0F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.6F, -3.0F, 0.0F));

        PartDefinition leftarm = body.addOrReplaceChild("leftarm", CubeListBuilder.create().texOffs(22, 18).addBox(-3.0F, 0.0F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.4F, -3.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        this.animateWalk(ModAnimationDefinitions.PIKMIN_WALK, limbSwing, limbSwingAmount, 2f, 1.0f);
        this.animate(((BluePikminEntity) entity).idleAnimationState, ModAnimationDefinitions.PIKMIN_IDLE, ageInTicks, 1f);
    }
    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);

        this.head.yRot = headYaw * ((float)Math.PI /180f);
        this.head.xRot = headPitch * ((float)Math.PI / 180f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
