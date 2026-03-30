package net.wili.wilispikmins.entity.custom.enums;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

// todos los tipos de pikmin con su nombre y color
public enum PikminType implements StringRepresentable {
    RED("red", 0xFF0000),
    YELLOW("yellow", 0xFFFF00),
    BLUE("blue", 0x0000FF),
    PURPLE("purple", 0x800080),
    WHITE("white", 0xFFFFFF),
    WINGED("winged", 0xFF69B4),
    ROCK("rock", 0x808080);

    private final String name;
    private final int color;

    PikminType(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {return name;}
    public int getColor() {return color;}

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public Component getDisplayName() {
        return Component.translatable("pikmin_type.wilispikmins." + name().toLowerCase());
    }

    public static final Codec<PikminType> CODEC = Codec.STRING.xmap(
            PikminType::valueOf,
            PikminType::name
    );

    public void writeToBuf(FriendlyByteBuf buf) {
        buf.writeEnum(this);
    }

    public static PikminType readFromBuf(FriendlyByteBuf buf) {
        return buf.readEnum(PikminType.class);
    }
}
