package com.ziggybadans.harvestia.mixin;

import com.ziggybadans.harvestia.HarvestiaClient;
import com.ziggybadans.harvestia.registry.ModBlocks;
import com.ziggybadans.harvestia.world.Season;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
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

        world.setBlockState(pos, getBlock(state), 3);
        ci.cancel();
    }

    @Unique
    private BlockState getBlock(BlockState state) {
        if (state.getBlock().equals(Blocks.WHEAT)) {
            return ModBlocks.WHEAT_CROP.getDefaultState();
        } else {
            return Blocks.AIR.getDefaultState();
        }
    }
}
