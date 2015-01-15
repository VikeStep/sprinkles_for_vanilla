package com.vikestep.sprinklesforvanilla;

import com.vikestep.sprinklesforvanilla.common.configuration.ConfigurationHandler;
import com.vikestep.sprinklesforvanilla.common.init.initFireInfo;
import com.vikestep.sprinklesforvanilla.common.reference.ModInfo;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;

import java.util.Map;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.VERSION)
public class SprinklesForVanilla
{
    public static boolean isOnServer;

    @Mod.Instance(ModInfo.MOD_ID)
    public static SprinklesForVanilla instance;

    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY_PATH, serverSide = ModInfo.SERVER_PROXY_PATH)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event)
    {
        isOnServer = true;
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event)
    {
        initFireInfo.addFireInfo();
    }

    @NetworkCheckHandler
    public boolean checkIfServer(Map<String, String> serverMods, Side remoteSide)
    {
        for (Object modName : serverMods.keySet())
        {
            if (modName.equals("sprinkles_for_vanilla"))
            {
                isOnServer = true;
            }
        }
        return true;
    }
}
