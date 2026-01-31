package net.wili.wilispikmins.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public record OnionData(
        boolean hasMainOnion,
        Set<PikminType> unlockedTypes,
        Map<PikminType, Integer> stored,
        Map<PikminType, Integer> capacity,
        Map<PikminType, Integer> outside
){
   public OnionData() {
       this(false, EnumSet.allOf(PikminType.class), createDefaultMap(0), createDefaultMap(20), createDefaultMap(0));
   }

    public OnionData withHasMainOnion(boolean value) {
        return new OnionData(value, this.unlockedTypes, this.stored, this.capacity, this.outside);
    }

    public OnionData withUnlockedType(PikminType type) {
       Set<PikminType> newTypes = new HashSet<>(this.unlockedTypes);
       newTypes.add(type);
       return new OnionData(this.hasMainOnion, newTypes, this.stored, this.capacity, this.outside);
    }

    public OnionData withStored(PikminType type, int value) {
       Map<PikminType, Integer> newStored = new HashMap<>(this.stored);
       newStored.put(type, value);
       return new OnionData(this.hasMainOnion, this.unlockedTypes, newStored, this.capacity, this.outside);
    }

    public OnionData withCapacity(PikminType type, int value) {
        Map<PikminType, Integer> newCapacity = new HashMap<>(this.capacity);
        newCapacity.put(type, Math.max(0, value));
        return new OnionData(this.hasMainOnion, this.unlockedTypes, this.stored, newCapacity, this.outside);
    }

    public OnionData withOutside(PikminType type, int value) {
       Map<PikminType, Integer> newOutside = new HashMap<>(this.outside);
       newOutside.put(type, Math.max(0, value));
       return new OnionData(this.hasMainOnion, this.unlockedTypes,this.stored, this.capacity, newOutside);
    }

    public boolean hasUnlocked(PikminType type) {
       return unlockedTypes.contains(type);
    }

    public int getStored(PikminType type) {
       return stored.getOrDefault(type, 0);
    }

    public int getCapacity(PikminType type) {
       return capacity.getOrDefault(type, 20);
    }

    public int getOutside(PikminType type) {
       return outside.getOrDefault(type, 0);
    }

    public boolean canTakeOut(PikminType type, int amount) {
       return hasUnlocked(type) && getStored(type) >= amount && amount > 0;
    }

    public boolean canPutIn(PikminType type, int amount) {
       return hasUnlocked(type) &&
               getOutside(type) >= amount &&
               getStored(type) + amount <= getCapacity(type) &&
               amount > 0;
    }

    public OnionData addStored(PikminType type, int amount) {
       return withStored(type, getStored(type) + amount);
    }

    public OnionData addCapacity(PikminType type, int amount) {
       return withCapacity(type, getCapacity(type) + amount);
    }

    public OnionData addOutside(PikminType type, int amount) {
       return withOutside(type, getOutside(type) + amount);
    }

    private static Map<PikminType, Integer> createDefaultMap(int defaultValue) {
       Map<PikminType, Integer> map = new HashMap<>();
       for (PikminType type : PikminType.values()) {
           map.put(type, defaultValue);
       }
       return  map;
    }

    public static final Codec<OnionData> CODEC = RecordCodecBuilder.create(onionDataInstance ->
            onionDataInstance.group(
                    Codec.BOOL.fieldOf("has_main_onion").forGetter(OnionData::hasMainOnion),
                    Codec.STRING.listOf().xmap(
                            list -> list.stream().map(PikminType::valueOf).collect(Collectors.toSet()),
                            set -> set.stream().map(Enum::name).toList()
                    ).fieldOf("unlocked_types").forGetter(OnionData::unlockedTypes),
                    Codec.unboundedMap(Codec.STRING.xmap(PikminType::valueOf, Enum::name), Codec.INT)
                            .fieldOf("stored").forGetter(OnionData::stored),
                    Codec.unboundedMap(Codec.STRING.xmap(PikminType::valueOf, Enum::name), Codec.INT)
                            .fieldOf("capacity").forGetter(OnionData::capacity),
                    Codec.unboundedMap(Codec.STRING.xmap(PikminType::valueOf, Enum::name), Codec.INT)
                            .fieldOf("outside").forGetter(OnionData::outside)
            ).apply(onionDataInstance, OnionData::new));

    public static final StreamCodec<ByteBuf, OnionData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, OnionData::hasMainOnion,
            ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.STRING_UTF8)
                    .map(list -> list.stream()
                            .map(PikminType::valueOf)
                            .collect(Collectors.toSet()),
                            set -> new ArrayList<>(set.stream()
                                    .map(Enum::name)
                                    .toList())),
            OnionData::unlockedTypes,
            createMapStreamCodec(),
            OnionData::stored,
            createMapStreamCodec(),
            OnionData::capacity,
            createMapStreamCodec(),
            OnionData::outside,
            OnionData::new
    );

    private static StreamCodec<ByteBuf, Map<PikminType, Integer>> createMapStreamCodec() {
        return new StreamCodec<>() {
            @Override
            public @NotNull Map<PikminType, Integer> decode(@NotNull ByteBuf byteBuf) {
                int size = ByteBufCodecs.VAR_INT.decode(byteBuf);
                Map<PikminType, Integer> map = new HashMap<>();
                for (int i = 0; i < size; i++) {
                    String key = ByteBufCodecs.STRING_UTF8.decode(byteBuf);
                    int value = ByteBufCodecs.INT.decode(byteBuf);
                    map.put(PikminType.valueOf(key), value);
                }
                return map;
            }

            @Override
            public void encode(@NotNull ByteBuf buf, @NotNull Map<PikminType, Integer> map) {
                ByteBufCodecs.VAR_INT.encode(buf, map.size());
                for (Map.Entry<PikminType, Integer> entry : map.entrySet()) {
                    ByteBufCodecs.STRING_UTF8.encode(buf, entry.getKey().name());
                    ByteBufCodecs.INT.encode(buf, entry.getValue());
                }
            }
        };
    }
}
