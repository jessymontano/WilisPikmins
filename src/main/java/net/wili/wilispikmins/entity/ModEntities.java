package net.wili.wilispikmins.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.entity.custom.BluePikminEntity;
import net.wili.wilispikmins.entity.custom.PikminEntity;
import net.wili.wilispikmins.entity.custom.RedPikminEntity;
import net.wili.wilispikmins.entity.custom.YellowPikminEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, WilisPikmins.MOD_ID);

    //aqui se registran las entidades
    public static final RegistryObject<EntityType<PikminEntity>> PIKMIN =
            ENTITY_TYPES.register("pikmin",
                    () -> EntityType.Builder.of((EntityType<PikminEntity> type, Level level) -> new PikminEntity(type, level), MobCategory.CREATURE)
                            .sized(0.35f, 0.75f)
                            .clientTrackingRange(8)
                            .build("pikmin"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
