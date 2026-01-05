package net.wili.wilispikmins.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.wili.wilispikmins.capability.OnionCapability;
import net.wili.wilispikmins.entity.custom.PikminEntity;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import net.wili.wilispikmins.screen.OnionMenu;

import java.util.function.Supplier;

public record RecallPikminPacket() {
    public static void encode(RecallPikminPacket msg, FriendlyByteBuf buf) {

    }

    public static RecallPikminPacket decode(FriendlyByteBuf buf) {
        return new RecallPikminPacket();
    }

    public static void handle(RecallPikminPacket msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null) return;

            if (player.containerMenu instanceof OnionMenu menu) {
                menu.recallAllPikmins();
            }
        });
        context.get().setPacketHandled(true);
    }
}
