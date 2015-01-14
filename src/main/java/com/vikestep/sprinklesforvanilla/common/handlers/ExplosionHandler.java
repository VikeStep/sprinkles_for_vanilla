package com.vikestep.sprinklesforvanilla.common.handlers;

import com.vikestep.sprinklesforvanilla.SprinklesForVanilla;
import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ExplosionEvent;

public class ExplosionHandler
{
    private static boolean playerSleepInNether;

    @SubscribeEvent
    public void onExplosion(ExplosionEvent event)
    {
        if (SprinklesForVanilla.isOnServer)
        {
            if (!Settings.explosionsAreEnabled)
            {
                event.setCanceled(true);
            }
            else
            {
                if (!Settings.TNTExplosionsAreEnabled && event.explosion.exploder instanceof EntityTNTPrimed)
                {
                    event.setCanceled(true);
                }
                if (event.explosion.exploder instanceof EntityCreeper)
                {
                    if (((EntityCreeper) event.explosion.exploder).getPowered() && !Settings.chargedCreeperExplosionsAreEnabled)
                    {
                        event.setCanceled(true);
                    }
                    else if (!((EntityCreeper) event.explosion.exploder).getPowered() && !Settings.creeperExplosionsAreEnabled)
                    {
                        event.setCanceled(true);
                    }
                }
                if (event.explosion.exploder instanceof EntityWither && !Settings.witherCreationExplosionsAreEnabled)
                {
                    event.setCanceled(true);
                }
                if (event.explosion.exploder instanceof EntityEnderCrystal && !Settings.enderCrystalExplosionsAreEnabled)
                {
                    event.setCanceled(true);
                }
                if (event.explosion.exploder instanceof EntityWitherSkull && !Settings.witherSkullProjectileExplosionsAreEnabled)
                {
                    event.setCanceled(true);
                }
                if (event.explosion.exploder instanceof EntityLargeFireball && !Settings.ghastFireballExplosionsAreEnabled)
                {
                    event.setCanceled(true);
                }
                if (playerSleepInNether && !Settings.bedExplosionsAreEnabled)
                {
                    event.setCanceled(true);
                    playerSleepInNether = false;
                }
            }
        }
    }

    @SubscribeEvent
    public void onBedActivated(PlayerInteractEvent event)
    {
        if (event.world.getBlock(event.x, event.y, event.z).isBed(event.world, event.x, event.y, event.z, event.entityPlayer) && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && SprinklesForVanilla.isOnServer)
        {
            if (!event.world.isRemote && (!event.world.provider.canRespawnHere() || event.world.getBiomeGenForCoords(event.x, event.z) == BiomeGenBase.hell))
            {
                playerSleepInNether = true;
            }
        }
    }
}
