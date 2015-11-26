package io.github.vikestep.sprinklesforvanilla;

import io.github.vikestep.sprinklesforvanilla.common.configuration.ConfigurationHandler;
import io.github.vikestep.sprinklesforvanilla.common.reference.ModInfo;
import io.github.vikestep.sprinklesforvanilla.common.utils.MetadataHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

@SuppressWarnings("UnusedDeclaration")
@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.VERSION)
public class SprinklesForVanilla
{
    //Check whether mod is loaded serverside
    public static boolean isOnServer = false;
    //Boolean to check if this is client or server (Initially shows if it's an integrated server)
    public static boolean isClient = true;

    @Mod.Instance
    public static SprinklesForVanilla instance;

    @Mod.Metadata
    public static ModMetadata metadata;

    //Our Network Wrapper used for sending packets
    public static SimpleNetworkWrapper network;

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
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        MetadataHelper.transformMetadata(metadata);
        proxy.init();
    }

    @SuppressWarnings("SameReturnValue")
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
        return true;
    }
}
