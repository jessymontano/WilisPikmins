package net.wili.wilispikmins.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.wili.wilispikmins.block.custom.OnionBlock;
import net.wili.wilispikmins.block.entity.OnionBlockEntity;
import net.wili.wilispikmins.data.OnionComponents;
import net.wili.wilispikmins.data.OnionData;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OnionBlockItem extends BlockItem {
    public OnionBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext pContext, @NotNull BlockState pState) {
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        ItemStack stack = pContext.getItemInHand();
        Player player = pContext.getPlayer();

        boolean placed = super.placeBlock(pContext, pState);

        if (placed && !level.isClientSide && player != null) {
            OnionData itemData = stack.get(OnionComponents.ONION_DATA.get());
            if (itemData != null && level.getBlockEntity(pos) instanceof OnionBlockEntity onionBE) {
                configureBlockEntityFromItem(onionBE, itemData, player);

                if (itemData.hasMainOnion()) {
                    updatePlayerData(player, itemData);
                }
            }
        }
        return placed;
    }

    private void configureBlockEntityFromItem(OnionBlockEntity onionBE, OnionData itemData, Player player) {
        if (!itemData.unlockedTypes().isEmpty()) {
            PikminType type = itemData.unlockedTypes().iterator().next();
            onionBE.setPikminType(type);
        }

        if (itemData.hasMainOnion()) {
            onionBE.setMainOnion(true);
            onionBE.setOwner(player.getUUID());
        }

        onionBE.setOnionData(itemData);
    }

    private void updatePlayerData(Player player, OnionData itemData) {
        if (player instanceof ServerPlayer serverPlayer) {
            OnionData playerData = serverPlayer.getData(OnionComponents.PLAYER_ONION_DATA);

            if (!playerData.hasMainOnion()) {
                OnionData newPlayerData =playerData.withHasMainOnion(true);

                for (PikminType type : itemData.unlockedTypes()) {
                    if (!newPlayerData.hasUnlocked(type)) {
                        newPlayerData = newPlayerData.withUnlockedType(type);
                    }
                }

                serverPlayer.setData(OnionComponents.PLAYER_ONION_DATA, newPlayerData);
            }
        }
    }

    public PikminType getPikminType(ItemStack stack) {
       OnionData data = stack.get(OnionComponents.ONION_DATA.get());
       if (data != null && !data.unlockedTypes().isEmpty()) {
           return data.unlockedTypes().iterator().next();
       }
        return PikminType.RED;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        OnionData data = stack.get(OnionComponents.ONION_DATA.get());

        if (data != null && !data.unlockedTypes().isEmpty()) {
            PikminType type = data.unlockedTypes().iterator().next();
            tooltipComponents.add(Component.translatable("tooltip.wilispikmins.onion.type", type.getDisplayName())
                    .withStyle(ChatFormatting.GRAY));

            if (data.hasMainOnion()) {
                tooltipComponents.add(Component.translatable("tooltip.wilispikmins.onion.main_onion").withStyle(ChatFormatting.GOLD));
            }

            int stored = data.getStored(type);
            int capacity = data.getCapacity(type);
            if (stored > 0) {
                tooltipComponents.add(Component.translatable("tooltip.wilispikmins.onion.stored", stored, capacity).withStyle(ChatFormatting.DARK_GREEN));
            }

            tooltipComponents.add(Component.translatable("tooltip.wilispikmins.onion.type", type.getDisplayName()).withStyle(ChatFormatting.DARK_GRAY));
            tooltipComponents.add(Component.translatable("tooltip.wilispikmins.onion.place_1").withStyle(ChatFormatting.DARK_GRAY));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.wilispikmins.onion.place_2").withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack pStack) {
        OnionData data = pStack.get(OnionComponents.ONION_DATA.get());
        if (data != null && !data.unlockedTypes().isEmpty()) {
            PikminType type = data.unlockedTypes().iterator().next();

            return Component.translatable("block.wilispikmins.onion_block")
                    .append(" (" + type.getDisplayName() + ")");
        }
        return super.getName(pStack);
    }

    public ResourceLocation getModelLocation(ItemStack stack) {
        PikminType type = getPikminType(stack);
        return ResourceLocation.fromNamespaceAndPath("wilispikmins", "block/" + type.getName() + "_onion");
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();

        stack.set(OnionComponents.ONION_DATA.get(),
                new OnionData()
                        .withUnlockedType(PikminType.RED)
                        .withHasMainOnion(false));

        return stack;
    }

    @Override
    protected @Nullable BlockState getPlacementState(@NotNull BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);

        if (state != null) {
            ItemStack stack = context.getItemInHand();
            PikminType type = getPikminType(stack);

            state = state.setValue(OnionBlock.TYPE, type);
        }

        return state;
    }
}
