package io.github.vikestep.sprinklesforvanilla.common.configuration;

import io.github.vikestep.sprinklesforvanilla.common.utils.LogHelper;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@SuppressWarnings({"CanBeFinal", "unchecked"})
public class Settings
{
    /**
     * Every Setting has two values, therefore they are stored in array of their type.
     * The only exception are clientside only configs which aren't an array
     * the 0th index of a setting is the clientside value
     * the 1st index of a setting is the serverside value
     */

    /**
     * CLIENTSIDE FEATURES
     */

    //GENERAL
    public static int displayChristmasChest;

    //SOUNDS
    public static List<String> disabledSounds = new ArrayList<String>();

    //PARTICLES
    public static final String[] particleNames =
            {
                    "hugeexplosion",
                    "largeexplode",
                    "fireworksSpark",
                    "bubble",
                    "suspended",
                    "depthsuspend",
                    "townaura",
                    "crit",
                    "magicCrit",
                    "smoke",
                    "mobSpell",
                    "mobSpellAmbient",
                    "spell",
                    "instantSpell",
                    "witchMagic",
                    "note",
                    "portal",
                    "enchantmenttable",
                    "explode",
                    "flame",
                    "lava",
                    "footstep",
                    "splash",
                    "wake",
                    "largesmoke",
                    "cloud",
                    "reddust",
                    "snowballpoof",
                    "dripWater",
                    "dripLava",
                    "snowshovel",
                    "slime",
                    "heart",
                    "angryVillager",
                    "happyVillager",
                    "iconcrack_",
                    "blockcrack_",
                    "blockdust_",
                    "blockBreak"
            };

    public static List<Boolean> particlesShown;
    public static int           potionEffectsShown;

    /**
     * GLOBAL FEATURES
     */

    //GENERAL
    public static boolean[] enderPearlsTeleport        = new boolean[2];
    public static int[]     playerKeepsHealthOnRespawn = new int[2];
    public static int[]     playerKeepsHungerOnRespawn = new int[2];
    public static boolean[] playerKeepsXPOnRespawn     = new boolean[2];
    public static boolean[] allowWaterInNether         = new boolean[2];
    public static boolean[] waterAndLavaMakesObsidian  = new boolean[2];
    public static boolean[] waterAndLavaMakesCobble    = new boolean[2];

    //SPAWNING
    public static boolean[] allowNetherRespawn    = new boolean[2];
    public static boolean[] allowEndRespawn       = new boolean[2];
    public static String[]  overworldSpawnDefault = new String[2];
    public static String[]  netherSpawnDefault    = new String[2];
    public static String[]  endSpawnDefault       = new String[2];

    //BEACONS
    public static final String[] defaultBeaconBaseBlocks =
            {
                    "minecraft:iron_block",
                    "minecraft:gold_block",
                    "minecraft:emerald_block",
                    "minecraft:diamond_block"
            };

    public static List<String>[] beaconBaseBlocks = (ArrayList<String>[])new ArrayList[2];

    //FLAMMABLE BLOCKS
    public static final String[] defaultFlammable =
            {
                    "minecraft:planks, 5, 20",
                    "minecraft:double_wooden_slab, 5, 20",
                    "minecraft:wooden_slab, 5, 20",
                    "minecraft:fence, 5, 20",
                    "minecraft:oak_stairs, 5, 20",
                    "minecraft:birch_stairs, 5, 20",
                    "minecraft:spruce_stairs, 5, 20",
                    "minecraft:jungle_stairs, 5, 20",
                    "minecraft:log, 5, 5",
                    "minecraft:log2, 5, 5",
                    "minecraft:leaves, 30, 60",
                    "minecraft:leaves2, 30, 60",
                    "minecraft:bookshelf, 30, 20",
                    "minecraft:tnt, 15, 100",
                    "minecraft:tallgrass, 60, 100",
                    "minecraft:double_plant, 60, 100",
                    "minecraft:yellow_flower, 60, 100",
                    "minecraft:red_flower, 60, 100",
                    "minecraft:wool, 30, 60",
                    "minecraft:vine, 15, 100",
                    "minecraft:coal_block, 5, 5",
                    "minecraft:hay_block, 60, 20",
                    "minecraft:carpet, 60, 20"
            };

    public static List<String>[] flammableBlocks = (ArrayList<String>[])new ArrayList[2];

    //DAMAGE SOURCES
    public static final String[] damageSources =
            {
                    "inFire",
                    "onFire",
                    "lava",
                    "inWall",
                    "drown",
                    "starve",
                    "cactus",
                    "fall",
                    "outOfWorld",
                    "generic",
                    "fall",
                    "magic",
                    "wither",
                    "anvil",
                    "fallingBlock"
            };

    public static List<Integer>[] damageSourceConfigs = (ArrayList<Integer>[])new ArrayList[2];

    //MOB GRIEFING
    public static final String[] mobGriefingTypes =
            {
                    "fallenOnFarmland",
                    "mobPickUpLoot",
                    "mobBreakDoor",
                    "mobEatTallGrass",
                    "mobEatGrassBlock",
                    "enderDragonBreakBlock",
                    "witherExplode",
                    "witherBreakBlock",
                    "creeperExplosion",
                    "endermanStealBlock",
                    "silverfishBreakBlock",
                    "largeFireballExplosion",
                    "witherSkullExplosion"
            };

    public static boolean[]       mobGriefingOverride = new boolean[2];
    public static List<Boolean>[] mobGriefingConfigs  = (ArrayList<Boolean>[])new ArrayList[2];

    //NETHER PORTALS
    public static boolean[] netherPortalsAllowTeleportation   = new boolean[2];
    public static boolean[] netherPortalBlocksAreGenerated    = new boolean[2];
    public static double[]  zombiePigmanNetherPortalSpawnMult = new double[2];

    //SLEEP
    public static boolean[]  sleepIsEnabled              = new boolean[2];
    public static boolean[]  bedSetsSpawn                = new boolean[2];
    public static boolean[]  nearbyMobsCancelSleep       = new boolean[2];
    public static boolean[]  dayCancelsSleep             = new boolean[]{true, true}; //Currently not in the configs. Either use coremodding or request a Forge PR to fix this
    public static boolean[]  otherDimensionsCancelSleep  = new boolean[2];
    public static boolean[]  distanceFromBedCancelsSleep = new boolean[2];
    public static double[][] nearbyMobDistance           = new double[3][2]; //{x, y, z}
    public static double[][] distanceFromBed             = new double[3][2]; //{x, y, z}
    public static int[]      timeToSleep                 = new int[2]; //Max is 100 Ticks

    //MOBS
    public static final LinkedHashMap<String, Class<?>> mobClasses = new LinkedHashMap<String, Class<?>>();

    static
    {
        mobClasses.put("chicken", EntityChicken.class);
        mobClasses.put("cow", EntityCow.class);
        mobClasses.put("horse", EntityHorse.class);
        mobClasses.put("ocelot", EntityOcelot.class);
        mobClasses.put("pig", EntityPig.class);
        mobClasses.put("sheep", EntitySheep.class);
        mobClasses.put("bat", EntityBat.class);
        mobClasses.put("mooshroom", EntityMooshroom.class);
        mobClasses.put("squid", EntitySquid.class);
        mobClasses.put("villager", EntityVillager.class);
        mobClasses.put("caveSpider", EntityCaveSpider.class);
        mobClasses.put("enderman", EntityEnderman.class);
        mobClasses.put("spider", EntitySpider.class);
        mobClasses.put("wolf", EntityWolf.class);
        mobClasses.put("zombiePigman", EntityPigZombie.class);
        mobClasses.put("blaze", EntityBlaze.class);
        mobClasses.put("creeper", EntityCreeper.class);
        mobClasses.put("ghast", EntityGhast.class);
        mobClasses.put("magmaCube", EntityMagmaCube.class);
        mobClasses.put("silverfish", EntitySilverfish.class);
        mobClasses.put("skeleton", EntitySkeleton.class);
        mobClasses.put("slime", EntitySlime.class);
        mobClasses.put("witch", EntityWitch.class);
        mobClasses.put("witherSkeleton", EntitySkeleton.class);
        mobClasses.put("zombie", EntityZombie.class);
        mobClasses.put("zombieVillager", EntityZombie.class);
        mobClasses.put("snowGolem", EntitySnowman.class);
        mobClasses.put("ironGolem", EntityIronGolem.class);
        mobClasses.put("wither", EntityWither.class);
        mobClasses.put("enderDragon", EntityDragon.class);
    }

    public static List<Boolean>[] mobConfigs = (ArrayList<Boolean>[])new ArrayList[2];

    //EXPLOSIONS

    public static final String[] defaultExplosionData =
            {
                    //Name, explosionSize, enabled, doesDamage, createsFire, doesBlockDamage
                    "Bed, 5.0, true, true, true, true",
                    "GhastFireball, 1.0, true, true, true, true",
                    "EnderCrystal, 6.0, true, true, false, true",
                    "ChargedCreeper, 6.0, true, true, false, true",
                    "Creeper, 3.0, true, true, false, true",
                    "WitherBoss, 7.0, true, true, false, true",
                    "WitherSkull, 1.0, true, true, false, true",
                    "MinecartTNT, 1x, true, true, false, true",
                    "PrimedTnt, 4.0, true, true, false, true"
            };
    public static List<String>[] explosionData = (ArrayList<String>[])new ArrayList[2];

    public static boolean[] enableExplosionLogging = new boolean[2];
    public static boolean[] disableAllExplosions   = new boolean[2];

    public static void copyClientToServer() {
        enderPearlsTeleport[1] = enderPearlsTeleport[0];
        playerKeepsHealthOnRespawn[1] = playerKeepsHealthOnRespawn[0];
        playerKeepsHungerOnRespawn[1] = playerKeepsHungerOnRespawn[0];
        playerKeepsXPOnRespawn[1] = playerKeepsXPOnRespawn[0];
        allowWaterInNether[1] = allowWaterInNether[0];
        waterAndLavaMakesObsidian[1] = waterAndLavaMakesObsidian[0];
        waterAndLavaMakesCobble[1] = waterAndLavaMakesCobble[0];
        allowNetherRespawn[1] = allowNetherRespawn[0];
        allowEndRespawn[1] = allowEndRespawn[0];
        overworldSpawnDefault[1] = overworldSpawnDefault[0];
        netherSpawnDefault[1] = netherSpawnDefault[0];
        endSpawnDefault[1] = endSpawnDefault[0];
        beaconBaseBlocks[1] = new ArrayList<String>(beaconBaseBlocks[0]);
        flammableBlocks[1] = new ArrayList<String>(flammableBlocks[0]);
        damageSourceConfigs[1] = new ArrayList<Integer>(damageSourceConfigs[0]);
        mobGriefingOverride[1] = mobGriefingOverride[0];
        mobGriefingConfigs[1] = new ArrayList<Boolean>(mobGriefingConfigs[0]);
        netherPortalsAllowTeleportation[1] = netherPortalsAllowTeleportation[0];
        netherPortalBlocksAreGenerated[1] = netherPortalBlocksAreGenerated[0];
        zombiePigmanNetherPortalSpawnMult[1] = zombiePigmanNetherPortalSpawnMult[0];
        sleepIsEnabled[1] = sleepIsEnabled[0];
        bedSetsSpawn[1] = bedSetsSpawn[0];
        nearbyMobsCancelSleep[1] = nearbyMobsCancelSleep[0];
        dayCancelsSleep[1] = dayCancelsSleep[0];
        otherDimensionsCancelSleep[1] = otherDimensionsCancelSleep[0];
        distanceFromBedCancelsSleep[1] = distanceFromBedCancelsSleep[0];
        nearbyMobDistance[1] = nearbyMobDistance[0].clone();
        distanceFromBed[1] = distanceFromBed[0].clone();
        timeToSleep[1] = timeToSleep[0];
        mobConfigs[1] = new ArrayList<Boolean>(mobConfigs[0]);
        explosionData[1] = new ArrayList<String>(explosionData[0]);
        enableExplosionLogging[1] = enableExplosionLogging[0];
        disableAllExplosions[1] = disableAllExplosions[0];
    }
}
