package net.wili.wilispikmins.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.wili.wilispikmins.block.entity.OnionBlockEntity;
import net.wili.wilispikmins.data.OnionComponents;
import net.wili.wilispikmins.data.OnionData;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import net.wili.wilispikmins.item.ModItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class OnionBlock extends BaseEntityBlock {
    public static final EnumProperty<PikminType> TYPE =
            EnumProperty.create("type", PikminType.class);
    public static final BooleanProperty MAIN = BooleanProperty.create("main");
    public static final MapCodec<OnionBlock> CODEC = simpleCodec(OnionBlock::new);

    public OnionBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(TYPE, PikminType.RED)
                .setValue(MAIN, false));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new OnionBlockEntity(blockPos, blockState);
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (!(pPlayer instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.PASS;
        }

        BlockEntity be = pLevel.getBlockEntity(pPos);
        if (!(be instanceof OnionBlockEntity onionBE)) {
            return InteractionResult.PASS;
        }

        PikminType type = pState.getValue(TYPE);
        OnionData playerData = serverPlayer.getData(OnionComponents.PLAYER_ONION_DATA);


        if (onionBE.getOwner() == null) {
            if (!playerData.hasMainOnion()) {
                return claimAsMainOnion(serverPlayer, onionBE, pState, pLevel, pPos, type);
            } else {
                return harvestAsUpgrade(serverPlayer, pLevel, pPos, type, playerData);
            }
        }

        if (serverPlayer.getUUID().equals(onionBE.getOwner())) {
            if (onionBE.isMainOnion()) {
                serverPlayer.openMenu(new SimpleMenuProvider(
                        onionBE,
                        Component.translatable("block.wilispikmins.onion.main")
                ), buf -> buf.writeBlockPos(pPos));
                return InteractionResult.CONSUME;
            }
        } else {
            return InteractionResult.FAIL;
        }

        return InteractionResult.PASS;
    }

    private  InteractionResult claimAsMainOnion(ServerPlayer player, OnionBlockEntity onionBE, BlockState state, Level level, BlockPos pos, PikminType type) {
        UUID onionId = UUID.randomUUID();

        onionBE.setOnionId(onionId);
        onionBE.setOwner(player.getUUID());

        OnionData playerData = player.getData(OnionComponents.PLAYER_ONION_DATA);

        OnionData initialData = playerData
                .withUnlockedType(type)
                .withHasMainOnion(true);
        onionBE.setOnionData(initialData);
        onionBE.setMainOnion(true);

        level.setBlock(pos, state.setValue(MAIN, true), Block.UPDATE_ALL);

        OnionData newData = playerData
                .withMainOnion(onionId, pos)
                .withHasMainOnion(true)
                .withUnlockedType(type);

        player.setData(OnionComponents.PLAYER_ONION_DATA, newData);

        OnionData blockData = onionBE.getOnionData();
        if (!blockData.equals(new OnionData())) {
            OnionData mergedData = new OnionData(
                    true,
                    newData.unlockedTypes(),
                    blockData.stored(),
                    newData.capacity(),
                    newData.outside()
            );
            player.setData(OnionComponents.PLAYER_ONION_DATA, mergedData);
        }

        player.openMenu(new SimpleMenuProvider(
                onionBE,
                Component.translatable("block.wilispikmins.onion.main")
        ), buf ->
                buf.writeBlockPos(pos));
        return InteractionResult.CONSUME;
    }

    private InteractionResult harvestAsUpgrade(ServerPlayer player, Level level, BlockPos pos, PikminType type, OnionData playerData) {
        if (!playerData.hasUnlocked(type)) {
            OnionData unlockedData = playerData.withUnlockedType(type);
            player.setData(OnionComponents.PLAYER_ONION_DATA, unlockedData);
        }

        ItemStack upgradeItem = createUpgradeItem(type);
        if (!player.getInventory().add(upgradeItem)) {
            popResource(level, pos, upgradeItem);
        }
        level.removeBlock(pos, false);

        return InteractionResult.CONSUME;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(TYPE, MAIN);
    }

    @Override
    public void playerDestroy(@NotNull Level level, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable BlockEntity blockEntity, @NotNull ItemStack tool) {
        if (level.isClientSide || !(player instanceof ServerPlayer serverPlayer)) return;

        if (!(blockEntity instanceof OnionBlockEntity onionBE)) return;

        PikminType type = state.getValue(TYPE);

        if (onionBE.isMainOnion() && serverPlayer.getUUID().equals(onionBE.getOwner())) {
            ItemStack mainItem = createMainOnionItem(onionBE.getOnionId(), pos, serverPlayer);

            OnionData data = serverPlayer.getData(OnionComponents.PLAYER_ONION_DATA);
            serverPlayer.setData(OnionComponents.PLAYER_ONION_DATA, data);

            popResource(level, pos, mainItem);
            return;
        }

        ItemStack upgrade = createUpgradeItem(type);
        popResource(level, pos, upgrade);
    }

    //    @Override
//    public @NotNull BlockState playerWillDestroy(Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull Player pPlayer) {
//        if (!pLevel.isClientSide && pPlayer instanceof ServerPlayer serverPlayer) {
//            PikminType type = pState.getValue(TYPE);
//            BlockEntity be = pLevel.getBlockEntity(pPos);
//
//            if (be instanceof  OnionBlockEntity onionBE) {
//
//                // only owner can break main onion
//               if (onionBE.getOwner() != null && !serverPlayer.getUUID().equals(onionBE.getOwner())) {
//                   pLevel.setBlock(pPos, pState, Block.UPDATE_ALL);
//                   return pState;
//               }
//
//                if (onionBE.isMainOnion() && serverPlayer.getUUID().equals(onionBE.getOwner())) {
//                     ItemStack mainOnionItem = createMainOnionItem(onionBE.getOnionId(), pPos, serverPlayer);
//
//                     OnionData playerData = serverPlayer.getData(OnionComponents.PLAYER_ONION_DATA);
//                     OnionData newData = playerData.updateMainOnionPosition(null);
//                     serverPlayer.setData(OnionComponents.PLAYER_ONION_DATA, newData);
//
//                     popResource(pLevel, pPos, mainOnionItem);
//                } else {
//                    ItemStack upgradeItem = createUpgradeItem(type);
//                    popResource(pLevel, pPos, upgradeItem);
//                }
//            }
//        }
//
//        return super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
//    }

    private ItemStack createMainOnionItem(UUID onionId, BlockPos pos, ServerPlayer player) {
        ItemStack stack = new ItemStack(ModItems.MAIN_ONION.get());

        OnionData currentData = player.getData(OnionComponents.PLAYER_ONION_DATA);
        OnionData itemData = new OnionData(
                currentData.hasMainOnion(),
                currentData.unlockedTypes(),
                currentData.stored(),
                currentData.capacity(),
                currentData.outside()
        );
        stack.set(OnionComponents.ONION_DATA.get(), itemData);

        return stack;
    }

    private ItemStack createUpgradeItem(PikminType type) {
        ItemStack upgradeStack = switch (type) {
            case RED -> new ItemStack(ModItems.RED_ONION_UPGRADE.get());
            case YELLOW -> new ItemStack(ModItems.YELLOW_ONION_UPGRADE.get());
            case BLUE -> new ItemStack(ModItems.BLUE_ONION_UPGRADE.get());
            case PURPLE -> new ItemStack(ModItems.PURPLE_ONION_UPGRADE.get());
            case WHITE -> new ItemStack(ModItems.WHITE_ONION_UPGRADE.get());
            case WINGED -> new ItemStack(ModItems.WINGED_ONION_UPGRADE.get());
            case ROCK -> new ItemStack(ModItems.ROCK_ONION_UPGRADE.get());
        };

        OnionData upgradeData = new OnionData()
                .withUnlockedType(type);
        upgradeStack.set(OnionComponents.ONION_DATA.get(), upgradeData);

        return upgradeStack;
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockState state, @NotNull HitResult target, LevelReader level, @NotNull BlockPos pos, @NotNull Player player) {
        ItemStack stack = new ItemStack(this);
        PikminType type = state.getValue(TYPE);

        OnionData itemData = new OnionData()
                .withUnlockedType(type);

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof  OnionBlockEntity onionBE && onionBE.isMainOnion()) {
            itemData = itemData.withHasMainOnion(true);
        }

        stack.set(OnionComponents.ONION_DATA.get(), itemData);

        return stack;
    }

    @Override
    protected float getDestroyProgress(@NotNull BlockState state, @NotNull Player player, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        ItemStack tool = player.getMainHandItem();

        if (tool.getItem() instanceof HoeItem) {
            return 100.0f;
        }

        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    protected @NotNull List<ItemStack> getDrops(@NotNull BlockState state, LootParams.@NotNull Builder params) {
        return List.of();
    }

    @Override
    public boolean canHarvestBlock(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return false;

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof OnionBlockEntity onionBE) {
            if (onionBE.isMainOnion()
            && onionBE.getOwner() != null
            && !serverPlayer.getUUID().equals(onionBE.getOwner())) {
                return false;
            }
        }
        return true;
    }
}
