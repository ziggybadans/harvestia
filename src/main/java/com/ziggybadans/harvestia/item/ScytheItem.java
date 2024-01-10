package com.ziggybadans.harvestia.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
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
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_SWORD_ACTIONS.contains(toolAction);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemStack, BlockPos pos, Player player) {
        Level world = player.level();

        if (!world.isClientSide && !player.isCreative()) {
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();

            int yRange = LEAVES.contains(block) ? 1 : 0;
            int xzRange = LEAVES.contains(block) ? 3 : 5;

            if (!player.isCrouching()) {
                for (int x = -xzRange; x <= xzRange; x++) {
                    for (int y = -yRange; y <= yRange; y++) {
                        for (int z = -xzRange; z <= xzRange; z++) {
                            if (x == 0 && y == 0 && z == 0) continue;
                            BlockPos targetPos = pos.offset(x, y, z);
                            BlockState targetState = world.getBlockState(targetPos);
                            Block targetBlock = targetState.getBlock();

                            if (LEAVES.contains(targetBlock) || (FOLIAGE.contains(targetBlock) || CROPS.contains(targetBlock))) {
                                Block.dropResources(targetState, world, targetPos, null, player, itemStack);
                                world.removeBlock(targetPos, false);
                            }
                        }
                    }
                }
                itemStack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.literal("Cuts a 3x3 area of foliage and crops, or a 3x3x3 area of leaves."));
    }
}
