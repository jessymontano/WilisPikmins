package net.wili.wilispikmins.item.custom;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.wili.wilispikmins.block.custom.OnionBlock;
import net.wili.wilispikmins.block.entity.OnionBlockEntity;
import net.wili.wilispikmins.data.OnionComponents;
import net.wili.wilispikmins.data.OnionData;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MainOnionBlockItem extends BlockItem {
    public MainOnionBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean placeBlock(@NotNull BlockPlaceContext context, @NotNull BlockState state) {
        boolean placed = super.placeBlock(context, state);

        if (!placed || context.getLevel().isClientSide || context.getPlayer() == null) return placed;

        context.getLevel().setBlock(
                context.getClickedPos(),
                state.setValue(OnionBlock.MAIN, true),
                Block.UPDATE_ALL_IMMEDIATE
        );

        BlockEntity be = context.getLevel().getBlockEntity(context.getClickedPos());
        if (!(be instanceof OnionBlockEntity onionBE)) return placed;

        onionBE.setMainOnion(true);
        onionBE.setOwner(context.getPlayer().getUUID());

        if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
            OnionData currentPlayerData = serverPlayer.getData(OnionComponents.PLAYER_ONION_DATA);

            OnionData updatedData = currentPlayerData.withHasMainOnion(true);
            serverPlayer.setData(OnionComponents.PLAYER_ONION_DATA, updatedData);
        }

        return placed;
    }
}
