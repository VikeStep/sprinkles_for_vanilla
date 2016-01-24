package io.github.vikestep.sprinklesforvanilla.common.modules.items;

import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.ModuleBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Arrays;
import java.util.List;

public class ItemsModule extends ModuleBase
{
    private static ItemsModule instance = null;

    private final EnderPearlHandler enderPearlHandler;

    private ItemsModule()
    {
        super("items", Side.SERVER);
        enderPearlHandler = new EnderPearlHandler();
    }

    public static ItemsModule getInstance()
    {
        if (instance == null)
        {
            instance = new ItemsModule();
        }
        return instance;
    }

    @Override
    public List<IProperty> getProperties()
    {
        return Arrays.asList(EnderPearlHandler.doEnderPearlsTeleport);
    }

    @Override
    public void enable()
    {
        MinecraftForge.EVENT_BUS.register(enderPearlHandler);
    }

    @Override
    public void disable()
    {
        MinecraftForge.EVENT_BUS.unregister(enderPearlHandler);
    }

    @Override
    public void init()
    {

    }
}
