package net.wili.wilispikmins.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OnionDataProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    private final OnionData data = new OnionData();
    private final LazyOptional<IOnionData> optional = LazyOptional.of(() -> data);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        return capability == OnionCapability.ONION_DATA
                ? optional.cast()
                : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.putBoolean("has_main_onion", data.hasMainOnion());

        CompoundTag stored = new CompoundTag();
        for (PikminType type : PikminType.values()) {
            stored.putInt(type.name(), data.getStored(type));
            tag.putInt("capacity", data.getCapacity(type));
        }
        tag.put("stored", stored);

        CompoundTag outside = new CompoundTag();
        for (PikminType type : PikminType.values()) {
            outside.putInt(type.name(), data.getOutside(type));
        }
        tag.put("outside", outside);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        data.setHasMainOnion(tag.getBoolean("has_main_onion"));

        CompoundTag stored = tag.getCompound("stored");
        CompoundTag outside = tag.getCompound("outside");
        for (PikminType type : PikminType.values()) {
            data.addStored(type, stored.getInt(type.name()));
            data.setCapacity(type, tag.getInt("capacity"));
            data.setOutside(type, outside.getInt(type.name()));
        }
    }
}
