package com.vikestep.sprinklesforvanilla.common.reference;

public class Settings
{
    //List of Mobs and the class that is checked against
    public static final String[][] mobNames      =
            {
                    {"chicken", "EntityChicken", "wg"},
                    {"cow", "EntityCow", "wh"},
                    {"horse", "EntityHorse", "wi"},
                    {"ocelot", "EntityOcelot", "wn"},
                    {"pig", "EntityPig", "wo"},
                    {"sheep", "EntitySheep", "wp"},
                    {"bat", "EntityBat", "we"},
                    {"mooshroom", "EntityMooshroom", "wm"},
                    {"squid", "EntitySquid", "ws"},
                    {"villager", "EntityVillager", "yv"},
                    {"caveSpider", "EntityCaveSpider", "xy"},
                    {"enderman", "EntityEnderman", "ya"},
                    {"spider", "EntitySpider", "yn"},
                    {"wolf", "EntityWolf", "wv"},
                    {"zombiePigman", "EntityPigZombie", "yh"},
                    {"blaze", "EntityBlaze", "xx"},
                    {"creeper", "EntityCreeper", "xz"},
                    {"ghast", "EntityGhast", "yd"},
                    {"magmaCube", "EntityMagmaCube", "yf"},
                    {"silverfish", "EntitySilverfish", "yk"},
                    {"skeleton", "EntitySkeleton", "yl"},
                    {"slime", "EntitySlime", "ym"},
                    {"witch", "EntityWitch", "yp"},
                    {"witherSkeleton", "EntitySkeleton", "yl"},
                    {"zombie", "EntityZombie", "yq"},
                    {"zombieVillager", "EntityZombie", "yq"},
                    {"snowGolem", "EntitySnowman", "wr"},
                    {"ironGolem", "EntityIronGolem", "wt"},
                    {"wither", "EntityWither", "xc"},
                    {"enderDragon", "EntityDragon", "xa"}
            };
    //Refer to http://minecraft.gamepedia.com/Particles for details
    public static final String[]   particleNames =
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
    public static boolean[] mobNameConfigs; //Initialized with values in ConfigurationHandler
    public static boolean[] particleNameConfigs; //Initialized with values in ConfigurationHandler

    //Overhauls
    public static boolean overhaulSleep               = true;
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
    public static int mobGriefingIsForcedFalse = 0;
    public static int christmasChest           = 0;

}
