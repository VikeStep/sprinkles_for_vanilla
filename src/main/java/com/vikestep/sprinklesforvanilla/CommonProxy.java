package com.vikestep.sprinklesforvanilla;

import com.vikestep.sprinklesforvanilla.common.handlers.*;
import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.ArrayUtils;

public class CommonProxy
{
    public void init()
    {
        //To reduce lag I will only register an event handler if the config value is modified
        if (Settings.overhaulSleep)
        {
            SleepHandler sleepHandler = new SleepHandler();
            MinecraftForge.EVENT_BUS.register(sleepHandler);
            FMLCommonHandler.instance().bus().register(sleepHandler);
        }
        //Arrays.asList doesn't seem to like boolean[] so we use ArrayUtils
        if (ArrayUtils.contains(Settings.mobNameConfigs, false))
        {
            MinecraftForge.EVENT_BUS.register(new MobHandler());
        }
        if (!Settings.doEnderPearlsTeleport)
        {
            MinecraftForge.EVENT_BUS.register(new EnderPearlHandler());
        }
        if (Settings.keepHealth >= 0 || Settings.keepXP || Settings.keepHunger >= 0)
        {
            RespawnHandler respawnHandler = new RespawnHandler();
            MinecraftForge.EVENT_BUS.register(respawnHandler);
            FMLCommonHandler.instance().bus().register(respawnHandler);
        }
        //If I ever turn Explosion configs into a boolean array, I'll check if they are turned on
        MinecraftForge.EVENT_BUS.register(new ExplosionHandler());
    }
}
