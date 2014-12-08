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
        String CATEGORY;

        CATEGORY = "Overhauls";
        COMMENT = "Set to true to enable the sleep configs, set to false to ignore sleep configs";
        Settings.overhaulSleep = config.get(CATEGORY, "overhaulSleep", true, COMMENT).getBoolean(true);

        CATEGORY = "Sleep";
        COMMENT = "Set to true to enable sleeping in bed, false to disable sleeping in bed (if this is false and bedsSetSpawn is true, then you can't sleep, but it will set spawn)";
        Settings.sleepIsEnabled = config.get(CATEGORY, "sleepIsEnabled", true, COMMENT).getBoolean(true);
        COMMENT = "Set to true to let beds set spawn, set to false to have beds not set spawn";
        Settings.bedSetsSpawn = config.get(CATEGORY, "bedsSetSpawn", true, COMMENT).getBoolean(true);
        COMMENT = "Set to true to have nearby mobs cancel sleep, set to false to allow sleep with nearby mobs";
        Settings.nearbyMobsCancelSleep = config.get(CATEGORY, "nearbyMobsCancelSleep", true, COMMENT).getBoolean(true);
        //Disabled and kept at true until I can find a work around
        //COMMENT = "Set to true to only sleep at night, set to false to be able to sleep at any time (and wake up in morning)";
        //Settings.dayCancelsSleep = config.get(CATEGORY, "dayCancelsSleep", true, COMMENT).getBoolean(true);
        COMMENT = "Set to true to only allow sleeping in the Overworld, set to false to enable sleep in other dimensions (spawn will not be set regardless of bedsSetSpawn)";
        Settings.playerMustSleepInOverworld = config.get(CATEGORY, "playerMustSleepInOverWorld", true, COMMENT).getBoolean(true);
        COMMENT = "Set to true to require player to be close to bed to sleep, set to false to remove this requirement";
        Settings.distanceFromBedCancelsSleep = config.get(CATEGORY, "distanceFromBedCancelsSleep", true, COMMENT).getBoolean(true);
        COMMENT = "Set this to the x, y, and z distance away from the bed that is checked for nearby mobs if nearbyMobsCancelSleep is true";
        Settings.nearbyMobDistance = config.get(CATEGORY, "nearbyMobDistance", new int[] {8, 5, 8}, COMMENT, 0, Integer.MAX_VALUE, true, 3).getIntList();
        COMMENT = "Set this to the x, y, and z distance away from the bed that the player must be within if distanceFromBedCancelsSleep is true";
        Settings.distanceFromBed = config.get(CATEGORY, "distanceFromBed", new int[] {3, 2, 3}, COMMENT, 0, Integer.MAX_VALUE, true, 3).getIntList();
        COMMENT = "Set this to the time in ticks (20 ticks = 1 second) that it takes for the player to sleep (max and default is 100)";
        Settings.timeToSleep = config.get(CATEGORY, "timeToSleep", 100, COMMENT, 0, 100).getInt(100);

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
