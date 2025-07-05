package com.vincychannel.journeymapfix.mixin.client.config;

import com.google.gson.JsonObject;

import com.vincychannel.journeymapfix.access.IJourneyMapClientAccessor;

import journeymap.client.JourneymapClient;
import journeymap.client.feature.FeatureManager;
import journeymap.common.Journeymap;
import journeymap.common.network.impl.Response;
import journeymap.common.util.PlayerConfigController;
import journeymap.server.properties.PermissionProperties;
import journeymap.server.properties.Permissions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = PlayerConfigController.class, remap = false)
public abstract class MixinPlayerConfigControllerClient {

    /**
     * @author Vincenzo Roberti
     * @reason Testing Client CFG
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

            if (settings.get("hideSneakingEntities") != null) {
                System.out.printf("Hide Sneaking ENT: " + settings.get("hideSneakingEntities").getAsBoolean());
                ((IJourneyMapClientAccessor) jmClient)
                        .journeymapfix$setHideSneakingEntities(settings.get("hideSneakingEntities").getAsBoolean());
            }
        }

        String dimProperties = response.getAsJson().get("dim").getAsString();
        PermissionProperties prop = (PermissionProperties)(new Permissions()).load(dimProperties, false);
        FeatureManager.INSTANCE.updateDimensionFeatures(prop);
    }
}

