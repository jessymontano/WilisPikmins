package net.wili.wilispikmins.block;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.block.custom.BuriedPikminBlock;
import net.wili.wilispikmins.block.custom.NectarEggBlock;
import net.wili.wilispikmins.block.custom.OnionBlock;
import net.wili.wilispikmins.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(BuiltInRegistries.BLOCK, WilisPikmins.MOD_ID);

    // aqui se agregan los bloques
    public static final Supplier<Block> BURIED_PIKMIN_BLOCK = registerBlock("buried_pikmin_block",
            () -> new BuriedPikminBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .instabreak()
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .randomTicks()
                    .noOcclusion()
                    .noCollission()
                    .sound(SoundType.GRASS)));

    public static final Supplier<Block> ONION_BLOCK = registerBlock("onion_block",
            () -> new OnionBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(0.3f, 3.0f)
                    .noOcclusion()
                    .sound(SoundType.FROGLIGHT)));

    public static final Supplier<Block> NECTAR_EGG_BLOCK = registerBlock("nectar_egg_block",
            () -> new NectarEggBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .strength(0.8f, 1.0f)
                    .sound(SoundType.BAMBOO)
                    .noOcclusion()));

    private static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        Supplier<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, Supplier<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
