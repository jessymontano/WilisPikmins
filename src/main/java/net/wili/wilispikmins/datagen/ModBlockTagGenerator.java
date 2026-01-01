package net.wili.wilispikmins.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,  @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, WilisPikmins.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Blocks.CAN_SPAWN_RED_PIKMIN)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.DIRT)
                .add(Blocks.COARSE_DIRT)
                .add(Blocks.PODZOL)
                .add(Blocks.MOSS_BLOCK);
        tag(ModTags.Blocks.CAN_SPAWN_YELLOW_PIKMIN)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.DIRT)
                .add(Blocks.COARSE_DIRT)
                .add(Blocks.PODZOL)
                .add(Blocks.ROOTED_DIRT)
                .add(Blocks.MUD);
        tag(ModTags.Blocks.CAN_SPAWN_BLUE_PIKMIN)
                .add(Blocks.SAND)
                .add(Blocks.RED_SAND)
                .add(Blocks.DIRT)
                .add(Blocks.MUD)
                .add(Blocks.CLAY)
                .add(Blocks.GRAVEL);
        tag(ModTags.Blocks.CAN_SPAWN_PURPLE_PIKMIN)
                .add(Blocks.SAND)
                .add(Blocks.RED_SAND)
                .add(Blocks.TERRACOTTA)
                .add(Blocks.RED_TERRACOTTA)
                .add(Blocks.COARSE_DIRT)
                .add(Blocks.GRAVEL);
        tag(ModTags.Blocks.CAN_SPAWN_WHITE_PIKMIN)
                .add(Blocks.SNOW_BLOCK)
                .add(Blocks.POWDER_SNOW)
                .add(Blocks.DIRT)
                .add(Blocks.CALCITE)
                .add(Blocks.TUFF)
                .add(Blocks.DRIPSTONE_BLOCK)
                .add(Blocks.MOSS_BLOCK);
        tag(ModTags.Blocks.CAN_SPAWN_WINGED_PIKMIN)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.DIRT)
                .add(Blocks.MOSS_BLOCK)
                .add(Blocks.STONE)
                .add(Blocks.GRAVEL);
        tag(ModTags.Blocks.CAN_SPAWN_ROCK_PIKMIN)
                .add(Blocks.STONE)
                .add(Blocks.ANDESITE)
                .add(Blocks.GRANITE)
                .add(Blocks.DIORITE)
                .add(Blocks.TUFF)
                .add(Blocks.DRIPSTONE_BLOCK)
                .add(Blocks.DEEPSLATE);
    }
}
