package com.vikestep.sprinklesforvanilla.common.handlers;

import com.vikestep.sprinklesforvanilla.SprinklesForVanilla;
import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class EnderPearlHandler
{
    @SubscribeEvent
    public void onEnderPearlImpact(EnderTeleportEvent event)
    {
        if (!Settings.doEnderPearlsTeleport && SprinklesForVanilla.isOnServer)
        {
            event.setCanceled(true);
        }
    }
}
