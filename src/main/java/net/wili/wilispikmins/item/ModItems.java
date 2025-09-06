package net.wili.wilispikmins.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.entity.ModEntities;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, WilisPikmins.MOD_ID);

    // aqui se registran los items
    public static final RegistryObject<Item> RED_PIKMIN_SPAWN_EGG = ITEMS.register("red_pikmin_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.RED_PIKMIN, 0xE32400, 0xFF3B1A, new Item.Properties()));

    public static final RegistryObject<Item> BLUE_PIKMIN_SPAWN_EGG = ITEMS.register("blue_pikmin_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.BLUE_PIKMIN, 0x0044FF, 0x0038D1, new Item.Properties()));

    public static final RegistryObject<Item> YELLOW_PIKMIN_SPAWN_EGG = ITEMS.register("yellow_pikmin_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.YELLOW_PIKMIN, 0xF6D337, 0xF6D337, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
