package com.vikestep.sprinklesforvanilla.common.reference;

public class Settings
{
    //I know there are better ways of doing this like a Map. May change later but it works well and is easily extendable
    public static final String[] mobNames =
            {
                    "chickens", "EntityChicken",
                    "cows", "EntityCow",
                    "horses", "EntityHorse",
                    "ocelots", "EntityOcelot",
                    "pigs", "EntityPig",
                    "sheep", "EntitySheep",
                    "bats", "EntityBat",
                    "mooshrooms", "EntityMooshroom",
                    "squids", "EntitySquid",
                    "villagers", "EntityVillager",
                    "caveSpiders", "EntityCaveSpider",
                    "endermen", "EntityEnderman",
                    "spiders", "EntitySpider",
                    "wolves", "EntityWolf",
                    "zombiePigmen", "EntityPigZombie",
                    "blazes", "EntityBlaze",
                    "creepers", "EntityCreeper",
                    "ghasts", "EntityGhast",
                    "magmaCubes", "EntityMagmaCube",
                    "silverfish", "EntitySilverfish",
                    "skeletons", "EntitySkeleton",
                    "slimes", "EntitySlime",
                    "witches", "EntityWitch",
                    "witherSkeletons", "EntitySkeleton",
                    "zombies", "EntityZombie",
                    "zombieVillagers", "EntityZombie",
                    "snowGolems", "EntitySnowman",
                    "ironGolems", "EntityIronGolem",
                    "withers", "EntityWither",
                    "enderDragons", "EntityDragon"
            };
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
    public static boolean[] mobNameConfigs; //Initialized with values in ConfigurationHandler

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

    //Particles
    public static boolean netherPortalsCreateParticles = true;

    //Sounds
    public static boolean netherPortalsCreateSound = true;

    //Misc
    public static boolean mobGriefingIsForcedFalse = false;

}
