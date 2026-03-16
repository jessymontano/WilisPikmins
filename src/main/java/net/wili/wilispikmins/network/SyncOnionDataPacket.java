package net.wili.wilispikmins.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.data.OnionData;
import org.jetbrains.annotations.NotNull;

public record SyncOnionDataPacket(OnionData data) implements CustomPacketPayload {
    public static final Type<SyncOnionDataPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID, "sync_onion_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncOnionDataPacket> STREAM_CODEC =
            StreamCodec.ofMember(SyncOnionDataPacket::write, SyncOnionDataPacket::new);

    public void write(RegistryFriendlyByteBuf buf) {
        OnionData.STREAM_CODEC.encode(buf, this.data);
    }

    private SyncOnionDataPacket(RegistryFriendlyByteBuf buf) {
        this(OnionData.STREAM_CODEC.decode(buf));
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
