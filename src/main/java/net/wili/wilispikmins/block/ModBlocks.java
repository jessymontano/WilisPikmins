package net.wili.wilispikmins.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.block.custom.BuriedPikminBlock;
import net.wili.wilispikmins.block.custom.OnionBlock;
import net.wili.wilispikmins.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, WilisPikmins.MOD_ID);

    // aqui se agregan los bloques
    public static final RegistryObject<Block> BURIED_PIKMIN_BLOCK = registerBlock("buried_pikmin_block",
            () -> new BuriedPikminBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .instabreak()
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .randomTicks()
                    .noOcclusion()
                    .noCollission()
                    .sound(SoundType.GRASS)));

    public static final RegistryObject<Block> ONION_BLOCK = registerBlock("onion_block",
            () -> new OnionBlock(BlockBehaviour.Properties.copy(Blocks.BROWN_MUSHROOM_BLOCK).noOcclusion()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
