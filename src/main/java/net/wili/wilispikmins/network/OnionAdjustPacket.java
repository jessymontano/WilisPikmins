package net.wili.wilispikmins.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import net.wili.wilispikmins.screen.OnionMenu;
import org.jetbrains.annotations.NotNull;

public record OnionAdjustPacket(PikminType pikminType, boolean takeOut) implements CustomPacketPayload {
    public static final Type<OnionAdjustPacket> TYPE =
            new Type<>(ModPackets.ONION_ADJUST_ID);

    public static final StreamCodec<ByteBuf, OnionAdjustPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8.map(PikminType::valueOf, Enum::name),
                    OnionAdjustPacket::pikminType,
                    ByteBufCodecs.BOOL,
                    OnionAdjustPacket::takeOut,
                    OnionAdjustPacket::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final OnionAdjustPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().containerMenu instanceof OnionMenu menu) {
                if (packet.takeOut()) {
                    menu.takeOut(packet.pikminType());
                } else {
                    menu.putIn(packet.pikminType());
                }
            }
        });
    }
}
