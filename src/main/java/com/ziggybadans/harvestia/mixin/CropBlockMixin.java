package com.ziggybadans.harvestia.mixin;

import com.ziggybadans.harvestia.Harvestia;
import com.ziggybadans.harvestia.HarvestiaClient;
import com.ziggybadans.harvestia.registry.CropConditionRegistry;
import com.ziggybadans.harvestia.registry.ModBlocks;
import com.ziggybadans.harvestia.util.CropGrowthUtil;
import com.ziggybadans.harvestia.world.CropConditions;
import com.ziggybadans.harvestia.world.Season;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
        Harvestia.LOGGER.info("Season growth chance: " + seasonGrowthChance);
        float moistureGrowthChance = CropGrowthUtil.getMoistureGrowthChance(cropConditions, moistureLevel);
        Harvestia.LOGGER.info("Moisture growth chance: " + moistureGrowthChance);
        float temperatureGrowthChance = CropGrowthUtil.getTemperatureGrowthModifier(cropConditions, temperature);
        Harvestia.LOGGER.info("Temperature growth chance: " + temperatureGrowthChance);
        float lightExposureGrowthModifier = CropGrowthUtil.getLightExposureGrowthModifier(cropConditions, lightLevel);
        Harvestia.LOGGER.info("Light exposure growth chance: " + lightExposureGrowthModifier);
        float hardinessGrowthModifier = cropConditions.getHardiness();
        Harvestia.LOGGER.info("Hardiness: " + hardinessGrowthModifier);

        float growthChance = seasonGrowthChance * moistureGrowthChance * temperatureGrowthChance * lightExposureGrowthModifier * hardinessGrowthModifier;
        float failureChance = 0.5f * hardinessGrowthModifier;
        float randomFloat = random.nextFloat();
        Harvestia.LOGGER.info("Growth chance: " + growthChance);
        Harvestia.LOGGER.info("Failure chance: " + failureChance);
        Harvestia.LOGGER.info("Random float: " + randomFloat);

        while (growthChance > 0) {
            if (randomFloat < Math.min(growthChance, 1.0f)) {
                // Apply one instance of growth
                state = state.with(CropBlock.AGE, Math.min(state.get(CropBlock.AGE) + 1, ((CropBlock)(Object)this).getMaxAge()));
                // Set the final state on the world
                world.setBlockState(pos, state, 3);
                Harvestia.LOGGER.info("Grown");
            } else if (randomFloat > failureChance) {
                world.setBlockState(pos, getBlock(state), 3);
                Harvestia.LOGGER.info("Failing");
                ci.cancel();
            }
            // Decrement growthChance by 1 because we've processed one simulated growth tick
            growthChance -= 1.0f;
        }
        // Cancel the original random tick behavior, since we've handled it manually
        ci.cancel();
    }

    @Unique
    private BlockState getBlock(BlockState state) {
        if (state.getBlock().equals(Blocks.WHEAT)) {
            Harvestia.LOGGER.info("Copying blockstate: " + state);
            Harvestia.LOGGER.info("New block will be: " + ModBlocks.FAILING_WHEAT_CROP.getStateWithProperties(state));
            return ModBlocks.FAILING_WHEAT_CROP.getStateWithProperties(state);
        }
        return Blocks.AIR.getDefaultState();
    }
}
