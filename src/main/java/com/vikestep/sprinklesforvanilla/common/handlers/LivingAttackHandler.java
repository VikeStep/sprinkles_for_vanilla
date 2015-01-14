package com.vikestep.sprinklesforvanilla.common.handlers;

import com.vikestep.sprinklesforvanilla.SprinklesForVanilla;
import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import java.util.HashMap;

public class LivingAttackHandler
{
    private HashMap<String, Integer> stoppedDamageSources = new HashMap<String, Integer>();

    public LivingAttackHandler()
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
    }

    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event)
    {
        if (stoppedDamageSources.keySet().contains(event.source.damageType) && SprinklesForVanilla.isOnServer)
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
