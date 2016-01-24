package io.github.vikestep.sprinklesforvanilla.client.modules.sounds;

import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.ModuleBase;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

public class SoundsModule extends ModuleBase
{
    private static SoundsModule instance = null;

    private SoundsModule()
    {
        super("sounds", Side.CLIENT);
    }

    public static SoundsModule getInstance()
    {
        if (instance == null)
        {
            instance = new SoundsModule();
        }
        return instance;
    }

    @Override
    public List<IProperty> getProperties()
    {
        return new ArrayList<IProperty>();
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
