package com.vincychannel.journeymapfix.mixin.common.network;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.vincychannel.journeymapfix.access.IPermissionPropertiesAccessor;
import com.vincychannel.journeymapfix.config.ModConfig;

import journeymap.common.network.UpdateAllConfigs;
import journeymap.common.network.impl.CompressedPacket;
import journeymap.common.network.impl.Response;
import journeymap.common.util.PlayerConfigController;
import journeymap.server.JourneymapServer;
import journeymap.server.properties.*;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Objects;

@Mixin(UpdateAllConfigs.class)
public abstract class MixinUpdateAllCFG extends CompressedPacket {

    @Shadow(remap = false) protected abstract void updateCommonProperties(PermissionProperties to, JsonObject from);

    /**
     * @author VincyChannel
     * @reason Added new config entries (hideSneakingEntities and hideInvisiblePlayers)
     */
    @Overwrite(remap = false)
    protected JsonObject onServer(Response response) {
        EntityPlayerMP player = response.getContext().getServerHandler().player;
        if (!PlayerConfigController.getInstance().canServerAdmin(player) && !FMLCommonHandler.instance().getSide().isClient()) {
            player.sendMessage(new TextComponentString("You do not have permission to modify Journeymap's server options!"));
        } else {
            JsonObject prop = response.getAsJson();
            if (prop.get("global") != null) {
                JsonObject global = prop.get("global").getAsJsonObject();
                GlobalProperties properties = PropertiesManager.getInstance().getGlobalProperties();
                if (!FMLCommonHandler.instance().getSide().isClient()) {
                    if (global.get("useWorldId") != null) {
                        properties.useWorldId.set(global.get("useWorldId").getAsBoolean());
                    }

                    if (global.get("op_can_track") != null) {
                        properties.opPlayerTrackingEnabled.set(global.get("op_can_track").getAsBoolean());
                    }

                    if (global.get("can_track") != null) {
                        properties.playerTrackingEnabled.set(global.get("can_track").getAsBoolean());
                    }

                    if (global.get("tracking_time") != null) {
                        properties.playerTrackingUpdateTime.set(global.get("tracking_time").getAsInt());
                    }

                    if (global.get("hide_sneaking_entities") != null) {
                        boolean oldValue = ((IPermissionPropertiesAccessor) properties)
                                .getHideSneakingEntities().get();
                        boolean newValue = global.get("hide_sneaking_entities").getAsBoolean();

                        if (newValue != oldValue) {
                            ((IPermissionPropertiesAccessor) properties)
                                    .getHideSneakingEntities().set(newValue);

                            if (ModConfig.server.announceChangedCFGMSG) {
                                journeyMapFix$sendConfigChangeMessage(player, "Hide Sneaking Entities", newValue);
                            }
                        }
                    }

                    if (global.get("hide_invisible_players") != null) {
                        boolean oldValue = ((IPermissionPropertiesAccessor) properties)
                                .getHideInvisiblePlayers().get();
                        boolean newValue = global.get("hide_invisible_players").getAsBoolean();

                        if (newValue != oldValue) {
                            ((IPermissionPropertiesAccessor) properties)
                                    .getHideInvisiblePlayers().set(newValue);

                            if (ModConfig.server.announceChangedCFGMSG) {
                                journeyMapFix$sendConfigChangeMessage(player, "Hide Invisible Players", newValue);
                            }
                        }
                    }
                }

                this.updateCommonProperties(properties, global);
                properties.save();
            }

            if (prop.get("default_dimension") != null) {
                JsonObject dDim = prop.get("default_dimension").getAsJsonObject();
                DefaultDimensionProperties properties = PropertiesManager.getInstance().getDefaultDimensionProperties();
                properties.enabled.set(dDim.get("enabled").getAsBoolean());
                this.updateCommonProperties(properties, dDim);
                properties.save();
            }

            if (prop.get("dimensions") != null) {
                for(JsonElement element : prop.get("dimensions").getAsJsonArray()) {
                    JsonObject dimProp = element.getAsJsonObject();
                    DimensionProperties properties = PropertiesManager.getInstance().getDimProperties(dimProp.get("dimId").getAsInt());
                    properties.enabled.set(dimProp.get("enabled").getAsBoolean());
                    this.updateCommonProperties(properties, dimProp);
                    properties.save();
                }
            }

            for(EntityPlayerMP playerTo : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
                JsonObject config = PlayerConfigController.getInstance().getPlayerConfig(playerTo);
                this.sendToPlayer(config, playerTo);
            }
        }

        return null;
    }

    @Unique
    private void journeyMapFix$sendConfigChangeMessage(EntityPlayerMP player, String configName, boolean newValue) {
        TextComponentString textMsg = new TextComponentString("§2[JourneyMap Fix] §b"
                + player.getName()
                + " §ehas changed the config value: "
                + "§c" + configName + " §6(New value: §d" + newValue + "§6)");

        synchronized (player.getServer()) {
            for (EntityPlayerMP p : player.getServer().getPlayerList().getPlayers()) {
                if (Objects.equals(p, player) || !JourneymapServer.isOp(p)) {
                    continue;
                }
                p.sendMessage(textMsg);
            }

            player.getServer().sendMessage(textMsg);
        }
    }
}
