package net.wili.wilispikmins.entity.custom.enums;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum GrowthStage implements StringRepresentable {
    LEAF("leaf", 1.0f),
    BUD("bud", 1.5f),
    FLOWER("flower", 2.0f);

    private final String name;
    private final float damageMultiplier;

    GrowthStage(String name, float damageMultiplier) {
        this.name = name;
        this.damageMultiplier = damageMultiplier;
    }

    public String getName() {return name;}

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }
}
