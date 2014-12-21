package com.vikestep.sprinklesforvanilla.asm.hooks;

import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Arrays;

public class HookRenderGlobal
{
    public static boolean particleIsAllowed(String particle)
    {
        EntityPlayer currentPlayer = Minecraft.getMinecraft().thePlayer;
        if (Settings.potionEffectsShown != 0)
        {
            if (currentPlayer == HookEntityLivingBase.getParticleOrigin())
            {
                HookEntityLivingBase.removeParticleOrigin();
                return false;
            }
            return Settings.potionEffectsShown == 1;
        }

        if (particle.contains("_"))
        {
            particle = particle.split("_")[0] + "_";
        }

        int index = Arrays.asList(Settings.particleNames).indexOf(particle);
        return index == -1 || Settings.particleNameConfigs[index];
        //This is so that minecraft can deal with it if its not in our particle list
    }
}
