package com.ziggybadans.harvestia.fabric;

import com.ziggybadans.harvestia.HarvestiaExpectPlatform;
import org.quiltmc.loader.api.QuiltLoader;

import java.nio.file.Path;

public class HarvestiaExpectPlatformImpl {
    /**
     * This is our actual method to {@link HarvestiaExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return QuiltLoader.getConfigDir();
    }
}
