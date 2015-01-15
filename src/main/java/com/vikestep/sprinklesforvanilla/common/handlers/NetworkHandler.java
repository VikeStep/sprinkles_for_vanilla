package com.vikestep.sprinklesforvanilla.common.handlers;

import com.vikestep.sprinklesforvanilla.SprinklesForVanilla;
import com.vikestep.sprinklesforvanilla.common.init.initFireInfo;
import com.vikestep.sprinklesforvanilla.common.util.LogHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;

public class NetworkHandler
{
    @SubscribeEvent
    public void onDisconnectServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        SprinklesForVanilla.isOnServer = false;
    }

    @SubscribeEvent
    public void onConnectServer(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        LogHelper.log(SprinklesForVanilla.isOnServer ? "sprinkles_for_vanilla found on server" : "sprinkles_for_vanilla not found on server, will only use clientside features");
        initFireInfo.addFireInfo();
    }
}
