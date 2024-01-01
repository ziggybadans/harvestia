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
        return conditionsMap.getOrDefault(block, new CropConditions(EnumSet.allOf(Season.class), 1.0f));
    }

    public static void setupRegistry() {
        // Example registrations with seasonality and placeholder moisture values
        registerCropConditions(Blocks.WHEAT,
                new CropConditions(EnumSet.of(Season.SPRING, Season.SUMMER), 0.8f));
        registerCropConditions(Blocks.BEETROOTS,
                new CropConditions(EnumSet.of(Season.SPRING, Season.AUTUMN), 0.6f));
        registerCropConditions(Blocks.CARROTS,
                new CropConditions(EnumSet.of(Season.SPRING, Season.AUTUMN), 0.5f));
        registerCropConditions(Blocks.POTATOES,
                new CropConditions(EnumSet.of(Season.SPRING, Season.SUMMER), 0.7f));
        registerCropConditions(Blocks.MELON_STEM,
                new CropConditions(EnumSet.of(Season.SUMMER), 0.8f));
        registerCropConditions(Blocks.PUMPKIN_STEM,
                new CropConditions(EnumSet.of(Season.SUMMER), 0.8f));

        // Full seasonality example crops
        registerCropConditions(Blocks.BAMBOO,
                new CropConditions(EnumSet.allOf(Season.class), 1.0f)); // Bamboo can grow in all seasons
        registerCropConditions(Blocks.COCOA,
                new CropConditions(EnumSet.allOf(Season.class), 0.9f));
        registerCropConditions(Blocks.SUGAR_CANE,
                new CropConditions(EnumSet.allOf(Season.class), 1.0f));
        registerCropConditions(Blocks.CACTUS,
                new CropConditions(EnumSet.allOf(Season.class), 0.3f)); // Assuming cactus prefers less moisture
        registerCropConditions(Blocks.KELP,
                new CropConditions(EnumSet.allOf(Season.class), 1.0f));

        // For mod blocks, make sure they're properly imported and available
        registerCropConditions(ModBlocks.SWEET_POTATO_CROP,
                new CropConditions(EnumSet.of(Season.SUMMER), 0.7f));
        registerCropConditions(ModBlocks.CORN_CROP,
                new CropConditions(EnumSet.of(Season.SUMMER), 0.9f));
    }
}
