package io.github.vikestep.sprinklesforvanilla.common.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import io.github.vikestep.sprinklesforvanilla.SprinklesForVanilla;
import io.github.vikestep.sprinklesforvanilla.common.configuration.Settings;
import io.github.vikestep.sprinklesforvanilla.common.utils.LogHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.world.Explosion;
import net.minecraftforge.event.world.ExplosionEvent;

public class WorldHandlers
{
    public static class ExplosionHandler
    {
        @SubscribeEvent
        public void onExplosionStart(ExplosionEvent.Start event)
        {
            if (!SprinklesForVanilla.isOnServer)
            {
                return;
            }
            Explosion explosion = event.explosion;
            if (Settings.enableExplosionLogging[1])
            {
                LogHelper.info("Explosion: " + EntityList.getEntityString(explosion.exploder) + ", " + explosion.explosionSize + ", " + !event.isCanceled() + ", " + Boolean.toString(true) + ", " + explosion.isFlaming + ", " + explosion.isSmoking);
            }
            if (Settings.disableAllExplosions[1])
            {
                event.setCanceled(true);
                return;
            }
            for (String explosionDatum : Settings.explosionData[1])
            {
                String[] data = explosionDatum.replace(", ", ",").split(",");
                String exploderName = data[0];
                String explosionSize = data[1];
                boolean isEnabled = Boolean.parseBoolean(data[2]);
                boolean isFlaming = Boolean.parseBoolean(data[4]);
                boolean isSmoking = Boolean.parseBoolean(data[5]);

                if (isNotCorrectConfig(exploderName, explosion.exploder))
                {
                    continue;
                }
                if (!isEnabled)
                {
                    event.setCanceled(true);
                    return;
                }
                if (explosionSize.substring(explosionSize.length() - 1).equals("x"))
                {
                    try
                    {
                        explosion.explosionSize *= Float.parseFloat(explosionSize.substring(0, explosionSize.length() - 1));
                    }
                    catch (NumberFormatException e)
                    {
                        LogHelper.warn("Explosion size is invalid in config: " + explosionDatum);
                    }
                }
                else
                {
                    try
                    {
                        explosion.explosionSize = Float.parseFloat(explosionSize);
                    }
                    catch (NumberFormatException e)
                    {
                        LogHelper.warn("Explosion size is invalid in config: " + explosionDatum);
                    }
                }
                explosion.isFlaming = isFlaming;
                explosion.isSmoking = isSmoking;
                return;
            }
        }

        @SubscribeEvent
        public void onExplosionDetonate(ExplosionEvent.Detonate event)
        {
            if (!SprinklesForVanilla.isOnServer)
            {
                return;
            }
            Explosion explosion = event.explosion;
            for (String explosionDatum : Settings.explosionData[1])
            {
                String[] data = explosionDatum.replace(", ", ",").split(",");
                String exploderName = data[0];
                boolean doesDamage = Boolean.parseBoolean(data[3]);

                if (isNotCorrectConfig(exploderName, explosion.exploder))
                {
                    continue;
                }

                if (!doesDamage)
                {
                    event.getAffectedEntities().clear();
                }

                return;
            }
        }

        public boolean isNotCorrectConfig(String configExploderName, Entity exploder)
        {
            String exploderInternalName = EntityList.getEntityString(exploder);
            if (!configExploderName.equals(exploderInternalName))
            {
                //Charged Creeper Handling
                if (configExploderName.equals("ChargedCreeper") && exploderInternalName.equals("Creeper"))
                {
                    if (((EntityCreeper) exploder).getPowered())
                    {
                        return false;
                    }
                }
                //TODO Add events for Ender Crystal, Bed and Ghast Fireball
            }
            else
            {
                //Handles detecting a charged creeper explosion when under Creeper exploderName
                if (configExploderName.equals("Creeper"))
                {
                    if (!((EntityCreeper) exploder).getPowered())
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
            return true;
        }
    }
}
