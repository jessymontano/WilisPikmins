package net.wili.wilispikmins.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.wili.wilispikmins.WilisPikmins;

public class ModTags {
    public static class Biomes {
        public static final TagKey<Biome> HAS_RED_PIKMIN =
                create("has_feature/red_pikmin");
        public static final TagKey<Biome> HAS_YELLOW_PIKMIN =
                create("has_feature/yellow_pikmin");
        public static final TagKey<Biome> HAS_BLUE_PIKMIN =
                create("has_feature/blue_pikmin");
        public static final TagKey<Biome> HAS_PURPLE_PIKMIN =
                create("has_feature/purple_pikmin");
        public static final TagKey<Biome> HAS_WHITE_PIKMIN =
                create("has_feature/white_pikmin");
        public static final TagKey<Biome> HAS_WINGED_PIKMIN =
                create("has_feature/winged_pikmin");
        public static final TagKey<Biome> HAS_ROCK_PIKMIN =
                create("has_feature/rock_pikmin");
        public static final TagKey<Biome> HAS_NECTAR_EGG_FOREST =
                create("has_feature/nectar_egg_forest");
        public static final TagKey<Biome> HAS_NECTAR_EGG_FLOWER =
                create("has_feature/nectar_egg_flower");

        private static TagKey<Biome> create(String name) {
            return TagKey.create(Registries.BIOME,
                    ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID, name));
        }
    }

    public static class Blocks {
        public static final TagKey<Block> GRASSY_BLOCKS =
                create("grassy_blocks");
        public static final TagKey<Block> SANDY_BLOCKS =
                create("sandy_blocks");
        public static final TagKey<Block> ROCKY_BLOCKS =
                create("rocky_blocks");
        public static final TagKey<Block> SNOWY_BLOCKS =
                create("snowy_blocks");
        public static final TagKey<Block> WATERY_BLOCKS =
                create("watery_blocks");
        public static final TagKey<Block> CAN_SPAWN_RED_PIKMIN =
                create("can_spawn_red_pikmin");
        public static final TagKey<Block> CAN_SPAWN_YELLOW_PIKMIN =
                create("can_spawn_yellow_pikmin");
        public static final TagKey<Block> CAN_SPAWN_BLUE_PIKMIN =
                create("can_spawn_blue_pikmin");
        public static final TagKey<Block> CAN_SPAWN_PURPLE_PIKMIN =
                create("can_spawn_purple_pikmin");
        public static final TagKey<Block> CAN_SPAWN_WHITE_PIKMIN =
                create("can_spawn_white_pikmin");
        public static final TagKey<Block> CAN_SPAWN_WINGED_PIKMIN =
                create("can_spawn_winged_pikmin");
        public static final TagKey<Block> CAN_SPAWN_ROCK_PIKMIN =
                create("can_spawn_rock_pikmin");

        private static TagKey<Block> create(String name) {
            return TagKey.create(Registries.BLOCK,
                    ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID, name));
        }
    }
}
