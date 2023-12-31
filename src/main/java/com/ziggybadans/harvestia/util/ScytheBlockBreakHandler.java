package com.ziggybadans.harvestia.util;

import com.ziggybadans.harvestia.item.ScytheItem;
import com.ziggybadans.harvestia.registry.ModBlocks;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;

import java.util.Set;

public class ScytheBlockBreakHandler {
    private static final Set<Block> FOLIAGE = Set.of(
            Blocks.GRASS,
            Blocks.TALL_GRASS,
            Blocks.VINE,
            Blocks.CORNFLOWER,
            Blocks.SUNFLOWER,
            Blocks.TORCHFLOWER,
            Blocks.DANDELION,
            Blocks.POPPY,
            Blocks.BLUE_ORCHID,
            Blocks.ALLIUM,
            Blocks.AZURE_BLUET,
            Blocks.RED_TULIP,
            Blocks.ORANGE_TULIP,
            Blocks.PINK_TULIP,
            Blocks.WHITE_TULIP,
            Blocks.OXEYE_DAISY,
            Blocks.LILY_OF_THE_VALLEY,
            Blocks.WITHER_ROSE,
            Blocks.LILAC,
            Blocks.ROSE_BUSH,
            Blocks.PEONY,
            Blocks.PITCHER_PLANT
    );

    private static final Set<Block> LEAVES = Set.of(
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

    private static final Set<Block> CROPS = Set.of(
            Blocks.WHEAT,
            ModBlocks.CORN_CROP,
            Blocks.PITCHER_CROP
    );

    public static void register() {
        PlayerBlockBreakEvents.AFTER.register(((world, player, pos, state, blockEntity) -> {
            ItemStack heldItem = player.getMainHandStack();
            if (!world.isClient() && heldItem.getItem() instanceof ScytheItem) {
                handleScytheBlockBreaking((ServerWorld) world, (ServerPlayerEntity) player, pos, state, heldItem);
            }
        }));
    }

    private static void handleScytheBlockBreaking(ServerWorld world, ServerPlayerEntity player, BlockPos centerPos, BlockState state, ItemStack scytheItemStack) {
        if (isFoliage(state) || isCrop(state)) {
            for (int x= -2; x <= 2; x++) {
                for (int z = -2; z<= 2; z++) {
                    if (x == 0 && z == 0) continue;

                    BlockPos currentPos = centerPos.add(x, 0, z);
                    BlockState currentState = world.getBlockState(currentPos);

                    if (!currentState.isAir() &&
                            (currentState.getBlock() == state.getBlock() || isCrop(currentState))) {
                        Integer age = currentState.contains(Properties.AGE_7) ? currentState.get(Properties.AGE_7) : null;
                        if (age == null || age >= 4) {
                            currentState.getBlock().onBreak(world, currentPos, currentState, player);
                            world.breakBlock(currentPos, true, player);
                        }
                    }
                }
            }
            scytheItemStack.damage(1, player, (entity) -> entity.sendToolBreakStatus(player.getActiveHand()));
        } else if (isLeaves(state)) {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x == 0 && y == 0 && z == 0) continue;

                        BlockPos currentPos = centerPos.add(x, y, z);
                        BlockState currentState = world.getBlockState(currentPos);

                        if (!currentState.isAir() && (currentState.getBlock() == state.getBlock())) {
                            currentState.getBlock().onBreak(world, currentPos, currentState, player);
                            world.breakBlock(currentPos, true, player);
                        }
                    }
                }
            }
            scytheItemStack.damage(1, player, (entity) -> entity.sendToolBreakStatus(player.getActiveHand()));
        }
    }

    private static boolean isFoliage(BlockState state) {
        return FOLIAGE.contains(state.getBlock());
    }

    private static boolean isLeaves(BlockState state) {
        return LEAVES.contains(state.getBlock());
    }

    private static boolean isCrop(BlockState state) {
        return CROPS.contains(state.getBlock());
    }
}
