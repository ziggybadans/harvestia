package com.ziggybadans.harvestia.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class ScytheItem extends TieredItem {
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
            Blocks.PITCHER_CROP
    );

    public ScytheItem(Tier pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pMiningEntity) {
        if (!pLevel.isClientSide && pMiningEntity instanceof Player player) {
            if (player.isCrouching()) {
                return super.mineBlock(pStack, pLevel, pState, pPos, pMiningEntity);
            }

            if (LEAVES.contains(pState.getBlock())) {
                areaOfEffectHarvest(pLevel, pPos, player, 1, LEAVES);
                pStack.hurtAndBreak(3, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
            } else if (FOLIAGE.contains(pState.getBlock()) || CROPS.contains(pState.getBlock())) {
                areaOfEffectHarvest(pLevel, pPos, player, 2, FOLIAGE, CROPS);
                pStack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
            }
        }
        return super.mineBlock(pStack, pLevel, pState, pPos, pMiningEntity);
    }

    private void areaOfEffectHarvest(Level world, BlockPos centerPos, Player player, int radius, Set<Block>... targets) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = (radius == 1 ? -radius : 0); y <= (radius == 1 ? radius : 0); y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos currentPos = centerPos.offset(x, y, z);
                    BlockState currentState = world.getBlockState(currentPos);
                    for (Set<Block> targetSet : targets) {
                        if (targetSet.contains(currentState.getBlock())) {
                            world.destroyBlock(currentPos, true, player);
                            break;
                        }
                    }
                }
            }
        }
    }
}
