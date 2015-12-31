package io.github.vikestep.sprinklesforvanilla.common.modules.portals;

import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.ModuleBase;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class PortalsModule extends ModuleBase
{
    private static PortalsModule instance = null;

    protected PortalsModule(String name, Side side)
    {
        super(name, side);
    }

    public static PortalsModule getInstance()
    {
        if (instance == null)
        {
            instance = new PortalsModule("portals", Side.SERVER);
        }
        return instance;
    }

    @Override
    public List<IProperty> getProperties()
    {
        return null;
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
