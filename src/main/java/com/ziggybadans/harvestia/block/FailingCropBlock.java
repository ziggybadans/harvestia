package com.ziggybadans.harvestia.block;

import com.ziggybadans.harvestia.Harvestia;
import com.ziggybadans.harvestia.HarvestiaClient;
import com.ziggybadans.harvestia.registry.CropConditionRegistry;
import com.ziggybadans.harvestia.util.CropGrowthUtil;
import com.ziggybadans.harvestia.world.CropConditions;
import com.ziggybadans.harvestia.world.Season;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class FailingCropBlock
        extends PlantBlock
        implements Fertilizable {

    // Block states
    public static final int MAX_FAIL_STATE = 3;
    public static final IntProperty FAIL_STATE = IntProperty.of("fail_state", 0, 3);
    public static final int MAX_AGE = 7;
    public static final IntProperty AGE = Properties.AGE_7;
    public boolean growable = false;

    public FailingCropBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState())
                .with(this.getFailStateProperty(), 0));
        Harvestia.LOGGER.info("FailingCropBlock created");
    }

    /*
    // Calculates the bounding box of the crop based on age and failure state
    private VoxelShape getVoxelShapeValues(int failState, int age) {
        Harvestia.LOGGER.info("Voxel Shape: " + ((age + 1) * 2 - (failState * 2)));
        return Block.createCuboidShape(0.0, 0.0, 0.0,
                16.0, ((age + 1) * 2 - (failState * 2)), 16.0);
    }
     */
    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 10.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)};
    /*
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getVoxelShapeValues(this.getFailState(state), this.getAge(state));
    }
     */

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return AGE_TO_SHAPE[this.getAge(state)];
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(Blocks.FARMLAND);
    }

    // Failure state getters
    protected IntProperty getFailStateProperty() {
        return FAIL_STATE;
    }

    public int getMaxFailState() {
        return MAX_FAIL_STATE;
    }

    public int getFailState(BlockState state) {
        return state.get(this.getFailStateProperty());
    }

    public BlockState withFailState(int failState) {
        return (BlockState)this.getDefaultState().with(this.getFailStateProperty(), failState);
    }

    public final boolean isFailed(BlockState blockState) {
        return this.getFailState(blockState) >= this.getMaxFailState();
    }

    // Age getters
    protected IntProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return MAX_AGE;
    }

    public int getAge(BlockState state) {
        return state.get(this.getAgeProperty());
    }

    public BlockState withAge(int age) {
        return (BlockState)this.getDefaultState().with(this.getAgeProperty(), age);
    }

    public final boolean isMature(BlockState blockState) {
        return this.getAge(blockState) >= this.getMaxAge();
    }

    // Random tick calculations
    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        MinecraftClient client = MinecraftClient.getInstance();
        Season currentSeason = HarvestiaClient.getCurrentClientSeason(client);
        CropConditions cropConditions = CropConditionRegistry.getConditionsForCrop(state.getBlock());

        float moistureLevel = CropGrowthUtil.calculateMoistureLevel(world, pos);
        Harvestia.LOGGER.info("Moisture (failure): " + moistureLevel);
        float temperature = CropGrowthUtil.getBiomeTemperature(world.getBiome(pos).value().getTemperature());
        Harvestia.LOGGER.info("Temperature (failure): " + temperature);
        int lightLevel = world.getLightLevel(pos.up());
        Harvestia.LOGGER.info("Light (failure): " + lightLevel);

        float seasonGrowthChance = CropGrowthUtil.calculateSeasonGrowthChance(cropConditions, currentSeason);
        Harvestia.LOGGER.info("Season growth chance (failure): " + seasonGrowthChance);
        float moistureGrowthChance = CropGrowthUtil.getMoistureGrowthChance(cropConditions, moistureLevel);
        Harvestia.LOGGER.info("Moisture growth chance (failure): " + moistureGrowthChance);
        float temperatureGrowthChance = CropGrowthUtil.getTemperatureGrowthModifier(cropConditions, temperature);
        Harvestia.LOGGER.info("Temperature growth chance (failure): " + temperatureGrowthChance);
        float lightExposureGrowthModifier = CropGrowthUtil.getLightExposureGrowthModifier(cropConditions, lightLevel);
        Harvestia.LOGGER.info("Light exposure growth chance (failure): " + lightExposureGrowthModifier);
        float hardinessGrowthModifier = cropConditions.getHardiness();
        Harvestia.LOGGER.info("Hardiness (failure): " + hardinessGrowthModifier);

        float growthChance = seasonGrowthChance * moistureGrowthChance * temperatureGrowthChance * lightExposureGrowthModifier * hardinessGrowthModifier;
        float failureChance = 0.5f * hardinessGrowthModifier;
        float randomFloat = random.nextFloat();
        Harvestia.LOGGER.info("Growth chance (failure): " + growthChance);
        Harvestia.LOGGER.info("Failure chance (failure): " + failureChance);
        Harvestia.LOGGER.info("Random float (failure): " + randomFloat);

        int i = this.getAge(state);
        Harvestia.LOGGER.info("Age (failure): " + i);
        int f = this.getFailState(state);
        Harvestia.LOGGER.info("Fail state (failure): " + f);
        /*
        if (random.nextFloat() < Math.min(growthChance, 1.0f) && !isMature(state)) {
            world.setBlockState(pos, this.withAge(i + 1), Block.NOTIFY_LISTENERS);
        } else if (random.nextFloat() > failureChance) {
            if (random.nextInt(1) > failureChance && !isFailed(state)) {
                world.setBlockState(pos, this.withFailState(f + 1), Block.NOTIFY_LISTENERS);
            }
        }
         */
        if (randomFloat > failureChance && !isFailed(state)) {
            Harvestia.LOGGER.info("Failing (failure)");
            world.setBlockState(pos, this.withFailState(f + 1), Block.NOTIFY_LISTENERS);
        }
    }

    public void applyGrowth(World world, BlockPos pos, BlockState state) {
        int j;
        int i = this.getAge(state) + this.getGrowthAmount(world);
        if (i > (j = this.getMaxAge())) {
            i = j;
        }
        world.setBlockState(pos, this.withAge(i), Block.NOTIFY_LISTENERS);
    }

    protected int getGrowthAmount(World world) {
        return MathHelper.nextInt(world.random, 2, 5);
    }

    protected static float getAvailableMoisture(Block block, BlockView world, BlockPos pos) {
        boolean bl2;
        float f = 1.0f;
        BlockPos blockPos = pos.down();
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                float g = 0.0f;
                BlockState blockState = world.getBlockState(blockPos.add(i, 0, j));
                if (blockState.isOf(Blocks.FARMLAND)) {
                    g = 1.0f;
                    if (blockState.get(FarmlandBlock.MOISTURE) > 0) {
                        g = 3.0f;
                    }
                }
                if (i != 0 || j != 0) {
                    g /= 4.0f;
                }
                f += g;
            }
        }
        BlockPos blockPos2 = pos.north();
        BlockPos blockPos3 = pos.south();
        BlockPos blockPos4 = pos.west();
        BlockPos blockPos5 = pos.east();
        boolean bl = world.getBlockState(blockPos4).isOf(block) || world.getBlockState(blockPos5).isOf(block);
        boolean bl3 = bl2 = world.getBlockState(blockPos2).isOf(block) || world.getBlockState(blockPos3).isOf(block);
        if (bl && bl2) {
            f /= 2.0f;
        } else {
            boolean bl32;
            boolean bl4 = bl32 = world.getBlockState(blockPos4.north()).isOf(block)
                    || world.getBlockState(blockPos5.north()).isOf(block)
                    || world.getBlockState(blockPos5.south()).isOf(block)
                    || world.getBlockState(blockPos4.south()).isOf(block);
            if (bl32) {
                f /= 2.0f;
            }
        }
        return f;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return (world.getBaseLightLevel(pos, 0) >= 8
                || world.isSkyVisible(pos)) && super.canPlaceAt(state, world, pos);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof RavagerEntity && world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
            world.breakBlock(pos, true, entity);
        }
        super.onEntityCollision(state, world, pos, entity);
    }

//    protected ItemConvertible getSeedsItem() {
//        return Items.WHEAT_SEEDS;
//    }

//    @Override
//    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
//        return new ItemStack(this.getSeedsItem());
//    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return !growable;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return growable;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        this.applyGrowth(world, pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FAIL_STATE);
        builder.add(AGE);
    }
}