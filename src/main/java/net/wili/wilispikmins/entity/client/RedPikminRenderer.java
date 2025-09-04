package net.wili.wilispikmins.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.entity.custom.PikminEntity;

public class RedPikminRenderer extends MobRenderer<PikminEntity, RedPikminModel<PikminEntity>> {
    public RedPikminRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new RedPikminModel<>(pContext.bakeLayer(ModModelLayers.RED_PIKMIN_LAYER)), 0.25f);
    }

    @Override
    public ResourceLocation getTextureLocation(PikminEntity pikminEntity) {
        return new ResourceLocation(WilisPikmins.MOD_ID, "textures/entity/red_pikmin.png");
    }

    @Override
    public void render(PikminEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()){
            pPoseStack.scale(0.45f, 0.45f, 0.45f);
        } else {
            pPoseStack.scale(1f, 1f, 1f);
        }
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
