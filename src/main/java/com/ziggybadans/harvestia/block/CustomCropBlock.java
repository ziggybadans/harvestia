package com.ziggybadans.harvestia.block;

import com.ziggybadans.harvestia.registry.CropConditionRegistry;
import com.ziggybadans.harvestia.world.CropConditions;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
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

        // Retrieve the crop conditions from the registry
        CropConditions cropConditions = CropConditionRegistry.getConditionsForCrop(state.getBlock());

        // Calculate the moisture level
        float moistureLevel = calculateMoistureLevel(world, pos);

        // Decide if the crop should grow based on the moisture level
        if (shouldGrow(random, cropConditions, moistureLevel)) {
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

    private boolean shouldGrow(Random random, CropConditions cropConditions, float moistureLevel) {
        // Here we check if the moisture level is acceptable for this crop
        float preferredMoisture = cropConditions.getPreferredMoisture();
        float moistureDifference = Math.abs(preferredMoisture - moistureLevel);

        // The chance for the crop to grow could be affected by the difference in moisture level.
        // For now, we're using a simple linear model for growth reduction.
        float growthChanceModifier = 1.0f - moistureDifference; // Simple linear stifling of growth

        // Ensure the modifier is between 0 and 1
        growthChanceModifier = Math.max(0, Math.min(growthChanceModifier, 1));

        // If we're using seasonality or other conditions, we should check them here as well

        // Using randomness to determine if growth should occur; this can be modified for different growth mechanics
        return random.nextFloat() < growthChanceModifier;
    }
}
