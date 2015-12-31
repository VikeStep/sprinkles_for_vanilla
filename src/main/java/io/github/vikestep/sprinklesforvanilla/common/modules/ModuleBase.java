package io.github.vikestep.sprinklesforvanilla.common.modules;

import net.minecraftforge.fml.relauncher.Side;

public abstract class ModuleBase implements IModule
{
    String name;
    Side side;

    public ModuleBase(String name, Side side)
    {
        this.name = name;
        this.side = side;
    }

    @Override
    public String getModuleName()
    {
        return name;
    }

    @Override
    public Side getModuleSide()
    {
        return side;
    }
}
