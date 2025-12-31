package net.wili.wilispikmins.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.entity.ModEntities;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import net.wili.wilispikmins.item.custom.PikminSpawnEggItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, WilisPikmins.MOD_ID);

    // aqui se registran los items
    public static final RegistryObject<Item> RED_PIKMIN_SPAWN_EGG = ITEMS.register("red_pikmin_spawn_egg", () -> new PikminSpawnEggItem(PikminType.RED));

    public static final RegistryObject<Item> BLUE_PIKMIN_SPAWN_EGG = ITEMS.register("blue_pikmin_spawn_egg", () -> new PikminSpawnEggItem(PikminType.BLUE));

    public static final RegistryObject<Item> YELLOW_PIKMIN_SPAWN_EGG = ITEMS.register("yellow_pikmin_spawn_egg", () -> new PikminSpawnEggItem(PikminType.YELLOW));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
