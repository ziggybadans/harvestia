package com.ziggybadans.harvestia.registry;

import com.ziggybadans.harvestia.world.CropConditions;
import com.ziggybadans.harvestia.world.Season;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class CropConditionRegistry {
    private static final Map<Block, CropConditions> conditionsMap = new HashMap<>();

    public static void registerCropConditions(Block block, CropConditions conditions) {
        conditionsMap.put(block, conditions);
    }

    public static CropConditions getConditionsForCrop(Block block) {
        return conditionsMap.getOrDefault(block, new CropConditions(EnumSet.allOf(Season.class), 1.0f, -15.0f, 40.0f, 15, 0));
    }

    public static void setupRegistry() {
        // Example registrations with seasonality and placeholder moisture values
        registerCropConditions(Blocks.WHEAT,
                new CropConditions(EnumSet.of(Season.SPRING, Season.SUMMER), 0.8f, 10.0f, 25.0f, 15, 1.5f));
        registerCropConditions(Blocks.BEETROOTS,
                new CropConditions(EnumSet.of(Season.SPRING, Season.AUTUMN), 0.6f, 16.0f, 18.0f, 13, 0.5f));
        registerCropConditions(Blocks.CARROTS,
                new CropConditions(EnumSet.of(Season.SPRING, Season.AUTUMN), 0.5f, 16.0f, 21.0f, 13, 0.75f));
        registerCropConditions(Blocks.POTATOES,
                new CropConditions(EnumSet.of(Season.SPRING, Season.SUMMER), 0.7f,  25.0f, 50.0f, 15, 1.75f));
        registerCropConditions(Blocks.MELON_STEM,
                new CropConditions(EnumSet.of(Season.SUMMER), 0.8f, 21.0f, 35.0f, 15, 1f));
        registerCropConditions(Blocks.PUMPKIN_STEM,
                new CropConditions(EnumSet.of(Season.SUMMER), 0.8f, 18.0f, 30.0f, 15, 1f));

        // Full seasonality example crops
        registerCropConditions(Blocks.BAMBOO,
                new CropConditions(EnumSet.allOf(Season.class), 1.0f, 20.0f, 50.0f, 15, 2f)); // Bamboo can grow in all seasons
        registerCropConditions(Blocks.COCOA,
                new CropConditions(EnumSet.allOf(Season.class), 0.9f, 18.0f, 32.0f, 12, 0.75f));
        registerCropConditions(Blocks.SUGAR_CANE,
                new CropConditions(EnumSet.allOf(Season.class), 1.0f, 21.0f, 50.0f, 15, 0.5f));
        registerCropConditions(Blocks.CACTUS,
                new CropConditions(EnumSet.allOf(Season.class), 0.3f, 25.0f, 50.0f, 15, 2f)); // Assuming cactus prefers less moisture
        registerCropConditions(Blocks.KELP,
                new CropConditions(EnumSet.allOf(Season.class), 1.0f, -15.0f, 15.0f, 15, 1.25f));

        // For mod blocks, make sure they're properly imported and available
        registerCropConditions(ModBlocks.SWEET_POTATO_CROP,
                new CropConditions(EnumSet.of(Season.SUMMER), 0.7f, 25.0f, 50.0f, 15, 1.5f));
        registerCropConditions(ModBlocks.CORN_CROP,
                new CropConditions(EnumSet.of(Season.SUMMER), 0.9f, 18.0f, 32.0f, 15, 1.25f));
    }
}
