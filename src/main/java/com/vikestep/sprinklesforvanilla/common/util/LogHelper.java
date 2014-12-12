package com.vikestep.sprinklesforvanilla.common.util;

import com.vikestep.sprinklesforvanilla.common.reference.ModInfo;
import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

public class LogHelper
{
    public static void log(Object logContent)
    {
        FMLLog.log(ModInfo.MOD_NAME, Level.INFO, String.valueOf(logContent));
    }
}
