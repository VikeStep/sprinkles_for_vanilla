package io.github.vikestep.sprinklesforvanilla.common.modules.mobs;

import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.ModuleBase;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

public class MobsModule extends ModuleBase
{
    private static MobsModule instance = null;

    protected MobsModule(String name, Side side)
    {
        super(name, side);
    }

    public static MobsModule getInstance()
    {
        if (instance == null)
        {
            instance = new MobsModule("mobs", Side.SERVER);
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
