package net.wili.wilispikmins.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.wili.wilispikmins.WilisPikmins;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> PATCH_RED_PIKMIN =
            registerKey("patch_red_pikmin");
    public static final ResourceKey<PlacedFeature> PATCH_YELLOW_PIKMIN =
            registerKey("patch_yellow_pikmin");
    public static final ResourceKey<PlacedFeature> PATCH_BLUE_PIKMIN =
            registerKey("patch_blue_pikmin");
    public static final ResourceKey<PlacedFeature> PATCH_PURPLE_PIKMIN =
            registerKey("patch_purple_pikmin");
    public static final ResourceKey<PlacedFeature> PATCH_WHITE_PIKMIN =
            registerKey("patch_white_pikmin");
    public static final ResourceKey<PlacedFeature> PATCH_WINGED_PIKMIN =
            registerKey("patch_winged_pikmin");
    public static final ResourceKey<PlacedFeature> PATCH_ROCK_PIKMIN =
            registerKey("patch_rock_pikmin");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures =
                context.lookup(Registries.CONFIGURED_FEATURE);

        Holder<ConfiguredFeature<?, ?>> redPatch = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.PATCH_RED_PIKMIN);
        Holder<ConfiguredFeature<?, ?>> yellowPatch = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.PATCH_YELLOW_PIKMIN);
        Holder<ConfiguredFeature<?, ?>> bluePatch = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.PATCH_BLUE_PIKMIN);
        Holder<ConfiguredFeature<?, ?>> purplePatch = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.PATCH_PURPLE_PIKMIN);
        Holder<ConfiguredFeature<?, ?>> whitePatch = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.PATCH_WHITE_PIKMIN);
        Holder<ConfiguredFeature<?, ?>> wingedPatch = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.PATCH_WINGED_PIKMIN);
        Holder<ConfiguredFeature<?, ?>> rockPatch = configuredFeatures.getOrThrow(
                ModConfiguredFeatures.PATCH_ROCK_PIKMIN);

        register(context, PATCH_RED_PIKMIN, redPatch,
                List.of(
                        RarityFilter.onAverageOnceEvery(5),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()
                ));
        register(context, PATCH_YELLOW_PIKMIN, yellowPatch,
                List.of(
                        RarityFilter.onAverageOnceEvery(5),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()
                ));
        register(context, PATCH_BLUE_PIKMIN, bluePatch,
                List.of(
                        RarityFilter.onAverageOnceEvery(5),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()
                ));
        register(context, PATCH_PURPLE_PIKMIN, purplePatch,
                List.of(
                        RarityFilter.onAverageOnceEvery(5),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()
                ));
        register(context, PATCH_WHITE_PIKMIN, whitePatch,
                List.of(
                        RarityFilter.onAverageOnceEvery(5),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()
                ));
        register(context, PATCH_WINGED_PIKMIN, wingedPatch,
                List.of(
                        RarityFilter.onAverageOnceEvery(5),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()
                ));
        register(context, PATCH_ROCK_PIKMIN, rockPatch,
                List.of(
                        RarityFilter.onAverageOnceEvery(5),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
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
