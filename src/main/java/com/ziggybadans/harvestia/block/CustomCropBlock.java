package com.ziggybadans.harvestia.block;

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
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class CustomCropBlock extends CropBlock {
    public static final IntProperty FAIL = IntProperty.of("fail", 0, 3);
    public static final int MAX_FAIL = 3;
    public CustomCropBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState) ((BlockState) this.getStateManager().getDefaultState().with(this.getAgeProperty(), 0)));
    }

    public IntProperty getFailProperty() {
        return FAIL;
    }

    public int getMaxFail() {
        return MAX_FAIL;
    }

    public int getFail(BlockState state) {
        return state.get(this.getFailProperty());
    }

    public BlockState withFail(int fail) {
        return (BlockState) this.getDefaultState().with(this.getFailProperty(), fail);
    }

    public final boolean isFailed(BlockState blockState) {
        return this.getFail(blockState) >= this.getMaxFail();
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return !this.isMature(state) && !this.isFailed(state);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        MinecraftClient client = MinecraftClient.getInstance();
        Season currentSeason = HarvestiaClient.getCurrentClientSeason(client);
        CropConditions cropConditions = CropConditionRegistry.getConditionsForCrop(state.getBlock());

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

        float growthChance = seasonGrowthChance *
                moistureGrowthChance * temperatureGrowthChance *
                lightExposureGrowthModifier * hardinessGrowthModifier;
        float failureChance = 0.5f * hardinessGrowthModifier;
        float randomFloat = random.nextFloat();
        Harvestia.LOGGER.info("Growth chance: " + growthChance);
        Harvestia.LOGGER.info("Failure chance: " + failureChance);
        Harvestia.LOGGER.info("Random float: " + randomFloat);

        while (growthChance > 0) {
            if (randomFloat < Math.min(growthChance, 1.0f) && !this.isMature(state) && !this.isFailed(state)) {
                world.setBlockState(pos, this.withAge(this.getAge(state) + 1), 3);
                Harvestia.LOGGER.info("Grown");
            } else if (randomFloat > Math.max(failureChance, 0.0f) && !this.isMature(state) && this.isFailed(state)) {
                world.setBlockState(pos, this.withFail(this.getFail(state) + 1), 3);
                Harvestia.LOGGER.info("Failing");
            }
            growthChance -= 1.0f;
        }
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return !isFailed(state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
        builder.add(FAIL);
    }
}
