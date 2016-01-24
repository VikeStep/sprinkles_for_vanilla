package io.github.vikestep.sprinklesforvanilla.client.modules.sounds;

import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.ModuleBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Arrays;
import java.util.List;

public class SoundsModule extends ModuleBase
{
    private static SoundsModule instance = null;

    private final SoundHandler soundHandler;

    private SoundsModule()
    {
        super("sounds", Side.CLIENT);
        soundHandler = new SoundHandler();
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
        return Arrays.asList(SoundHandler.soundsToStop);
    }

    @Override
    public void enable()
    {
        MinecraftForge.EVENT_BUS.register(soundHandler);
    }

    @Override
    public void disable()
    {
        MinecraftForge.EVENT_BUS.unregister(soundHandler);
    }

    @Override
    public void init()
    {

    }
}
