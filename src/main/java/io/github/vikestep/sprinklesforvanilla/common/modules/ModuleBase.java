package io.github.vikestep.sprinklesforvanilla.common.modules;

import net.minecraftforge.fml.relauncher.Side;

public abstract class ModuleBase implements IModule
{
    private final String  name;
    private final Side    side;
    private       boolean isEnabled;

    protected ModuleBase(String name, Side side)
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

    @Override
    public boolean isEnabled()
    {
        return isEnabled;
    }

    @Override
    public void setEnabled(boolean isEnabled)
    {
        this.isEnabled = isEnabled;
    }
}
