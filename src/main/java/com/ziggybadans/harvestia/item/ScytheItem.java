package com.ziggybadans.harvestia.item;

import com.google.common.collect.ImmutableSet;
import com.ziggybadans.harvestia.Harvestia;
import com.ziggybadans.harvestia.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class ScytheItem extends ToolItem {
    public ScytheItem(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient && miner instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) miner;

            Harvestia.LOGGER.info("Scythe useOnBlock called on block: " + state.getBlock());

            if (isFoliage(state) || (isGrainCrop(state) && state.getBlock() instanceof CropBlock && ((CropBlock) state.getBlock()).isMature(state))) {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx == 0 && dz == 0) continue;

                        BlockPos currentPos = pos.add(dx, 0, dz);
                        BlockState currentState = world.getBlockState(currentPos);

                        if (currentState.getBlock() == state.getBlock() || (isGrainCrop(currentState) && ((CropBlock) currentState.getBlock()).isMature(currentState))) {
                            Harvestia.LOGGER.info("Breaking block at position: " + currentPos);
                            world.breakBlock(currentPos, true, miner);
                        }
                    }
                }

                    stack.damage(1, miner, (entity) -> entity.sendToolBreakStatus(miner.getActiveHand()));

                Harvestia.LOGGER.info("Scythe action was successful");
            } else if (isFoliageBlock(state)) {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dz = -1; dz <= 1; dz++) {
                            // Skip the middle block since it's already being broken
                            if (dx == 0 && dz == 0) continue;

                            BlockPos currentPos = pos.add(dx, dy, dz);
                            BlockState currentState = world.getBlockState(currentPos);

                            if (isFoliageBlock(currentState) && isFoliageBlock(state)) {
                                // Matched block type to break; for crops, also check if it's mature
                                Harvestia.LOGGER.info("Breaking leaves block at position: " + pos);
                                world.breakBlock(currentPos, true, miner);
                            }
                        }
                    }
                }

                // Damage the scythe
                    stack.damage(3, miner, (entity) -> entity.sendToolBreakStatus(miner.getActiveHand()));

                Harvestia.LOGGER.info("Scythe action was successful");
            }
        }
        return super.postMine(stack, world, state, pos, miner);
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

    private static final ImmutableSet<Block> EFFECTIVE_ON = ImmutableSet.<Block>builder()
            .addAll(FOLIAGE)
            .addAll(FOLIAGE_BLOCKS)
            .addAll(GRAIN_CROPS)
            .build();

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
