package io.github.vikestep.sprinklesforvanilla.common.modules.portals;

import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.ModuleBase;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

public class PortalsModule extends ModuleBase
{
    private static PortalsModule instance = null;

    private PortalsModule()
    {
        super("portals", Side.SERVER);
    }

    public static PortalsModule getInstance()
    {
        if (instance == null)
        {
            instance = new PortalsModule();
        }
        return instance;
    }

    @Override
    public List<IProperty> getProperties()
    {
        return new ArrayList<>();
    }

    @Override
    public void enable()
    {

    }

    @Override
    public void disable()
    {

    }

    @Override
    public void init()
    {

    }
}
