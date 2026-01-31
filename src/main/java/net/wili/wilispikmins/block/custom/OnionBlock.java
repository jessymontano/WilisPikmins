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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.wili.wilispikmins.block.entity.OnionBlockEntity;
import net.wili.wilispikmins.data.OnionComponents;
import net.wili.wilispikmins.data.OnionData;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import net.wili.wilispikmins.item.ModItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OnionBlock extends BaseEntityBlock {
    public static final EnumProperty<PikminType> TYPE =
            EnumProperty.create("type", PikminType.class);
    public static final MapCodec<OnionBlock> CODEC = simpleCodec(OnionBlock::new);

    public OnionBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(TYPE, PikminType.RED));
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

        if (onionBE.isMainOnion() && serverPlayer.getUUID().equals(onionBE.getOwner())) {
            serverPlayer.openMenu(new SimpleMenuProvider(
                    onionBE,
                    Component.translatable("block.wilispikmins.onion.main")
            ), buf ->
                buf.writeBlockPos(pPos));
            return InteractionResult.CONSUME;
        }

        if (!playerData.hasMainOnion() && !onionBE.isMainOnion() && onionBE.getOwner() == null) {
            onionBE.setOwner(serverPlayer.getUUID());
            onionBE.setMainOnion(true);
            onionBE.setPikminType(type);

            OnionData newPlayerData = playerData
                    .withHasMainOnion(true)
                    .withUnlockedType(type)
                    .withCapacity(type, 20);

            serverPlayer.setData(OnionComponents.PLAYER_ONION_DATA, newPlayerData);

            serverPlayer.openMenu(new SimpleMenuProvider(
                    onionBE,
                    Component.translatable("block.wilispikmins.onion.main")
            ), buf ->
                buf.writeBlockPos(pPos));
            return InteractionResult.CONSUME;
        }

        if (playerData.hasMainOnion()) {
            if (onionBE.getOwner() == null) {
                if (!playerData.hasUnlocked(type)) {
                    OnionData unlockedData = playerData.withUnlockedType(type);
                    serverPlayer.setData(OnionComponents.PLAYER_ONION_DATA, unlockedData);
                }
                ItemStack upgradeItem = createUpgradeItem(type);
                if (!serverPlayer.getInventory().add(upgradeItem)) {
                    popResource(pLevel, pPos, upgradeItem);
                }

                pLevel.removeBlock(pPos, false);
                return InteractionResult.CONSUME;
            }

            if (serverPlayer.getUUID().equals(onionBE.getOwner()) && !onionBE.isMainOnion()) {
                serverPlayer.openMenu(new SimpleMenuProvider(
                        onionBE,
                        Component.translatable("block.wilispikmins.onion.main")
                ), buf ->
                    buf.writeBlockPos(pPos));
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(TYPE);
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull Player pPlayer) {
        if (!pLevel.isClientSide && pPlayer instanceof ServerPlayer serverPlayer) {
            PikminType type = pState.getValue(TYPE);
            BlockEntity be = pLevel.getBlockEntity(pPos);

            if (be instanceof  OnionBlockEntity onionBE) {
                OnionData playerData = serverPlayer.getData(OnionComponents.PLAYER_ONION_DATA);

                if (onionBE.isMainOnion() && serverPlayer.getUUID().equals(onionBE.getOwner())) {
                    OnionData newPlayerData = playerData.withHasMainOnion(false);
                    serverPlayer.setData(OnionComponents.PLAYER_ONION_DATA, newPlayerData);

                    ItemStack onionItem = createMainOnionItem(type);

                    OnionData itemData = new OnionData()
                            .withHasMainOnion(true)
                            .withUnlockedType(type);

                    onionItem.set(OnionComponents.ONION_DATA.get(), itemData);

                    popResource(pLevel, pPos, onionItem);
                } else if (serverPlayer.getUUID().equals(onionBE.getOwner())) {
                    ItemStack onionItem = createMainOnionItem(type);

                    OnionData itemData = new OnionData()
                            .withHasMainOnion(false)
                            .withUnlockedType(type);
                    onionItem.set(OnionComponents.ONION_DATA.get(), itemData);

                    popResource(pLevel, pPos, onionItem);
                } else {
                    ItemStack upgradeItem = createUpgradeItem(type);

                    if (!playerData.hasUnlocked(type)) {
                        OnionData upgradeData = new OnionData()
                                .withUnlockedType(type);
                        upgradeItem.set(OnionComponents.ONION_DATA.get(), upgradeData);
                    }

                    popResource(pLevel, pPos, upgradeItem);
                }
            }
        }

        return super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    private ItemStack createMainOnionItem(PikminType type) {
        ItemStack stack = new ItemStack(this);

        stack.set(OnionComponents.ONION_DATA.get(), new OnionData()
                .withUnlockedType(type)
                .withHasMainOnion(true));

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
}
