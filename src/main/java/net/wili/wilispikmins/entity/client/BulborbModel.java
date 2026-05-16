package net.wili.wilispikmins.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.entity.custom.BulborbEntity;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.model.GeoModel;

public class BulborbModel<T extends GeoEntity> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T entity) {
        return ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID, "geo/bulborb.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T entity) {
        return ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID,
                "textures/entity/bulborb.png");
    }

    @Override
    public ResourceLocation getAnimationResource(T entity) {
        return ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID,
                "animations/bulborb.animation.json");
    }
}
