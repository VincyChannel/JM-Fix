package com.vincychannel.journeymapfix;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = JourneyMapFix.MODID,
    name = JourneyMapFix.NAME,
    version = JourneyMapFix.VERSION
)
public class JourneyMapFix {
    public static final String MODID = "journeymapfix";
    public static final String NAME = "JourneyMap Fullscreen Fix";
    public static final String VERSION = "1.0-ALPHA";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent preinit) {
        LOGGER.info("Hello, world!");
    }
}
