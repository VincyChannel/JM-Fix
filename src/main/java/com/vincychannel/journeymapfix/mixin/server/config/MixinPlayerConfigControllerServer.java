package com.vincychannel.journeymapfix.mixin.server.config;

import com.google.gson.JsonObject;

import com.vincychannel.journeymapfix.access.IPermissionPropertiesAccessor;

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
     * @author Vincenzo Roberti
     * @reason Testing MC SRV CFG
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
        settings.addProperty("hideSneakingEntities",
                ((IPermissionPropertiesAccessor) props.getGlobalProperties()).getHideSneakingEntities().get());
        System.out.printf("Pex prop: " + ((IPermissionPropertiesAccessor) props.getGlobalProperties()).getHideSneakingEntities().get());

        config.add("settings", settings);
        config.addProperty("dim", this.getDimProperties(player));

        return config;
    }
}
