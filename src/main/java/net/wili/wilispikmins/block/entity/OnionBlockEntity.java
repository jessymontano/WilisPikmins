package net.wili.wilispikmins.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.block.custom.OnionBlock;
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

    private OnionData onionData = new OnionData();

    public OnionBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ONION_BE.get(), pPos, pBlockState);
        this.pikminType = pBlockState.getValue(OnionBlock.TYPE);
    }

    public OnionData getOnionData() {
        return onionData;
    }

    public void setOnionData(OnionData data) {
        this.onionData = data;
        setChanged();
    }

    public boolean canTakeOut(PikminType type, int amount) {
        return onionData.canTakeOut(type, amount);
    }

    public boolean canPutIn(PikminType type, int amount) {
        return onionData.canPutIn(type, amount);
    }

    public int getStored(PikminType type) {
        return onionData.getStored(type);
    }

    public void addStored(PikminType type, int amount) {
        setOnionData(onionData.addStored(type, amount));
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
        setOnionData(onionData.withHasMainOnion(mainOnion));
        setChanged();
    }

    public PikminType getPikminType() {
        return pikminType;
    }

    public void setPikminType(PikminType type) {
        this.pikminType = type;
        setChanged();
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public ItemStack getItemInSlot(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        if (owner != null) {
            tag.putUUID("owner", owner);
        }
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putBoolean("is_main_onion", isMainOnion);
        tag.putString("pikmin_type", pikminType.name());

        OnionData.CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), onionData)
                .resultOrPartial(WilisPikmins.LOGGER::error)
                .ifPresent(dataTag -> tag.put("onion_data", dataTag));
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));

        if (tag.hasUUID("owner")) {
            owner = tag.getUUID("owner");
        }
        if (tag.contains("is_main_onion")) {
            isMainOnion = tag.getBoolean("is_main_onion");
        }
        if (tag.contains("pikmin_type")) {
            pikminType = PikminType.valueOf(tag.getString("pikmin_type"));
        }

        if (tag.contains("onion_data")) {
            OnionData.CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE),
                    tag.get("onion_data"))
                    .resultOrPartial(WilisPikmins.LOGGER::error)
                    .ifPresent(data -> onionData = data);
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
