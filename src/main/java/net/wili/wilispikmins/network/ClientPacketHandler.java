package net.wili.wilispikmins.network;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.wili.wilispikmins.data.OnionComponents;

public class ClientPacketHandler {
    public static void handleOnionDataSync(final SyncOnionDataPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.setData(OnionComponents.PLAYER_ONION_DATA, packet.data());
            }
        });
    }
}
