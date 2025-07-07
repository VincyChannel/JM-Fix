package com.vincychannel.journeymapfix.mixin.server.config;

import com.google.gson.JsonObject;

import com.vincychannel.journeymapfix.access.IPermissionPropertiesAccessor;
import com.vincychannel.journeymapfix.config.ModConfig;

import journeymap.common.util.PlayerConfigController;
import journeymap.server.nbt.WorldNbtIDSaveHandler;
import journeymap.server.properties.PropertiesManager;

import net.minecraft.entity.player.EntityPlayerMP;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = PlayerConfigController.class, remap = false)
public abstract class MixinPlayerConfigControllerServer {

    @Shadow protected abstract boolean canTeleport(EntityPlayerMP player);

    @Shadow protected abstract boolean canPlayerTrack(EntityPlayerMP player);

    @Shadow public abstract boolean canServerAdmin(EntityPlayerMP player);

    @Shadow protected abstract String getDimProperties(EntityPlayerMP player);

    /**
     * @author VincyChannel
     * @reason Sending new settings properties "hide_sneaking_entities and hide_invisible_entities"
     */
    @Overwrite
    public JsonObject getPlayerConfig(EntityPlayerMP player) {
        JsonObject config = new JsonObject();
        JsonObject settings = new JsonObject();

        PropertiesManager props = PropertiesManager.getInstance();

        WorldNbtIDSaveHandler worldSaveHandler = new WorldNbtIDSaveHandler();
        String worldID = worldSaveHandler.getWorldID();
        settings.addProperty("world_id", worldID);

        settings.addProperty("can_teleport", this.canTeleport(player));
        settings.addProperty("can_track", this.canPlayerTrack(player));
        settings.addProperty("server_admin", this.canServerAdmin(player));

        if (ModConfig.server.enableHideSneakingEntities) {
            settings.addProperty("hide_sneaking_entities",
                    ((IPermissionPropertiesAccessor) props.getGlobalProperties()).getHideSneakingEntities().get());
        }

        if (ModConfig.server.enableHideInvisiblePlayers) {
            settings.addProperty("hide_invisible_players",
                    ((IPermissionPropertiesAccessor) props.getGlobalProperties()).getHideInvisiblePlayers().get());
        }

        config.add("settings", settings);
        config.addProperty("dim", this.getDimProperties(player));

        return config;
    }
}
