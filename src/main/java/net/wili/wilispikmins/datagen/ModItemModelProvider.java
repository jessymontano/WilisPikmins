package net.wili.wilispikmins.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.block.ModBlocks;
import net.wili.wilispikmins.item.ModItems;

import java.util.function.Supplier;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, WilisPikmins.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // aqui se ponen los items
        withExistingParent(getItemPath(ModItems.RED_PIKMIN_SPAWN_EGG), mcLoc("item/template_spawn_egg"));
        withExistingParent(getItemPath(ModItems.BLUE_PIKMIN_SPAWN_EGG), mcLoc("item/template_spawn_egg"));
        withExistingParent(getItemPath(ModItems.YELLOW_PIKMIN_SPAWN_EGG), mcLoc("item/template_spawn_egg"));
        withExistingParent(getItemPath(ModItems.PURPLE_PIKMIN_SPAWN_EGG), mcLoc("item/template_spawn_egg"));
        withExistingParent(getItemPath(ModItems.WHITE_PIKMIN_SPAWN_EGG), mcLoc("item/template_spawn_egg"));
        withExistingParent(getItemPath(ModItems.WINGED_PIKMIN_SPAWN_EGG), mcLoc("item/template_spawn_egg"));
        withExistingParent(getItemPath(ModItems.ROCK_PIKMIN_SPAWN_EGG), mcLoc("item/template_spawn_egg"));

        withExistingParent(getBlockPath(ModBlocks.BURIED_PIKMIN_BLOCK),
                modLoc("block/buried_pikmin_leaf"));

        withExistingParent(
                getBlockPath(ModBlocks.ONION_BLOCK),
                modLoc("block/onion_red"));

        withExistingParent(
                getItemPath(ModItems.RED_ONION_UPGRADE),
                modLoc("block/onion_red"));
        withExistingParent(
                getItemPath(ModItems.YELLOW_ONION_UPGRADE),
                modLoc("block/onion_yellow"));
        withExistingParent(
                getItemPath(ModItems.BLUE_ONION_UPGRADE),
                modLoc("block/onion_blue"));
        withExistingParent(
                getItemPath(ModItems.PURPLE_ONION_UPGRADE),
                modLoc("block/onion_purple"));
        withExistingParent(
                getItemPath(ModItems.WHITE_ONION_UPGRADE),
                modLoc("block/onion_white"));
        withExistingParent(
                getItemPath(ModItems.WINGED_ONION_UPGRADE),
                modLoc("block/onion_winged"));
        withExistingParent(
                getItemPath(ModItems.ROCK_ONION_UPGRADE),
                modLoc("block/onion_rock"));
        withExistingParent(
                getItemPath(ModItems.MAIN_ONION),
                modLoc("block/onion_main")
        );

        basicItem(ModItems.NECTAR.get());
    }

    private ItemModelBuilder simpleItem(Supplier<Item> item) {
        return withExistingParent(getItemPath(item),
                ResourceLocation.parse("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID, "item/" + getItemPath(item)));
    }

    private ItemModelBuilder simpleBlockItem(Supplier<Block> item) {
        return withExistingParent(getBlockPath(item),
                ResourceLocation.parse("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID, "item/" + getBlockPath(item)));
    }

    private String getItemPath(Supplier<Item> itemSupplier) {
        ResourceLocation location = BuiltInRegistries.ITEM.getKey(itemSupplier.get());
        return location.getPath();
    }

    private String getBlockPath(Supplier<Block> blockSupplier) {
        ResourceLocation location = BuiltInRegistries.BLOCK.getKey(blockSupplier.get());
        return location.getPath();
    }
}
