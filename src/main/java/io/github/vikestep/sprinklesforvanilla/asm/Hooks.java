package io.github.vikestep.sprinklesforvanilla.asm;

import io.github.vikestep.sprinklesforvanilla.SprinklesForVanilla;
import io.github.vikestep.sprinklesforvanilla.common.configuration.Settings;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Random;

public class Hooks
{
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
}
