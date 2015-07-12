package io.github.vikestep.sprinklesforvanilla.asm;

import io.github.vikestep.sprinklesforvanilla.SprinklesForVanilla;
import io.github.vikestep.sprinklesforvanilla.common.configuration.Settings;
import io.github.vikestep.sprinklesforvanilla.common.init.InitMobRegistry;
import io.github.vikestep.sprinklesforvanilla.common.utils.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@SuppressWarnings("UnusedDeclaration")
public class Hooks
{
    public static ArrayList<EntityLargeFireball> fireballsExploding = new ArrayList<EntityLargeFireball>();

    //String name is passed in case we use it in the future
    public static void particleSpawnedFromEntity(EntityLivingBase entity, String particle)
    {
        if (entity.worldObj.isRemote)
        {
            HooksClient.particlePlayerOrigin = entity;
        }
    }

    public static boolean netherPortalTeleports()
    {
        return Settings.netherPortalsAllowTeleportation[1] || !SprinklesForVanilla.isOnServer;
    }

    public static boolean portalBlocksAreCreated()
    {
        return Settings.netherPortalBlocksAreGenerated[1] || !SprinklesForVanilla.isOnServer;
    }

    public static boolean spawnZombiePigmen(World world, Random rand)
    {
        double difficultyID = (float) world.difficultySetting.getDifficultyId();
        double multiplier = !SprinklesForVanilla.isOnServer ? 1 : Settings.zombiePigmanNetherPortalSpawnMult[1];
        double randomVal = rand.nextDouble();
        double chance = (difficultyID * multiplier) / 2000.0f;
        return randomVal < chance;
    }

    public static boolean canMobGrief(World world, String griefType)
    {
        if (Settings.mobGriefingOverride[1] && SprinklesForVanilla.isOnServer)
        {
            int index = Arrays.asList(Settings.mobGriefingTypes).indexOf(griefType);
            return index != -1 ? Settings.mobGriefingConfigs[1].get(index) : world.getGameRules().getGameRuleBooleanValue("mobGriefing");
        }
        return world.getGameRules().getGameRuleBooleanValue("mobGriefing");
    }

    public static boolean canMobGrief(int preset, World world, String griefType)
    {
        return preset == 1 && canMobGrief(world, griefType);
    }

    public static boolean isBeaconBase(Block block, IBlockAccess worldObj, int x, int y, int z)
    {
        if (SprinklesForVanilla.isOnServer)
        {
            for (String beaconBase : Settings.beaconBaseBlocks[1])
            {
                if (beaconBase.startsWith("#"))
                {
                    continue;
                }
                String[] blockNameData = beaconBase.split(":");
                if (block == Block.blockRegistry.getObject(blockNameData[0] + ":" + blockNameData[1]))
                {
                    if (blockNameData.length > 2)
                    {
                        if (worldObj.getBlockMetadata(x, y, z) == Integer.parseInt(blockNameData[2]))
                        {
                            return true;
                        }
                    }
                    else
                    {
                        return true;
                    }
                }
            }
            return false;
        }
        else
        {
            return block == Blocks.emerald_block || block == Blocks.gold_block || block == Blocks.diamond_block || block == Blocks.iron_block;
        }
    }

    public static boolean allowOtherDimensions()
    {
        return !SprinklesForVanilla.isOnServer || Settings.otherDimensionsCancelSleep[1];
    }

    public static boolean respawnInNether()
    {
        return SprinklesForVanilla.isOnServer && Settings.allowNetherRespawn[1];
    }

    public static boolean respawnInEnd()
    {
       return SprinklesForVanilla.isOnServer && Settings.allowEndRespawn[1];
    }

    public static boolean allowWater()
    {
        return SprinklesForVanilla.isOnServer && Settings.allowWaterInNether[1];
    }

    public static boolean createObsidian(World world)
    {
        boolean notOnServer = !SprinklesForVanilla.isOnServer;
        boolean doesNotContain = !Settings.waterAndLavaMakesObsidianBlacklist[1].contains(world.provider.dimensionId);
        return notOnServer || doesNotContain;
    }

    public static boolean createCobblestone(World world)
    {
        boolean notOnServer = !SprinklesForVanilla.isOnServer;
        boolean doesNotContain = !Settings.waterAndLavaMakesCobbleBlacklist[1].contains(world.provider.dimensionId);
        return notOnServer || doesNotContain;
    }

    public static ChunkCoordinates getSpawnPoint(ChunkCoordinates chunkCoordinates, EntityPlayerMP player)
    {
        if (player.worldObj.isRemote || !SprinklesForVanilla.isOnServer)
        {
            return chunkCoordinates;
        }
        int dimID = player.dimension;
        ChunkCoordinates bedLocation = player.getBedLocation(dimID);
        if (bedLocation != null)
        {
            if (EntityPlayerMP.verifyRespawnCoordinates(player.worldObj, bedLocation, player.isSpawnForced(dimID)) != null)
            {
                return chunkCoordinates;
            }
        }
        String[] coordinates;
        switch (dimID)
        {
            case -1:
                coordinates = Settings.netherSpawnDefault[1].split(", ");
                break;
            case 0:
                coordinates = Settings.overworldSpawnDefault[1].split(", ");
                break;
            case 1:
                coordinates = Settings.endSpawnDefault[1].split(", ");
                break;
            default:
                return chunkCoordinates;
        }
        if (coordinates.length == 3)
        {
            try
            {
                int SpawnX = Integer.parseInt(coordinates[0]);
                int SpawnY = Integer.parseInt(coordinates[1]);
                int SpawnZ = Integer.parseInt(coordinates[2]);
                player.setPosition(SpawnX, SpawnY, SpawnZ);
                return new ChunkCoordinates(SpawnX, SpawnY, SpawnZ);
            }
            catch (NumberFormatException e)
            {
                LogHelper.warn("INCORRECT FORMATTING FOR DEFAULT SPAWN WITH DIM ID " + dimID);
                return chunkCoordinates;
            }
        }
        return chunkCoordinates;
    }

    public static boolean shouldBeaconCheckForSky()
    {
        return SprinklesForVanilla.isOnServer && Settings.shouldBeaconCheckForSunlight[1];
    }

    public static long getSpawnTicksWait()
    {
        return SprinklesForVanilla.isOnServer ? InitMobRegistry.gcdPassiveSpawn : 400L;
    }

    public static int getMinimumLightSapling()
    {
        return SprinklesForVanilla.isOnServer ? Settings.minimumSaplingLightLevel[1] : 9;
    }

    public static int getMinimumLightCrops()
    {
        return SprinklesForVanilla.isOnServer ? Settings.minimumCropsLightLevel[1] : 9;
    }

    public static byte getMaxChunkRadius()
    {
        return (byte) (SprinklesForVanilla.isOnServer ? Settings.maxChunkRadius[1] : 8);
    }

    public static double getMinBlockRadius()
    {
        return SprinklesForVanilla.isOnServer ? (double)(Settings.minBlockRadius[1]) : 24.0D;
    }

    public static boolean shouldEndPortalTeleport()
    {
        return !SprinklesForVanilla.isOnServer || Settings.endPortalsAllowTeleportation[1];
    }

    public static boolean shouldPortalBlockBeGenerated()
    {
        return !SprinklesForVanilla.isOnServer || Settings.endPortalBlocksAreGenerated[1];
    }
}
