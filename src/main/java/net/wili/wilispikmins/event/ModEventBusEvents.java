package net.wili.wilispikmins.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wili.wilispikmins.WilisPikmins;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.wili.wilispikmins.entity.ModEntities;
import net.wili.wilispikmins.entity.custom.RedPikminEntity;

@Mod.EventBusSubscriber(modid = WilisPikmins.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.RED_PIKMIN.get(), RedPikminEntity.createAttribute().build());
    }
}
