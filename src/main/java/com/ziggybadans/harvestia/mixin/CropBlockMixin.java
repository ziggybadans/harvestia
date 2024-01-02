package com.ziggybadans.harvestia.mixin;

import com.ziggybadans.harvestia.Harvestia;
import com.ziggybadans.harvestia.HarvestiaClient;
import com.ziggybadans.harvestia.registry.CropConditionRegistry;
import com.ziggybadans.harvestia.util.CropGrowthUtil;
import com.ziggybadans.harvestia.world.CropConditions;
import com.ziggybadans.harvestia.world.Season;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin extends Block {
    public CropBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    public void onRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        Season currentSeason = HarvestiaClient.getCurrentClientSeason(client);
        CropBlock block = (CropBlock)(Object)this;

        CropConditions cropConditions = CropConditionRegistry.getConditionsForCrop(block);

        float moistureLevel = CropGrowthUtil.calculateMoistureLevel(world, pos);
        Harvestia.LOGGER.info("Moisture: " + moistureLevel);
        float temperature = CropGrowthUtil.getBiomeTemperature(world.getBiome(pos).value().getTemperature());
        Harvestia.LOGGER.info("Temperature: " + temperature);
        int lightLevel = world.getLightLevel(pos.up());
        Harvestia.LOGGER.info("Light: " + lightLevel);

        float seasonGrowthChance = CropGrowthUtil.calculateSeasonGrowthChance(cropConditions, currentSeason);
        float moistureGrowthChance = CropGrowthUtil.getMoistureGrowthChance(cropConditions, moistureLevel);
        float temperatureGrowthChance = CropGrowthUtil.getTemperatureGrowthModifier(cropConditions, temperature);
        float lightExposureGrowthModifier = CropGrowthUtil.getLightExposureGrowthModifier(cropConditions, lightLevel);
        float hardinessGrowthModifier = cropConditions.getHardiness();

        float growthChance = seasonGrowthChance * moistureGrowthChance * temperatureGrowthChance * lightExposureGrowthModifier * hardinessGrowthModifier;
        Harvestia.LOGGER.info("Growth chance: " + growthChance);

        while (growthChance > 0) {
            if (random.nextFloat() < Math.min(growthChance, 1.0f)) {
                // Apply one instance of growth
                state = state.with(CropBlock.AGE, Math.min(state.get(CropBlock.AGE) + 1, ((CropBlock)(Object)this).getMaxAge()));
            }
            // Decrement growthChance by 1 because we've processed one simulated growth tick
            growthChance -= 1.0f;
        }

        // Set the final state on the world
        world.setBlockState(pos, state, 3);

        // Cancel the original random tick behavior, since we've handled it manually
        ci.cancel();
    }
}
