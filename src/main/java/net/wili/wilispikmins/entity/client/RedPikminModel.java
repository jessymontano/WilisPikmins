package net.wili.wilispikmins.entity.client;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.wili.wilispikmins.entity.animations.ModAnimationDefinitions;
import net.wili.wilispikmins.entity.custom.PikminEntity;

public class RedPikminModel<T extends PikminEntity> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart leftarm;
    private final ModelPart rightarm;
    private final ModelPart head;
    private final ModelPart colita;
    private final ModelPart hoja;
    private final ModelPart eyes;
    private final ModelPart rightleg;
    private final ModelPart leftleg;

    public RedPikminModel(ModelPart root) {
        this.root = root.getChild("root");
        this.body = this.root.getChild("body");
        this.leftarm = this.body.getChild("leftarm");
        this.rightarm = this.body.getChild("rightarm");
        this.head = this.body.getChild("head");
        this.colita = this.head.getChild("colita");
        this.hoja = this.colita.getChild("hoja");
        this.eyes = this.head.getChild("eyes");
        this.rightleg = this.root.getChild("rightleg");
        this.leftleg = this.root.getChild("leftleg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.5F, 24.0F, -0.5F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 11).addBox(-1.5F, -6.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leftarm = body.addOrReplaceChild("leftarm", CubeListBuilder.create().texOffs(20, 2).addBox(-3.0F, -0.75F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -5.0F, 0.0F));

        PartDefinition rightarm = body.addOrReplaceChild("rightarm", CubeListBuilder.create().texOffs(20, 0).addBox(-0.25F, -1.0F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -5.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -6.0F, -2.5F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(12, 11).addBox(-1.5F, -8.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(19, 6).addBox(-0.5F, -2.5F, -5.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));

        PartDefinition colita = head.addOrReplaceChild("colita", CubeListBuilder.create().texOffs(0, 18).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, -1.0F));

        PartDefinition cube_r1 = colita.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(12, 16).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, -1.2929F, 0.7071F, -0.7854F, 0.0F, 0.0F));

        PartDefinition hoja = colita.addOrReplaceChild("hoja", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, 1.0F));

        PartDefinition cube_r2 = hoja.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, -6.0F, 0.0F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition eyes = head.addOrReplaceChild("eyes", CubeListBuilder.create().texOffs(10, 22).addBox(-2.0F, -2.0F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 25).addBox(2.0F, -2.0F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -3.0F, -3.5F));

        PartDefinition rightleg = root.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(8, 18).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offset(-1.0F, -2.0F, -0.5F));

        PartDefinition leftleg = root.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(20, 16).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offset(1.0F, -2.0F, -0.5F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(PikminEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        this.animateWalk(ModAnimationDefinitions.PIKMIN_WALK, limbSwing, limbSwingAmount, 2.5f, 0.65f); // Ajustado
        this.animate(entity.idleAnimationState, ModAnimationDefinitions.PIKMIN_IDLE, ageInTicks, 1f);

        if (entity.popAnimationState.isStarted()) {
            this.animate(entity.popAnimationState, ModAnimationDefinitions.PIKMIN_POP, ageInTicks, 1.0f);
        }
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