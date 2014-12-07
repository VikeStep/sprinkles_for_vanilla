package com.vikestep.sprinklesforvanilla;

import com.vikestep.sprinklesforvanilla.common.handlers.SleepHandler;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
    public void init() {
        MinecraftForge.EVENT_BUS.register(new SleepHandler());
    }
}
