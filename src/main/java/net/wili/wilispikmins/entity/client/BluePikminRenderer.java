package net.wili.wilispikmins.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.entity.custom.BluePikminEntity;

public class BluePikminRenderer extends MobRenderer<BluePikminEntity, BluePikminModel<BluePikminEntity>> {
    public BluePikminRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new BluePikminModel<>(pContext.bakeLayer(ModModelLayers.BLUE_PIKMIN_LAYER)), 0.25f);
    }

    @Override
    public ResourceLocation getTextureLocation(BluePikminEntity bluePikminEntity) {
        return new ResourceLocation(WilisPikmins.MOD_ID, "textures/entity/blue_pikmin.png");
    }

    @Override
    public void render(BluePikminEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pPoseStack.scale(0.5f, 0.5f, 0.5f);
        }
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
