package io.github.vikestep.sprinklesforvanilla;

import io.github.vikestep.sprinklesforvanilla.common.modules.IModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.Modules;
import io.github.vikestep.sprinklesforvanilla.common.network.NetworkHandler;
import io.github.vikestep.sprinklesforvanilla.common.utils.ModuleHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommonProxy
{
    public void init()
    {
        FMLCommonHandler.instance().bus().register(new NetworkHandler());
        Modules.modules.forEach(IModule::init);
        ModuleHelper.reloadModules();
    }
}
