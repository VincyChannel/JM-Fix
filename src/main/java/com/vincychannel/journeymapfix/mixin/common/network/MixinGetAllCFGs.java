package com.vincychannel.journeymapfix.mixin.common.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.vincychannel.journeymapfix.access.IPermissionPropertiesAccessor;
import com.vincychannel.journeymapfix.config.ModConfig;

import journeymap.common.network.GetAllConfigs;
import journeymap.server.nbt.WorldNbtIDSaveHandler;
import journeymap.server.properties.*;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GetAllConfigs.class)
public abstract class MixinGetAllCFGs {

    @Shadow(remap = false) protected abstract void getCommonProperties(PermissionProperties from, JsonObject to);

    /**
     * @author VincyChannel
     * @reason Added new config entry (hideSneakingEntities)
     */
    @Overwrite(remap = false)
    private JsonObject collectServerSettings() {
        JsonObject serverConfigs = new JsonObject();
        JsonArray dimensionConfigs = new JsonArray();
        JsonObject globalConfig = new JsonObject();
        JsonObject defaultDimConfig = new JsonObject();
        Integer[] dimensions = DimensionManager.getStaticDimensionIDs();
        GlobalProperties globalProperties = PropertiesManager.getInstance().getGlobalProperties();
        DefaultDimensionProperties defaultDimensionProperties = PropertiesManager.getInstance().getDefaultDimensionProperties();
        if (!FMLCommonHandler.instance().getSide().isClient()) {
            globalConfig.addProperty("useWorldId", globalProperties.useWorldId.get());
            globalConfig.addProperty("world_id", (new WorldNbtIDSaveHandler()).getWorldID());
        } else {
            globalConfig.addProperty("dimName", "global");
        }

        globalConfig.addProperty("op_can_track", globalProperties.opPlayerTrackingEnabled.get());
        globalConfig.addProperty("can_track", globalProperties.playerTrackingEnabled.get());
        globalConfig.addProperty("tracking_time", globalProperties.playerTrackingUpdateTime.get());

        if (ModConfig.server.enableHideSneakingEntities && FMLCommonHandler.instance().getSide() == Side.SERVER) {
            globalConfig.addProperty("hide_sneaking_entities", ((IPermissionPropertiesAccessor) globalProperties).getHideSneakingEntities().get());
        }

        this.getCommonProperties(globalProperties, globalConfig);
        defaultDimConfig.addProperty("enabled", defaultDimensionProperties.enabled.get());
        defaultDimConfig.addProperty("dimName", "default");
        this.getCommonProperties(defaultDimensionProperties, defaultDimConfig);
        Integer[] var8 = dimensions;
        int var9 = dimensions.length;

        for(int var10 = 0; var10 < var9; ++var10) {
            int d = var8[var10];
            JsonObject dim = new JsonObject();
            DimensionProperties dimensionProperties = PropertiesManager.getInstance().getDimProperties(d);
            dim.addProperty("enabled", dimensionProperties.enabled.get());
            dim.addProperty("dimId", d);
            dim.addProperty("dimName", DimensionManager.getProviderType(d).getName());
            this.getCommonProperties(dimensionProperties, dim);
            dimensionConfigs.add(dim);
        }

        serverConfigs.add("global", globalConfig);
        serverConfigs.add("default_dimension", defaultDimConfig);
        serverConfigs.add("dimensions", dimensionConfigs);
        return serverConfigs;
    }
}
