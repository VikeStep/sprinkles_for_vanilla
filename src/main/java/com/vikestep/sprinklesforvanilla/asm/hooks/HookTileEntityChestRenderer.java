package com.vikestep.sprinklesforvanilla.asm.hooks;

import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import com.vikestep.sprinklesforvanilla.common.util.LogHelper;

import java.util.Calendar;

@SuppressWarnings("MagicConstant")
public class HookTileEntityChestRenderer
{
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
}
