package net.wili.wilispikmins.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.wili.wilispikmins.data.OnionComponents;
import net.wili.wilispikmins.data.OnionData;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OnionUpgradeItem extends Item{
    private final PikminType type;

    public OnionUpgradeItem(PikminType type, Properties pProperties) {
        super(pProperties);
        this.type = type;
    }

    public PikminType getPikminType() {
        return  type;
    }

    public static PikminType getTypeFromStack(ItemStack stack) {
        if (stack.getItem() instanceof  OnionUpgradeItem upgradeItem) {
            return  upgradeItem.getPikminType();
        }

        OnionData data = stack.get(OnionComponents.ONION_DATA.get());
        if (data != null && !data.unlockedTypes().isEmpty()) {
            return data.unlockedTypes().iterator().next();
        }

        return PikminType.RED;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.wilispikmins.onion_upgrade.use").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.wilispikmins.onion_upgrade.effect", 20).withStyle(ChatFormatting.GREEN));
        tooltipComponents.add(Component.empty());
        tooltipComponents.add(Component.translatable("tooltip.wilispikmins.onion_upgrade.source").withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack pStack) {
        return Component.translatable("item.wilispikmins.onion_upgrade")
                .append(" (" + type.getName() + ")");
    }

    @Override
    public boolean isFoil(@NotNull ItemStack pStack) {
        return true;
    }
}
