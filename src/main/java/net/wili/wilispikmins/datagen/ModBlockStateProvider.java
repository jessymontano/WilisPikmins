package net.wili.wilispikmins.datagen;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.block.ModBlocks;
import net.wili.wilispikmins.block.custom.BuriedPikminBlock;
import net.wili.wilispikmins.block.custom.OnionBlock;
import net.wili.wilispikmins.entity.custom.enums.GrowthStage;
import net.wili.wilispikmins.entity.custom.enums.PikminType;

import java.lang.reflect.Array;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, WilisPikmins.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        makeBuriedPikminBlock();
        makeOnionBlock();
        makeNectarEggBlock();
    }

    private void makeOnionBlock() {
        Block block = ModBlocks.ONION_BLOCK.get();

        getVariantBuilder(block).forAllStates(state -> {
            PikminType type = state.getValue(OnionBlock.TYPE);
            boolean isMain = state.getValue(OnionBlock.MAIN);

            ModelFile model;

            if (isMain) {
                model = models().cube(
                        "onion_main",
                        modLoc("block/onion_main_down"),
                        modLoc("block/onion_main_up"),
                        modLoc("block/onion_main_north"),
                        modLoc("block/onion_main_south"),
                        modLoc("block/onion_main_east"),
                        modLoc("block/onion_main_west")
                ).texture("particle", "block/onion_main_up");
            } else {
                String typeName = type.getSerializedName();
                String modelName = "onion_" + typeName;

                model = models().cubeBottomTop(
                        modelName,
                        modLoc("block/" + typeName + "_onion_side"),
                        modLoc("block/" + typeName + "_onion_bottom"),
                        modLoc("block/" + typeName + "_onion_top")
                );
            }

            return ConfiguredModel.builder()
                    .modelFile(model)
                    .build();
        });
    }

    private void makeBuriedPikminBlock() {
        Block block = ModBlocks.BURIED_PIKMIN_BLOCK.get();
        getVariantBuilder(block).forAllStates(state -> {
            Direction dir = state.getValue(BuriedPikminBlock.FACING);
            PikminType type = state.getValue(BuriedPikminBlock.PIKMIN_TYPE);
            GrowthStage stage = state.getValue(BuriedPikminBlock.GROWTH_STAGE);

            int rotY = ((int) dir.toYRot()) % 360;

            String baseModel = "buried_pikmin_" + stage.getName();
            String variantModel = type.getName() + "_" + baseModel;

            ModelFile model = models()
                    .withExistingParent(variantModel,
                            modLoc("block/" + baseModel))
                    .texture("pikmin",
                            modLoc("block/" + variantModel))
                    .texture("particle",
                            modLoc("block/" + variantModel));
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationY(rotY)
                    .build();
        });
    }

    private void makeNectarEggBlock() {
        ModelFile.UncheckedModelFile model = new ModelFile.UncheckedModelFile(
                ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID, "block/nectar_egg")
        );

        simpleBlock(ModBlocks.NECTAR_EGG_BLOCK.get(), model);
        simpleBlockItem(ModBlocks.NECTAR_EGG_BLOCK.get(), model);
    }
}
