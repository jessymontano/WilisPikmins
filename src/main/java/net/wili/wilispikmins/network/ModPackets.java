package net.wili.wilispikmins.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.wili.wilispikmins.WilisPikmins;

public class ModPackets {
    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(WilisPikmins.MOD_ID, "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );

    private static int index = 0;

    public static void register() {
        CHANNEL.registerMessage(
                index++,
                OnionUpgradePacket.class,
                OnionUpgradePacket::encode,
                OnionUpgradePacket::decode,
                OnionUpgradePacket::handle
        );
        CHANNEL.registerMessage(
                index++,
                RecallPikminPacket.class,
                RecallPikminPacket::encode,
                RecallPikminPacket::decode,
                RecallPikminPacket::handle
        );
        CHANNEL.registerMessage(
                index++,
                OnionAdjustPacket.class,
                OnionAdjustPacket::encode,
                OnionAdjustPacket::decode,
                OnionAdjustPacket::handle
        );
        CHANNEL.registerMessage(
                index++,
                OnionConfirmPacket.class,
                OnionConfirmPacket::encode,
                OnionConfirmPacket::decode,
                OnionConfirmPacket::handle
        );
    }

    public static void sendToServer(Object msg) {
        CHANNEL.sendToServer(msg);
    }
}
