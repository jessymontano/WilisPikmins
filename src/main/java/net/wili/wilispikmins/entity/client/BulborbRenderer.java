package net.wili.wilispikmins.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.wili.wilispikmins.entity.custom.BulborbEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BulborbRenderer extends GeoEntityRenderer<BulborbEntity> {
    public BulborbRenderer(EntityRendererProvider.Context context) {
        super(context, new BulborbModel<>());
        this.shadowRadius = 0.9f;
        this.shadowStrength = 0.5f;
    }

    @Override
    public void preRender(PoseStack poseStack, BulborbEntity animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        poseStack.scale(2.5f, 2.5f, 2.5f);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }
}
