package net.wili.wilispikmins.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.block.ModBlocks;
import net.wili.wilispikmins.entity.ModEntities;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import net.wili.wilispikmins.item.custom.OnionBlockItem;
import net.wili.wilispikmins.item.custom.OnionUpgradeItem;
import net.wili.wilispikmins.item.custom.PikminSpawnEggItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, WilisPikmins.MOD_ID);

    // aqui se registran los items
    public static final RegistryObject<Item> RED_PIKMIN_SPAWN_EGG = ITEMS.register("red_pikmin_spawn_egg", () -> new PikminSpawnEggItem(PikminType.RED));
    public static final RegistryObject<Item> BLUE_PIKMIN_SPAWN_EGG = ITEMS.register("blue_pikmin_spawn_egg", () -> new PikminSpawnEggItem(PikminType.BLUE));
    public static final RegistryObject<Item> YELLOW_PIKMIN_SPAWN_EGG = ITEMS.register("yellow_pikmin_spawn_egg", () -> new PikminSpawnEggItem(PikminType.YELLOW));
    public static final RegistryObject<Item> PURPLE_PIKMIN_SPAWN_EGG = ITEMS.register("purple_pikmin_spawn_egg", () -> new PikminSpawnEggItem(PikminType.PURPLE));
    public static final RegistryObject<Item> WHITE_PIKMIN_SPAWN_EGG = ITEMS.register("white_pikmin_spawn_egg", () -> new PikminSpawnEggItem(PikminType.WHITE));
    public static final RegistryObject<Item> WINGED_PIKMIN_SPAWN_EGG = ITEMS.register("winged_pikmin_spawn_egg", () -> new PikminSpawnEggItem(PikminType.WINGED));
    public static final RegistryObject<Item> ROCK_PIKMIN_SPAWN_EGG = ITEMS.register("rock_pikmin_spawn_egg", () -> new PikminSpawnEggItem(PikminType.ROCK));

    public static final RegistryObject<Item> ONION = ITEMS.register("onion",
            () -> new OnionBlockItem(ModBlocks.ONION_BLOCK.get(),
                    new Item.Properties()));
    public static final RegistryObject<Item> RED_ONION_UPGRADE = ITEMS.register("red_onion_upgrade",
            () -> new OnionUpgradeItem(PikminType.RED, new Item.Properties()
                    .stacksTo(64)));
    public static final RegistryObject<Item> YELLOW_ONION_UPGRADE = ITEMS.register("yellow_onion_upgrade",
            () -> new OnionUpgradeItem(PikminType.YELLOW, new Item.Properties()
                    .stacksTo(64)));
    public static final RegistryObject<Item> BLUE_ONION_UPGRADE = ITEMS.register("blue_onion_upgrade",
            () -> new OnionUpgradeItem(PikminType.BLUE, new Item.Properties()
                    .stacksTo(64)));
    public static final RegistryObject<Item> PURPLE_ONION_UPGRADE = ITEMS.register("purple_onion_upgrade",
            () -> new OnionUpgradeItem(PikminType.PURPLE, new Item.Properties()
                    .stacksTo(64)));
    public static final RegistryObject<Item> WHITE_ONION_UPGRADE = ITEMS.register("white_onion_upgrade",
            () -> new OnionUpgradeItem(PikminType.WHITE, new Item.Properties()
                    .stacksTo(64)));
    public static final RegistryObject<Item> WINGED_ONION_UPGRADE = ITEMS.register("winged_onion_upgrade",
            () -> new OnionUpgradeItem(PikminType.WINGED, new Item.Properties()
                    .stacksTo(64)));
    public static final RegistryObject<Item> ROCK_ONION_UPGRADE = ITEMS.register("rock_onion_upgrade",
            () -> new OnionUpgradeItem(PikminType.ROCK, new Item.Properties()
                    .stacksTo(64)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
