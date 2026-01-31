package net.wili.wilispikmins.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.entity.custom.PikminEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, WilisPikmins.MOD_ID);

    //aqui se registran las entidades
    public static final DeferredHolder<EntityType<?>, EntityType<PikminEntity>> PIKMIN =
            ENTITY_TYPES.register("pikmin",
                    () -> EntityType.Builder.of((EntityType<PikminEntity> type, Level level) -> new PikminEntity(type, level), MobCategory.CREATURE)
                            .sized(0.35f, 0.75f)
                            .clientTrackingRange(8)
                            .build("pikmin"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
        eventBus.addListener(ModEntities::registerAttributes);
    }

    private static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(PIKMIN.get(), PikminEntity.createAttributes().build());
    }
}
