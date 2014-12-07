package com.vikestep.sprinklesforvanilla.common.handlers;

import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;

public class SleepHandler
{
    @SubscribeEvent
    public void onPlayerSleep(PlayerSleepInBedEvent event)
    {
        if (Settings.isSleepDisabled)
        {
            event.result = EntityPlayer.EnumStatus.OTHER_PROBLEM;
        }
    }
}
