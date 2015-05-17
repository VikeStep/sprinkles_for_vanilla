package io.github.vikestep.sprinklesforvanilla.common.handlers;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import io.github.vikestep.sprinklesforvanilla.SprinklesForVanilla;
import io.github.vikestep.sprinklesforvanilla.common.configuration.Settings;
import io.github.vikestep.sprinklesforvanilla.common.init.InitMobRegistry;
import io.github.vikestep.sprinklesforvanilla.common.utils.LogHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WorldHandlers
{
    public static class ExplosionHandler
    {
        private static boolean playerSleepInNether = false;

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

                if (isNotCorrectConfig(exploderName, explosion.exploder, event.explosion.explosionX, event.explosion.explosionY, event.explosion.explosionZ, event.world))
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
                // This is because the user may have disabled creeper explosions in mob griefing section
                if (event.explosion.exploder instanceof EntityCreeper && !Settings.mobGriefingConfigs[1].get(Arrays.asList(Settings.mobGriefingTypes).indexOf("creeperExplosion")))
                {
                    return;
                }
                // This is because the user may have disabled ghast fireball explosions in mob griefing section
                if (exploderName.equals("GhastFireball") && !Settings.mobGriefingConfigs[1].get(Arrays.asList(Settings.mobGriefingTypes).indexOf("largeFireballExplosion")))
                {
                    return;
                }
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

                if (isNotCorrectConfig(exploderName, explosion.exploder, event.explosion.explosionX, event.explosion.explosionY, event.explosion.explosionZ, event.world))
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

        @SubscribeEvent
        public void onBedActivated(PlayerInteractEvent event)
        {
            if (event.world.getBlock(event.x, event.y, event.z).isBed(event.world, event.x, event.y, event.z, event.entityPlayer) && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && SprinklesForVanilla.isOnServer)
            {
                if (!event.world.isRemote && ((!event.world.provider.canRespawnHere() || event.world.getBiomeGenForCoords(event.x, event.z) == BiomeGenBase.hell) && Settings.otherDimensionsCancelSleep[1]))
                {
                    playerSleepInNether = true;
                }
            }
        }

        @SuppressWarnings("unchecked")
        public boolean isNotCorrectConfig(String configExploderName, Entity exploder, double x, double y, double z, World world)
        {
            String exploderInternalName = exploder != null ? EntityList.getEntityString(exploder) : "";
            if (exploderInternalName == null)
            {
                return true;
            }
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

                List<Entity> entityList = world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(x, y, z, x, y, z));
                for (Entity entity : entityList)
                {
                    if (entity instanceof EntityLargeFireball && configExploderName.equals("GhastFireball"))
                    {
                        if (((EntityLargeFireball) entity).shootingEntity instanceof EntityGhast)
                        {
                            return false;
                        }
                    }
                    else if (entity instanceof EntityEnderCrystal && configExploderName.equals("EnderCrystal"))
                    {
                        if (((EntityEnderCrystal) entity).health <= 0)
                        {
                            return false;
                        }
                    }
                }

                if (playerSleepInNether && configExploderName.equals("Bed"))
                {
                    playerSleepInNether = false;
                    return false;
                }
                /*if (Hooks.fireballsExploding.size() > 0 && exploder == (Entity)     null)
                {
                    Hooks.fireballsExploding.remove(0);
                    return false;
                }*/
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

    public static class WorldPotentialSpawnsHandler
    {
        @SubscribeEvent
        public void checkSpawn(LivingSpawnEvent.CheckSpawn event)
        {
            if (event.getResult() == Event.Result.DEFAULT && !((EntityLiving) event.entity).getCanSpawnHere() && InitMobRegistry.modificationMap.get(event.entity.getClass()) != null)
            {
                boolean shouldAllowIfRule = false;
                EntityLiving entity = (EntityLiving) event.entity;
                if (entity instanceof EntityAnimal)
                {
                    int i = MathHelper.floor_double(entity.posX);
                    int j = MathHelper.floor_double(entity.boundingBox.minY);
                    int k = MathHelper.floor_double(entity.posZ);
                    if (entity.worldObj.getBlock(i, j - 1, k) != Blocks.grass && entity.dimension != 0)
                    {
                       shouldAllowIfRule = true;
                    }
                    else if (entity instanceof EntityOcelot && !entity.worldObj.getBlock(i, j - 1, k).isLeaves(event.world, i, j - 1, k))
                    {
                        shouldAllowIfRule = true;
                    }
                }
                if (shouldAllowIfRule)
                {
                    for (Map.Entry entry : InitMobRegistry.modificationMap.entrySet())
                    {
                        Class entityClass = (Class) entry.getKey();
                        BiomeGenBase[] biomesChecked = (BiomeGenBase[])entry.getValue();
                        String entityName = (String) EntityList.classToStringMapping.get(entityClass);
                        String entitySpawnedName = EntityList.getEntityString(entity);
                        if (entityName.equals(entitySpawnedName))
                        {
                            if (Arrays.asList(biomesChecked).contains(event.world.getBiomeGenForCoords((int) event.x, (int) event.z)))
                            {
                                event.setResult(Event.Result.ALLOW);
                            }
                        }
                    }
                }
            }
        }
    }
}
