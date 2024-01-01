package com.ziggybadans.harvestia.mixin;

import com.ziggybadans.harvestia.HarvestiaClient;
import com.ziggybadans.harvestia.registry.CropSeasonalityRegistry;
import com.ziggybadans.harvestia.world.CropSeasonality;
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
public class CropBlockMixin extends Block {
    public CropBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    public void onRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        Season currentSeason = HarvestiaClient.getCurrentClientSeason(client);
        Block block = state.getBlock();

        CropSeasonality cropSeasonality = CropSeasonalityRegistry.getSeasonalityForCrop(block);
        if (cropSeasonality != null && !cropSeasonality.canGrowIn(currentSeason)) {
            ci.cancel();
        }
    }
}
