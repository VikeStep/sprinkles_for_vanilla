package io.github.vikestep.sprinklesforvanilla.asm;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.vikestep.sprinklesforvanilla.common.configuration.Settings;
import io.github.vikestep.sprinklesforvanilla.common.utils.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Arrays;
import java.util.Calendar;

@SuppressWarnings("UnusedDeclaration")
@SideOnly(Side.CLIENT)
public class HooksClient
{
    public static Entity particlePlayerOrigin;

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
        return index == -1 || Settings.particlesShown.get(index);
        //This is so that minecraft can deal with it if its not in our particle list
    }

    public static boolean isChristmasChest()
    {
        Calendar calendar = Calendar.getInstance();
        switch (Settings.displayChristmasChest)
        {
            case 0:
                return (calendar.get(Calendar.MONTH) + 1 == 12 && calendar.get(Calendar.DATE) >= 24 && calendar.get(Calendar.DATE) <= 26);
            case 1:
                return true;
            case 2:
                return false;
            default:
                LogHelper.warn("It seems that you have set the config christmasChest to " + Settings.displayChristmasChest + ". The range for that is integers between 0 and 2");
                return false;
        }
    }
}
