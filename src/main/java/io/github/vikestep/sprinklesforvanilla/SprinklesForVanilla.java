package io.github.vikestep.sprinklesforvanilla;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import io.github.vikestep.sprinklesforvanilla.common.configuration.ConfigurationHandler;
import io.github.vikestep.sprinklesforvanilla.common.configuration.Settings;
import io.github.vikestep.sprinklesforvanilla.common.network.ConfigPacket;
import io.github.vikestep.sprinklesforvanilla.common.network.GenericPacket;
import io.github.vikestep.sprinklesforvanilla.common.reference.ModInfo;
import io.github.vikestep.sprinklesforvanilla.common.utils.MetadataHelper;

import java.util.Map;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.VERSION)
public class SprinklesForVanilla
{
    //When a client starts up the game they aren't instantly on a server
    public static boolean isOnServer = false;
    //Boolean to check if this is client or server (Initially shows if it's an integrated server)
    public static boolean isClient = true;
    //Our Network Wrapper used for sending packets
    public static SimpleNetworkWrapper network;

    @Mod.Instance
    public static SprinklesForVanilla instance;

    @Mod.Metadata
    public static ModMetadata metadata;

    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY_PATH, serverSide = ModInfo.SERVER_PROXY_PATH)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        //On an integrated server it will say that it is a client
        isClient = FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
        if (!isClient)
        {
            isOnServer = true;
        }

        //I register packets for both sides since I do not always have one side being sent to the other
        network = NetworkRegistry.INSTANCE.newSimpleChannel("sfv_channel");
        network.registerMessage(GenericPacket.Handler.class, GenericPacket.class, 0, Side.SERVER);
        //network.registerMessage(ConfigPacket.Handler.class, ConfigPacket.class, 0, Side.SERVER);
        network.registerMessage(GenericPacket.Handler.class, GenericPacket.class, 0, Side.CLIENT);
        network.registerMessage(ConfigPacket.Handler.class, ConfigPacket.class, 0, Side.CLIENT);

        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        MetadataHelper.transformMetadata(metadata);

        if (!isOnServer)
        {
            Settings.copyClientToServer();
        }

        proxy.init();
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event)
    {
        isOnServer = true;
        //isClient will be true if its an integrated server so it sets
        if (isClient)
        {
            Settings.copyClientToServer();
            isClient = false;
        }
    }

    @NetworkCheckHandler
    public boolean checkIfServer(Map<String, String> serverMods, Side remoteSide)
    {
        if (remoteSide == Side.CLIENT)
            return true;
        isOnServer = false;
        for (String modName : serverMods.keySet())
        {
            if (modName.equals("sprinkles_for_vanilla"))
            {
                isOnServer = true;
            }
        }
        if (!isOnServer && ConfigurationHandler.configLoaded)
        {
            Settings.copyClientToServer();
        }
        return true;
    }
}
