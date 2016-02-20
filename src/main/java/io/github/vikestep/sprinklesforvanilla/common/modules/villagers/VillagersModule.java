package io.github.vikestep.sprinklesforvanilla.common.modules.villagers;

import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.ModuleBase;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

public class VillagersModule extends ModuleBase
{
    private static VillagersModule instance = null;

    private VillagersModule()
    {
        super("villagers", Side.SERVER);
    }

    public static VillagersModule getInstance()
    {
        if (instance == null)
        {
            instance = new VillagersModule();
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
