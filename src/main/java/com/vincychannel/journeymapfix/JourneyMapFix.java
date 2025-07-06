package com.vincychannel.journeymapfix;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = JourneyMapFix.MODID,
    name = JourneyMapFix.NAME,
    version = JourneyMapFix.VERSION
)
public class JourneyMapFix {
    public static final String MODID = "journeymapfix";
    public static final String NAME = "JourneyMap Fixes";
    public static final String VERSION = "1.0";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent preinit) {
        LOGGER.info("Enabling JourneyMapFix v." + VERSION);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent postInit) {
        LOGGER.info("Enabled JourneyMapFix v." + VERSION);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(JourneyMapFix.MODID)) {
            ConfigManager.sync(JourneyMapFix.MODID, Config.Type.INSTANCE);
        }
    }
}
