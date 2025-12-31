package net.wili.wilispikmins.datagen;

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
        VariantBlockStateBuilder builder = getVariantBuilder(block);

        for (PikminType type: PikminType.values()) {
            for (GrowthStage stage : GrowthStage.values()) {
                String modelName = type.getName() + "_buried_pikmin_" + stage.getName();

                ModelFile model = models()
                        .withExistingParent(modelName,
                                modLoc("block/buried_pikmin_" + stage.getName()))
                                .texture("pikmin",
                                        modLoc("block/" + type.getName() + "_buried_pikmin_" + stage.getName()))
                                        .texture("particle",
                                                modLoc("block/" + type.getName() + "_buried_pikmin_" + stage.getName()));
                builder.partialState()
                        .with(BuriedPikminBlock.PIKMIN_TYPE, type)
                        .with(BuriedPikminBlock.GROWTH_STAGE, stage)
                        .modelForState()
                        .modelFile(model)
                        .addModel();
            }
        }
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
