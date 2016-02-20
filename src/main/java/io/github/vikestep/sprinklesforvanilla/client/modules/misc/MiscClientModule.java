package io.github.vikestep.sprinklesforvanilla.client.modules.misc;

import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.ModuleBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Arrays;
import java.util.List;

public class MiscClientModule extends ModuleBase
{
    private static MiscClientModule instance = null;

    private final MiscClientHandler miscClientHandler;

    private MiscClientModule()
    {
        super("misc", Side.CLIENT);
        miscClientHandler = new MiscClientHandler();
    }

    public static MiscClientModule getInstance()
    {
        if (instance == null)
        {
            instance = new MiscClientModule();
        }
        return instance;
    }

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    @Override
    public List<IProperty> getProperties()
    {
        return Arrays.asList(MiscClientHandler.shouldSkipRespawnScreen);
    }

    @Override
    public void enable()
    {
        MinecraftForge.EVENT_BUS.register(miscClientHandler);
    }

    @Override
    public void disable()
    {
        MinecraftForge.EVENT_BUS.unregister(miscClientHandler);
    }

    @Override
    public void init()
    {

    }
}
