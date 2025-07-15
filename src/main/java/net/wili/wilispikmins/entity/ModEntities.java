package net.wili.wilispikmins.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.entity.custom.RedPikminEntity;

import java.util.Objects;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, WilisPikmins.MOD_ID);

    public static final RegistryObject<EntityType<RedPikminEntity>> RED_PIKMIN =
            ENTITY_TYPES.register("red_pikmin", () -> EntityType.Builder.of(RedPikminEntity::new, MobCategory.CREATURE).sized(0.75f, 0.35f).build("red_pikmin"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
