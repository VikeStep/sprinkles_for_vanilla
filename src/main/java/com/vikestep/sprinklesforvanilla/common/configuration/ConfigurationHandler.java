package com.vikestep.sprinklesforvanilla.common.configuration;

import com.vikestep.sprinklesforvanilla.common.reference.ModInfo;
import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigurationHandler
{
    private static Configuration config;

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
        config.setCategoryComment(CATEGORY, "Use this section to disable certain config sections (May fix mod compat issues)");
        COMMENT = "Set to true to enable the sleep configs, set to false to ignore sleep configs";
        Settings.overhaulSleep = config.get(CATEGORY, "overhaulSleep", true, COMMENT).getBoolean(true);

        CATEGORY = "Sleep";
        config.setCategoryComment(CATEGORY, "This section handles the mechanics of beds and sleeping (May cause issues with mods like Insomnia or PerfectSpawn");
        COMMENT = "Set to true to enable sleeping in bed, false to disable sleeping in bed (if this is false and bedsSetSpawn is true, then you can't sleep, but it will set spawn)";
        Settings.sleepIsEnabled = config.get(CATEGORY, "sleepIsEnabled", true, COMMENT).getBoolean(true);
        COMMENT = "Set to true to let beds set spawn, set to false to have beds not set spawn";
        Settings.bedSetsSpawn = config.get(CATEGORY, "bedsSetSpawn", true, COMMENT).getBoolean(true);
        COMMENT = "Set to true to have nearby mobs cancel sleep, set to false to allow sleep with nearby mobs";
        Settings.nearbyMobsCancelSleep = config.get(CATEGORY, "nearbyMobsCancelSleep", true, COMMENT).getBoolean(true);
        //Disabled and kept at true until I can find a work around because players are auto kicked from beds in EntityPlayer update method (PlayerTickEvent) I could cancel PlayerTickEvent but it would be a lot of work for a small feature. Maybe get a forge hook
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

        Settings.mobNameConfigs = new boolean[Settings.mobNames.length];
        CATEGORY = "Mobs";
        config.setCategoryComment(CATEGORY, "Set to true to enable the mob, set to false to disable the mob");
        for (int i = 0; i < Settings.mobNameConfigs.length; i++)
        {
            Settings.mobNameConfigs[i] = config.get(CATEGORY, Settings.mobNames[i][0], true).getBoolean(true);
        }

        Settings.particleNameConfigs = new boolean[Settings.particleNames.length];
        CATEGORY = "Particles";
        config.setCategoryComment(CATEGORY, "Set to true to enable the particle, set to false to disable the particle. Refer to http://minecraft.gamepedia.com/Particles for details");
        for (int i = 0; i < Settings.particleNameConfigs.length; i++)
        {
            Settings.particleNameConfigs[i] = config.get(CATEGORY, Settings.particleNames[i], true).getBoolean(true);
        }

        CATEGORY = "Explosions";
        config.setCategoryComment(CATEGORY, "enable/disable the explosion (if explosionsAreEnabled is false, all are false including non-vanilla)");
        Settings.explosionsAreEnabled = config.get(CATEGORY, "explosionsAreEnabled", true).getBoolean(true);
        Settings.TNTExplosionsAreEnabled = config.get(CATEGORY, "TNTExplosionsAreEnabled", true).getBoolean(true);
        Settings.creeperExplosionsAreEnabled = config.get(CATEGORY, "creeperExplosionsAreEnabled", true).getBoolean(true);
        Settings.chargedCreeperExplosionsAreEnabled = config.get(CATEGORY, "chargedCreeperExplosionsAreEnabled", true).getBoolean(true);
        Settings.witherCreationExplosionsAreEnabled = config.get(CATEGORY, "witherCreationExplosionsAreEnabled", true).getBoolean(true);
        Settings.enderCrystalExplosionsAreEnabled = config.get(CATEGORY, "enderCrystalExplosionsAreEnabled", true).getBoolean(true);
        Settings.ghastFireballExplosionsAreEnabled = config.get(CATEGORY, "ghastFireballExplosionsAreEnabled", true).getBoolean(true);
        Settings.witherSkullProjectileExplosionsAreEnabled = config.get(CATEGORY, "witherSkullProjectileExplosionsAreEnabled", true).getBoolean(true);
        Settings.bedExplosionsAreEnabled = config.get(CATEGORY, "bedExplosionsAreEnabled", true).getBoolean(true);

        CATEGORY = "General";
        config.setCategoryComment(CATEGORY, "This section is for general/miscellaneous configs that don't fit in other categories");
        COMMENT = "Set to true to manually change mob griefing, set to false to automatically disable mob griefing";
        Settings.mobGriefingIsForcedFalse = config.get(CATEGORY, "mobGriefingIsForcedFalse", false, COMMENT).getBoolean(false);

        CATEGORY = "Nether Portals";
        config.setCategoryComment(CATEGORY, "This section handles the behaviour of nether portals");
        COMMENT = "Set to true to enable teleportation to the nether via portal block (generated in Nether Portal), set to false to disable it";
        Settings.netherPortalsCanTeleport = config.get(CATEGORY, "netherPortalsCanTeleport", true, COMMENT).getBoolean(true);
        COMMENT = "Set to true to enable portal blocks being generated when the portal structure catches fire, set to false to disable portal blocks being made";
        Settings.netherPortalsAreGenerated = config.get(CATEGORY, "netherPortalsAreGenerated", true, COMMENT).getBoolean(true);
        COMMENT = "Set this to a value which is multiplied by the chance of a zombie pigman being spawned at a nether portal. Set to 0 to disable zombie pigmen being spawned at portals";
        Settings.netherPortalPigmenSpawnMult = config.get(CATEGORY, "netherPortalPigmenSpawnMult", 1.0, COMMENT, 0, Double.MAX_VALUE).getDouble(1.0);

        CATEGORY = "Sound";
        config.setCategoryComment(CATEGORY, "Set to true to enable the sound, set to false to disable the sound");
        Settings.netherPortalsCreateSound = config.get(CATEGORY, "netherPortalsCreateSound", true).getBoolean(true);

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
