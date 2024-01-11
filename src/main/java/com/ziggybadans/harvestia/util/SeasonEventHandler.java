package com.ziggybadans.harvestia.util;

import com.ziggybadans.harvestia.ModNetwork;
import com.ziggybadans.harvestia.networking.SeasonUpdatePacket;
import com.ziggybadans.harvestia.world.SeasonData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber
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
}
