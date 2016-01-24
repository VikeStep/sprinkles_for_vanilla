package io.github.vikestep.sprinklesforvanilla;

import io.github.vikestep.sprinklesforvanilla.common.modules.IModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.Modules;
import io.github.vikestep.sprinklesforvanilla.common.network.NetworkHandler;
import io.github.vikestep.sprinklesforvanilla.common.utils.ModuleHelper;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
    public void init()
    {
        MinecraftForge.EVENT_BUS.register(new NetworkHandler());
        Modules.modules.forEach(IModule::init);
        ModuleHelper.reloadModules();
    }
}
