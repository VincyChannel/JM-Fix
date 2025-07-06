package com.vincychannel.journeymapfix.mixin.server;

import com.google.gson.JsonObject;

import journeymap.server.events.ForgeEvents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(ForgeEvents.class)
public abstract class MixinSendPlayerData {

    @Shadow(remap = false) protected abstract JsonObject buildJsonPlayer(EntityPlayer playerMp, boolean receiverOp);

    @Shadow(remap = false) protected abstract void sendPlayerList(List<JsonObject> allPlayers, EntityPlayerMP player);

    /**
     * @author VincyChannel
     * @reason Fixing Hide Sneaking Entities in multiplayer servers with expanded radar enabled
     */
    @Overwrite(remap = false)
    private void sendPlayerTrackingData(EntityPlayerMP entityPlayerMP) {
        List<EntityPlayerMP> serverPlayers = new ArrayList<>(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers());
        List<JsonObject> playerList = new ArrayList<>();
        if (serverPlayers != null || serverPlayers.size() > 1) {
            for(EntityPlayerMP playerMp : serverPlayers) {
                UUID playerId = playerMp.getUniqueID();
                if (!entityPlayerMP.getUniqueID().equals(playerId)) {
                    playerList.add(this.buildJsonPlayer(playerMp, false));
                }
            }

            this.sendPlayerList(playerList, entityPlayerMP);
        }
    }
}
