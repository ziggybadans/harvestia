package com.ziggybadans.harvestia.fabric;

import com.ziggybadans.harvestia.fabriclike.HarvestiaModFabricLike;
import net.fabricmc.api.ModInitializer;

public class HarvestiaModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        HarvestiaModFabricLike.init();
    }
}
