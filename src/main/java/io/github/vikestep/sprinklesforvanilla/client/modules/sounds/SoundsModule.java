package io.github.vikestep.sprinklesforvanilla.client.modules.sounds;

import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.ModuleBase;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class SoundsModule extends ModuleBase
{
    private static SoundsModule instance = null;

    protected SoundsModule(String name, Side side)
    {
        super(name, side);
    }

    public static SoundsModule getInstance()
    {
        if (instance == null)
        {
            instance = new SoundsModule("sounds", Side.CLIENT);
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
