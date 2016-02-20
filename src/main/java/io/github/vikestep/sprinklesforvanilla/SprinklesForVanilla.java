package io.github.vikestep.sprinklesforvanilla;

import io.github.vikestep.sprinklesforvanilla.common.configuration.ConfigurationHandler;
import io.github.vikestep.sprinklesforvanilla.common.network.ConfigPacket;
import io.github.vikestep.sprinklesforvanilla.common.reference.ModInfo;
import io.github.vikestep.sprinklesforvanilla.common.utils.MetadataHelper;
import io.github.vikestep.sprinklesforvanilla.common.utils.ModuleHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.VERSION)
public class SprinklesForVanilla
{
    //Boolean to check if this is running client-side or server-side
    public static boolean isClient = true;
    @Mod.Instance
    public static SprinklesForVanilla  instance;
    @SuppressWarnings("unused")
    @Mod.Metadata
    private static ModMetadata          metadata;
    //Our Network Wrapper used for sending packets
    public static SimpleNetworkWrapper network;
    @SuppressWarnings("unused")
    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY_PATH, serverSide = ModInfo.SERVER_PROXY_PATH)
    private static CommonProxy          proxy;
    //Check whether mod is loaded server-side
    private static boolean isOnServer = false;

    public static boolean isOnServer()
    {
        return isOnServer;
    }

    public static void setIsOnServer(boolean isOnServer)
    {
        if (SprinklesForVanilla.isOnServer != isOnServer)
        {
            SprinklesForVanilla.isOnServer = isOnServer;
            ModuleHelper.reloadModules();
            return;
        }
        SprinklesForVanilla.isOnServer = isOnServer;
    }

    @SuppressWarnings("UnusedDeclaration")
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MetadataHelper.transformMetadata(metadata);
        //On an integrated server it will say that it is a client
        isClient = FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
        if (!isClient)
        {
            setIsOnServer(true);
        }

        network = NetworkRegistry.INSTANCE.newSimpleChannel("sfv_channel");
        network.registerMessage(ConfigPacket.Handler.class, ConfigPacket.class, 0, Side.CLIENT);

        // Loads all config values, the server-side configs will be overwritten if client-side
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
    }

    @SuppressWarnings("UnusedDeclaration")
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
    }

    @SuppressWarnings("UnusedDeclaration")
    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event)
    {
        setIsOnServer(true);
        isClient = false;
    }

    @SuppressWarnings({"SameReturnValue", "UnusedDeclaration"})
    @NetworkCheckHandler
    public boolean checkIfServer(Map<String, String> serverMods, Side remoteSide)
    {
        if (remoteSide != Side.CLIENT)
        {
            setIsOnServer(false);
            serverMods.keySet().stream().filter(m -> m.equals("sprinkles_for_vanilla")).forEach(m -> isOnServer = true);
        }
        return true;
    }
}
