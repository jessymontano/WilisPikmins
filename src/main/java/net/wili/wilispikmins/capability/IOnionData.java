package net.wili.wilispikmins.capability;

import net.wili.wilispikmins.entity.custom.enums.PikminType;

import java.util.Set;

public interface IOnionData {
    // datos de cebolla
    boolean hasMainOnion();
    void setHasMainOnion(boolean value);

    // datos de tipos de pikmin
    Set<PikminType> getUnlockedTypes();
    boolean hasUnlocked(PikminType type);
    void unlockType(PikminType type);

    // datos de capacidad de pikmins
    int getCapacity(PikminType type);
    void addCapacity(PikminType type, int amount);
    void setCapacity(PikminType type, int value);

    // datos de pikmins almacenados
    int getStored(PikminType type);
    void addStored(PikminType type, int amount);

    // datos de pikmins fuera
    int getOutside(PikminType type);
    void addOutside(PikminType type, int amount);
    void setOutside(PikminType type, int value);

    boolean canTakeOut(PikminType type, int amount);
    boolean canPutIn(PikminType type, int amount);
}
