package net.wili.wilispikmins.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.entity.ModEntities;
import net.wili.wilispikmins.entity.client.ModModelLayers;
import net.wili.wilispikmins.entity.client.PikminModel;
import net.wili.wilispikmins.entity.client.PikminRenderer;
import net.wili.wilispikmins.screen.ModMenuTypes;
import net.wili.wilispikmins.screen.OnionScreen;

@EventBusSubscriber(modid = WilisPikmins.MOD_ID, value = Dist.CLIENT)
public class WilisPikminsClient {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {

        });
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.ONION_MENU.get(), OnionScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.PIKMIN.get(), PikminRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        //event.registerLayerDefinition(ModModelLayers.PIKMIN_LAYER, PikminModel::createBodyLayer);
    }
}
