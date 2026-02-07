package net.wili.wilispikmins.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.util.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,  @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, WilisPikmins.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(ModTags.Blocks.GRASSY_BLOCKS)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.DIRT)
                .add(Blocks.PODZOL)
                .add(Blocks.MOSS_BLOCK)
                .add(Blocks.ROOTED_DIRT)
                .add(Blocks.MUD);

        tag(ModTags.Blocks.SANDY_BLOCKS)
                .add(Blocks.SAND)
                .add(Blocks.RED_SAND)
                .add(Blocks.SANDSTONE)
                .add(Blocks.RED_SANDSTONE)
                .add(Blocks.SUSPICIOUS_SAND)
                .addTag(Tags.Blocks.SANDS)
                .addTag(Tags.Blocks.SANDSTONE_BLOCKS);

        tag(ModTags.Blocks.ROCKY_BLOCKS)
                .add(Blocks.STONE)
                .add(Blocks.COBBLESTONE)
                .add(Blocks.GRAVEL)
                .add(Blocks.ANDESITE)
                .add(Blocks.GRANITE)
                .add(Blocks.DIORITE)
                .add(Blocks.TUFF)
                .add(Blocks.DEEPSLATE)
                .add(Blocks.COBBLED_DEEPSLATE)
                .add(Blocks.CALCITE)
                .add(Blocks.DRIPSTONE_BLOCK)
                .add(Blocks.BASALT)
                .add(Blocks.BLACKSTONE)
                .add(Blocks.NETHERRACK)
                .addTag(Tags.Blocks.STONES)
                .addTag(Tags.Blocks.COBBLESTONES)
                .addTag(BlockTags.BASE_STONE_OVERWORLD);

        tag(ModTags.Blocks.SNOWY_BLOCKS)
                .add(Blocks.SNOW_BLOCK)
                .add(Blocks.POWDER_SNOW)
                .add(Blocks.ICE)
                .add(Blocks.PACKED_ICE)
                .add(Blocks.BLUE_ICE)
                .add(Blocks.FROSTED_ICE);

        tag(ModTags.Blocks.WATERY_BLOCKS)
                .add(Blocks.CLAY)
                .add(Blocks.MUD)
                .add(Blocks.SAND)
                .add(Blocks.MUD_BRICKS)
                .add(Blocks.PACKED_MUD)
                .add(Blocks.GRAVEL)
                .addTag(BlockTags.CORAL_BLOCKS)
                .addTag(Tags.Blocks.GRAVELS);

        tag(ModTags.Blocks.CAN_SPAWN_RED_PIKMIN)
                .addTag(ModTags.Blocks.GRASSY_BLOCKS)
                .addTag(ModTags.Blocks.SANDY_BLOCKS)
                .add(Blocks.COARSE_DIRT)
                .add(Blocks.PODZOL)
                .add(Blocks.MOSS_BLOCK)
                .add(Blocks.ROOTED_DIRT)
                .add(Blocks.MYCELIUM)
                .add(Blocks.TERRACOTTA)
                .addOptionalTag(Tags.Blocks.SANDS.location())
                .addOptionalTag(Tags.Blocks.GRAVELS.location());

        tag(ModTags.Blocks.CAN_SPAWN_YELLOW_PIKMIN)
                .addTag(ModTags.Blocks.SANDY_BLOCKS)
                .addTag(ModTags.Blocks.ROCKY_BLOCKS)
                .add(Blocks.TERRACOTTA)
                .add(Blocks.COARSE_DIRT)
                .add(Blocks.DIRT)
                .add(Blocks.GRASS_BLOCK);

        tag(ModTags.Blocks.CAN_SPAWN_BLUE_PIKMIN)
                .addTag(ModTags.Blocks.WATERY_BLOCKS)
                .addTag(ModTags.Blocks.SANDY_BLOCKS)
                .addTag(ModTags.Blocks.GRASSY_BLOCKS)
                .add(Blocks.STONE)
                .add(Blocks.ICE);

        tag(ModTags.Blocks.CAN_SPAWN_PURPLE_PIKMIN)
                .addTag(ModTags.Blocks.ROCKY_BLOCKS)
                .addTag(ModTags.Blocks.SANDY_BLOCKS)
                .add(Blocks.TERRACOTTA)
                .add(Blocks.RED_TERRACOTTA)
                .add(Blocks.COARSE_DIRT)
                .add(Blocks.GRAVEL)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.DIRT);

        tag(ModTags.Blocks.CAN_SPAWN_WHITE_PIKMIN)
                .addTag(ModTags.Blocks.SNOWY_BLOCKS)
                .addTag(ModTags.Blocks.ROCKY_BLOCKS)
                .add(Blocks.DIRT)
                .add(Blocks.MOSS_BLOCK)
                .add(Blocks.GRAVEL)
                .add(Blocks.AMETHYST_BLOCK);

        tag(ModTags.Blocks.CAN_SPAWN_WINGED_PIKMIN)
                .addTag(ModTags.Blocks.GRASSY_BLOCKS)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.DIRT)
                .add(Blocks.MOSS_BLOCK)
                .add(Blocks.STONE)
                .add(Blocks.GRAVEL)
                .add(Blocks.STONE)
                .add(Blocks.SNOW_BLOCK)
                .add(Blocks.ICE);

        tag(ModTags.Blocks.CAN_SPAWN_ROCK_PIKMIN)
                .addTag(ModTags.Blocks.ROCKY_BLOCKS)
                .addTag(ModTags.Blocks.GRASSY_BLOCKS)
                .add(Blocks.STONE)
                .add(Blocks.ANDESITE)
                .add(Blocks.GRANITE)
                .add(Blocks.DIORITE)
                .add(Blocks.TUFF)
                .add(Blocks.DRIPSTONE_BLOCK)
                .add(Blocks.DEEPSLATE)
                .add(Blocks.BLACKSTONE)
                .add(Blocks.DIRT)
                .add(Blocks.MOSS_BLOCK)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.BASALT);
    }
}
