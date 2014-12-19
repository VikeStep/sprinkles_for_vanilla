package com.vikestep.sprinklesforvanilla.asm.hooks;

import com.vikestep.sprinklesforvanilla.common.reference.Settings;

import java.util.Arrays;

public class HookRenderGlobal
{
    public static boolean particleIsAllowed(String particle)
    {
        int index = Arrays.asList(Settings.particleNames).indexOf(particle);
        if (index != -1)
        {
            return Settings.particleNameConfigs[index];
        }
        else
        {
            //This is so that minecraft can deal with it if its not in our particle list
            return true;
        }
    }
}
