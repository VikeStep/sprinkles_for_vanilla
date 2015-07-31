package io.github.vikestep.sprinklesforvanilla.common.init;

import io.github.vikestep.sprinklesforvanilla.common.configuration.Settings;
import net.minecraft.block.Block;

public class InitLightLevels
{
    public static void tweakLightValues()
    {
        for (String entry : Settings.blockLightValues[1])
        {
            if (entry.startsWith("#"))
                continue;
            String[] args = entry.replaceAll("\\s+", "").split(",");
            if (args.length == 2)
            {
                Block block = Block.getBlockFromName(args[0]);
                if (block != null)
                {
                    int lightLevel = Integer.parseInt(args[1]);
                    Float adjustedLightLevel = (lightLevel + 1)/16F;
                    block.setLightLevel(adjustedLightLevel);
                }
            }
        }
    }
}
