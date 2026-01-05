package net.wili.wilispikmins.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.capability.OnionCapability;
import net.wili.wilispikmins.entity.custom.PikminEntity;
import net.wili.wilispikmins.entity.custom.enums.PikminType;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = WilisPikmins.MOD_ID)
public class PikminTrackingEvents {
    @SubscribeEvent
    public static void onPikminSpawn(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide && event.getEntity() instanceof PikminEntity pikmin) {
            if (pikmin.getOwnerUUID() != null) {
                Entity owner = event.getLevel().getPlayerByUUID(pikmin.getOwnerUUID());

                if (owner instanceof ServerPlayer player) {
                    player.getCapability(OnionCapability.ONION_DATA).ifPresent(data -> {
                        PikminType type = pikmin.getPikminType();
                        data.addOutside(type, 1);
                    });
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPikminDeath(LivingDeathEvent event) {
        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof PikminEntity pikmin) {
            Entity owner = pikmin.level().getPlayerByUUID(pikmin.getOwnerUUID());
            if (owner instanceof ServerPlayer player) {
                player.getCapability(OnionCapability.ONION_DATA).ifPresent(data -> {
                    PikminType type = pikmin.getPikminType();
                    data.addOutside(type, -1);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer serverPlayer) {
            serverPlayer.getCapability(OnionCapability.ONION_DATA).ifPresent(data -> {

                for (PikminType type : PikminType.values()) {
                    data.setOutside(type, 0);
                }

                ServerLevel level = serverPlayer.serverLevel();
                UUID id = serverPlayer.getUUID();

                for (Entity entity : level.getAllEntities()) {
                    if (entity instanceof PikminEntity pikmin) {
                        if (id.equals(pikmin.getOwnerUUID())) {
                            data.addOutside(pikmin.getPikminType(), 1);
                        }
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(OnionCapability.ONION_DATA).ifPresent(data -> {

            });
        }
    }
}
