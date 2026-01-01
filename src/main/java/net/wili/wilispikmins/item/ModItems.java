package net.wili.wilispikmins.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.entity.ModEntities;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
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

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
