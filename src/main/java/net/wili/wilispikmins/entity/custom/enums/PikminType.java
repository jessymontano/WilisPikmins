package net.wili.wilispikmins.entity.custom.enums;

import net.minecraft.util.StringRepresentable;

// todos los tipos de pikmin con su nombre y color
public enum PikminType implements StringRepresentable {
    RED("red", 0xFF0000),
    YELLOW("yellow", 0xFFFF00),
    BLUE("blue", 0x0000FF),
    PURPLE("purple", 0x800080),
    WHITE("white", 0xFFFFFF),
    ROCK("rock", 0x808080),
    WINGED("winged", 0xFF69B4);

    private final String name;
    private final int color;

    PikminType(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {return name;}
    public int getColor() {return color;}

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
