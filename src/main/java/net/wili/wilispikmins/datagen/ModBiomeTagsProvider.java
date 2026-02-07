package net.wili.wilispikmins.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.util.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagsProvider extends BiomeTagsProvider {
    public ModBiomeTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, WilisPikmins.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        // red pikmin biomes
        tag(ModTags.Biomes.HAS_RED_PIKMIN)
                .addTag(Tags.Biomes.IS_PLAINS)
                .addTag(Tags.Biomes.IS_HILL)
                .addTag(Tags.Biomes.IS_SPARSE_VEGETATION)
                .addTag(Tags.Biomes.IS_HOT)
                .addTag(Tags.Biomes.IS_SAVANNA)
                .addTag(Tags.Biomes.IS_FOREST)
                .addTag(Tags.Biomes.IS_DENSE_VEGETATION)
                .add(Biomes.SAVANNA)
                .add(Biomes.PLAINS)
                .add(Biomes.SAVANNA_PLATEAU)
                .add(Biomes.WINDSWEPT_SAVANNA)
                .add(Biomes.FOREST)
                .add(Biomes.BIRCH_FOREST)
                .add(Biomes.FLOWER_FOREST);

        // yellow pikmin biomes
        tag(ModTags.Biomes.HAS_YELLOW_PIKMIN)
                .addTag(Tags.Biomes.IS_DRY)
                .addTag(Tags.Biomes.IS_HOT)
                .addTag(Tags.Biomes.IS_SANDY)
                .addTag(Tags.Biomes.IS_DESERT)
                .add(Biomes.DESERT)
                .add(Biomes.BADLANDS)
                .add(Biomes.ERODED_BADLANDS)
                .add(Biomes.WOODED_BADLANDS)
                .addOptionalTag(Tags.Biomes.IS_MAGICAL.location());

        // blue pikmin biomes
        tag(ModTags.Biomes.HAS_BLUE_PIKMIN)
                .addTag(Tags.Biomes.IS_AQUATIC)
                .addTag(Tags.Biomes.IS_SWAMP)
                .addTag(Tags.Biomes.IS_WET)
                .addTag(Tags.Biomes.IS_AQUATIC_ICY)
                .add(Biomes.SWAMP)
                .add(Biomes.MANGROVE_SWAMP)
                .add(Biomes.RIVER)
                .add(Biomes.BEACH)
                .add(Biomes.SNOWY_BEACH)
                .add(Biomes.STONY_SHORE);

        // purple pikmin biomes
        tag(ModTags.Biomes.HAS_PURPLE_PIKMIN)
                .addTag(Tags.Biomes.IS_WASTELAND)
                .addTag(Tags.Biomes.IS_DRY)
                .addTag(Tags.Biomes.IS_DESERT)
                .addTag(Tags.Biomes.IS_SPOOKY)
                .add(Biomes.DRIPSTONE_CAVES)
                .add(Biomes.DEEP_DARK)
                .addOptionalTag(Tags.Biomes.IS_CAVE.location())
                .addOptionalTag(Tags.Biomes.IS_UNDERGROUND.location());

        // white pikmin biomes
        tag(ModTags.Biomes.HAS_WHITE_PIKMIN)
                .addTag(Tags.Biomes.IS_COLD)
                .addTag(Tags.Biomes.IS_SNOWY)
                .addTag(BiomeTags.IS_TAIGA)
                .addTag(Tags.Biomes.IS_CONIFEROUS_TREE)
                .add(Biomes.SNOWY_TAIGA)
                .add(Biomes.ICE_SPIKES)
                .add(Biomes.FROZEN_PEAKS)
                .add(Biomes.JAGGED_PEAKS)
                .add(Biomes.SNOWY_SLOPES)
                .add(Biomes.SNOWY_PLAINS)
                .addOptionalTag(Tags.Biomes.IS_ICY.location())
                .addOptionalTag(Tags.Biomes.IS_COLD_OVERWORLD.location());

        // winged pikmin biomes
        tag(ModTags.Biomes.HAS_WINGED_PIKMIN)
                .addTag(Tags.Biomes.IS_FLORAL)
                .addTag(Tags.Biomes.IS_LUSH)
                .addTag(Tags.Biomes.IS_MOUNTAIN)
                .addTag(Tags.Biomes.IS_MOUNTAIN_PEAK)
                .addTag(Tags.Biomes.IS_FOREST)
                .add(Biomes.CHERRY_GROVE)
                .add(Biomes.MEADOW)
                .add(Biomes.FLOWER_FOREST)
                .add(Biomes.WINDSWEPT_HILLS)
                .add(Biomes.WINDSWEPT_FOREST)
                .addOptionalTag(Tags.Biomes.IS_MAGICAL.location());

        // rock pikmin biomes
        tag(ModTags.Biomes.HAS_ROCK_PIKMIN)
                .addTag(Tags.Biomes.IS_MOUNTAIN)
                .addTag(Tags.Biomes.IS_MOUNTAIN_PEAK)
                .addTag(Tags.Biomes.IS_MOUNTAIN_SLOPE)
                .addTag(Tags.Biomes.IS_HILL)
                .add(Biomes.STONY_SHORE)
                .add(Biomes.WINDSWEPT_GRAVELLY_HILLS)
                .add(Biomes.STONY_PEAKS)
                .addOptionalTag(Tags.Biomes.IS_STONY_SHORES);

        // nectar eggs
        tag(ModTags.Biomes.HAS_NECTAR_EGG_FOREST)
                .addTag(Tags.Biomes.IS_FOREST)
                .addTag(Tags.Biomes.IS_CONIFEROUS_TREE)
                .addTag(Tags.Biomes.IS_DENSE_VEGETATION)
                .add(Biomes.FOREST)
                .add(Biomes.DARK_FOREST)
                .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
                .add(Biomes.OLD_GROWTH_PINE_TAIGA)
                .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                .add(Biomes.TAIGA)
                .add(Biomes.BIRCH_FOREST);

        tag(ModTags.Biomes.HAS_NECTAR_EGG_FLOWER)
                .addTag(Tags.Biomes.IS_FLORAL)
                .addTag(Tags.Biomes.IS_LUSH)
                .add(Biomes.FLOWER_FOREST)
                .add(Biomes.SUNFLOWER_PLAINS)
                .add(Biomes.MEADOW)
                .add(Biomes.CHERRY_GROVE);
    }
}
