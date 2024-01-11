package com.ziggybadans.harvestia.forge;

import com.ziggybadans.harvestia.HarvestiaMod;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(HarvestiaMod.MOD_ID)
public class HarvestiaModForge {
    public HarvestiaModForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(HarvestiaMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        HarvestiaMod.init();
    }
}
