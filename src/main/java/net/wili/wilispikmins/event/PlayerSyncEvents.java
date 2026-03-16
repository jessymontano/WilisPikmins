package net.wili.wilispikmins.event;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.data.OnionComponents;
import net.wili.wilispikmins.data.OnionData;
import net.wili.wilispikmins.network.SyncOnionDataPacket;

@EventBusSubscriber(modid = WilisPikmins.MOD_ID)
public class PlayerSyncEvents {
    @SubscribeEvent
    public static void onPlayerJoinWorld(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            syncData(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            syncData(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            syncData(player);
        }
    }

    private static void syncData(ServerPlayer player) {
        OnionData data = player.getData(OnionComponents.PLAYER_ONION_DATA);
        PacketDistributor.sendToPlayer(player, new SyncOnionDataPacket(data));
    }
}
