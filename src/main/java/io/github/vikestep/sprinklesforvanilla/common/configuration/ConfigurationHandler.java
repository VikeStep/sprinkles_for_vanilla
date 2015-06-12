package io.github.vikestep.sprinklesforvanilla.common.configuration;

import io.github.vikestep.sprinklesforvanilla.SprinklesForVanilla;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ConfigurationHandler
{
    public static boolean configLoaded = false;

    private static SprinkleConfiguration config;

    public static void init(File file)
    {
        config = new SprinkleConfiguration(file);
        setConfigDescription();
        loadConfiguration();
        saveConfiguration();
        configLoaded = true;
    }

    private static void loadConfiguration()
    {
        String CATEGORY;
        String COMMENT;
        List<String> propOrder;

        //If its the clientside, save the config value in array value 0, if its server-side, save it in array value 1
        int side = SprinklesForVanilla.isClient ? 0 : 1;

        /*******************************************
         *
         * CLIENTSIDE
         *
         *******************************************/

        /************
         * General
         ************/

        propOrder = new ArrayList<String>();
        CATEGORY = "clientside.general";

        COMMENT = "Set to 0 to have christmas chest on christmas, 1 for all the time, 2 for no christmas chest on christmas (you scrooge!)";
        Settings.displayChristmasChest = config.get(CATEGORY, "displayChristmasChest", 0, COMMENT, 0, 2).getInt(0);
        propOrder.add("displayChristmasChest");

        config.setCategoryPropertyOrder(CATEGORY, propOrder);

        /************
         * Sounds
         ************/

        propOrder = new ArrayList<String>();
        CATEGORY = "clientside.sounds";

        COMMENT = "In this list you must put the name of every sound you wish to not hear. The format for each entry must be of type (modname:soundpath)\n" +
                  "For example Wither Spawn is minecraft:mob.wither.spawn. A list of vanilla sounds are here: http://minecraft.gamepedia.com/Sounds.json\n" +
                  "If you wish to find out the soundpath for another mod, look in the assets folder inside that mods .jar file\n" +
                  "If you want one of the values in the list to be ignored, place a # in front of it";
        String[] defaultVals = new String[] {"#minecraft:mob.wither.spawn", "#minecraft:mob.enderdragon.end", "#minecraft:portal.portal"};
        Settings.disabledSounds = Arrays.asList(config.get(CATEGORY, "disabledSounds", defaultVals, COMMENT, false, -1, Pattern.compile(".*:.*")).getStringList());
        propOrder.add("disabledSounds");

        config.setCategoryPropertyOrder(CATEGORY, propOrder);

        /************
         * Particles
         ************/

        propOrder = new ArrayList<String>();
        CATEGORY = "clientside.particles";

        COMMENT = "This config allows you to hide your own potion particles, hide everyone's or go by default of other configs.\n" +
                  "Set to 0 to go by default. Set to 1 to hide your own potion particles. Set to 2 to hide everyone's potion particles (including mobs)";
        Settings.potionEffectsShown = config.get(CATEGORY, "potionEffectsShown", 0, COMMENT, 0, 2).getInt(0);
        propOrder.add("potionEffectsShown");

        COMMENT = "Each of these config options can either be set to true or false. true means that the particle will be shown, false means it won't\n" +
                  "To see what each of these particle types are, go to http://minecraft.gamepedia.com/Particles";
        Settings.particlesShown = new ArrayList<Boolean>();
        for (int i = 0; i < Settings.particleNames.length; i++)
        {
            Settings.particlesShown.add(config.get(CATEGORY, Settings.particleNames[i], true, i == 0 ? COMMENT : null).getBoolean(true));
            propOrder.add(Settings.particleNames[i]);
        }

        config.setCategoryPropertyOrder(CATEGORY, propOrder);

        /*******************************************
         *
         * GLOBAL
         *
         *******************************************/

        /************
         * General
         ************/

        propOrder = new ArrayList<String>();
        CATEGORY = "global.general";

        COMMENT = "Set this to true to have ender pearls teleport, set to false to disallow teleportation via ender pearls";
        Settings.enderPearlsTeleport[side] = config.get(CATEGORY, "enderPearlsTeleport", true, COMMENT).getBoolean(true);
        propOrder.add("enderPearlsTeleport");

        COMMENT = "This config changes how much health you respawn with after dying. If you wish to go back to full health after dying, leave this value at 0\n" +
                  "If you wish to go back to the same health you had before you died (the health you had before it went to 0), set this to a number between 1 and 20\n" +
                  "If you set it to 1 you will always respawn with at least half a heart whereas setting this to 10 will respawn you with at least 5 hearts even if you had less before you died";
        Settings.playerKeepsHealthOnRespawn[side] = config.get(CATEGORY, "playerKeepsHealthOnRespawn", 0, COMMENT, 0, 20).getInt(0);
        propOrder.add("playerKeepsHealthOnRespawn");

        COMMENT = "This config changes how much hunger you respawn with after dying. If you wish to go back to full hunger after dying, leave this value at 0\n" +
                  "If you wish to go back to the same hunger you had before you died, set this to a number between 1 and 20\n" +
                  "If you set it to 1 you will always respawn with at least half a shank (hunger unit) whereas setting this to 10 will respawn you with at least 5 shanks even if you had less before you died";
        Settings.playerKeepsHungerOnRespawn[side] = config.get(CATEGORY, "playerKeepsHungerOnRespawn", 0, COMMENT, 0, 20).getInt(0);
        propOrder.add("playerKeepsHungerOnRespawn");

        COMMENT = "This config changes whether or not you lose your experience (xp) when you respawn after dying.\n" +
                  "Set this to true to have experience kept when respawning, set to false to have experience reset to 0 on death";
        Settings.playerKeepsXPOnRespawn[side] = config.get(CATEGORY, "playerKeepsXPOnRespawn", false, COMMENT).getBoolean(false);
        propOrder.add("playerKeepsXPOnRespawn");

        COMMENT = "Set this to true to check for bed if spawn was set by bed. Set this to false to force the spawn set by a bed.";
        // Settings.playerChecksBedRespawn[side] = config.get(CATEGORY, "checkForBedWhenRespawning", true, COMMENT).getBoolean(true);
        // propOrder.add("checkForBedWhenRespawning");

        COMMENT = "Set this to true to enable placing water in the nether. Set to false to have water evaporated";
        Settings.allowWaterInNether[side] = config.get(CATEGORY, "allowWaterInNether", false, COMMENT).getBoolean(false);
        propOrder.add("allowWaterInNether");

        int[] DEFAULT = new int[]{};
        COMMENT = "Set this to a list of dimension ids which should not allow obsidian from water and lava";
        int[] result = config.get(CATEGORY, "waterAndLavaMakesObsidianBlacklist", DEFAULT, COMMENT).getIntList();
        Settings.waterAndLavaMakesObsidianBlacklist[side] = result.length == 0 ? new ArrayList<Integer>() : new ArrayList<Integer>(Arrays.asList(ArrayUtils.toObject(result)));
        propOrder.add("waterAndLavaMakesObsidianBlacklist");

        COMMENT = "Set this to a list of dimension ids which should not allow cobblestone from water and lava";
        result = config.get(CATEGORY, "waterAndLavaMakesCobbleBlacklist", DEFAULT, COMMENT).getIntList();
        Settings.waterAndLavaMakesCobbleBlacklist[side] = result.length == 0 ? new ArrayList<Integer>() : new ArrayList<Integer>(Arrays.asList(ArrayUtils.toObject(result)));
        propOrder.add("waterAndLavaMakesCobbleBlacklist");

        config.setCategoryPropertyOrder(CATEGORY, propOrder);

        /************
         * Spawning
         ************/

        propOrder = new ArrayList<String>();
        CATEGORY = "global.spawning";

        COMMENT = "Set this to true to enable spawn fuzz which will make the spawn a spawn radius rather than a specific location";
        Settings.enableSpawnFuzz[side] = config.get(CATEGORY, "enableSpawnFuzz", true, COMMENT).getBoolean(true);
        propOrder.add("enableSpawnFuzz");

        COMMENT = "Set this to true to allow spawn being set in the nether. Set to false to disable. Spawn can only be set if sleeping\n" +
                  "in other dimensions is enabled by setting otherDimensionsCancelSleep to false";
        Settings.allowNetherRespawn[side] = config.get(CATEGORY, "allowNetherRespawn", false, COMMENT).getBoolean(false);
        propOrder.add("allowNetherRespawn");

        COMMENT = "Set this to true to allow spawn being set in the end. Set to false to disable. Spawn can only be set if sleeping\n" +
                  "in other dimensions is enabled by setting otherDimensionsCancelSleep to false";
        Settings.allowEndRespawn[side] = config.get(CATEGORY, "allowEndRespawn", false, COMMENT).getBoolean(false);
        propOrder.add("allowEndRespawn");

        COMMENT = "Set these values to what you wish the spawn to be in their respective dimensions. Non-overworld dimensions must have spawning\n" +
                  "allowed for these spawns to be spawned at. If you wish to not set a value, then set the value to DEFAULT. Should be formatted as\n" +
                  "'x, y, z'";
        Settings.overworldSpawnDefault[side] = config.get(CATEGORY, "overworldSpawnDefault", "DEFAULT", COMMENT).getString();
        Settings.netherSpawnDefault[side] = config.get(CATEGORY, "netherSpawnDefault", "DEFAULT").getString();
        Settings.endSpawnDefault[side] = config.get(CATEGORY, "endSpawnDefault", "DEFAULT").getString();
        propOrder.addAll(Arrays.asList("overworldSpawnDefault", "netherSpawnDefault", "endSpawnDefault"));

        config.setCategoryPropertyOrder(CATEGORY, propOrder);

        /************
         * Beacons
         ************/

        propOrder = new ArrayList<String>();
        CATEGORY = "global.beacons";

        COMMENT = "Set this to false to ignore sunlight checks for beacons. Set to true to check for sunlight";
        Settings.shouldBeaconCheckForSunlight[side] = config.get(CATEGORY, "shouldBeaconCheckForSunlight", true, COMMENT).getBoolean(true);
        propOrder.add("shouldBeaconCheckForSunlight");

        COMMENT = "In this list you must put a the blocks you wish to be used as the base for beacon blocks\n" +
                  "In here by default are the ones vanilla used. Each entry must be of the form (modname:blockname)\n" +
                  "You can put a \"#\" before an entry if you wish to disable it without removing the line";
        Settings.beaconBaseBlocks[side] = new ArrayList<String>(Arrays.asList(config.get(CATEGORY, "beaconBaseBlocks", Settings.defaultBeaconBaseBlocks, COMMENT, false, -1, Pattern.compile(".*:.*")).getStringList()));
        propOrder.add("beaconBaseBlocks");

        config.setCategoryPropertyOrder(CATEGORY, propOrder);


        /************
         * Flammable Blocks
         ************/

        /*propOrder = new ArrayList<String>();
        CATEGORY = "global.flammable blocks";

        COMMENT = "NOT IMPLEMENTED YET";
        COMMENT = "In this list you must put the blocks you wish to be flammable. The format for an entry is \"modname:blockname, speed, flammability\"\n" +
                  "The Higher the speed, the higher the chance of fire spreading. The chance of a block catching fire is given by (flammability / 300)\n" +
                  "Therefore flammability must be between 0 and 300, and the speed must be 0 or higher. You can put a \"#\" before an entry to disable it without removing the line";
        Settings.flammableBlocks[side] = new ArrayList<String>(Arrays.asList(config.get(CATEGORY, "flammableBlocks", Settings.defaultFlammable, COMMENT, false, -1, Pattern.compile(".*:.*,.*,.*")).getStringList()));
        propOrder.add("flammableBlocks");

        config.setCategoryPropertyOrder(CATEGORY, propOrder);*/

        /************
         * Damage Sources
         ************/

        propOrder = new ArrayList<String>();
        CATEGORY = "global.damage sources";

        COMMENT = "Set to 0 to enable this damage for all, set to 1 to only affect non-players, set to 2 to completely disable this damage type";
        Settings.damageSourceConfigs[side] = new ArrayList<Integer>();
        for (int i = 0; i < Settings.damageSources.length; i++)
        {
            Settings.damageSourceConfigs[side].add(config.get(CATEGORY, Settings.damageSources[i], 0, i == 0 ? COMMENT : null, 0, 2).getInt(0));
            propOrder.add(Settings.damageSources[i]);
        }

        config.setCategoryPropertyOrder(CATEGORY, propOrder);

        /************
         * Mob Griefing
         ************/

        propOrder = new ArrayList<String>();
        CATEGORY = "global.mob griefing";

        COMMENT = "Enable this if you want the below configs to override the vanilla mobGriefing gamerule. Disabling this means that mobGriefing gamerule will be used and will either toggle everything on or off";
        Settings.mobGriefingOverride[side] = config.get(CATEGORY, "mobGriefingOverride", true, COMMENT).getBoolean();
        propOrder.add("mobGriefingOverride");

        COMMENT = "Set to true to enable this type of mob griefing. Set to false to disable it. These will only be used if mobGriefingOverride is true";
        Settings.mobGriefingConfigs[side] = new ArrayList<Boolean>();
        for (int i = 0; i < Settings.mobGriefingTypes.length; i++)
        {
            Settings.mobGriefingConfigs[side].add(config.get(CATEGORY, Settings.mobGriefingTypes[i], true, i == 0 ? COMMENT : null).getBoolean(true));
            propOrder.add(Settings.mobGriefingTypes[i]);
        }

        config.setCategoryPropertyOrder(CATEGORY, propOrder);

        /************
         * Nether Portals
         ************/

        propOrder = new ArrayList<String>();
        CATEGORY = "global.nether portals";

        COMMENT = "Set to true to allow nether portals to teleport. Set to false to disallow";
        Settings.netherPortalsAllowTeleportation[side] = config.get(CATEGORY, "netherPortalsAllowTeleportation", true, COMMENT).getBoolean(true);
        propOrder.add("netherPortalsAllowTeleportation");

        COMMENT = "Set to true to have portals generated when the obsidian portal structure is lit with fire. Set to false to have no reaction when the obsidian structure is lit";
        Settings.netherPortalBlocksAreGenerated[side] = config.get(CATEGORY, "netherPortalBlocksAreGenerated", true, COMMENT).getBoolean(true);
        propOrder.add("netherPortalBlocksAreGenerated");

        COMMENT = "Set this to a number (0 or greater and can be a decimal) that is multiplied by the chance of a zombie pigmen spawning in the overworld at a nether portal";
        Settings.zombiePigmanNetherPortalSpawnMult[side] = config.get(CATEGORY, "zombiePigmanNetherPortalSpawnMult", 1.0, COMMENT, 0, Double.MAX_VALUE).getDouble(1.0);
        propOrder.add("zombiePigmanNetherPortalSpawnMult");

        config.setCategoryPropertyOrder(CATEGORY, propOrder);

        /************
         * Sleep
         ************/

        propOrder = new ArrayList<String>();
        CATEGORY = "global.sleep";

        COMMENT = "Set this to true if you want beds to allow sleep and skip night, Set to false if you don't wish for beds to sleep and skip night";
        Settings.sleepIsEnabled[side] = config.get(CATEGORY, "sleepIsEnabled", true, COMMENT).getBoolean(true);
        propOrder.add("sleepIsEnabled");

        COMMENT = "Set this to true if you want beds to set spawn (sleepIsEnabled does not affect this). Set this to false to disallow spawn being set when a bed is used";
        Settings.bedSetsSpawn[side] = config.get(CATEGORY, "bedSetsSpawn", true, COMMENT).getBoolean(true);
        propOrder.add("bedSetsSpawn");

        COMMENT = "Set this to true if you want beds to explode and cancel sleep when in dimensions such as the nether. Set this to false to allow sleep in other dimensions.\n" +
                  "It should be noted that having this to false will allow you to set a spawn in other dimensions";
        Settings.otherDimensionsCancelSleep[side] = config.get(CATEGORY, "otherDimensionsCancelSleep", true ,COMMENT).getBoolean(true);
        propOrder.add("otherDimensionsCancelSleep");

        COMMENT = "Set this to true if you want nearby mobs to cancel sleep, set to false to sleep even if mobs are nearby";
        Settings.nearbyMobsCancelSleep[side] = config.get(CATEGORY, "nearbyMobsCancelSleep", true, COMMENT).getBoolean(true);
        propOrder.add("nearbyMobsCancelSleep");

        COMMENT = "Set this to the x, y, and z radius to check for nearby mobs if nearbyMobsCancelSleep is true";
        Settings.nearbyMobDistance[side] = config.get(CATEGORY, "nearbyMobDistance", new double[] {8, 5, 8}, COMMENT, 0, Integer.MAX_VALUE, true, 3).getDoubleList();
        propOrder.add("nearbyMobDistance");

        COMMENT = "Set this to true if you want to check if the user is close to the bed to sleep, set to false to be able to hop into a bed from any distance away that the block can still be selected";
        Settings.distanceFromBedCancelsSleep[side] = config.get(CATEGORY, "distanceFromBedCancelsSleep", true, COMMENT).getBoolean(true);
        propOrder.add("distanceFromBedCancelsSleep");

        COMMENT = "Set this to the x, y, and z radius distance the player has to be inside for the player to be able to sleep if distanceFromBedCancelsSleep is true";
        Settings.distanceFromBed[side] = config.get(CATEGORY, "distanceFromBed", new double[] {3, 2, 3}, COMMENT, 0, Integer.MAX_VALUE, true, 3).getDoubleList();
        propOrder.add("distanceFromBed");

        COMMENT = "Set this to the time in ticks it takes from clicking on a bed to sleeping. Default is 100 ticks (5 seconds). Must be between 0 and 100 ticks";
        Settings.timeToSleep[side] = config.get(CATEGORY, "timeToSleep", 100, COMMENT, 0, 100).getInt(100);
        propOrder.add("timeToSleep");

        config.setCategoryPropertyOrder(CATEGORY, propOrder);

        /************
         * Mob Spawning
         ************/

        //No property order since I want the mobs to be ordered alphabetically
        CATEGORY = "global.mob spawning";

        COMMENT = "Set this to the number of ticks between when creatures (everything except mobs, bats and squids) will" +
                  "spawn. Default value in minecraft is 400 ticks.";
        Settings.timeBetweenCreatureSpawns[side] = config.get(CATEGORY, "ticksBetweenCreatureSpawns", 400, COMMENT, 1, Integer.MAX_VALUE).getInt();
        propOrder.add("ticksBetweenCreatureSpawns");

        COMMENT = "Set to true to allow the mob to be spawned, set to false to disable that mob from being spawned";
        Settings.mobConfigs[side] = new ArrayList<Boolean>();
        int index = 0;
        for (String mobName : Settings.mobClasses.keySet())
        {
            Settings.mobConfigs[side].add(config.get(CATEGORY, mobName, true, index == 0 ? COMMENT : null).getBoolean(true));
            propOrder.add(mobName);
            index++;
        }

        COMMENT = "In this list you will put a list of commands which will change the spawn conditions. The format is \"command: arg1, arg2, {biome1, biome2}\".\n" +
                  "The commands available are: add, modify, and remove. if you use add or modify, you will need 4 arguments: mob name, weight, min group size, max\n" +
                  "group size. If you use remove, you will need 1 argument: mob name. The biome list is the list of biomes which these changes will affect. A \"#\"\n" +
                  "can be used to comment out commands for testing if you wish for them not to be used. Using \"Overworld\" as the biome will add all overworld biomes";
        Settings.mobSpawnRulesModifications[side] = new ArrayList<String>(Arrays.asList(config.get(CATEGORY, "mobSpawnRules", Settings.defaultModifications, COMMENT).getStringList()));
        propOrder.add("mobSpawnRules");

        config.setCategoryPropertyOrder(CATEGORY, propOrder);

        /************
         * Explosions
         ************/

        propOrder = new ArrayList<String>();
        CATEGORY = "global.explosions";

        COMMENT = "Set this to true to enable explosion logging";
        Settings.enableExplosionLogging[side] = config.get(CATEGORY, "explosionLogging", false, COMMENT).getBoolean(false);
        propOrder.add("explosionLogging");

        COMMENT = "Set this to true to disable all explosions";
        Settings.disableAllExplosions[side] = config.get(CATEGORY, "disableAllExplosions", false, COMMENT).getBoolean(false);
        propOrder.add("disableAllExplosions");

        COMMENT = "In this list are all the types of explosions and are formatted as \"Name, Explosion Size, Enabled, Does Damage, Creates Flames, Does Block Damage\"\n" +
                  "In some cases, the \"Does Block Damage\" element will be overridden by mobGriefing settings. Also, the Explosion Size setting can also be a multiplier if you put \"x\"\n" +
                  "in front of it. For example the MinecartTNT is a multiplier because it's explosion size is not constant and determined by the cart's speed. You may add explosions from\n" +
                  "mods if you know the exploder's name. Turn on Explosion Logging to see the name to use and the default settings. If the name of the exploder is \"null\" then it is not possible to add";
        Settings.explosionData[side] = new ArrayList<String>(Arrays.asList(config.get(CATEGORY, "explosionSettings", Settings.defaultExplosionData, COMMENT, false, -1, Pattern.compile(".*,.*,.*,.*,.*,.*")).getStringList()));
        propOrder.add("explosionSettings");

        config.setCategoryPropertyOrder(CATEGORY, propOrder);
    }

    public static void saveConfiguration()
    {
        config.save();
    }

    public static void setConfigDescription()
    {
        List<String> configDescription = new ArrayList<String>();
        configDescription.add("To view this text art you need to be using a monospaced font (such as Courier)");
        configDescription.add("");
        configDescription.add("                   _         _     _                      __                                            _  _  _");
        configDescription.add("                  (_)       | |   | |                    / _|                                          (_)| || |");
        configDescription.add(" ___  _ __   _ __  _  _ __  | | __| |  ___  ___         | |_   ___   _ __         __   __  __ _  _ __   _ | || |  __ _");
        configDescription.add("/ __|| '_ \\ | '__|| || '_ \\ | |/ /| | / _ \\/ __|        |  _| / _ \\ | '__|        \\ \\ / / / _` || '_ \\ | || || | / _` |");
        configDescription.add("\\__ \\| |_) || |   | || | | ||   < | ||  __/\\__ \\        | |  | (_) || |            \\ V / | (_| || | | || || || || (_| |");
        configDescription.add("|___/| .__/ |_|   |_||_| |_||_|\\_\\|_| \\___||___/        |_|   \\___/ |_|             \\_/   \\__,_||_| |_||_||_||_| \\__,_|");
        configDescription.add("     | |                                         ______                    ______");
        configDescription.add("     |_|                                        |______|                  |______|");
        configDescription.add("");
        configDescription.add("Thank you for using sprinkles_for_vanilla! The config starts when you get to the large line of hyphens (-). This means you can safely remove or add things before that line.");
        configDescription.add("");
        configDescription.add("I suggest you include a list of changes you have made to the config file before that line in case anyone who is looking through your pack wishes to easily see which modifications you have made with this mod.");
        configDescription.add("");
        configDescription.add("This config file has 2 sections: Clientside features and Global Features.");
        configDescription.add("- Clientside features will work all the time regardless of whether the server has the mod on the server.");
        configDescription.add("- Global features will only work if the server has the mod.");
        configDescription.add("- Please remember that in Single Player you are technically on your own server which does have the mod so global features will work then.");
        configDescription.add("- If you join a server that does not have this mod, then the configs in the global features section will be ignored.");
        configDescription.add("- Clientside features will also still work if the server does have the mod.");
        configDescription.add("- If you switch between a server which does have the mod to a server which doesn't have the mod, it will be detected and the configs will be used.");
        configDescription.add("- If the server and the client have different configs then the client will be sent the server configs and use them");
        configDescription.add("- You will be notified in the console if the server does not have the mod or if the configs are different so make sure you watch out for that.");
        configDescription.add("");
        configDescription.add("If you accidentally remove a feature and are getting an error related to this config, you can delete it and it will be regenerated for you.");
        configDescription.add("");
        configDescription.add("I shall also point out that there must be no spaces between the config entry, the equals sign \"=\" and the value set to it.");
        configDescription.add("The reason for this is because there are certain config values which take strings (sentences or words) that may allow for it to start with a space.");
        configDescription.add("However, if the value is in a list such as sounds, whitespace will be ignored before the first character");
        configDescription.add("");
        configDescription.add("DO NOT REMOVE OR ALTER THIS LINE OR THE MOD VERSION");
        configDescription.add("--------------------------------------------------");
        config.setConfigDescription(configDescription);
    }
}
