package net.wili.wilispikmins.event;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.capability.OnionCapability;
import net.wili.wilispikmins.capability.OnionDataProvider;
import net.wili.wilispikmins.entity.custom.enums.PikminType;

@Mod.EventBusSubscriber(
        modid = WilisPikmins.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class ForgeEventBusEvents {
    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(
                    new ResourceLocation(WilisPikmins.MOD_ID, "onion_data"),
                    new OnionDataProvider()
            );
        }
    }

    @SubscribeEvent
    public static void clonePlayer(PlayerEvent.Clone event) {
        event.getOriginal().getCapability(OnionCapability.ONION_DATA).ifPresent(oldData -> {
            event.getEntity().getCapability(OnionCapability.ONION_DATA).ifPresent(newData -> {
                newData.setHasMainOnion(oldData.hasMainOnion());

                for (var type : oldData.getUnlockedTypes()) {
                    newData.unlockType(type);
                }

                for (var type : PikminType.values()) {
                    int stored = oldData.getStored(type);
                    if (stored > 0) {
                        newData.addStored(type, stored);
                    }
                    newData.addCapacity(type, oldData.getCapacity(type));
                }

            });
        });
    }
}


