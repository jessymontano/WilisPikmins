package net.wili.wilispikmins.entity.custom.enums;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum GrowthStage implements StringRepresentable {
    LEAF("leaf"),
    BUD("bud"),
    FLOWER("flower");

    private final String name;

    GrowthStage(String name) {
        this.name = name;
    }

    public String getName() {return name;}

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
