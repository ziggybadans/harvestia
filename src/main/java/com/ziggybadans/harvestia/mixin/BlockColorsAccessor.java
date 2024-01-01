package com.ziggybadans.harvestia.mixin;

import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.util.collection.IdList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockColors.class)
public interface BlockColorsAccessor {
    @Accessor("providers")
    IdList<BlockColorProvider> getProviders();
}
