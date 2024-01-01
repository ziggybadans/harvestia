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

    }

    public static CropSeasonality getSeasonalityForCrop(Block block) {
        return seasonalityMap.get(block);
    }

    public static void setupSeasonalityRegistry() {
        registerSeasonalityForCrop(Blocks.WHEAT, new CropSeasonality(Season.SPRING, Season.SUMMER));
    }
}
