package io.github.vikestep.sprinklesforvanilla.common.modules.players;

import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.ModuleBase;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

public class PlayersModule extends ModuleBase
{
    private static PlayersModule instance = null;

    protected PlayersModule(String name, Side side)
    {
        super(name, side);
    }

    public static PlayersModule getInstance()
    {
        if (instance == null)
        {
            instance = new PlayersModule("players", Side.SERVER);
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
