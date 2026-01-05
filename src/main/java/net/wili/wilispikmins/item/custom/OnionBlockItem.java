package net.wili.wilispikmins.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.wili.wilispikmins.block.entity.OnionBlockEntity;
import net.wili.wilispikmins.capability.OnionCapability;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OnionBlockItem extends BlockItem {
    public OnionBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext pContext, BlockState pState) {
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        ItemStack stack = pContext.getItemInHand();
        Player player = pContext.getPlayer();

        if (!level.isClientSide && player != null) {
            CompoundTag tag = stack.getTag();
            if (tag != null && tag.getBoolean("was_main_onion")) {
                boolean placed = super.placeBlock(pContext, pState);

                if (placed) {
                    if (level.getBlockEntity(pos) instanceof OnionBlockEntity onionBE) {
                        PikminType type = PikminType.valueOf(tag.getString("pikmin_type"));

                        onionBE.setOwner(player.getUUID());
                        onionBE.setMainOnion(true);
                        onionBE.setPikminType(type);

                        player.getCapability(OnionCapability.ONION_DATA).ifPresent(data -> {
                            if (!data.hasMainOnion()) {
                                data.setHasMainOnion(true);
                                data.unlockType(type);
                                data.setCapacity(type, 20);
                            }
                        });
                    }
                }
                return placed;
            }
        }

        return super.placeBlock(pContext, pState);
    }

    public PikminType getPikminType(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("pikmin_type")) {
            return  PikminType.valueOf(tag.getString("pikmin_type"));
        }
        return PikminType.RED;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);

        CompoundTag tag = pStack.getTag();
        if (tag != null && tag.contains("PikminType")) {
            PikminType type = PikminType.valueOf(tag.getString("PikminType"));
            pTooltip.add(Component.literal("Tipo: " + type.getName())
                    .withStyle(ChatFormatting.GRAY));

            pTooltip.add(Component.translatable("tooltip.wilispikmins.onion.type", type.getDisplayName()).withStyle(ChatFormatting.DARK_GRAY));
            pTooltip.add(Component.translatable("tooltip_wilispikmins.onion.place_1").withStyle(ChatFormatting.DARK_GRAY));
        } else {
            pTooltip.add(Component.translatable("tooltip.wilispikmins.onion.place_2").withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public Component getName(ItemStack pStack) {
        CompoundTag tag = pStack.getTag();
        if (tag != null && tag.contains("PikminType")) {
            PikminType type = PikminType.valueOf(tag.getString("PikminType"));
            return Component.translatable("block.wilispikmins.onion_block")
                    .append(" (" + type.getDisplayName() + ")");
        }
        return super.getName(pStack);
    }

    public ResourceLocation getModelLocation(ItemStack stack) {
        PikminType type = getPikminType(stack);
        return new ResourceLocation("wilispikmins", "block/" + type.getName() + "_onion");
    }
}
