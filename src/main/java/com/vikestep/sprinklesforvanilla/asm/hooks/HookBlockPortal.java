package com.vikestep.sprinklesforvanilla.asm.hooks;

import com.vikestep.sprinklesforvanilla.common.util.LogHelper;
import net.minecraft.world.World;

import java.util.Random;

public class HookBlockPortal
{
    //TODO: Link this to config values
    public static boolean netherPortalTeleports()
    {
        return true;
    }

    public static boolean portalBlocksAreCreated()
    {
        return false;
    }

    public static boolean spawnZombiePigmen(World world, Random rand)
    {
        boolean zombiePigmenSpawnAtPortal = true;
        float difficultyID = (float) world.difficultySetting.getDifficultyId();
        float multiplier = 1.0f;
        float randomVal = rand.nextFloat();
        float chance = (difficultyID * multiplier) / 2000.0f;
        if (randomVal < chance && zombiePigmenSpawnAtPortal)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
