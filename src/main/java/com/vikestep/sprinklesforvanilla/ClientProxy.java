package com.vikestep.sprinklesforvanilla;

import com.vikestep.sprinklesforvanilla.client.handlers.SoundHandler;
import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    @Override
    public void init()
    {
        super.init();
        if (Settings.disabledSounds != new String[] {"#minecraft:mob.wither.spawn", "#minecraft:mob.enderdragon.end", "#minecraft:portal.portal"})
        {
            MinecraftForge.EVENT_BUS.register(new SoundHandler());
        }
    }
}
