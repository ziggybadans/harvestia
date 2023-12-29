package com.ziggybadans.harvestia.registry;

import com.ziggybadans.harvestia.Harvestia;
import com.ziggybadans.harvestia.block.CornCropBlock;
import com.ziggybadans.harvestia.block.TomatoCropBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block ROCKY_STONE = registerBlock("rocky_stone",
            new Block(FabricBlockSettings.copyOf(Blocks.STONE)));

    public static final Block TOMATO_CROP = Registry.register(Registries.BLOCK, new Identifier(Harvestia.MOD_ID, "tomato_crop"),
            new TomatoCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT)));
    public static final Block CORN_CROP = Registry.register(Registries.BLOCK, new Identifier(Harvestia.MOD_ID, "corn_crop"),
            new CornCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT)));

    private static void addItemsToNaturalItemGroup(FabricItemGroupEntries entries) {
        entries.add(ROCKY_STONE);
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(Harvestia.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(Harvestia.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        Harvestia.LOGGER.info("Registering Mod Blocks for " + Harvestia.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(ModBlocks::addItemsToNaturalItemGroup);
    }
}
