package net.wili.wilispikmins.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.wili.wilispikmins.block.custom.OnionBlock;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import net.wili.wilispikmins.screen.OnionMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class OnionBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(1);

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private UUID owner;
    private boolean isMainOnion = false;
    private PikminType pikminType = PikminType.RED;

    public OnionBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ONION_BE.get(), pPos, pBlockState);
        this.pikminType = pBlockState.getValue(OnionBlock.TYPE);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER){
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
        setChanged();
    }

    public boolean isMainOnion() {
        return  isMainOnion;
    }

    public void setMainOnion(boolean mainOnion) {
        this.isMainOnion = mainOnion;
        setChanged();
    }

    public PikminType getPikminType() {
        return pikminType;
    }

    public void setPikminType(PikminType type) {
        this.pikminType = type;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (owner != null) {
            pTag.putUUID("owner", owner);
        }
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putBoolean("is_main_onion", isMainOnion);
        pTag.putString("pikmin_type", pikminType.name());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));

        if (pTag.hasUUID("owner")) {
            owner = pTag.getUUID("owner");
        }
        if (pTag.contains("is_main_onion")) {
            isMainOnion = pTag.getBoolean("is_main_onion");
        }
        if (pTag.contains("pikmin_type")) {
            pikminType = PikminType.valueOf(pTag.getString("pikmin_type"));
        }
    }

    @Override
    public Component getDisplayName() {
        String key = isMainOnion ? "block.wilispikmins.onion.main" : "block.wilispikmins.onion";
        return Component.translatable(key);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new OnionMenu(containerId, inventory, this);
    }

}
