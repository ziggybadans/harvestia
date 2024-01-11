package com.ziggybadans.harvestia.fabric;

import com.ziggybadans.harvestia.HarvestiaExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class HarvestiaExpectPlatformImpl {
    /**
     * This is our actual method to {@link HarvestiaExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
