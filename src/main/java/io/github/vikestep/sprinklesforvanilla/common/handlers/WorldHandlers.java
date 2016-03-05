package io.github.vikestep.sprinklesforvanilla.common.handlers;

import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import io.github.vikestep.sprinklesforvanilla.SprinklesForVanilla;
import io.github.vikestep.sprinklesforvanilla.common.configuration.Settings;
import io.github.vikestep.sprinklesforvanilla.common.init.InitMobRegistry;
import io.github.vikestep.sprinklesforvanilla.common.utils.LogHelper;
import io.github.vikestep.sprinklesforvanilla.server.utils.ServerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ExplosionEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
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
            Entity exploder = explosion.getExplosivePlacedBy();
            if (Settings.enableExplosionLogging[1])
            {
                String name = exploder == null ? "null" : EntityList.getEntityString(exploder);
                LogHelper.info("Explosion: " + name + ", " + explosion.explosionSize + ", " + !event.isCanceled() + ", " + Boolean.toString(true) + ", " + explosion.isFlaming + ", " + explosion.isSmoking);
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

                if (isNotCorrectConfig(exploderName, exploder, event.explosion.getPosition().xCoord, event.explosion.getPosition().yCoord, event.explosion.getPosition().zCoord, event.world))
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
                if (exploder instanceof EntityCreeper && !Settings.mobGriefingConfigs[1].get(Arrays.asList(Settings.mobGriefingTypes).indexOf("creeperExplosion")))
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
            Entity exploder = explosion.getExplosivePlacedBy();
            for (String explosionDatum : Settings.explosionData[1])
            {
                String[] data = explosionDatum.replace(", ", ",").split(",");
                String exploderName = data[0];
                boolean doesDamage = Boolean.parseBoolean(data[3]);
                boolean isSmoking = Boolean.parseBoolean(data[5]);

                if (isNotCorrectConfig(exploderName, exploder, event.explosion.getPosition().xCoord, event.explosion.getPosition().yCoord, event.explosion.getPosition().zCoord, event.world))
                {
                    continue;
                }

                if (!doesDamage)
                {
                    event.getAffectedEntities().clear();
                }

                if (!isSmoking)
                {
                    explosion.getAffectedBlockPositions().clear();
                }

                return;
            }
        }

        @SubscribeEvent
        public void onBedActivated(PlayerInteractEvent event)
        {
            if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && SprinklesForVanilla.isOnServer && event.world.getBlockState(event.pos).getBlock().isBed(event.world, event.pos, event.entityPlayer))
            {
                if (!event.world.isRemote && ((!event.world.provider.canRespawnHere() || event.world.getBiomeGenForCoords(event.pos) == BiomeGenBase.hell)))
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
                if (configExploderName.equals("ChargedCreeper") && exploderInternalName.equals("Creeper") && exploder != null)
                {
                    if (((EntityCreeper) exploder).getPowered())
                    {
                        return false;
                    }
                }

                List<Entity> entityList = world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.fromBounds(x, y, z, x, y, z));
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
                if (configExploderName.equals("Creeper") & exploder != null)
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
            EntityLiving entity = (EntityLiving) event.entity;
            boolean canSpawn = entity.getCanSpawnHere();
            if (SprinklesForVanilla.isOnServer && event.getResult() == Event.Result.DEFAULT && !entity.worldObj.isRemote)
            {
                int dim = entity.dimension;
                EnumCreatureType creatureType = getCreatureType(entity);
                Map<EnumCreatureType, Integer> heightMap = InitMobRegistry.heightMap.get(dim);
                Map<EnumCreatureType, Integer> rateMap = InitMobRegistry.rateMap.get(dim);
                boolean shouldCheck = InitMobRegistry.gcdPassiveSpawn != 400 && creatureType == EnumCreatureType.CREATURE;
                if (heightMap != null)
                {
                    Integer max = heightMap.get(creatureType);
                    if (max != null)
                    {
                        double y = entity.posY;
                        if (y > max)
                        {
                            event.setResult(Event.Result.DENY);
                            return;
                        }
                    }
                }
                if (rateMap != null)
                {
                    Integer rate = rateMap.get(creatureType);
                    if (rate != null)
                    {
                        WorldServer world = ServerHelper.getWorldServer(entity);
                        long time = world.getWorldInfo().getWorldTotalTime();
                        if (time % rate != 0)
                        {
                            event.setResult(Event.Result.DENY);
                            return;
                        }
                        shouldCheck = false;
                    }
                }
                if (shouldCheck)
                {
                    WorldServer world = ServerHelper.getWorldServer(entity);
                    long time = world.getWorldInfo().getWorldTotalTime();
                    if (time % 400 != 0)
                    {
                        event.setResult(Event.Result.DENY);
                        return;
                    }
                }
                if (InitMobRegistry.modificationMap.get(entity.getClass()) != null && !canSpawn)
                {
                    boolean shouldAllowIfRule = false;
                    if (entity instanceof EntityAnimal)
                    {
                        int i = MathHelper.floor_double(entity.posX);
                        int j = MathHelper.floor_double(entity.getEntityBoundingBox().minY);
                        int k = MathHelper.floor_double(entity.posZ);
                        boolean enoughLight = entity.worldObj.getLight(new BlockPos(i, j, k)) > 8;
                        BlockPos pos = new BlockPos(i, j - 1, k);
                        if (dim != 0 && entity.worldObj.getBlockState(pos).getBlock() != Blocks.grass && enoughLight)
                        {
                            shouldAllowIfRule = true;
                        }
                        else if (entity instanceof EntityOcelot && !entity.worldObj.getBlockState(pos).getBlock().isLeaves(event.world, pos) && enoughLight)
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
                                if (Arrays.asList(biomesChecked).contains(event.world.getBiomeGenForCoords(new BlockPos(event.x, event.y, event.z))))
                                {
                                    event.setResult(Event.Result.ALLOW);
                                }
                            }
                        }
                    }
                }
            }
        }

        private static EnumCreatureType getCreatureType(EntityLiving entity)
        {
            Class<? extends EntityLiving> entityClass = entity.getClass();
            if (IMob.class.isAssignableFrom(entityClass))
            {
                return EnumCreatureType.MONSTER;
            }
            else if (EntityAnimal.class.isAssignableFrom(entityClass))
            {
                return EnumCreatureType.CREATURE;
            }
            else if (EntityAmbientCreature.class.isAssignableFrom(entityClass))
            {
                return EnumCreatureType.AMBIENT;
            }
            else if (EntityWaterMob.class.isAssignableFrom(entityClass))
            {
                return EnumCreatureType.WATER_CREATURE;
            }
            else
            {
                return null;
            }
        }
    }
}
