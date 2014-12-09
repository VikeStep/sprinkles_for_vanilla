package com.vikestep.sprinklesforvanilla;

import com.vikestep.sprinklesforvanilla.common.handlers.MobHandler;
import com.vikestep.sprinklesforvanilla.common.handlers.SleepHandler;
import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
    public void init() {
        if (Settings.overhaulSleep)
        {
            MinecraftForge.EVENT_BUS.register(new SleepHandler());
            FMLCommonHandler.instance().bus().register(new SleepHandler());
        }
        MinecraftForge.EVENT_BUS.register(new MobHandler());
    }
}
