package com.ziggybadans.harvestia.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.ziggybadans.harvestia.networking.SeasonHandler;
import com.ziggybadans.harvestia.world.Season;
import com.ziggybadans.harvestia.world.SeasonData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.MutableComponent;

import java.util.Arrays;

public class SeasonCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("season")
                .then(Commands.literal("get")
                        .then(Commands.literal("current")
                                .executes(context -> {
                                    ServerLevel serverLevel = context.getSource().getLevel();
                                    SeasonData seasonData = SeasonData.get(serverLevel);

                                    context.getSource().sendSuccess(() -> Component.literal(
                                            "Current season: " + seasonData.getCurrentSeason().getName()),
                                            false);
                                    return 1;
                        }))
                        .then(Commands.literal("time")
                                .then(Commands.literal("days")
                                        .executes(context -> {
                                            ServerLevel serverLevel = context.getSource().getLevel();
                                            SeasonData seasonData = SeasonData.get(serverLevel);

                                            int daysElapsed = seasonData.getDaysElapsed();
                                            int remainingDays = seasonData.SEASON_LENGTH - daysElapsed;
                                            context.getSource().sendSuccess(() -> Component.literal(
                                                    "Days until next season: " + remainingDays),
                                                    false);
                                            return 1;
                                        }))
                                .then(Commands.literal("ticks")
                                        .executes(context -> {
                                            ServerLevel serverLevel = context.getSource().getLevel();
                                            SeasonData seasonData = SeasonData.get(serverLevel);

                                            int daysElapsed = seasonData.getDaysElapsed();
                                            int remainingDays = seasonData.SEASON_LENGTH - daysElapsed;
                                            long worldTime = serverLevel.getDayTime();
                                            long currentDayTime = worldTime % 24000;
                                            long remainingTicksToday = 24000 - currentDayTime;
                                            long totalRemainingTicks = remainingDays * 24000L + remainingTicksToday;
                                            context.getSource().sendSuccess(() -> Component.literal(
                                                    "Ticks until next season: " + totalRemainingTicks),
                                                    false);
                                            return 1;
                                        }))))
                .then(Commands.literal("set")
                        .then(Commands.argument("season", StringArgumentType.string())
                                .executes(context -> {
                                    String seasonName = StringArgumentType.getString(context, "season");
                                    ServerLevel serverLevel = context.getSource().getLevel();
                                    SeasonData seasonData = SeasonData.get(serverLevel);

                                    Season oldSeason = SeasonHandler.getCurrentSeason();
                                    try {
                                        Season newSeason = Season.valueOf(seasonName.toUpperCase());
                                        if (newSeason != oldSeason) {
                                            seasonData.setCurrentSeason(newSeason);
                                            context.getSource().sendSuccess(() -> Component.literal(
                                                            "Season set to " + newSeason.getName()),
                                                    false);
                                            return 1;
                                        } else {
                                            context.getSource().sendSuccess(() -> Component.literal(
                                                            "It is already " + newSeason.getName() + "!"),
                                                    false);
                                            return 1;
                                        }
                                    } catch (IllegalArgumentException e) {
                                        context.getSource().sendSuccess(() -> Component.literal(
                                                "Invalid season, please try again"), false);
                                        return 1;
                                    }
                                })))
                .then(Commands.literal("pause")
                        .executes(context -> {
                            context.getSource().sendSuccess(() -> Component.literal(
                                    "Season cycle paused"), false);
                            return 1;
                        }))
                .then(Commands.literal("resume")
                        .executes(context -> {
                            context.getSource().sendSuccess(() -> Component.literal(
                                    "Season cycle resumed"), false);
                            return 1;
                        })));
    }
}
