package net.wili.wilispikmins.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.entity.client.BluePikminModel;
import net.wili.wilispikmins.entity.client.ModModelLayers;
import net.wili.wilispikmins.entity.client.RedPikminModel;
import net.wili.wilispikmins.entity.client.YellowPikminModel;

@Mod.EventBusSubscriber(modid = WilisPikmins.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.RED_PIKMIN_LAYER, RedPikminModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.YELLOW_PIKMIN_LAYER, YellowPikminModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.BLUE_PIKMIN_LAYER, BluePikminModel::createBodyLayer);
    }
}
