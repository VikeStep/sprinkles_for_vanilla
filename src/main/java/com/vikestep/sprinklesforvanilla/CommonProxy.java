package com.vikestep.sprinklesforvanilla;

import com.vikestep.sprinklesforvanilla.common.handlers.*;
import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.ArrayUtils;

public class CommonProxy
{
    private static void registerHandler(Object eventHandler, boolean fml, boolean forge)
    {
        if (fml)
        {
            FMLCommonHandler.instance().bus().register(eventHandler);
        }
        if (forge)
        {
            MinecraftForge.EVENT_BUS.register(eventHandler);
        }
    }

    public void init()
    {
        registerHandler(new NetworkHandler(), true, false);
        //To reduce lag I will only register an event handler if the config value is modified
        if (Settings.overhaulSleep)
        {
            registerHandler(new SleepHandler(), true, true);
        }
        //Arrays.asList doesn't seem to like boolean[] so we use ArrayUtils
        if (ArrayUtils.contains(Settings.mobNameConfigs, false))
        {
            registerHandler(new MobHandler(), false, true);
        }
        if (!Settings.doEnderPearlsTeleport)
        {
            registerHandler(new EnderPearlHandler(), false, true);
        }
        if (Settings.keepHealth >= 0 || Settings.keepXP || Settings.keepHunger >= 0)
        {
            registerHandler(new RespawnHandler(), true, true);
        }
        if (ArrayUtils.contains(Settings.damageSourceConfigs, 1) || ArrayUtils.contains(Settings.damageSourceConfigs, 2))
        {
            registerHandler(new LivingAttackHandler(), false, true);
        }
        //If I ever turn Explosion configs into a boolean array, I'll check if they are turned on
        registerHandler(new ExplosionHandler(), false, true);
    }
}
