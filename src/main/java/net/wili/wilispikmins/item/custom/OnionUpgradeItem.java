package net.wili.wilispikmins.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import org.jetbrains.annotations.Nullable;

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

        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("pikmin_type")) {
            return PikminType.valueOf(tag.getString("pikmin_type"));
        }

        return PikminType.RED;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        pTooltipComponents.add(Component.translatable("tooltip.wilispikmins.onion_upgrade.use").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(Component.translatable("tooltip.wilispikmins.onion_upgrade.effect", 20).withStyle(ChatFormatting.GREEN));
        pTooltipComponents.add(Component.empty());
        pTooltipComponents.add(Component.translatable("tooltip.wilispikmins.onion_upgrade.source").withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public Component getName(ItemStack pStack) {
        return Component.translatable("item.wilispikmins.onion_upgrade")
                .append(" (" + type.getName() + ")");
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }
}
