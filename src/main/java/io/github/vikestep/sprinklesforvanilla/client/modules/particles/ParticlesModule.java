package io.github.vikestep.sprinklesforvanilla.client.modules.particles;

import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.ModuleBase;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

public class ParticlesModule extends ModuleBase
{
    private static ParticlesModule instance = null;

    protected ParticlesModule(String name, Side side)
    {
        super(name, side);
    }

    public static ParticlesModule getInstance()
    {
        if (instance == null)
        {
            instance = new ParticlesModule("particles", Side.CLIENT);
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
