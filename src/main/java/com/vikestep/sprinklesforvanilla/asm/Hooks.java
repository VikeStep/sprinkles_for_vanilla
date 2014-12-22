package com.vikestep.sprinklesforvanilla.asm;

import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import com.vikestep.sprinklesforvanilla.common.util.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

public class Hooks
{
    private static EntityPlayer particlePlayerOrigin;
    private static String       particleType;

    public static boolean isChristmasChest()
    {
        Calendar calendar = Calendar.getInstance();
        switch (Settings.christmasChest)
        {
            case 0:
                return (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26);
            case 1:
                return true;
            case 2:
                return false;
            default:
                LogHelper.log("It seems that you have set the config christmasChest to " + Settings.christmasChest + ". The range for that is integers between 0 and 2");
                return false;
        }
    }

    public static boolean particleIsAllowed(String particle)
    {
        EntityPlayer currentPlayer = Minecraft.getMinecraft().thePlayer;
        if (Settings.potionEffectsShown != 0)
        {
            if (currentPlayer == particlePlayerOrigin)
            {
                particlePlayerOrigin = null;
                return false;
            }
            return Settings.potionEffectsShown == 1;
        }

        if (particle.contains("_"))
        {
            particle = particle.split("_")[0] + "_";
        }

        int index = Arrays.asList(Settings.particleNames).indexOf(particle);
        return index == -1 || Settings.particleNameConfigs[index];
        //This is so that minecraft can deal with it if its not in our particle list
    }

    public static void particleSpawnedFromEntity(EntityLivingBase entity, String particle)
    {
        if (entity instanceof EntityPlayer)
        {
            particlePlayerOrigin = (EntityPlayer) entity;
            particleType = particle;
        }
        else
        {
            particlePlayerOrigin = null;
            particleType = null;
        }
    }

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
