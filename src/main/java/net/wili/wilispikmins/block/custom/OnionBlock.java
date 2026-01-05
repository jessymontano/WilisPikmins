package net.wili.wilispikmins.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.wili.wilispikmins.block.entity.OnionBlockEntity;
import net.wili.wilispikmins.capability.OnionCapability;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import net.wili.wilispikmins.item.ModItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OnionBlock extends BaseEntityBlock {
    public static final EnumProperty<PikminType> TYPE =
            EnumProperty.create("type", PikminType.class);

    public OnionBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(TYPE, PikminType.RED));
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new OnionBlockEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
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

        return serverPlayer.getCapability(OnionCapability.ONION_DATA).map(data -> {
            if (onionBE.isMainOnion() && serverPlayer.getUUID().equals(onionBE.getOwner())) {
                NetworkHooks.openScreen(serverPlayer, onionBE, pPos);
                return InteractionResult.CONSUME;
            }

            if (!data.hasMainOnion() && !onionBE.isMainOnion() && onionBE.getOwner() == null) {
                onionBE.setOwner(serverPlayer.getUUID());
                onionBE.setMainOnion(true);
                onionBE.setPikminType(type);

                data.setHasMainOnion(true);
                data.unlockType(type);
                data.setCapacity(type, 20);

                NetworkHooks.openScreen(serverPlayer, onionBE, pPos);
                return InteractionResult.CONSUME;
            }

            if (data.hasMainOnion()) {
                if (onionBE.getOwner() == null) {
                    if (!data.hasUnlocked(type)) {
                        data.unlockType(type);
                    }

                    ItemStack upgradeItem = createUpgradeItem(type);
                    if (!serverPlayer.getInventory().add(upgradeItem)) {
                        popResource(pLevel, pPos, upgradeItem);
                    }

                    pLevel.removeBlock(pPos, false);
                    return InteractionResult.CONSUME;
                }

                if (serverPlayer.getUUID().equals(onionBE.getOwner()) && !onionBE.isMainOnion()) {
                    NetworkHooks.openScreen(serverPlayer, onionBE, pPos);
                    return InteractionResult.CONSUME;
                }
            }

            return InteractionResult.PASS;
        }).orElse(InteractionResult.PASS);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(TYPE);
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (!pLevel.isClientSide && pPlayer instanceof ServerPlayer serverPlayer) {
            PikminType type = pState.getValue(TYPE);
            BlockEntity be = pLevel.getBlockEntity(pPos);

            if (be instanceof  OnionBlockEntity onionBE) {
                serverPlayer.getCapability(OnionCapability.ONION_DATA).ifPresent(data -> {
                    if (onionBE.isMainOnion() && serverPlayer.getUUID().equals(onionBE.getOwner())) {
                        data.setHasMainOnion(false);

                        ItemStack onionItem = createMainOnionItem(type);
                        CompoundTag tag = new CompoundTag();

                        tag.putString("pikmin_type", type.name());
                        tag.putBoolean("was_main_onion", true);
                        onionItem.setTag(tag);

                        popResource(pLevel, pPos, onionItem);
                    } else if (serverPlayer.getUUID().equals(onionBE.getOwner())) {
                        ItemStack onionItem = createMainOnionItem(type);
                        CompoundTag tag = new CompoundTag();

                        tag.putString("pikmin_type", type.name());
                        tag.putBoolean("was_main_onion", false);
                        onionItem.setTag(tag);

                        popResource(pLevel, pPos, onionItem);
                    } else {
                        ItemStack upgradeItem = createUpgradeItem(type);
                        popResource(pLevel, pPos, upgradeItem);
                    }
                });
            }
        }

        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    private ItemStack createMainOnionItem(PikminType type) {
        ItemStack stack = new ItemStack(this);
        CompoundTag tag = new CompoundTag();
        tag.putString("pikmin_type", type.name());
        tag.putBoolean("was_main_onion", true);
        stack.setTag(tag);
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
        CompoundTag tag = new CompoundTag();
        tag.putString("pikmin_type", type.name());
        upgradeStack.setTag(tag);

        return upgradeStack;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = new ItemStack(this);
        CompoundTag tag = new CompoundTag();
        tag.putString("pikmin_type", state.getValue(TYPE).name());
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof OnionBlockEntity onionBE && onionBE.isMainOnion()) {
            tag.putBoolean("was_main_onion", true);
        }
        stack.setTag(tag);
        return stack;
    }
}
