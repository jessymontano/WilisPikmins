package net.wili.wilispikmins;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.wili.wilispikmins.block.ModBlocks;
import net.wili.wilispikmins.entity.ModEntities;
import net.wili.wilispikmins.entity.client.PikminRenderer;
import net.wili.wilispikmins.item.ModItems;
import net.wili.wilispikmins.sound.ModSounds;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(WilisPikmins.MOD_ID)
public class WilisPikmins
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "wilispikmins";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public WilisPikmins(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        // register mod items
        ModItems.register(modEventBus);

        // register mod blocks
        ModBlocks.register(modEventBus);

        // register mod sounds
        ModSounds.register(modEventBus);

        GeckoLib.initialize();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // register mod entities
        ModEntities.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        // agrega los spawn eggs al menu de modo creativo
        if(event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.RED_PIKMIN_SPAWN_EGG);
            event.accept(ModItems.BLUE_PIKMIN_SPAWN_EGG);
            event.accept(ModItems.YELLOW_PIKMIN_SPAWN_EGG);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // registrar los renderers de las entidades
            EntityRenderers.register(ModEntities.PIKMIN.get(), PikminRenderer::new);
        }
    }
}
