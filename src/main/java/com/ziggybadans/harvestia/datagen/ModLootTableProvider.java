package com.ziggybadans.harvestia.datagen;

import com.ziggybadans.harvestia.block.CornCropBlock;
import com.ziggybadans.harvestia.block.SweetPotatoCropBlock;
import com.ziggybadans.harvestia.block.TomatoCropBlock;
import com.ziggybadans.harvestia.registry.ModBlocks;
import com.ziggybadans.harvestia.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.loot.condition.AnyOfLootCondition;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.predicate.StatePredicate;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        //BlockStatePropertyLootCondition.Builder tomatoBuilder = BlockStatePropertyLootCondition.builder(ModBlocks.TOMATO_CROP).properties(StatePredicate.Builder.create()
                //.exactMatch(TomatoCropBlock.AGE, 5));
        //addDrop(ModBlocks.TOMATO_CROP, cropDrops(ModBlocks.TOMATO_CROP, ModItems.TOMATO, ModItems.TOMATO_SEEDS, tomatoBuilder));

        BlockStatePropertyLootCondition.Builder cornBuilder = BlockStatePropertyLootCondition.builder(ModBlocks.CORN_CROP).properties(StatePredicate.Builder.create()
                .exactMatch(CornCropBlock.AGE, 8));
        addDrop(ModBlocks.CORN_CROP, cropDrops(ModBlocks.CORN_CROP, ModItems.CORN, ModItems.CORN_KERNALS, cornBuilder));

        BlockStatePropertyLootCondition.Builder sweetPotatoBuilder = BlockStatePropertyLootCondition.builder(ModBlocks.SWEET_POTATO_CROP).properties(StatePredicate.Builder.create()
                .exactMatch(SweetPotatoCropBlock.AGE, 3));
        addDrop(ModBlocks.SWEET_POTATO_CROP, cropDrops(ModBlocks.SWEET_POTATO_CROP, ModItems.SWEET_POTATO, ModItems.SWEET_POTATO, sweetPotatoBuilder));
    }
}
