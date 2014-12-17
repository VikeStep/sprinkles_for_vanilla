package com.vikestep.sprinklesforvanilla.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;

import java.util.Map;

@SuppressWarnings("unused")
@MCVersion("1.7.10")
public class SprinklesForVanillaPlugin implements IFMLLoadingPlugin
{
    @Override
    public String[] getASMTransformerClass()
    {
        return new String[] {"com.vikestep.sprinklesforvanilla.asm.SprinklesForVanillaTransformer"};
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {
        ;
    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }
}
