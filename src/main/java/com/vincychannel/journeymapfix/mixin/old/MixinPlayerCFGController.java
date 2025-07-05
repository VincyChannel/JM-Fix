package com.vincychannel.journeymapfix.mixin.old;

import com.google.gson.JsonObject;

import com.vincychannel.journeymapfix.access.IJourneyMapClientAccessor;
import com.vincychannel.journeymapfix.access.IPermissionPropertiesAccessor;

import journeymap.client.JourneymapClient;
import journeymap.client.feature.FeatureManager;
import journeymap.common.Journeymap;
import journeymap.common.network.impl.Response;
import journeymap.common.util.PlayerConfigController;
import journeymap.server.nbt.WorldNbtIDSaveHandler;
import journeymap.server.properties.PermissionProperties;
import journeymap.server.properties.Permissions;
import journeymap.server.properties.PropertiesManager;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerConfigController.class)
public abstract class MixinPlayerCFGController {

    @Shadow protected abstract boolean canTeleport(EntityPlayerMP player);

    @Shadow protected abstract boolean canPlayerTrack(EntityPlayerMP player);

    @Shadow public abstract boolean canServerAdmin(EntityPlayerMP player);

    @Shadow protected abstract String getDimProperties(EntityPlayerMP player);

    /**
     * @author Vincenzo Roberti
     * @reason Adding Hide Sneaking Entities to Server Config
     */
    @Overwrite(remap = false)
    public JsonObject getPlayerConfig(EntityPlayerMP player) {
        JsonObject config = new JsonObject();
        JsonObject settings = new JsonObject();

        PropertiesManager props = PropertiesManager.getInstance();

        if (props.getGlobalProperties().useWorldId.get() && !FMLCommonHandler.instance().getSide().isClient()) {
            WorldNbtIDSaveHandler worldSaveHandler = new WorldNbtIDSaveHandler();
            String worldID = worldSaveHandler.getWorldID();
            settings.addProperty("world_id", worldID);
        }

        settings.addProperty("can_teleport", this.canTeleport(player));
        settings.addProperty("can_track", this.canPlayerTrack(player));
        settings.addProperty("server_admin", this.canServerAdmin(player));
        settings.addProperty("hideSneakingEntities", ((IPermissionPropertiesAccessor) props).getHideSneakingEntities().get());
        config.add("settings", settings);
        config.addProperty("dim", this.getDimProperties(player));
        return config;
    }

    /**
     * @author Vincenzo Roberti
     * @reason Adding Hide Sneaking Entities to Server Config
     */
    @Overwrite(remap = false)
    public void updateClientConfigs(Response response) {
        if (response.getAsJson().get("settings") != null) {
            JsonObject settings = response.getAsJson().get("settings").getAsJsonObject();
            JourneymapClient jmClient = Journeymap.getClient();
            if (settings.get("world_id") != null) {
                jmClient.setCurrentWorldId(settings.get("world_id").getAsString());
            }

            if (settings.get("can_teleport") != null) {
                jmClient.setTeleportEnabled(settings.get("can_teleport").getAsBoolean());
            }

            if (settings.get("can_track") != null) {
                jmClient.setPlayerTrackingEnabled(settings.get("can_track").getAsBoolean());
            }

            if (settings.get("server_admin") != null) {
                jmClient.setServerAdmin(settings.get("server_admin").getAsBoolean());
            }

            if (settings.get("hideSneakingEntities") != null) {
                ((IJourneyMapClientAccessor) jmClient).journeymapfix$setHideSneakingEntities(settings.get("hideSneakingEntities").getAsBoolean());
            }
        }

            String dimProperties = response.getAsJson().get("dim").getAsString();
            PermissionProperties prop = (PermissionProperties)(new Permissions()).load(dimProperties, false);
            FeatureManager.INSTANCE.updateDimensionFeatures(prop);
    }
}
