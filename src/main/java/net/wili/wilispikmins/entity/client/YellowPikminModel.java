package net.wili.wilispikmins.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.wili.wilispikmins.entity.animations.ModAnimationDefinitions;
import net.wili.wilispikmins.entity.custom.YellowPikminEntity;

public class YellowPikminModel<T extends Entity> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart leftear;
    private final ModelPart rightear;
    private final ModelPart colita;
    private final ModelPart hoja;
    private final ModelPart eyes;
    private final ModelPart rightarm;
    private final ModelPart leftarm;
    private final ModelPart rightleg;
    private final ModelPart leftleg;

    public YellowPikminModel(ModelPart root) {
        this.root = root.getChild("root");
        this.body = this.root.getChild("body");
        this.head = this.body.getChild("head");
        this.leftear = this.head.getChild("leftear");
        this.rightear = this.head.getChild("rightear");
        this.colita = this.head.getChild("colita");
        this.hoja = this.colita.getChild("hoja");
        this.eyes = this.head.getChild("eyes");
        this.rightarm = this.body.getChild("rightarm");
        this.leftarm = this.body.getChild("leftarm");
        this.rightleg = this.root.getChild("rightleg");
        this.leftleg = this.root.getChild("leftleg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.5F, 12.0F, -0.5F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 11).addBox(-1.5F, 6.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(12, 11).addBox(-1.5F, 2.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.5F, 4.0F, -2.5F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition leftear = head.addOrReplaceChild("leftear", CubeListBuilder.create().texOffs(20, 0).addBox(-1.0F, -2.0F, -1.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(-1.0F, 0.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 7.0F, 0.5F));

        PartDefinition rightear = head.addOrReplaceChild("rightear", CubeListBuilder.create().texOffs(24, 10).addBox(0.0F, -3.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 3).addBox(-1.0F, -5.0F, -1.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.5F, 10.0F, 0.5F));

        PartDefinition colita = head.addOrReplaceChild("colita", CubeListBuilder.create().texOffs(20, 6).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -1.0F));

        PartDefinition cube_r1 = colita.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 18).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, -1.2929F, 0.7071F, -0.7854F, 0.0F, 0.0F));

        PartDefinition hoja = colita.addOrReplaceChild("hoja", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, 3.0F));

        PartDefinition cube_r2 = hoja.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(12, 16).addBox(-2.0F, -6.0F, 0.0F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition eyes = head.addOrReplaceChild("eyes", CubeListBuilder.create().texOffs(22, 16).addBox(-2.0F, -2.0F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(22, 19).addBox(2.0F, -2.0F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 7.0F, -3.5F));

        PartDefinition rightarm = body.addOrReplaceChild("rightarm", CubeListBuilder.create().texOffs(22, 22).addBox(0.0F, -0.25F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 7.25F, 0.0F));

        PartDefinition leftarm = body.addOrReplaceChild("leftarm", CubeListBuilder.create().texOffs(8, 23).addBox(-3.0F, 0.0F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 7.0F, 0.0F));

        PartDefinition rightleg = root.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(8, 18).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offset(-1.0F, 10.0F, -0.5F));

        PartDefinition leftleg = root.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(16, 23).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offset(1.0F, 10.0F, -0.5F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        this.animateWalk(ModAnimationDefinitions.PIKMIN_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(((YellowPikminEntity) entity).idleAnimationState, ModAnimationDefinitions.PIKMIN_IDLE, ageInTicks, 1f);
    }
    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);

        this.head.yRot = headYaw * ((float)Math.PI /180f);
        this.head.xRot = headPitch * ((float)Math.PI / 180f);
    }
}
