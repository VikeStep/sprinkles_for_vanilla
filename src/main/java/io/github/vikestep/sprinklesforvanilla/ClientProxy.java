package io.github.vikestep.sprinklesforvanilla;

import io.github.vikestep.sprinklesforvanilla.client.handlers.ClientHandlers;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    @Override
    public void init()
    {
        super.init();
        MinecraftForge.EVENT_BUS.register(new ClientHandlers.SoundHandler());
        MinecraftForge.EVENT_BUS.register(new ClientHandlers.GuiHandler());
    }
}
