package net.wili.wilispikmins.entity.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.wili.wilispikmins.WilisPikmins;

public class ModModelLayers {
    public static final ModelLayerLocation RED_PIKMIN_LAYER = new ModelLayerLocation(
            new ResourceLocation(WilisPikmins.MOD_ID, "red_pikmin_layer"), "main");

    public static final ModelLayerLocation YELLOW_PIKMIN_LAYER = new ModelLayerLocation(
            new ResourceLocation(WilisPikmins.MOD_ID, "yellow_pikmin_layer"), "main");

    public static final ModelLayerLocation BLUE_PIKMIN_LAYER = new ModelLayerLocation(
            new ResourceLocation(WilisPikmins.MOD_ID, "blue_pikmin_layer"), "main");
}
