package net.wili.wilispikmins.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagsProvider extends BiomeTagsProvider {
    public ModBiomeTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, WilisPikmins.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // red pikmin biomes
        tag(ModTags.Biomes.HAS_RED_PIKMIN)
                .add(Biomes.PLAINS)
                .add(Biomes.SUNFLOWER_PLAINS)
                .add(Biomes.MEADOW)
                .add(Biomes.CHERRY_GROVE);

        // yellow pikmin biomes
        tag(ModTags.Biomes.HAS_YELLOW_PIKMIN)
                .addTag(BiomeTags.IS_FOREST)
                .add(Biomes.JUNGLE)
                .add(Biomes.SPARSE_JUNGLE)
                .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
                .add(Biomes.OLD_GROWTH_PINE_TAIGA)
                .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA);

        // blue pikmin biomes
        tag(ModTags.Biomes.HAS_BLUE_PIKMIN)
                .addTag(BiomeTags.IS_RIVER)
                .addTag(BiomeTags.IS_BEACH)
                .add(Biomes.SWAMP)
                .add(Biomes.MANGROVE_SWAMP)
                .add(Biomes.OCEAN);

        // purple pikmin biomes
        tag(ModTags.Biomes.HAS_PURPLE_PIKMIN)
                .addTag(BiomeTags.IS_BADLANDS)
                .add(Biomes.DESERT)
                .add(Biomes.SAVANNA)
                .add(Biomes.SAVANNA_PLATEAU)
                .add(Biomes.WINDSWEPT_SAVANNA);

        // white pikmin biomes
        tag(ModTags.Biomes.HAS_WHITE_PIKMIN)
                .addTag(BiomeTags.IS_TAIGA)
                .add(Biomes.ICE_SPIKES)
                .add(Biomes.GROVE)
                .add(Biomes.FROZEN_PEAKS)
                .add(Biomes.SNOWY_SLOPES)
                .add(Biomes.DRIPSTONE_CAVES)
                .add(Biomes.LUSH_CAVES)
                .add(Biomes.DEEP_DARK);

        // winged pikmin biomes
        tag(ModTags.Biomes.HAS_WINGED_PIKMIN)
                .addTag(BiomeTags.IS_MOUNTAIN)
                .add(Biomes.WINDSWEPT_HILLS)
                .add(Biomes.WINDSWEPT_GRAVELLY_HILLS)
                .add(Biomes.JAGGED_PEAKS)
                .add(Biomes.STONY_PEAKS)
                .add(Biomes.FROZEN_PEAKS)
                .add(Biomes.MEADOW)
                .add(Biomes.CHERRY_GROVE);

        // rock pikmin biomes
        tag(ModTags.Biomes.HAS_ROCK_PIKMIN)
                .add(Biomes.STONY_SHORE)
                .add(Biomes.WINDSWEPT_GRAVELLY_HILLS)
                .add(Biomes.DRIPSTONE_CAVES)
                .add(Biomes.LUSH_CAVES)
                .add(Biomes.DEEP_DARK)
                .add(Biomes.STONY_PEAKS)
                .add(Biomes.JAGGED_PEAKS)
                .add(Biomes.BADLANDS)
                .add(Biomes.ERODED_BADLANDS);
    }
}
