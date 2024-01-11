package com.ziggybadans.harvestia.util;

import com.mojang.brigadier.CommandDispatcher;
import com.ziggybadans.harvestia.world.SeasonData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.MutableComponent

public class SeasonCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("season")
                .then(Commands.literal("get")
                        .then(Commands.literal("current").executes(context -> {
                            return getCurrentSeason(context.getSource());
                        }))))
    }

    private static int getCurrentSeason(CommandSourceStack source) {
        ServerLevel serverLevel = source.getServer().overworld();
        SeasonData seasonData = SeasonData.get(serverLevel);
        source.sendSuccess();
    }
}
