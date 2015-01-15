package com.vikestep.sprinklesforvanilla.asm;

import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import com.vikestep.sprinklesforvanilla.common.util.LogHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Arrays;
import java.util.Calendar;

@SideOnly(Side.CLIENT)
public class HooksClient
{
    public static EntityPlayer particlePlayerOrigin;

    public static boolean particleIsAllowed(String particle)
    {
        EntityPlayer currentPlayer = Minecraft.getMinecraft().thePlayer;
        if (Settings.potionEffectsShown != 0 && particlePlayerOrigin != null)
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

    public static boolean isChristmasChest()
    {
        Calendar calendar = Calendar.getInstance();
        switch (Settings.christmasChest)
        {
            case 0:
                return (calendar.get(Calendar.MONTH) + 1 == 12 && calendar.get(Calendar.DATE) >= 24 && calendar.get(Calendar.DATE) <= 26);
            case 1:
                return true;
            case 2:
                return false;
            default:
                LogHelper.log("It seems that you have set the config christmasChest to " + Settings.christmasChest + ". The range for that is integers between 0 and 2");
                return false;
        }
    }
}
