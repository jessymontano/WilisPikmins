package net.wili.wilispikmins.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.wili.wilispikmins.entity.custom.BulborbEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BulborbRenderer extends GeoEntityRenderer<BulborbEntity> {
    public BulborbRenderer(EntityRendererProvider.Context context) {
        super(context, new BulborbModel());
        this.shadowRadius = 0.5f;
        this.shadowStrength = 0.5f;
    }
}
