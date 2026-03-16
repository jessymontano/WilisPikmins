package net.wili.wilispikmins.network;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.data.OnionComponents;

public class ModPackets {
    public static final ResourceLocation ONION_UPGRADE_ID =
            ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID, "onion_upgrade");
    public static final ResourceLocation RECALL_PIKMIN_ID =
            ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID, "recall_pikmin");
    public static final ResourceLocation ONION_ADJUST_ID =
            ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID, "onion_adjust");
    public static final ResourceLocation ONION_CONFIRM_ID =
            ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID, "onion_confirm");

    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(WilisPikmins.MOD_ID)
                        .versioned("1.0");

        registrar.playToServer(
               OnionAdjustPacket.TYPE,
                OnionAdjustPacket.STREAM_CODEC,
                OnionAdjustPacket::handle
        );
        registrar.playToServer(
                OnionConfirmPacket.TYPE,
                OnionConfirmPacket.STREAM_CODEC,
                OnionConfirmPacket::handle
        );
        registrar.playToServer(
                OnionUpgradePacket.TYPE,
                OnionUpgradePacket.STREAM_CODEC,
                OnionUpgradePacket::handle
        );
        registrar.playToServer(
                RecallPikminPacket.TYPE,
                RecallPikminPacket.STREAM_CODEC,
                RecallPikminPacket::handle
        );
        registrar.playToClient(
                SyncOnionDataPacket.TYPE,
                SyncOnionDataPacket.STREAM_CODEC,
                ClientPacketHandler::handleOnionDataSync
        );

        WilisPikmins.LOGGER.info("Mod packets registered");
    }
}
