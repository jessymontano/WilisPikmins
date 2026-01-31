package net.wili.wilispikmins.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.util.ModTags;

public class ModBiomeModifiers {

    // pikmin patches
    public static final ResourceKey<BiomeModifier> ADD_RED_PIKMIN =
            registerKey("add_red_pikmin");
    public static final ResourceKey<BiomeModifier> ADD_YELLOW_PIKMIN =
            registerKey("add_yellow_pikmin");
    public static final ResourceKey<BiomeModifier> ADD_BLUE_PIKMIN =
            registerKey("add_blue_pikmin");
    public static final ResourceKey<BiomeModifier> ADD_BLUE_PIKMIN_WATER =
            registerKey("add_blue_pikmin_water");
    public static final ResourceKey<BiomeModifier> ADD_PURPLE_PIKMIN =
            registerKey("add_purple_pikmin");
    public static final ResourceKey<BiomeModifier> ADD_WHITE_PIKMIN =
            registerKey("add_white_pikmin");
    public static final ResourceKey<BiomeModifier> ADD_WHITE_PIKMIN_CAVE =
            registerKey("add_white_pikmin_cave");
    public static final ResourceKey<BiomeModifier> ADD_WINGED_PIKMIN =
            registerKey("add_winged_pikmin");
    public static final ResourceKey<BiomeModifier> ADD_ROCK_PIKMIN =
            registerKey("add_rock_pikmin");
    public static final ResourceKey<BiomeModifier> ADD_ROCK_PIKMIN_CAVE =
            registerKey("add_rock_pikmin_cave");

    // onions
    public static final ResourceKey<BiomeModifier> ADD_RED_ONION =
            registerKey("add_red_onion");
    public static final ResourceKey<BiomeModifier> ADD_YELLOW_ONION =
            registerKey("add_yellow_onion");
    public static final ResourceKey<BiomeModifier> ADD_BLUE_ONION =
            registerKey("add_blue_onion");
    public static final ResourceKey<BiomeModifier> ADD_PURPLE_ONION =
            registerKey("add_purple_onion");
    public static final ResourceKey<BiomeModifier> ADD_WHITE_ONION =
            registerKey("add_white_onion");
    public static final ResourceKey<BiomeModifier> ADD_WHITE_ONION_CAVE =
            registerKey("add_white_onion_cave");
    public static final ResourceKey<BiomeModifier> ADD_WINGED_ONION =
            registerKey("add_winged_onion");
    public static final ResourceKey<BiomeModifier> ADD_ROCK_ONION =
            registerKey("add_rock_onion");
    public static final ResourceKey<BiomeModifier> ADD_ROCK_ONION_CAVE =
            registerKey("add_rock_onion_cave");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        // pikmin patches
        registerModifier(context, ADD_RED_PIKMIN, placedFeatures.getOrThrow(ModPlacedFeatures.PATCH_RED_PIKMIN), ModTags.Biomes.HAS_RED_PIKMIN);
        registerModifier(context, ADD_YELLOW_PIKMIN, placedFeatures.getOrThrow(ModPlacedFeatures.PATCH_YELLOW_PIKMIN), ModTags.Biomes.HAS_YELLOW_PIKMIN);
        registerModifier(context, ADD_BLUE_PIKMIN, placedFeatures.getOrThrow(ModPlacedFeatures.PATCH_BLUE_PIKMIN), ModTags.Biomes.HAS_BLUE_PIKMIN);
        registerModifier(context, ADD_BLUE_PIKMIN_WATER, placedFeatures.getOrThrow(ModPlacedFeatures.PATCH_BLUE_PIKMIN_WATER), ModTags.Biomes.HAS_BLUE_PIKMIN);
        registerModifier(context, ADD_PURPLE_PIKMIN, placedFeatures.getOrThrow(ModPlacedFeatures.PATCH_PURPLE_PIKMIN), ModTags.Biomes.HAS_PURPLE_PIKMIN);
        registerModifier(context, ADD_WHITE_PIKMIN, placedFeatures.getOrThrow(ModPlacedFeatures.PATCH_WHITE_PIKMIN), ModTags.Biomes.HAS_WHITE_PIKMIN);
        registerModifier(context, ADD_WHITE_PIKMIN_CAVE, placedFeatures.getOrThrow(ModPlacedFeatures.PATCH_WHITE_PIKMIN_CAVE), BiomeTags.IS_OVERWORLD);
        registerModifier(context, ADD_WINGED_PIKMIN, placedFeatures.getOrThrow(ModPlacedFeatures.PATCH_WINGED_PIKMIN), ModTags.Biomes.HAS_WINGED_PIKMIN);
        registerModifier(context, ADD_ROCK_PIKMIN, placedFeatures.getOrThrow(ModPlacedFeatures.PATCH_ROCK_PIKMIN), ModTags.Biomes.HAS_ROCK_PIKMIN);
        registerModifier(context, ADD_ROCK_PIKMIN_CAVE, placedFeatures.getOrThrow(ModPlacedFeatures.PATCH_ROCK_PIKMIN_CAVE), BiomeTags.IS_OVERWORLD);

        // onions
        registerModifier(context, ADD_RED_ONION, placedFeatures.getOrThrow(ModPlacedFeatures.RED_ONION), ModTags.Biomes.HAS_RED_PIKMIN);
        registerModifier(context, ADD_YELLOW_ONION, placedFeatures.getOrThrow(ModPlacedFeatures.YELLOW_ONION), ModTags.Biomes.HAS_YELLOW_PIKMIN);
        registerModifier(context, ADD_BLUE_ONION, placedFeatures.getOrThrow(ModPlacedFeatures.BLUE_ONION), ModTags.Biomes.HAS_BLUE_PIKMIN);
        registerModifier(context, ADD_PURPLE_ONION, placedFeatures.getOrThrow(ModPlacedFeatures.PURPLE_ONION), ModTags.Biomes.HAS_PURPLE_PIKMIN);
        registerModifier(context, ADD_WHITE_ONION, placedFeatures.getOrThrow(ModPlacedFeatures.WHITE_ONION), ModTags.Biomes.HAS_WHITE_PIKMIN);
        registerModifier(context, ADD_WHITE_ONION_CAVE, placedFeatures.getOrThrow(ModPlacedFeatures.WHITE_ONION_CAVE), BiomeTags.IS_OVERWORLD);
        registerModifier(context, ADD_WINGED_ONION, placedFeatures.getOrThrow(ModPlacedFeatures.WINGED_ONION), ModTags.Biomes.HAS_WINGED_PIKMIN);
        registerModifier(context, ADD_ROCK_ONION, placedFeatures.getOrThrow(ModPlacedFeatures.ROCK_ONION), ModTags.Biomes.HAS_ROCK_PIKMIN);
        registerModifier(context, ADD_ROCK_ONION_CAVE, placedFeatures.getOrThrow(ModPlacedFeatures.ROCK_ONION_CAVE), BiomeTags.IS_OVERWORLD);
    }
    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID, name));
    }
    private static void registerModifier(BootstrapContext<BiomeModifier> context, ResourceKey<BiomeModifier> key, Holder<PlacedFeature> feature, TagKey<Biome> biomeTag) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        context.register(key, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(biomeTag),
                HolderSet.direct(feature),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));
    }
}
