package com.ziggybadans.harvestia.quilt;

import com.ziggybadans.harvestia.fabriclike.HarvestiaModFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class HarvestiaModQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        HarvestiaModFabricLike.init();
    }
}
