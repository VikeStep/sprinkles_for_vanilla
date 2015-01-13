package com.vikestep.sprinklesforvanilla.common.reference;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;

import java.util.LinkedHashMap;

public class Settings
{
    //Mobs
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
    //Particles
    //Refer to http://minecraft.gamepedia.com/Particles for details
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
    //Mob Griefing
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
    //DamageSources
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
    public static boolean[] mobNameConfigs; //Initialized with values in ConfigurationHandler
    public static boolean[] particleNameConfigs; //Initialized with values in ConfigurationHandler
    public static int potionEffectsShown = 0;
    public static boolean[] griefTypeConfigs;
    public static boolean mobGriefingOverride = false;
    public static int[] damageSourceConfigs;
    //Beacon Blocks
    public static String[] beaconBlocks;
    //Blocks that catch fire
    public static String[] flammableBlocks;
    //Overhauls
    public static boolean overhaulSleep = true;

    //Sleep
    public static boolean sleepIsEnabled              = true;
    public static boolean bedSetsSpawn                = true;
    public static boolean nearbyMobsCancelSleep       = true;
    public static boolean dayCancelsSleep             = true; //Currently not in the configs. Either use coremodding or request a Forge PR to fix this
    public static boolean playerMustSleepInOverworld  = true;
    public static boolean distanceFromBedCancelsSleep = true;
    public static int[]   nearbyMobDistance           = {8, 5, 8}; //{x, y, z}
    public static int[]   distanceFromBed             = {3, 2, 3}; //{x, y, z}
    public static int     timeToSleep                 = 100; //Max is 100 Ticks

    //Explosions
    public static boolean explosionsAreEnabled                      = true;
    public static boolean TNTExplosionsAreEnabled                   = true;
    public static boolean creeperExplosionsAreEnabled               = true;
    public static boolean chargedCreeperExplosionsAreEnabled        = true;
    public static boolean witherCreationExplosionsAreEnabled        = true;
    public static boolean enderCrystalExplosionsAreEnabled          = true;
    public static boolean ghastFireballExplosionsAreEnabled         = true;
    public static boolean witherSkullProjectileExplosionsAreEnabled = true;
    public static boolean bedExplosionsAreEnabled                   = true;

    //Nether Portals
    public static boolean netherPortalsCanTeleport    = true;
    public static boolean netherPortalsAreGenerated   = true;
    public static double  netherPortalPigmenSpawnMult = 1.0;

    //Sounds
    public static boolean netherPortalsCreateSound = true;

    //Misc
    public static int     christmasChest        = 0;
    public static boolean doEnderPearlsTeleport = true;
    public static int     keepHealth            = -1;
    public static int     keepHunger            = -1;
    public static boolean keepXP                = false;

}
