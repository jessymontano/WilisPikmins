package net.wili.wilispikmins.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.wili.wilispikmins.block.custom.OnionBlock;
import net.wili.wilispikmins.data.OnionComponents;
import net.wili.wilispikmins.data.OnionData;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import net.wili.wilispikmins.screen.OnionMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class OnionBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private UUID owner;
    private boolean isMainOnion = false;
    private PikminType pikminType;
    private UUID onionId;

    public OnionBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ONION_BE.get(), pPos, pBlockState);
        this.pikminType = pBlockState.getValue(OnionBlock.TYPE);
    }

    public OnionData getOnionData() {
        if (level != null && !level.isClientSide && owner != null) {
            Player player = level.getPlayerByUUID(owner);
            if (player != null) {
                return player.getData(OnionComponents.PLAYER_ONION_DATA);


            }
        }

        return  new OnionData();
    }

    public void setOnionData(OnionData data) {
        if (level != null && !level.isClientSide && owner != null) {
            Player player = level.getPlayerByUUID(owner);
            if (player != null) {
                player.setData(OnionComponents.PLAYER_ONION_DATA, data);
            }
        }
        setChanged();
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
        setChanged();
    }

    public void setOnionId(UUID id) {
        this.onionId = id;
        setChanged();
    }

    public UUID getOnionId() {
        return onionId;
    }

    public boolean isMainOnion() {
        return  isMainOnion || getBlockState().getValue(OnionBlock.MAIN);
    }

    public void setMainOnion(boolean mainOnion) {
        this.isMainOnion = mainOnion;

        setChanged();
    }

    public void setPikminType(PikminType type) {
        this.pikminType = type;
        setChanged();
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);

        if (owner != null) {
            tag.putUUID("owner", owner);
        }
        if (onionId != null) {
            tag.putUUID("onion_id", onionId);
        }

        tag.putBoolean("is_main_onion", isMainOnion);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.contains("owner")) {
            this.owner = tag.getUUID("owner");
        }

        if (tag.contains("onion_id")) {
            this.onionId = tag.getUUID("onion_id");
        }

        if (tag.contains("is_main_onion")) {
            this.isMainOnion = tag.getBoolean("is_main_onion");
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        String key = isMainOnion ? "block.wilispikmins.onion.main" : "block.wilispikmins.onion";
        return Component.translatable(key);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new OnionMenu(containerId, inventory, this);
    }

}
