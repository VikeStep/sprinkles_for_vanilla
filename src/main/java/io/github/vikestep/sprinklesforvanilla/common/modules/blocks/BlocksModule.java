package io.github.vikestep.sprinklesforvanilla.common.modules.blocks;

import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.ModuleBase;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class BlocksModule extends ModuleBase
{
    private static BlocksModule instance = null;

    protected BlocksModule(String name, Side side)
    {
        super(name, side);
    }

    public static BlocksModule getInstance()
    {
        if (instance == null)
        {
            instance = new BlocksModule("blocks", Side.SERVER);
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
