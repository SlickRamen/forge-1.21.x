package net.slickramen.producecraft.datagen;

import net.slickramen.producecraft.ProduceCraft;
import net.slickramen.producecraft.block.ModBlocks;
import net.slickramen.producecraft.block.custom.RiceCropBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ProduceCraft.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        makeRiceCrop(((CropBlock) ModBlocks.RICE_CROP.get()), "rice_crop_stage", "rice_crop_stage");
    }

    public void makeCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> states(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] states(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((RiceCropBlock) block).getAgeProperty()),
                ResourceLocation.fromNamespaceAndPath(ProduceCraft.MOD_ID, "block/" + textureName + state.getValue(((RiceCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    public void makeRiceCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> riceStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] riceStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((RiceCropBlock) block).getAgeProperty()),
                ResourceLocation.fromNamespaceAndPath(ProduceCraft.MOD_ID, "block/" + textureName + state.getValue(((RiceCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }
}