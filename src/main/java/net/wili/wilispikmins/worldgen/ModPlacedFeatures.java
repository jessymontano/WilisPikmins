package net.wili.wilispikmins.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluids;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.util.ModTags;

import java.util.List;

public class ModPlacedFeatures {

    // pikmin patches
    public static final ResourceKey<PlacedFeature> PATCH_RED_PIKMIN =
            registerKey("patch_red_pikmin");
    public static final ResourceKey<PlacedFeature> PATCH_YELLOW_PIKMIN =
            registerKey("patch_yellow_pikmin");
    public static final ResourceKey<PlacedFeature> PATCH_BLUE_PIKMIN =
            registerKey("patch_blue_pikmin");
    public static final ResourceKey<PlacedFeature> PATCH_BLUE_PIKMIN_WATER =
            registerKey("patch_blue_pikmin_water");
    public static final ResourceKey<PlacedFeature> PATCH_PURPLE_PIKMIN =
            registerKey("patch_purple_pikmin");
    public static final ResourceKey<PlacedFeature> PATCH_WHITE_PIKMIN =
            registerKey("patch_white_pikmin");
    public static final ResourceKey<PlacedFeature> PATCH_WHITE_PIKMIN_CAVE =
            registerKey("patch_white_pikmin_cave");
    public static final ResourceKey<PlacedFeature> PATCH_WINGED_PIKMIN =
            registerKey("patch_winged_pikmin");
    public static final ResourceKey<PlacedFeature> PATCH_ROCK_PIKMIN =
            registerKey("patch_rock_pikmin");
    public static final ResourceKey<PlacedFeature> PATCH_ROCK_PIKMIN_CAVE =
            registerKey("patch_rock_pikmin_cave");

    // onions
    public static final ResourceKey<PlacedFeature> RED_ONION =
            registerKey("red_onion");
    public static final ResourceKey<PlacedFeature> YELLOW_ONION =
            registerKey("yellow_onion");
    public static final ResourceKey<PlacedFeature> BLUE_ONION =
            registerKey("blue_onion");
    public static final ResourceKey<PlacedFeature> PURPLE_ONION =
            registerKey("purple_onion");
    public static final ResourceKey<PlacedFeature> WHITE_ONION =
            registerKey("white_onion");
    public static final ResourceKey<PlacedFeature> WHITE_ONION_CAVE =
            registerKey("white_onion_cave");
    public static final ResourceKey<PlacedFeature> WINGED_ONION =
            registerKey("winged_onion");
    public static final ResourceKey<PlacedFeature> ROCK_ONION =
            registerKey("rock_onion");
    public static final ResourceKey<PlacedFeature> ROCK_ONION_CAVE =
            registerKey("rock_onion_cave");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures =
                context.lookup(Registries.CONFIGURED_FEATURE);

        // pikmin patches
        Holder<ConfiguredFeature<?, ?>> redPatch = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.PATCH_RED_PIKMIN);
        Holder<ConfiguredFeature<?, ?>> yellowPatch = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.PATCH_YELLOW_PIKMIN);
        Holder<ConfiguredFeature<?, ?>> bluePatch = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.PATCH_BLUE_PIKMIN);
        Holder<ConfiguredFeature<?, ?>> bluePatchWater = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.PATCH_BLUE_PIKMIN_WATER);
        Holder<ConfiguredFeature<?, ?>> purplePatch = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.PATCH_PURPLE_PIKMIN);
        Holder<ConfiguredFeature<?, ?>> whitePatch = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.PATCH_WHITE_PIKMIN);
        Holder<ConfiguredFeature<?, ?>> whitePatchCave = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.PATCH_WHITE_PIKMIN_CAVE);
        Holder<ConfiguredFeature<?, ?>> wingedPatch = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.PATCH_WINGED_PIKMIN);
        Holder<ConfiguredFeature<?, ?>> rockPatch = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.PATCH_ROCK_PIKMIN);
        Holder<ConfiguredFeature<?, ?>> rockPatchCave = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.PATCH_ROCK_PIKMIN_CAVE);

        register(context, PATCH_RED_PIKMIN, redPatch,
                List.of(
                        CountPlacement.of(2),
                        RarityFilter.onAverageOnceEvery(32),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()
                ));
        register(context, PATCH_YELLOW_PIKMIN, yellowPatch,
                List.of(
                        CountPlacement.of(2),
                        RarityFilter.onAverageOnceEvery(32),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()
                ));
        register(context, PATCH_BLUE_PIKMIN, bluePatch,
                List.of(
                        CountPlacement.of(2),
                        RarityFilter.onAverageOnceEvery(32),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()
                ));
        register(context, PATCH_BLUE_PIKMIN_WATER, bluePatchWater,
                List.of(
                        CountPlacement.of(2),
                        RarityFilter.onAverageOnceEvery(32),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                        BiomeFilter.biome()
                ));
        register(context, PATCH_PURPLE_PIKMIN, purplePatch,
                List.of(
                        CountPlacement.of(2),
                        RarityFilter.onAverageOnceEvery(32),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()
                ));
        register(context, PATCH_WHITE_PIKMIN_CAVE, whitePatchCave,
                List.of(
                        CountPlacement.of(2),
                        RarityFilter.onAverageOnceEvery(32),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(
                                VerticalAnchor.bottom(),
                                VerticalAnchor.absolute(70)
                        ),
                        BiomeFilter.biome()
                ));
        register(context, PATCH_WHITE_PIKMIN, whitePatch,
                List.of(
                        CountPlacement.of(2),
                        RarityFilter.onAverageOnceEvery(32),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()
                ));
        register(context, PATCH_WINGED_PIKMIN, wingedPatch,
                List.of(
                        CountPlacement.of(2),
                        RarityFilter.onAverageOnceEvery(32),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()
                ));
        register(context, PATCH_ROCK_PIKMIN, rockPatch,
                List.of(
                        CountPlacement.of(2),
                        RarityFilter.onAverageOnceEvery(32),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()
                ));
        register(context, PATCH_ROCK_PIKMIN_CAVE, rockPatchCave,
                List.of(
                        CountPlacement.of(2),
                        RarityFilter.onAverageOnceEvery(32),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(
                                VerticalAnchor.bottom(),
                                VerticalAnchor.absolute(70)
                        ),
                        BiomeFilter.biome()
                ));

        // onions
        Holder<ConfiguredFeature<?, ?>> redOnion = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.RED_ONION
        );
        Holder<ConfiguredFeature<?, ?>> yellowOnion = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.YELLOW_ONION
        );
        Holder<ConfiguredFeature<?, ?>> blueOnion = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.BLUE_ONION
        );
        Holder<ConfiguredFeature<?, ?>> purpleOnion = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.PURPLE_ONION
        );
        Holder<ConfiguredFeature<?, ?>> whiteOnion = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.WHITE_ONION
        );
        Holder<ConfiguredFeature<?, ?>> whiteOnionCave = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.WHITE_ONION_CAVE
        );
        Holder<ConfiguredFeature<?, ?>> wingedOnion = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.WINGED_ONION
        );
        Holder<ConfiguredFeature<?, ?>> rockOnion = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.ROCK_ONION
        );
        Holder<ConfiguredFeature<?, ?>> rockOnionCave = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.ROCK_ONION_CAVE
        );

        register(context, RED_ONION, redOnion,
                List.of(
                        CountPlacement.of(1),
                        RarityFilter.onAverageOnceEvery(48),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BlockPredicateFilter.forPredicate(
                                BlockPredicate.allOf(
                                        BlockPredicate.ONLY_IN_AIR_PREDICATE,
                                        BlockPredicate.matchesTag(
                                                new BlockPos(0, -1, 0),
                                                ModTags.Blocks.CAN_SPAWN_RED_PIKMIN
                                        ),
                                        BlockPredicate.matchesFluids(Fluids.EMPTY)
                                )
                        ),
                        BiomeFilter.biome()
                ));
        register(context, YELLOW_ONION, yellowOnion,
                List.of(
                        RarityFilter.onAverageOnceEvery(96),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BlockPredicateFilter.forPredicate(
                                BlockPredicate.allOf(
                                        BlockPredicate.ONLY_IN_AIR_PREDICATE,
                                        BlockPredicate.matchesTag(
                                                new BlockPos(0, -1, 0),
                                                ModTags.Blocks.CAN_SPAWN_YELLOW_PIKMIN
                                        ),
                                        BlockPredicate.matchesFluids(Fluids.EMPTY)
                                )
                        ),
                        BiomeFilter.biome()
                ));
        register(context, BLUE_ONION, blueOnion,
                List.of(
                        RarityFilter.onAverageOnceEvery(96),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                        BlockPredicateFilter.forPredicate(
                                BlockPredicate.allOf(
                                        BlockPredicate.matchesTag(
                                                new BlockPos(0, -1, 0),
                                                ModTags.Blocks.CAN_SPAWN_BLUE_PIKMIN
                                        ),
                                        BlockPredicate.matchesFluids(Fluids.WATER)
                                )
                        ),
                        BiomeFilter.biome()
                ));
        register(context, PURPLE_ONION, purpleOnion,
                List.of(
                        RarityFilter.onAverageOnceEvery(96),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BlockPredicateFilter.forPredicate(
                                BlockPredicate.allOf(
                                        BlockPredicate.ONLY_IN_AIR_PREDICATE,
                                        BlockPredicate.matchesTag(
                                                new BlockPos(0, -1, 0),
                                                ModTags.Blocks.CAN_SPAWN_PURPLE_PIKMIN
                                        ),
                                        BlockPredicate.matchesFluids(Fluids.EMPTY)
                                )
                        ),
                        BiomeFilter.biome()
                ));
        register(context, WHITE_ONION, whiteOnion,
                List.of(
                        RarityFilter.onAverageOnceEvery(96),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BlockPredicateFilter.forPredicate(
                                BlockPredicate.allOf(
                                        BlockPredicate.ONLY_IN_AIR_PREDICATE,
                                        BlockPredicate.matchesTag(
                                                new BlockPos(0, -1, 0),
                                                ModTags.Blocks.CAN_SPAWN_WHITE_PIKMIN
                                        ),
                                        BlockPredicate.matchesFluids(Fluids.EMPTY)
                                )
                        ),
                        BiomeFilter.biome()
                ));
        register(context, WHITE_ONION_CAVE, whiteOnionCave,
                List.of(
                        RarityFilter.onAverageOnceEvery(96),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(
                                VerticalAnchor.bottom(),
                                VerticalAnchor.absolute(70)
                        ),
                        BlockPredicateFilter.forPredicate(
                                BlockPredicate.allOf(
                                        BlockPredicate.ONLY_IN_AIR_PREDICATE,
                                        BlockPredicate.matchesTag(
                                                new BlockPos(0, -1, 0),
                                                ModTags.Blocks.CAN_SPAWN_WHITE_PIKMIN
                                        ),
                                        BlockPredicate.matchesFluids(Fluids.EMPTY)
                                )
                        ),
                        BiomeFilter.biome()
                ));
        register(context, WINGED_ONION, wingedOnion,
                List.of(
                        RarityFilter.onAverageOnceEvery(96),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BlockPredicateFilter.forPredicate(
                                BlockPredicate.allOf(
                                        BlockPredicate.ONLY_IN_AIR_PREDICATE,
                                        BlockPredicate.matchesTag(
                                                new BlockPos(0, -1, 0),
                                                ModTags.Blocks.CAN_SPAWN_WINGED_PIKMIN
                                        ),
                                        BlockPredicate.matchesFluids(Fluids.EMPTY)
                                )
                        ),
                        BiomeFilter.biome()
                ));
        register(context, ROCK_ONION, rockOnion,
                List.of(
                        RarityFilter.onAverageOnceEvery(96),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        HeightRangePlacement.uniform(
                                VerticalAnchor.bottom(),
                                VerticalAnchor.absolute(70)
                        ),
                        BlockPredicateFilter.forPredicate(
                                BlockPredicate.allOf(
                                        BlockPredicate.ONLY_IN_AIR_PREDICATE,
                                        BlockPredicate.matchesTag(
                                                new BlockPos(0, -1, 0),
                                                ModTags.Blocks.CAN_SPAWN_ROCK_PIKMIN
                                        ),
                                        BlockPredicate.matchesFluids(Fluids.EMPTY)
                                )
                        ),
                        BiomeFilter.biome()
                ));
        register(context, ROCK_ONION_CAVE, rockOnionCave,
                List.of(
                        RarityFilter.onAverageOnceEvery(96),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(
                                VerticalAnchor.bottom(),
                                VerticalAnchor.absolute(70)
                        ),
                        BlockPredicateFilter.forPredicate(
                                BlockPredicate.allOf(
                                        BlockPredicate.ONLY_IN_AIR_PREDICATE,
                                        BlockPredicate.matchesTag(
                                                new BlockPos(0, -1, 0),
                                                ModTags.Blocks.CAN_SPAWN_ROCK_PIKMIN
                                        ),
                                        BlockPredicate.matchesFluids(Fluids.EMPTY)
                                )
                        ),
                        BiomeFilter.biome()
                ));
    }
    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(WilisPikmins.MOD_ID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
