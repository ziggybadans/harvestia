package com.ziggybadans.harvestia.block;

import com.ziggybadans.harvestia.HarvestiaClient;
import com.ziggybadans.harvestia.registry.CropConditionRegistry;
import com.ziggybadans.harvestia.world.CropConditions;
import com.ziggybadans.harvestia.world.Season;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
        float moistureLevel = calculateMoistureLevel(world, pos);

        // Calculate the growth chance modifiers
        float seasonGrowthChance = calculateSeasonGrowthChance(cropConditions, currentSeason);
        float moistureGrowthChance = getMoistureGrowthChance(cropConditions, moistureLevel);

        // Calculate the overall growth chance
        float growthChance = seasonGrowthChance * moistureGrowthChance;

        // Decide if the crop should grow based on the moisture level
        if (random.nextFloat() < growthChance) {
            applyGrowth(world, pos, state);
        }
    }

    private float calculateMoistureLevel(ServerWorld world, BlockPos pos) {
        final int maxWaterDistance = 4; // Vanilla water limit for farmland hydration
        float moistureValue = 0.0f;

        for (int x = -maxWaterDistance; x <= maxWaterDistance; x++) {
            for (int z = -maxWaterDistance; z <= maxWaterDistance; z++) {
                BlockPos waterCheckPos = pos.add(x, 0, z);
                if (world.getBlockState(waterCheckPos).getBlock() == Blocks.WATER) {
                    int distance = Math.abs(x) + Math.abs(z);
                    float moistureForThisBlock = 1.0f - (float)distance / maxWaterDistance;
                    moistureValue = Math.max(moistureValue, moistureForThisBlock);
                }
            }
        }
        return moistureValue;
    }

    private float calculateSeasonGrowthChance(CropConditions cropConditions, Season currentSeason) {
        // Get the distance from the preferred season(s)
        int seasonDistance = cropConditions.getSeasonDistance(currentSeason);

        // Convert this distance to an exponential chance (decay rate can be adjusted as needed)
        double decayRate = 2.0; // The base of the exponential decay, can be tweaked for balance
        return (float) Math.pow(decayRate, -seasonDistance);
    }

    private float getMoistureGrowthChance(CropConditions cropConditions, float moistureLevel) {
        float preferredMoisture = cropConditions.getPreferredMoisture();
        float moistureDifference = Math.abs(preferredMoisture - moistureLevel);

        // Linear stifling of growth for moisture, as before
        return Math.max(0, 1.0f - moistureDifference);
    }
}
