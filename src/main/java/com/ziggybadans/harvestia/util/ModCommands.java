package com.ziggybadans.harvestia.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.ziggybadans.harvestia.HarvestiaClient;
import com.ziggybadans.harvestia.world.Season;
import com.ziggybadans.harvestia.world.SeasonColorManager;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.biome.Biome;

public class ModCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> registerSeasonCommand(dispatcher)));
    }

    private static void registerSeasonCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        // Registration for setSeason
        dispatcher.register(CommandManager.literal("setSeason")
                .requires(source -> source.hasPermissionLevel(2)) // Require OP level 2
                .then(CommandManager.argument("season", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            for (Season season : Season.values()) {
                                builder.suggest(season.name().toLowerCase());
                            }
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            String seasonName = StringArgumentType.getString(context, "season");
                            Season season = SeasonColorManager.setSeasonFromString(seasonName);

                            if (season != null) {
                                context.getSource().sendFeedback(() -> Text.literal("Season set to " + seasonName), false);

                                // Create a packet to send to all connected clients
                                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                                passedData.writeInt(season.ordinal());

                                // Send to all connected players
                                ServerCommandSource source = context.getSource();
                                MinecraftServer server = source.getServer();
                                if (server != null) {
                                    server.getPlayerManager().getPlayerList().forEach(player -> ServerPlayNetworking.send(player, HarvestiaClient.SEASON_COLOR_TOGGLE_PACKET_ID, passedData));
                                }

                                return Command.SINGLE_SUCCESS;
                            } else {
                                context.getSource().sendError(Text.literal("Invalid season: \"" + seasonName + "\""));
                                return 0;
                            }
                        }))
        );

        // Registration for getSeason
        dispatcher.register(CommandManager.literal("getSeason")
                .executes(context -> {
                    // Get the current season
                    Season season = SeasonColorManager.getCurrentSeason();
                    // Send feedback to the player
                    context.getSource().sendFeedback(() -> Text.literal("Current season is " + season.name()), false);
                    return Command.SINGLE_SUCCESS;
                })
        );

        // Registration for getTemperature
        dispatcher.register(CommandManager.literal("getTemperature")
                .executes(context -> {
                    // Get the player and current biome temperature
                    ServerCommandSource source = context.getSource();
                    // Ensure the command issuer is a player
                    if (source.getEntity() instanceof ServerPlayerEntity player) {
                        // Retrieve the biome at the player's position
                        Biome biome = player.getWorld().getBiome(player.getBlockPos()).value();

                        // Get the temperature of the biome (not taking into account the BlockPos height adjustments)
                        float adjustedTemperature = SeasonColorManager.getAdjustedBiomeTemperature(biome);

                        // Send feedback with the biome temperature to the player
                        source.sendFeedback(() -> Text.literal(String.format("The current temperature in " + biome + " is %.2f", adjustedTemperature)), false);
                    } else {
                        source.sendError(Text.literal("You need to be a player to use this command!"));
                    }
                    return Command.SINGLE_SUCCESS;
                })
        );
    }
}