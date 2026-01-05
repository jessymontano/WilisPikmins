package net.wili.wilispikmins.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import net.wili.wilispikmins.screen.OnionMenu;

import java.util.function.Supplier;

public record OnionAdjustPacket(PikminType type, boolean takeOut) {
    public static void encode (OnionAdjustPacket msg, FriendlyByteBuf buf) {
        buf.writeEnum(msg.type);
        buf.writeBoolean(msg.takeOut);
    }

    public static OnionAdjustPacket decode(FriendlyByteBuf buf) {
        return new OnionAdjustPacket(buf.readEnum(PikminType.class), buf.readBoolean());
    }

    public static void handle(OnionAdjustPacket msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null) return;

            if (player.containerMenu instanceof OnionMenu menu) {
               if (msg.takeOut()) {
                   menu.takeOut(msg.type);
               } else {
                   menu.putIn(msg.type);
               }
            }
        });
        context.get().setPacketHandled(true);
    }
}
