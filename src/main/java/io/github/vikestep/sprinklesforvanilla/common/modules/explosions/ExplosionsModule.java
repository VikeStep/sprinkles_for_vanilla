package io.github.vikestep.sprinklesforvanilla.common.modules.explosions;

import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.ModuleBase;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

public class ExplosionsModule extends ModuleBase
{
    private static ExplosionsModule instance = null;

    protected ExplosionsModule(String name, Side side)
    {
        super(name, side);
    }

    public static ExplosionsModule getInstance()
    {
        if (instance == null)
        {
            instance = new ExplosionsModule("explosions", Side.SERVER);
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
