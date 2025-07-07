package com.vincychannel.journeymapfix.mixin.client.config;

import com.google.gson.JsonObject;

import com.vincychannel.journeymapfix.access.IJourneyMapClientAccessor;

import journeymap.client.JourneymapClient;
import journeymap.client.data.DataCache;
import journeymap.client.feature.FeatureManager;
import journeymap.common.Journeymap;
import journeymap.common.network.impl.Response;
import journeymap.common.util.PlayerConfigController;
import journeymap.server.properties.PermissionProperties;
import journeymap.server.properties.Permissions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = PlayerConfigController.class, remap = false)
public abstract class MixinPlayerCFGControllerClient {

    /**
     * @author VincyChannel
     * @reason Getting Hide Sneaking Entities setting from Server Configs
     */
    @Overwrite
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

            // Added hide sneaking entities force config from server configs
            boolean value;
            if (settings.get("hide_sneaking_entities") != null) {
                value = settings.get("hide_sneaking_entities").getAsBoolean();
                DataCache.INSTANCE.purge(); // Refresh All Cached Date, for reloading all the DrawStepEntity objects
            } else {
                value = false;
            }
            ((IJourneyMapClientAccessor) jmClient)
                    .journeymapfix$setHideSneakingEntities(value);
        }

        String dimProperties = response.getAsJson().get("dim").getAsString();
        PermissionProperties prop = (PermissionProperties)(new Permissions()).load(dimProperties, false);
        FeatureManager.INSTANCE.updateDimensionFeatures(prop);
    }
}

