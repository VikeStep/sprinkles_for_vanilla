package io.github.vikestep.sprinklesforvanilla.common.modules.beacons;

import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.ModuleBase;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

public class BeaconsModule extends ModuleBase
{
    private static BeaconsModule instance = null;

    private BeaconsModule()
    {
        super("beacons", Side.SERVER);
    }

    public static BeaconsModule getInstance()
    {
        if (instance == null)
        {
            instance = new BeaconsModule();
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
