package com.ziggybadans.harvestia.datagen;

import com.ziggybadans.harvestia.block.CornCropBlock;
import com.ziggybadans.harvestia.block.SweetPotatoCropBlock;
import com.ziggybadans.harvestia.registry.ModBlocks;
import com.ziggybadans.harvestia.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ROCKY_STONE);

        //blockStateModelGenerator.registerCrop(ModBlocks.TOMATO_CROP, TomatoCropBlock.AGE, 0, 1, 2, 3, 4, 5);
        blockStateModelGenerator.registerCrop(ModBlocks.CORN_CROP, CornCropBlock.AGE, 0, 1, 2, 3, 4, 5, 6, 7, 8);
        blockStateModelGenerator.registerCrop(ModBlocks.SWEET_POTATO_CROP, SweetPotatoCropBlock.AGE, 0, 1, 2, 3);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        //itemModelGenerator.register(ModItems.TOMATO, Models.GENERATED);
        itemModelGenerator.register(ModItems.CORN, Models.GENERATED);
        itemModelGenerator.register(ModItems.BAKED_SWEET_POTATO, Models.GENERATED);
        itemModelGenerator.register(ModItems.IRON_SCYTHE, Models.HANDHELD);
    }
}
