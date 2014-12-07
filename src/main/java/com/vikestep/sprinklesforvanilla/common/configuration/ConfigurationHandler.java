package com.vikestep.sprinklesforvanilla.common.configuration;

import com.vikestep.sprinklesforvanilla.common.reference.ModInfo;
import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigurationHandler
{
    public static Configuration config;

    public static void init(File configFile)
    {
        if (config == null)
        {
            config = new Configuration(configFile);
            loadConfiguration();
        }
    }

    private static void loadConfiguration()
    {
        String COMMENT;

        COMMENT = "Set to true to disable sleep, false to enable";
        Settings.isSleepDisabled = config.getBoolean("isSleepDisabled", "Sleep", false, COMMENT);

        if (config.hasChanged())
        {
            config.save();
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.modID.equalsIgnoreCase(ModInfo.MOD_ID))
        {
            loadConfiguration();
        }
    }
}
