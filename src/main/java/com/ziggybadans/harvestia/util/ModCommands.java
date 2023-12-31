package com.ziggybadans.harvestia.util;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

public class ModCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> registerSeasonCommand(dispatcher)));
    }

    private static void registerSeasonCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
    }
}