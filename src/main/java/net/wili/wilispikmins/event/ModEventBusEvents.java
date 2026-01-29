package net.wili.wilispikmins.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wili.wilispikmins.WilisPikmins;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.wili.wilispikmins.capability.IOnionData;
import net.wili.wilispikmins.capability.OnionCapability;
import net.wili.wilispikmins.capability.OnionDataProvider;
import net.wili.wilispikmins.entity.ModEntities;
import net.wili.wilispikmins.entity.custom.PikminEntity;
import net.wili.wilispikmins.entity.custom.enums.PikminType;

@Mod.EventBusSubscriber(modid = WilisPikmins.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.PIKMIN.get(), PikminEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IOnionData.class);
    }
}
