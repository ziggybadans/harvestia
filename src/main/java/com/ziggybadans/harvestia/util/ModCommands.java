package com.ziggybadans.harvestia.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.ziggybadans.harvestia.world.Season;
import com.ziggybadans.harvestia.world.SeasonState;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.level.ServerWorldProperties;

public class ModCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> registerSeasonCommands(dispatcher)));
    }

    private static void registerSeasonCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("season")
                .then(CommandManager.literal("get")
                        .then(CommandManager.literal("current")
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    MinecraftServer server= source.getServer();
                                    SeasonState state = SeasonState.get(server);
                                    source.sendFeedback(() -> Text.literal("Current season is " + state.getCurrentSeason().getName()), false);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                        .then(CommandManager.literal("time")
                                .then(CommandManager.literal("days")
                                        .executes(context -> {
                                            ServerCommandSource source = context.getSource();
                                            MinecraftServer server = source.getServer();
                                            SeasonState state = SeasonState.get(server);
                                            int daysRemaining = SeasonState.SEASON_LENGTH - state.getDaysInCurrentSeason();
                                            source.sendFeedback(() -> Text.literal("Time until next season " + daysRemaining + " days"), false);
                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                                .then(CommandManager.literal("ticks")
                                        .executes(context -> {
                                            ServerCommandSource source = context.getSource();
                                            MinecraftServer server = source.getServer();
                                            SeasonState state = SeasonState.get(server);
                                            long currentTime = server.getOverworld().getLevelProperties().getTimeOfDay();
                                            long lastDayTime = state.getLastDayTime();
                                            long ticksSpentCurrentDay = currentTime - lastDayTime;

                                            // Calculate total ticks remaining until the next season
                                            long totalTicksRemaining = (SeasonState.SEASON_LENGTH * 24000 - state.getDaysInCurrentSeason() * 24000 + 24000 - ticksSpentCurrentDay);

                                            source.sendFeedback(() -> Text.literal("Time until next season " + totalTicksRemaining + " ticks"), false);
                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                        )
                        .then(CommandManager.literal("weather")
                                .then(CommandManager.literal("clear")
                                        .executes(context -> {
                                            ServerCommandSource source = context.getSource();
                                            MinecraftServer server = source.getServer();
                                            SeasonState state = SeasonState.get(server);
                                            ServerWorld world = source.getWorld();
                                            ServerWorldProperties worldProperties = (ServerWorldProperties) world.getLevelProperties();

                                            int clearTime = worldProperties.getClearWeatherTime();
                                            source.sendFeedback(() -> Text.literal("Time until rain: " + clearTime + " ticks"), false);
                                            return Command.SINGLE_SUCCESS;
                                        }))
                                .then(CommandManager.literal("rain")
                                        .executes(context -> {
                                            ServerCommandSource source = context.getSource();
                                            MinecraftServer server = source.getServer();
                                            SeasonState state = SeasonState.get(server);
                                            ServerWorld world = source.getWorld();
                                            ServerWorldProperties worldProperties = (ServerWorldProperties) world.getLevelProperties();

                                            int rainTime = worldProperties.getRainTime();
                                            source.sendFeedback(() -> Text.literal("Time until clear weather: " + rainTime + " ticks"), false);
                                            return Command.SINGLE_SUCCESS;
                                        })))
                )
                .then(CommandManager.literal("set")
                        .then(CommandManager.argument("season", StringArgumentType.word())
                                .suggests(((context, builder) -> CommandSource.suggestMatching(new String[]{"spring", "summer", "autumn", "winter"}, builder)))
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    MinecraftServer server = source.getServer();
                                    SeasonState state = SeasonState.get(server);
                                    String seasonName = StringArgumentType.getString(context, "season").toUpperCase();
                                    try {
                                        Season season = Season.valueOf(seasonName);
                                        state.setCurrentSeason(season, server);
                                        //Harvestia.LOGGER.info("(ModCommands) Set season to " + season + " on server " + server);
                                        source.sendFeedback(() -> Text.literal("Season set to " + season.getName()), false);
                                    } catch (IllegalArgumentException e) {
                                        source.sendError(Text.literal("Invalid season name. Use one of: spring, summer, autumn, winter"));
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(CommandManager.literal("pause")
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            MinecraftServer server = source.getServer();
                            SeasonState state = SeasonState.get(server);

                            if (state.isSeasonPaused()) {
                                source.sendError(Text.literal("Seasons are already paused."));
                                return 0;
                            }

                            state.setSeasonPaused(true);
                            source.sendFeedback(() -> Text.literal("Seasons have been paused."), false);
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(CommandManager.literal("resume")
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            MinecraftServer server = source.getServer();
                            SeasonState state = SeasonState.get(server);

                            if (!state.isSeasonPaused()) {
                                source.sendError(Text.literal("Seasons are not paused."));
                                return 0;
                            }

                            state.setSeasonPaused(false);
                            source.sendFeedback(() -> Text.literal("Seasons have been resumed."), false);
                            return Command.SINGLE_SUCCESS;
                        })
                )
        );
    }
}