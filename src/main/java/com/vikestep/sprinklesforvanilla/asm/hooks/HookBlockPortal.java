package com.vikestep.sprinklesforvanilla.asm.hooks;

import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import net.minecraft.world.World;

import java.util.Random;

public class HookBlockPortal
{
    public static boolean netherPortalTeleports()
    {
        return Settings.netherPortalsCanTeleport;
    }

    public static boolean netherPortalPlaysSound()
    {
        return Settings.netherPortalsCreateSound;
    }

    public static boolean portalBlocksAreCreated()
    {
        return Settings.netherPortalsAreGenerated;
    }

    public static boolean spawnZombiePigmen(World world, Random rand)
    {
        double difficultyID = (float) world.difficultySetting.getDifficultyId();
        double multiplier = Settings.netherPortalPigmenSpawnMult;
        double randomVal = rand.nextDouble();
        double chance = (difficultyID * multiplier) / 2000.0f;
        return randomVal < chance;
    }
}
