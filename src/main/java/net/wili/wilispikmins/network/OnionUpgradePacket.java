package net.wili.wilispikmins.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.wili.wilispikmins.capability.OnionCapability;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import net.wili.wilispikmins.item.custom.OnionUpgradeItem;
import net.wili.wilispikmins.screen.OnionMenu;

import java.util.function.Supplier;

public record OnionUpgradePacket() {

    public static void encode(OnionUpgradePacket msg, FriendlyByteBuf buf) {

    }

    public static OnionUpgradePacket decode(FriendlyByteBuf buf) {
        return  new OnionUpgradePacket();
    }

    public static void handle(OnionUpgradePacket msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null) return;

            if (player.containerMenu instanceof OnionMenu menu) {
                menu.processUpgrade();
            } else {
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (stack.getItem() instanceof OnionUpgradeItem) {
                        PikminType type = OnionUpgradeItem.getTypeFromStack(stack);
                        int count = stack.getCount();

                        player.getCapability(OnionCapability.ONION_DATA).ifPresent(data -> {
                            data.addCapacity(type, 20 * count);
                            stack.shrink(count);
                        });
                        break;
                    }
                }
            }
        });

        context.get().setPacketHandled(true);
    }
}
