package com.vikestep.sprinklesforvanilla.asm.hooks;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class HookEntityLivingBase
{
    private static EntityPlayer particlePlayerOrigin;
    private static String       particleType;

    public static void particleSpawnedFromEntity(EntityLivingBase entity, String particle)
    {
        if (entity instanceof EntityPlayer)
        {
            particlePlayerOrigin = (EntityPlayer) entity;
            particleType = particle;
        }
        else
        {
            particlePlayerOrigin = null;
            particleType = null;
        }
    }

    public static EntityPlayer getParticleOrigin()
    {
        return particlePlayerOrigin;
    }

    public static String getParticleType()
    {
        return particleType;
    }

    public static void removeParticleOrigin()
    {
        particlePlayerOrigin = null;
    }
}
