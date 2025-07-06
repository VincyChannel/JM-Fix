package com.vincychannel.journeymapfix.config;

import com.vincychannel.journeymapfix.JourneyMapFix;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;

@Config(
    modid = JourneyMapFix.MODID
)
public class ModConfig {

    // ========================
    //     CLIENT CATEGORY
    // ========================
    @LangKey(JourneyMapFix.MODID + "config.client")
    public static Client client = new Client();

    public static class Client {

        @Name("Enable Fullscreen Map Grid BugFix")
        @Comment("Fixes the annoying bug with the grid, when the map is opened in fullscreen (Default: true) \n§c[Requires Restart]")
        public boolean fixFullscreenBug = true;

        @Name("Disable Waypoint creation with left click")
        @Comment("Remove the waypoint creation with left mouse click in the fullscreen map\nIt will enable the creation with double right click (Default: true) \n§c[Requires Restart]")
        public boolean disableWaypointMouseRC = true;
    }

    // ========================
    //     SERVER CATEGORY
    // ========================
    @LangKey(JourneyMapFix.MODID + "config.server")
    public static Server server = new Server();

    public static class Server {

        @Name("Enable Hide Sneaking Entities Config")
        @Comment("Enable the config property \"Hide Sneaking Entities\" (Default: true) \n§c[Requires Restart]")
        public boolean enableHideSneakingEntities = true;

        @Name("Send changed config value messages")
        @Comment("If enabled, OP players (and server console) will receive a message when an admin changes the 'Hide Sneaking Entities' config value")
        public boolean announceHideSneakingEntities = false;
    }
}
