package io.github.vikestep.sprinklesforvanilla.common.modules.villagers;

import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.ModuleBase;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class VillagersModule extends ModuleBase
{
    private static VillagersModule instance = null;

    protected VillagersModule(String name, Side side)
    {
        super(name, side);
    }

    public static VillagersModule getInstance()
    {
        if (instance == null)
        {
            instance = new VillagersModule("villagers", Side.SERVER);
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
