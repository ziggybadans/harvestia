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

        float growthChance = seasonGrowthChance * moistureGrowthChance * temperatureGrowthChance * lightExposureGrowthModifier;
        Harvestia.LOGGER.info("Growth chance: " + growthChance);

        if (random.nextFloat() >= growthChance) {
            Harvestia.LOGGER.info("Cancelled tick for " + block);
            ci.cancel();
        }
    }
}
