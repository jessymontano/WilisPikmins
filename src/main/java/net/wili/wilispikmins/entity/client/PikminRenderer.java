package net.wili.wilispikmins.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.wili.wilispikmins.entity.custom.PikminEntity;
import net.wili.wilispikmins.entity.custom.enums.GrowthStage;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PikminRenderer extends GeoEntityRenderer<PikminEntity> {
    public PikminRenderer(EntityRendererProvider.Context context) {
        super(context, new PikminModel());
        this.shadowRadius = 0.25f;
        this.shadowStrength = 0.5f;
    }

    @Override
    public void render(PikminEntity entity, float entityYaw, float partialTick, PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        PikminType type = entity.getPikminType();

        float scale = getScaleForType(type);

        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);

        if (type == PikminType.WINGED && entity.isFlying()) {
            float hoverBob = Mth.sin((entity.tickCount + partialTick) * 0.2f) * 0.05f;
            poseStack.translate(0, hoverBob, 0);

            if (entity.getDeltaMovement().horizontalDistanceSqr() > 0.01) {
                float tilt = Mth.lerp(partialTick,
                        entity.yBodyRotO, entity.yBodyRot) * Mth.DEG_TO_RAD;
                poseStack.mulPose(Axis.YP.rotation(tilt * 0.1f));
            }
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }

    @Override
    public void renderRecursively(PoseStack poseStack, PikminEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        PikminType type = animatable.getPikminType();
        GrowthStage stage = animatable.getGrowthStage();
        String boneName = bone.getName();

        switch (boneName) {
            case "nose":
                bone.setHidden(type != PikminType.RED);
                break;
            case "ears":
                bone.setHidden(type != PikminType.YELLOW);
                break;
            case "small_eyes":
                bone.setHidden(type != PikminType.WHITE && type !=PikminType.WINGED);
                break;
            case "wings":
            case "right_wing":
            case "left_wing":
                bone.setHidden(type != PikminType.WINGED);
                break;
        }

        switch (boneName) {
            case "leaf":
                bone.setHidden(stage != GrowthStage.LEAF);
                break;
            case "bud":
                bone.setHidden(stage != GrowthStage.BUD);
                break;
            case "flower":
                bone.setHidden(stage != GrowthStage.FLOWER);
                break;
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
   }

   private float getScaleForType(PikminType type) {
        return switch (type) {
            case WHITE, WINGED -> 0.7f;
            default -> 1.0f;
        };
   }
}
