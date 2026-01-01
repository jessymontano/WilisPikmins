package net.wili.wilispikmins.datagen;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.block.ModBlocks;
import net.wili.wilispikmins.block.custom.BuriedPikminBlock;
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


    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
