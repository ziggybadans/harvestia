package com.ziggybadans.harvestia.mixin;

import com.ziggybadans.harvestia.HarvestiaClient;
import com.ziggybadans.harvestia.world.Season;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.registry.Registries;
import net.minecraft.util.collection.IdList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockColors.class)
public class BlockColorsMixin {
    @Inject(method = "create", at = @At(value = "RETURN"))
    private static void injectSeasonalColors(CallbackInfoReturnable<BlockColors> cir) {
        BlockColors blockColors = cir.getReturnValue();
        IdList<BlockColorProvider> providers = ((BlockColorsAccessor) blockColors).getProviders();

        Block[] affectedBlocks = new Block[] {
                Blocks.GRASS_BLOCK,
                Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.BIRCH_LEAVES,
                Blocks.GRASS, Blocks.TALL_GRASS, Blocks.FERN, Blocks.VINE
        };

        for (Block block : affectedBlocks) {
            int providerId = Registries.BLOCK.getRawId(block);

            // Get the existing color provider
            BlockColorProvider existingProvider = providers.get(providerId);

            if (existingProvider != null) {
                // Wrap the existing provider with custom logic
                BlockColorProvider wrappedProvider = (state, view, pos, tintIndex) -> {
                    int originalColor = existingProvider.getColor(state, view, pos, tintIndex);
                    return applySeasonalTint(originalColor, view, pos);
                };

                // Register the wrapped provider using the protected register method we can now access
                providers.set(wrappedProvider, providerId);
            }
        }
    }

    @Unique
    private static int applySeasonalTint(int originalColor, BlockRenderView view, BlockPos pos) {
        MinecraftClient client = MinecraftClient.getInstance();
        Season currentSeason = HarvestiaClient.getCurrentClientSeason(client);

        float redTint = currentSeason.getRedTint();
        float greenTint = currentSeason.getGreenTint();
        float blueTint = currentSeason.getBlueTint();

        int red = (originalColor >> 16) & 0xFF;
        int green = (originalColor >> 8) & 0xFF;
        int blue = originalColor & 0xFF;

        red = (int)(red * redTint);
        green = (int)(green * greenTint);
        blue = (int)(blue * blueTint);

        // Clamp colors and recombine
        return (0xFF000000) | (clamp(red) << 16) | (clamp(green) << 8) | clamp(blue);
    }

    @Unique
    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }


}
