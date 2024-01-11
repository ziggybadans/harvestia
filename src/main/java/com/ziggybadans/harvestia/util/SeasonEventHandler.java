package com.ziggybadans.harvestia.util;

import com.mojang.brigadier.CommandDispatcher;
import com.ziggybadans.harvestia.HarvestiaMod;
import com.ziggybadans.harvestia.ModNetwork;
import com.ziggybadans.harvestia.networking.SeasonUpdatePacket;
import com.ziggybadans.harvestia.world.SeasonData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(modid = HarvestiaMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SeasonEventHandler {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            ServerLevel serverLevel = serverPlayer.serverLevel();
            SeasonData seasonData = SeasonData.get(serverLevel);
            ModNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new SeasonUpdatePacket(seasonData.getCurrentSeason()));
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        SeasonCommands.register(dispatcher);
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                for (ServerLevel world : server.getAllLevels()) {
                    //System.out.println("(Harvestia): Day time is " + world.getDayTime());
                    if (world.getDayTime() % 24000 == 0) {
                        //System.out.println("(Harvestia): Season is ready to increment day");
                        SeasonData seasonData = SeasonData.get(world);
                        seasonData.incrementDay(world);
                    } else if (world.getDayTime() / 24000 % 3 == 0) {
                        //System.out.println("(Harvestia): Resetting season changing");
                        SeasonData seasonData = SeasonData.get(world);
                        seasonData.resetSeasonChanging();
                    }
                }
            }
        }
    }
}