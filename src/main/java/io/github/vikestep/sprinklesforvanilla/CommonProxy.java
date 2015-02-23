package io.github.vikestep.sprinklesforvanilla;

import cpw.mods.fml.common.FMLCommonHandler;
import io.github.vikestep.sprinklesforvanilla.common.handlers.PlayerHandlers;
import io.github.vikestep.sprinklesforvanilla.common.handlers.WorldHandlers;
import io.github.vikestep.sprinklesforvanilla.common.network.NetworkHandler;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
    public void init()
    {
        FMLCommonHandler.instance().bus().register(new NetworkHandler());
        PlayerHandlers.PlayerSleepHandler sleepHandler = new PlayerHandlers.PlayerSleepHandler();
        FMLCommonHandler.instance().bus().register(sleepHandler);
        MinecraftForge.EVENT_BUS.register(sleepHandler);
        MinecraftForge.EVENT_BUS.register(new WorldHandlers.ExplosionHandler());
    }
}
