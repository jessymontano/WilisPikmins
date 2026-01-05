package net.wili.wilispikmins.capability;

import com.eliotlash.mclib.math.functions.classic.Pi;
import net.wili.wilispikmins.entity.custom.enums.PikminType;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

public class OnionData implements  IOnionData{
    private boolean hasMainOnion = false;
    private final Set<PikminType> unlockedTypes = EnumSet.allOf(PikminType.class);
    private final EnumMap<PikminType, Integer> stored = new EnumMap<>(PikminType.class);
    private final EnumMap<PikminType, Integer> capacity = new EnumMap<>(PikminType.class);
    private boolean tracking = false;
    private final EnumMap<PikminType, Integer> outside = new EnumMap<>(PikminType.class);

    public OnionData() {
        for (PikminType type : PikminType.values()) {
            stored.put(type, 0);
            outside.put(type, 0);
            capacity.put(type, 20);
        }
    }

    @Override
    public boolean hasMainOnion() {
        return hasMainOnion;
    }

    @Override
    public void setHasMainOnion(boolean value) {
        this.hasMainOnion = value;
    }

    @Override
    public Set<PikminType> getUnlockedTypes() {
        return unlockedTypes;
    }

    @Override
    public boolean hasUnlocked(PikminType type) {
        return true;
    }

    @Override
    public void unlockType(PikminType type) {
        unlockedTypes.add(type);
    }

    @Override
    public int getCapacity(PikminType type) {
        return capacity.getOrDefault(type, 20);
    }

    @Override
    public void addCapacity(PikminType type, int amount) {
        capacity.put(type, getCapacity(type) + amount);
    }

    @Override
    public void setCapacity(PikminType type, int value) {
        capacity.put(type, Math.max(0, value));
    }

    @Override
    public int getStored(PikminType type) {
        return stored.getOrDefault(type, 0);
    }

    @Override
    public void addStored(PikminType type, int amount) {
        stored.put(type, getStored(type) + amount);
    }

    @Override
    public int getOutside(PikminType type) {
        return outside.getOrDefault(type, 0);
    }

    @Override
    public void addOutside(PikminType type, int amount) {
        outside.put(type, Math.max(0, outside.getOrDefault(type, 0) + amount));
    }

    @Override
    public void setOutside(PikminType type, int value) {
        outside.put(type, Math.max(0, value));
    }

    @Override
    public boolean canTakeOut(PikminType type, int amount) {
        return hasUnlocked(type) &&
                getStored(type) >= amount &&
                amount > 0;
    }

    @Override
    public boolean canPutIn(PikminType type, int amount) {
        return hasUnlocked(type) &&
                getOutside(type) >= amount &&
                getStored(type) + amount <= getCapacity(type) &&
                amount > 0;
    }
}
