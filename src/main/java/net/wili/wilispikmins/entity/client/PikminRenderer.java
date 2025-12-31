package net.wili.wilispikmins.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.wili.wilispikmins.entity.custom.PikminEntity;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PikminRenderer extends GeoEntityRenderer<PikminEntity> {
    public PikminRenderer(EntityRendererProvider.Context context) {
        super(context, new PikminModel());
        this.shadowRadius = 0.25f;
    }

   @Override
    public  void renderRecursively(PoseStack poseStack, PikminEntity entity, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        PikminType type = entity.getPikminType();
        String boneName = bone.getName();

        switch (boneName) {
            case "nose":
                bone.setHidden(type != PikminType.RED);
                break;
            case "ears":
                bone.setHidden(type != PikminType.YELLOW);
                break;
        }

        super.renderRecursively(poseStack, entity, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
   }
}
