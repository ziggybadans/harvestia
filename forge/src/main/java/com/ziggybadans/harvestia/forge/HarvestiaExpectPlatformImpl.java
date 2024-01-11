package com.ziggybadans.harvestia.forge;

import com.ziggybadans.harvestia.HarvestiaExpectPlatform;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class HarvestiaExpectPlatformImpl {
    /**
     * This is our actual method to {@link HarvestiaExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
