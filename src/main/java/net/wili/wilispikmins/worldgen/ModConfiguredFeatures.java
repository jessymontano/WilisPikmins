package net.wili.wilispikmins.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.block.ModBlocks;
import net.wili.wilispikmins.block.custom.BuriedPikminBlock;
import net.wili.wilispikmins.entity.custom.enums.GrowthStage;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import net.wili.wilispikmins.util.ModTags;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_RED_PIKMIN =
            registerKey("patch_red_pikmin");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_YELLOW_PIKMIN =
            registerKey("patch_yellow_pikmin");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_BLUE_PIKMIN =
            registerKey("patch_blue_pikmin");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_PURPLE_PIKMIN =
            registerKey("patch_purple_pikmin");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_WHITE_PIKMIN =
            registerKey("patch_white_pikmin");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_WINGED_PIKMIN =
            registerKey("patch_winged_pikmin");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_ROCK_PIKMIN =
            registerKey("patch_rock_pikmin");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        register(context, PATCH_RED_PIKMIN, Feature.RANDOM_PATCH,
                createPikminPatchConfig(PikminType.RED, 7, 4));
        register(context, PATCH_YELLOW_PIKMIN, Feature.RANDOM_PATCH,
                createPikminPatchConfig(PikminType.YELLOW, 7, 4));
        register(context, PATCH_BLUE_PIKMIN, Feature.RANDOM_PATCH,
                createPikminPatchConfig(PikminType.BLUE, 7, 4));
        register(context, PATCH_PURPLE_PIKMIN, Feature.RANDOM_PATCH,
                createPikminPatchConfig(PikminType.PURPLE, 7, 4));
        register(context, PATCH_WHITE_PIKMIN, Feature.RANDOM_PATCH,
                createPikminPatchConfig(PikminType.WHITE, 7, 4));
        register(context, PATCH_WINGED_PIKMIN, Feature.RANDOM_PATCH,
                createPikminPatchConfig(PikminType.WINGED, 7, 4));
        register(context, PATCH_ROCK_PIKMIN, Feature.RANDOM_PATCH,
                createPikminPatchConfig(PikminType.ROCK, 7, 4));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(WilisPikmins.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register (BootstapContext<ConfiguredFeature<?,?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }

    private static RandomPatchConfiguration createPikminPatchConfig(
            PikminType type, int tries, int spread) {
        TagKey<Block> spawnableTag = getSpawnableBlockTag(type);

        return new RandomPatchConfiguration(
                tries, spread, 2,
                PlacementUtils.filtered(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(
                                ModBlocks.BURIED_PIKMIN_BLOCK.get().defaultBlockState()
                                        .setValue(BuriedPikminBlock.PIKMIN_TYPE, type)
                                        .setValue(BuriedPikminBlock.GROWTH_STAGE, GrowthStage.LEAF)
                                        .setValue(BuriedPikminBlock.FACING, Direction.NORTH)
                        )
                        ),
                        BlockPredicate.allOf(
                                BlockPredicate.ONLY_IN_AIR_PREDICATE,
                                BlockPredicate.matchesTag(
                                        new BlockPos(0, -1, 0),
                                        spawnableTag
                                )
                        )
                )
        );
    }

    private static TagKey<Block> getSpawnableBlockTag(PikminType type) {
        return switch (type) {
            case RED -> ModTags.Blocks.CAN_SPAWN_RED_PIKMIN;
            case YELLOW -> ModTags.Blocks.CAN_SPAWN_YELLOW_PIKMIN;
            case BLUE -> ModTags.Blocks.CAN_SPAWN_BLUE_PIKMIN;
            case PURPLE -> ModTags.Blocks.CAN_SPAWN_PURPLE_PIKMIN;
            case WHITE -> ModTags.Blocks.CAN_SPAWN_WHITE_PIKMIN;
            case WINGED -> ModTags.Blocks.CAN_SPAWN_WINGED_PIKMIN;
            case ROCK -> ModTags.Blocks.CAN_SPAWN_ROCK_PIKMIN;
        };
    }
}
