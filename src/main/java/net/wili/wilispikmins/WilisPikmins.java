package net.wili.wilispikmins;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.wili.wilispikmins.advancement.ModTriggers;
import net.wili.wilispikmins.block.ModBlocks;
import net.wili.wilispikmins.block.entity.ModBlockEntities;
import net.wili.wilispikmins.data.OnionComponents;
import net.wili.wilispikmins.entity.ModEntities;
import net.wili.wilispikmins.item.ModItems;
import net.wili.wilispikmins.network.ModPackets;
import net.wili.wilispikmins.screen.ModMenuTypes;
import net.wili.wilispikmins.sound.ModSounds;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(WilisPikmins.MOD_ID)
public class WilisPikmins
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "wilispikmins";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public WilisPikmins(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // register mod items
        ModItems.register(modEventBus);

        // register mod blocks
        ModBlocks.register(modEventBus);

        // register mod block entities
        ModBlockEntities.register(modEventBus);

        // register mod sounds
        ModSounds.register(modEventBus);

        // register mod menu types
        ModMenuTypes.register(modEventBus);

        // register mod packets
        modEventBus.addListener(this::registerPackets);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // register mod entities
        ModEntities.register(modEventBus);

        // register triggers
        ModTriggers.register(modEventBus);

        // register components and attachments
        OnionComponents.DATA_COMPONENT_TYPES.register(modEventBus);
        OnionComponents.ATTACHMENT_TYPES.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        // agrega los spawn eggs al menu de modo creativo
        if(event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.RED_PIKMIN_SPAWN_EGG.get());
            event.accept(ModItems.BLUE_PIKMIN_SPAWN_EGG.get());
            event.accept(ModItems.YELLOW_PIKMIN_SPAWN_EGG.get());
            event.accept(ModItems.PURPLE_PIKMIN_SPAWN_EGG.get());
            event.accept(ModItems.WHITE_PIKMIN_SPAWN_EGG.get());
            event.accept(ModItems.WINGED_PIKMIN_SPAWN_EGG.get());
            event.accept(ModItems.ROCK_PIKMIN_SPAWN_EGG.get());
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    private void registerPackets(final RegisterPayloadHandlersEvent event) {
        ModPackets.register(event);
    }
}
