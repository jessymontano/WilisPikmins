package net.wili.wilispikmins.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.wili.wilispikmins.data.OnionComponents;
import net.wili.wilispikmins.data.OnionData;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import net.wili.wilispikmins.item.custom.OnionUpgradeItem;
import net.wili.wilispikmins.screen.OnionMenu;
import org.jetbrains.annotations.NotNull;

public record OnionUpgradePacket() implements CustomPacketPayload {
    public static final Type<OnionUpgradePacket> TYPE =
            new Type<>(ModPackets.ONION_UPGRADE_ID);

    public static final StreamCodec<ByteBuf, OnionUpgradePacket> STREAM_CODEC =
            StreamCodec.unit(new OnionUpgradePacket());

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final OnionUpgradePacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().containerMenu instanceof OnionMenu menu) {
                menu.processUpgrade();
            } else {
                processUpgradeFromInventory(context.player());
            }
        });
    }

    private static void processUpgradeFromInventory(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof OnionUpgradeItem upgradeItem) {
                PikminType type = upgradeItem.getPikminType();
                int count = stack.getCount();

                OnionData playerData = player.getData(OnionComponents.PLAYER_ONION_DATA);
                OnionData newData = playerData.addCapacity(type, 20 * count);
                player.setData(OnionComponents.PLAYER_ONION_DATA, newData);

                stack.shrink(count);
                break;
            }
        }
    }
}
