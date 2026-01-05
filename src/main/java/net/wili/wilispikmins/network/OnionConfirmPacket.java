package net.wili.wilispikmins.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import net.wili.wilispikmins.screen.OnionMenu;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public record OnionConfirmPacket() {
    public static void encode(OnionConfirmPacket msg, FriendlyByteBuf buf) {
    }

    public static OnionConfirmPacket decode(FriendlyByteBuf buf) {
        return new OnionConfirmPacket();
    }

    public static void handle(OnionConfirmPacket msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null) return;

            if (player.containerMenu instanceof OnionMenu menu) {
                menu.confirmOperations();
            }
        });
        context.get().setPacketHandled(true);
    }
}
