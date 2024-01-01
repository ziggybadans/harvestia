package com.ziggybadans.harvestia.registry;

import com.ziggybadans.harvestia.world.CropSeasonality;
import com.ziggybadans.harvestia.world.Season;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.HashMap;
import java.util.Map;

public class CropSeasonalityRegistry {
    private static final Map<Block, CropSeasonality> seasonalityMap = new HashMap<>();

    public static void registerSeasonalityForCrop(Block block, CropSeasonality seasonality) {
        seasonalityMap.put(block, seasonality);
    }

    public static CropSeasonality getSeasonalityForCrop(Block block) {
        return seasonalityMap.get(block);
    }

    public static void setupSeasonalityRegistry() {
        registerSeasonalityForCrop(Blocks.WHEAT, new CropSeasonality(Season.SPRING, Season.SUMMER));
        registerSeasonalityForCrop(Blocks.BEETROOTS, new CropSeasonality(Season.SPRING, Season.AUTUMN));
        registerSeasonalityForCrop(Blocks.CARROTS, new CropSeasonality(Season.SPRING, Season.AUTUMN));
        registerSeasonalityForCrop(Blocks.POTATOES, new CropSeasonality(Season.SPRING, Season.SUMMER));
        registerSeasonalityForCrop(Blocks.MELON_STEM, new CropSeasonality(Season.SUMMER));
        registerSeasonalityForCrop(Blocks.PUMPKIN_STEM, new CropSeasonality(Season.SUMMER));
        registerSeasonalityForCrop(Blocks.BAMBOO, new CropSeasonality(Season.SUMMER, Season.SPRING, Season.AUTUMN, Season.WINTER));
        registerSeasonalityForCrop(Blocks.COCOA, new CropSeasonality(Season.SUMMER, Season.SPRING, Season.AUTUMN, Season.WINTER));
        registerSeasonalityForCrop(Blocks.SUGAR_CANE, new CropSeasonality(Season.SUMMER, Season.SPRING, Season.AUTUMN, Season.WINTER));
        registerSeasonalityForCrop(Blocks.CACTUS, new CropSeasonality(Season.SUMMER, Season.SPRING, Season.AUTUMN, Season.WINTER));
        registerSeasonalityForCrop(Blocks.KELP, new CropSeasonality(Season.SUMMER, Season.SPRING, Season.AUTUMN, Season.WINTER));
        registerSeasonalityForCrop(ModBlocks.SWEET_POTATO_CROP, new CropSeasonality(Season.SUMMER));
        registerSeasonalityForCrop(ModBlocks.CORN_CROP, new CropSeasonality(Season.SUMMER));
    }
}
