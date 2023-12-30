package com.ziggybadans.harvestia.item;

import com.ziggybadans.harvestia.Harvestia;
import com.ziggybadans.harvestia.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class ScytheItem extends ToolItem {
    public ScytheItem(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!world.isClient()) {
            Harvestia.LOGGER.info("Running on the server side");
            BlockPos centerPos = context.getBlockPos();
            BlockState centerState = world.getBlockState(centerPos);

            Harvestia.LOGGER.info("Scythe useOnBlock called on block: " + centerState);

            if (isFoliage(centerState) || isGrainCrop(centerState)) {
                PlayerEntity player = context.getPlayer();
                ItemStack itemStack = context.getStack();

                for (int dx = -1; dx <= 1; dx++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        BlockPos pos = centerPos.add(dx, 0, dz);
                        BlockState state = world.getBlockState(pos);

                        if ((isFoliage(state) && isFoliage(centerState))
                                || (isGrainCrop(state) && isGrainCrop(centerState) && state.getBlock() instanceof CropBlock && ((CropBlock) state.getBlock()).isMature(state))) {
                            // Matched block type to break; for crops, also check if it's mature
                            Harvestia.LOGGER.info("Breaking block at position: " + pos);
                            world.breakBlock(pos, true, player);
                            if (isFoliage(state)) {
                                spawnExtraSeeds((ServerWorld) world, pos, state);
                            }
                        }
                    }
                }

                // Damage the scythe by 1
                if (player != null) {
                    itemStack.damage(1, player, (p) -> p.sendToolBreakStatus(context.getHand()));
                }

                Harvestia.LOGGER.info("Scythe action was successful");
                return ActionResult.SUCCESS;
            } else if (isFoliageBlock(centerState)) {
                PlayerEntity player = context.getPlayer();
                ItemStack itemStack = context.getStack();

                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dz = -1; dz <= 1; dz++) {
                            BlockPos pos = centerPos.add(dx, dy, dz);
                            BlockState state = world.getBlockState(pos);

                            if (isFoliageBlock(state) && isFoliageBlock(centerState)) {
                                // Matched block type to break; for crops, also check if it's mature
                                Harvestia.LOGGER.info("Breaking leaves block at position: " + pos);
                                world.breakBlock(pos, true, player);
                            }
                        }
                    }
                }

                // Damage the scythe by 3
                if (player != null) {
                    itemStack.damage(3, player, (p) -> p.sendToolBreakStatus(context.getHand()));
                }

                Harvestia.LOGGER.info("Scythe action was successful");
                return ActionResult.SUCCESS;
            }
        }

        // If the block is not foliage or a grain crop, or we're on the client side, we pass to the default implementation.
        return super.useOnBlock(context);
    }

    private void spawnExtraSeeds(ServerWorld world, BlockPos pos, BlockState state) {
        Harvestia.LOGGER.info("Spawning extra seeds at position: " + pos);
    }

    private static final Set<Block> FOLIAGE = Set.of(
            Blocks.GRASS,
            Blocks.TALL_GRASS,
            Blocks.VINE,
            Blocks.CORNFLOWER,
            Blocks.SUNFLOWER,
            Blocks.TORCHFLOWER
    );

    private static final Set<Block> FOLIAGE_BLOCKS = Set.of(
            Blocks.ACACIA_LEAVES,
            Blocks.BIRCH_LEAVES,
            Blocks.CHERRY_LEAVES,
            Blocks.JUNGLE_LEAVES,
            Blocks.AZALEA_LEAVES,
            Blocks.DARK_OAK_LEAVES,
            Blocks.FLOWERING_AZALEA_LEAVES,
            Blocks.MANGROVE_LEAVES,
            Blocks.OAK_LEAVES,
            Blocks.SPRUCE_LEAVES
    );

    private static final Set<Block> GRAIN_CROPS = Set.of(
            Blocks.WHEAT,
            ModBlocks.CORN_CROP
    );

    private boolean isFoliage(BlockState state) {
        Harvestia.LOGGER.info("Block is foliage: " + FOLIAGE.contains(state.getBlock()));
        return FOLIAGE.contains(state.getBlock());
    }

    private boolean isFoliageBlock(BlockState state) {
        Harvestia.LOGGER.info("Block is leaves: " + FOLIAGE_BLOCKS.contains(state.getBlock()));
        return FOLIAGE_BLOCKS.contains(state.getBlock());
    }

    private boolean isGrainCrop(BlockState state) {
        Harvestia.LOGGER.info("Block is grain crop: " + GRAIN_CROPS.contains(state.getBlock()));
        return GRAIN_CROPS.contains(state.getBlock());
    }
}
