package net.wili.wilispikmins.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.wili.wilispikmins.screen.OnionMenu;
import org.jetbrains.annotations.NotNull;

public record RecallPikminPacket() implements CustomPacketPayload {
    public static final Type<RecallPikminPacket> TYPE =
            new Type<>(ModPackets.RECALL_PIKMIN_ID);

    public static final StreamCodec<ByteBuf, RecallPikminPacket> STREAM_CODEC =
            StreamCodec.unit(new RecallPikminPacket());

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final RecallPikminPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
           if (context.player().containerMenu instanceof OnionMenu menu) {
               menu.recallAllPikmins();
           }
        });
    }
}
