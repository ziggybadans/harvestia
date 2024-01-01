package com.ziggybadans.harvestia.network;

import net.minecraft.server.MinecraftServer;

public class ThreadLocalManager {
    private static final ThreadLocal<MinecraftServer> SERVER_THREAD_LOCAL = new ThreadLocal<>();

    public static void setServer(MinecraftServer server) {
        SERVER_THREAD_LOCAL.set(server);
    }

    public static MinecraftServer getServer() {
        return SERVER_THREAD_LOCAL.get();
    }

    public static void clear() {
        SERVER_THREAD_LOCAL.remove();
    }
}
