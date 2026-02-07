package net.wili.wilispikmins.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.advancement.ModTriggers;
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

                   checkTotalPikminAchievement(player, newData);
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

                        checkTotalPikminAchievement(player, newData);
                    }
                }
            }
        }
    }

    private static void checkTotalPikminAchievement(ServerPlayer player, OnionData data) {
        int totalPikmin = 0;

        for (PikminType type: PikminType.values()) {
            totalPikmin += data.getStored(type);
            totalPikmin += data.getOutside(type);
        }

        if (totalPikmin >= 100) {
         ModTriggers.TOTAL_PIKMIN.get().trigger(player, 100);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer serverPlayer) {

            OnionData newData = serverPlayer.getData(OnionComponents.PLAYER_ONION_DATA);
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

    @SubscribeEvent
    public static void onEntityKilled(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) return;

        LivingEntity victim = event.getEntity();
        DamageSource source = event.getSource();

        Entity directAttacker = source.getDirectEntity();
        Entity causingEntity = source.getEntity();

        Entity attackingPikmin = null;
        if (directAttacker instanceof PikminEntity) {
            attackingPikmin = directAttacker;
        } else if (causingEntity instanceof PikminEntity) {
            attackingPikmin = causingEntity;
        }

        if (attackingPikmin instanceof PikminEntity pikmin) {
            Entity owner = pikmin.getOwner();

            if (owner instanceof ServerPlayer serverPlayer) {
                ModTriggers.KILL_WITH_PIKMIN.get().trigger(serverPlayer);
            }
        }
    }
}
