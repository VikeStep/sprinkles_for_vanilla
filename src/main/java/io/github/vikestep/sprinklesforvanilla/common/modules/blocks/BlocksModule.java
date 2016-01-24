package io.github.vikestep.sprinklesforvanilla.common.modules.blocks;

import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.ModuleBase;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

public class BlocksModule extends ModuleBase
{
    private static BlocksModule instance = null;

    private BlocksModule()
    {
        super("blocks", Side.SERVER);
    }

    public static BlocksModule getInstance()
    {
        if (instance == null)
        {
            instance = new BlocksModule();
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
