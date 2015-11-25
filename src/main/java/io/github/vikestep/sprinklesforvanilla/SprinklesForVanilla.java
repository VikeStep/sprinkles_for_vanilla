package io.github.vikestep.sprinklesforvanilla;

import io.github.vikestep.sprinklesforvanilla.common.configuration.ConfigurationHandler;
import io.github.vikestep.sprinklesforvanilla.common.reference.ModInfo;
import io.github.vikestep.sprinklesforvanilla.common.utils.MetadataHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@SuppressWarnings("UnusedDeclaration")
@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.VERSION)
public class SprinklesForVanilla
{
    @Mod.Instance
    public static SprinklesForVanilla instance;

    @Mod.Metadata
    public static ModMetadata metadata;

    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY_PATH, serverSide = ModInfo.SERVER_PROXY_PATH)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        MetadataHelper.transformMetadata(metadata);
        proxy.init();
    }
}
