package com.ziggybadans.harvestia.datagen;

import com.ziggybadans.harvestia.Harvestia;
import com.ziggybadans.harvestia.block.CornCropBlock;
import com.ziggybadans.harvestia.block.FailingCropBlock;
import com.ziggybadans.harvestia.block.SweetPotatoCropBlock;
import com.ziggybadans.harvestia.registry.ModBlocks;
import com.ziggybadans.harvestia.registry.ModItems;
import com.ziggybadans.harvestia.util.BlockModelGenerationHandler;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        Harvestia.LOGGER.info("Generating block state models");
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ROCKY_STONE);

        blockStateModelGenerator.registerCrop(ModBlocks.CORN_CROP, CornCropBlock.AGE, 0, 1, 2, 3, 4, 5, 6, 7, 8);
        blockStateModelGenerator.registerCrop(ModBlocks.SWEET_POTATO_CROP, SweetPotatoCropBlock.AGE, 0, 1, 2, 3);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.CORN, Models.GENERATED);
        itemModelGenerator.register(ModItems.BAKED_SWEET_POTATO, Models.GENERATED);

        itemModelGenerator.register(ModItems.WOOD_SCYTHE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.STONE_SCYTHE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.IRON_SCYTHE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.GOLD_SCYTHE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.DIAMOND_SCYTHE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.NETHERITE_SCYTHE, Models.HANDHELD);
    }
}
