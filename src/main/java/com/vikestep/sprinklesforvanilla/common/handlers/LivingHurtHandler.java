package com.vikestep.sprinklesforvanilla.common.handlers;

import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.HashMap;

public class LivingHurtHandler
{
    private HashMap<String, Integer> stoppedDamageSources = new HashMap<String, Integer>();

    public LivingHurtHandler()
    {
        for (int i = 0; i < Settings.damageSourceConfigs.length; i++)
        {
            int value = Settings.damageSourceConfigs[i];
            if (value == 1)
            {
                stoppedDamageSources.put(Settings.damageSources[i], 1);
            }
            else if (value == 2)
            {
                stoppedDamageSources.put(Settings.damageSources[i], 2);
            }
        }
        System.out.println(stoppedDamageSources);
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event)
    {
        if (stoppedDamageSources.keySet().contains(event.source.damageType))
        {
            int value = stoppedDamageSources.get(event.source.damageType);
            if (value == 2)
            {
                event.setCanceled(true);
            }
            else if (value == 1 && event.entity instanceof EntityPlayer)
            {
                event.setCanceled(true);
            }
        }
    }
}
