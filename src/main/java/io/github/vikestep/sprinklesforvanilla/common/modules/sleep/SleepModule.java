package io.github.vikestep.sprinklesforvanilla.common.modules.sleep;

import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.ModuleBase;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class SleepModule extends ModuleBase
{
    private static SleepModule instance = null;

    protected SleepModule(String name, Side side)
    {
        super(name, side);
    }

    public static SleepModule getInstance()
    {
        if (instance == null)
        {
            instance = new SleepModule("sleep", Side.SERVER);
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
