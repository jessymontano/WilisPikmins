package net.wili.wilispikmins.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.data.OnionComponents;
import net.wili.wilispikmins.data.OnionData;
import net.wili.wilispikmins.entity.custom.PikminEntity;
import net.wili.wilispikmins.entity.custom.enums.PikminType;

import java.util.UUID;

@EventBusSubscriber(modid = WilisPikmins.MOD_ID)
public class PikminTrackingEvents {
    @SubscribeEvent
    public static void onPikminSpawn(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide && event.getEntity() instanceof PikminEntity pikmin) {
            if (pikmin.getOwnerUUID() != null) {
                Entity owner = event.getLevel().getPlayerByUUID(pikmin.getOwnerUUID());

                if (owner instanceof ServerPlayer player) {
                    OnionData playerData = player.getData(OnionComponents.PLAYER_ONION_DATA);
                    PikminType type = pikmin.getPikminType();

                    OnionData newData = playerData.addOutside(type, 1);
                    player.setData(OnionComponents.PLAYER_ONION_DATA, newData);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPikminDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        if (!entity.level().isClientSide() && entity instanceof  PikminEntity pikmin) {
            UUID ownerUUID = pikmin.getOwnerUUID();
            if (ownerUUID != null) {
                Entity owner = entity.level().getPlayerByUUID(ownerUUID);

                if (owner instanceof ServerPlayer player) {
                    OnionData playerData = player.getData(OnionComponents.PLAYER_ONION_DATA);
                    PikminType type = pikmin.getPikminType();

                    int currentOutside = playerData.getOutside(type);
                    if (currentOutside > 0) {
                        OnionData newData = playerData.addOutside(type, -1);
                        player.setData(OnionComponents.PLAYER_ONION_DATA, newData);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer serverPlayer) {
            OnionData playerData = serverPlayer.getData(OnionComponents.PLAYER_ONION_DATA);

            OnionData newData = playerData;
            for (PikminType type : PikminType.values()) {
                newData = newData.withOutside(type, 0);
            }

            if (serverPlayer.level() instanceof ServerLevel level) {
                UUID playerId = serverPlayer.getUUID();

                for (Entity entity : level.getAllEntities()) {
                    if (entity instanceof PikminEntity pikmin) {
                        UUID pikminOwner = pikmin.getOwnerUUID();
                        if (playerId.equals(pikminOwner)) {
                            PikminType type = pikmin.getPikminType();
                            newData = newData.addOutside(type, 1);
                        }
                    }
                }

                serverPlayer.setData(OnionComponents.PLAYER_ONION_DATA, newData);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer player) {
            // todo: agregar algo aqi
        }
    }
}
