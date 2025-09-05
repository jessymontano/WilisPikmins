package net.wili.wilispikmins.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, WilisPikmins.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // aqui se ponen los items
        withExistingParent(ModItems.RED_PIKMIN_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.BLUE_PIKMIN_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.YELLOW_PIKMIN_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(WilisPikmins.MOD_ID, "item/" + item.getId().getPath()));
    }
}
