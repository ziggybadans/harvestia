package com.ziggybadans.harvestia.block;

import com.ziggybadans.harvestia.HarvestiaClient;
import com.ziggybadans.harvestia.registry.CropConditionRegistry;
import com.ziggybadans.harvestia.util.CropGrowthUtil;
import com.ziggybadans.harvestia.world.CropConditions;
import com.ziggybadans.harvestia.world.Season;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class CustomCropBlock extends CropBlock {
    private CropConditionRegistry cropConditionRegistry;
    public CustomCropBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);

        // Retrieve the current season of the world
        MinecraftClient client = MinecraftClient.getInstance();
        Season currentSeason = HarvestiaClient.getCurrentClientSeason(client);

        // Retrieve the crop conditions from the registry
        CropConditions cropConditions = CropConditionRegistry.getConditionsForCrop(state.getBlock());

        // Calculate the moisture level
        float moistureLevel = CropGrowthUtil.calculateMoistureLevel(world, pos);

        // Calculate the growth chance modifiers
        float seasonGrowthChance = CropGrowthUtil.calculateSeasonGrowthChance(cropConditions, currentSeason);
        float moistureGrowthChance = CropGrowthUtil.getMoistureGrowthChance(cropConditions, moistureLevel);

        // Calculate the overall growth chance
        float growthChance = seasonGrowthChance * moistureGrowthChance;

        // Decide if the crop should grow based on the moisture level
        if (random.nextFloat() < growthChance) {
            applyGrowth(world, pos, state);
        }
    }
}
