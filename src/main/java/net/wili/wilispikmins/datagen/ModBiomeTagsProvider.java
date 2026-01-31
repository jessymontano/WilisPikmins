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
                .addTag(Tags.Biomes.IS_SPARSE_VEGETATION);

        // yellow pikmin biomes
        tag(ModTags.Biomes.HAS_YELLOW_PIKMIN)
                .addTag(Tags.Biomes.IS_DRY)
                .addTag(Tags.Biomes.IS_HOT)
                .addTag(Tags.Biomes.IS_SANDY);

        // blue pikmin biomes
        tag(ModTags.Biomes.HAS_BLUE_PIKMIN)
                .addTag(Tags.Biomes.IS_AQUATIC)
                .addTag(Tags.Biomes.IS_SWAMP)
                .addTag(Tags.Biomes.IS_WET);

        // purple pikmin biomes
        tag(ModTags.Biomes.HAS_PURPLE_PIKMIN)
                .addTag(Tags.Biomes.IS_WASTELAND)
                .addTag(Tags.Biomes.IS_DRY)
                .addTag(Tags.Biomes.IS_DESERT);

        // white pikmin biomes
        tag(ModTags.Biomes.HAS_WHITE_PIKMIN)
                .addTag(Tags.Biomes.IS_COLD)
                .addTag(Tags.Biomes.IS_SNOWY)
                .addTag(BiomeTags.IS_TAIGA);

        // winged pikmin biomes
        tag(ModTags.Biomes.HAS_WINGED_PIKMIN)
                .addTag(Tags.Biomes.IS_FLORAL)
                .addTag(Tags.Biomes.IS_LUSH)
                .addTag(Tags.Biomes.IS_MOUNTAIN)
                .add(Biomes.CHERRY_GROVE)
                .add(Biomes.MEADOW);

        // rock pikmin biomes
        tag(ModTags.Biomes.HAS_ROCK_PIKMIN)
                .addTag(Tags.Biomes.IS_MOUNTAIN)
                .addTag(Tags.Biomes.IS_MOUNTAIN_PEAK)
                .addTag(Tags.Biomes.IS_MOUNTAIN_SLOPE)
                .add(Biomes.STONY_SHORE)
                .add(Biomes.WINDSWEPT_GRAVELLY_HILLS);
    }
}
