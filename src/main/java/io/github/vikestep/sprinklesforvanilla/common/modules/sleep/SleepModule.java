package io.github.vikestep.sprinklesforvanilla.common.modules.sleep;

import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.ModuleBase;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

public class SleepModule extends ModuleBase
{
    private static SleepModule instance = null;

    private SleepModule()
    {
        super("sleep", Side.SERVER);
    }

    public static SleepModule getInstance()
    {
        if (instance == null)
        {
            instance = new SleepModule();
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
