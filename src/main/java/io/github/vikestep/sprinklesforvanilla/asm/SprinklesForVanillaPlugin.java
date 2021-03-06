package io.github.vikestep.sprinklesforvanilla.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.8.9")
@IFMLLoadingPlugin.TransformerExclusions({"io.github.vikestep.sprinklesforvanilla.asm"})
public class SprinklesForVanillaPlugin implements IFMLLoadingPlugin
{
    public static boolean isObf;

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[] {SprinklesForVanillaTransformer.class.getName()};
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
        isObf = (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }
}
