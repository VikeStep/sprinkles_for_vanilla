package io.github.vikestep.sprinklesforvanilla.common.handlers;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import io.github.vikestep.sprinklesforvanilla.SprinklesForVanilla;
import io.github.vikestep.sprinklesforvanilla.common.configuration.Settings;
import io.github.vikestep.sprinklesforvanilla.common.reference.ModInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;

import java.util.*;

@SuppressWarnings("ALL")
public class PlayerHandlers
{
    public static class PlayerSleepHandler
    {
        private Map<UUID, SpawnPoint> playersToNotSetSpawn = new HashMap<UUID, SpawnPoint>();

        @SubscribeEvent
        public void onPlayerSleep(PlayerSleepInBedEvent event)
        {
            EntityPlayer.EnumStatus result = event.result;
            EntityPlayer player = event.entityPlayer;

            //If another mod has already set it to be disabled, we will not overwrite it. Also if the player is already sleeping, or the entity is alive we won't overwrite it
            //If the world is remote then that means that sleeping has successfully been carried out and we should let vanilla deal with it
            if ((result != null && result != EntityPlayer.EnumStatus.OK) || !SprinklesForVanilla.isOnServer || player.isPlayerSleeping() || !player.isEntityAlive() || player.worldObj.isRemote)
            {
                //This will make it so that under all circumstances, sleep is disabled.
                if (!Settings.sleepIsEnabled[1] && (result == null || result == EntityPlayer.EnumStatus.OK) && !player.worldObj.isRemote && SprinklesForVanilla.isOnServer)
                {
                    event.result = EntityPlayer.EnumStatus.OTHER_PROBLEM;
                }
                return;
            }

            //Now we check distance from bed. If
            double distX = Settings.distanceFromBed[1][0];
            double distY = Settings.distanceFromBed[1][1];
            double distZ = Settings.distanceFromBed[1][2];
            if ((Math.abs(player.posX - event.pos.getX()) > distX || Math.abs(player.posY - event.pos.getY()) > distY || Math.abs(player.posZ - event.pos.getZ()) > distZ) && Settings.distanceFromBedCancelsSleep[1])
            {
                //We manually set it because it may be close enough with vanilla code
                event.result = EntityPlayer.EnumStatus.TOO_FAR_AWAY;
                return;
            }

            //This checks for nearby mobs
            distX = Settings.nearbyMobDistance[1][0];
            distY = Settings.nearbyMobDistance[1][1];
            distZ = Settings.nearbyMobDistance[1][2];
            if (!(player.worldObj.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.fromBounds(event.pos.getX() - distX, event.pos.getY() - distY, event.pos.getZ() - distZ, event.pos.getX() + distX, event.pos.getY() + distY, event.pos.getZ() + distZ)).isEmpty()) && Settings.nearbyMobsCancelSleep[1])
            {
                //We manually set it because it may be safe with vanilla code
                event.result = EntityPlayer.EnumStatus.NOT_SAFE;
                return;
            }

            //This checks if if its day time or not sleeping overworld and returns if it should continue to cancel sleep
            if ((player.worldObj.isDaytime() && Settings.dayCancelsSleep[1]) || (!player.worldObj.provider.isSurfaceWorld() && Settings.otherDimensionsCancelSleep[1]))
            {
                //We let vanilla handle setting the result as of this time
                return;
            }

            //If sleep is disabled then we say there is a problem and we check if the bed should set a spawn
            if (!Settings.sleepIsEnabled[1])
            {
                event.result = EntityPlayer.EnumStatus.OTHER_PROBLEM;
                if (Settings.bedSetsSpawn[1])
                {
                    BlockPos blockPos = new BlockPos(event.pos.getX(), event.pos.getY(), event.pos.getZ());
                    BlockPos bedCoordinates = player.worldObj.getBlockState(event.pos).getBlock().getBedSpawnPosition(player.worldObj, blockPos, player);
                    if (bedCoordinates == null) {
                        bedCoordinates = new BlockPos(blockPos);
                    }

                    player.setSpawnChunk(blockPos, false, player.dimension); //!Settings.playerChecksBedRespawn[1]);
                }
                return;
            }

            boolean wouldSleep = wouldSleepInVanilla(player, event.pos.getX(), event.pos.getY(), event.pos.getZ());
            //If the tests would be failed in vanilla then we sleep using our own method
            if (!wouldSleep)
            {
                sleep(player, event.pos.getX(), event.pos.getY(), event.pos.getZ());
                event.result = EntityPlayer.EnumStatus.OK;
            }
            if (!Settings.bedSetsSpawn[1])
            {
                playersToNotSetSpawn.put(player.getUniqueID(), new SpawnPoint(player.getBedLocation(player.dimension), player.isSpawnForced(player.dimension)));
            }
        }

        @SubscribeEvent
        public void onWakeUp(PlayerWakeUpEvent event)
        {
            boolean willSleep = event.setSpawn;
            EntityPlayer player = event.entityPlayer;
            if (!willSleep && playersToNotSetSpawn.get(player.getPersistentID()) != null)
            {
                playersToNotSetSpawn.remove(player.getUniqueID());
            }
            /*else if (!Settings.playerChecksBedRespawn[1])
            {
                LogHelper.info("Forcing bed spawn location");
                ChunkCoordinates bedLocation = player.getBedLocation(player.dimension);
                player.setSpawnChunk(bedLocation, true);
            }*/
        }

        //TODO: Possibly add my own event for when the player's spawn is set via ASM. Need to profile to see if there is much benefit
        @SubscribeEvent
        public void onServerTick(TickEvent.ServerTickEvent event)
        {
            if (!playersToNotSetSpawn.isEmpty())
            {
                for (Map.Entry spawnData : playersToNotSetSpawn.entrySet())
                {
                    for (Object playerObj : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
                    {
                        EntityPlayer player = (EntityPlayer) playerObj;
                        UUID entityPlayerUUID = player.getUniqueID();
                        BlockPos bedLocation = player.getBedLocation(player.dimension);
                        boolean playerSpawnForced = player.isSpawnForced(player.dimension);
                        SpawnPoint mapValue = (SpawnPoint) spawnData.getValue();
                        if (entityPlayerUUID != spawnData.getKey() || bedLocation != mapValue.coordinates || playerSpawnForced != mapValue.isForced)
                        {
                            player.setSpawnChunk(mapValue.coordinates, mapValue.isForced, player.dimension);
                        }
                    }
                }
            }
        }

        @SuppressWarnings("SimplifiableIfStatement")
        public boolean wouldSleepInVanilla(EntityPlayer player, double x, double y, double z)
        {
            if (player.isPlayerSleeping() || !player.isEntityAlive() || !player.worldObj.provider.isSurfaceWorld() || player.worldObj.isDaytime())
            {
                return false;
            }

            if (Math.abs(player.posX - x) > 3.0D || Math.abs(player.posY - y) > 2.0D || Math.abs(player.posZ - z) > 3.0D)
            {
                return false;
            }

            return player.worldObj.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.fromBounds(x - 8.0D, y - 5.0D, z - 8.0D, x + 8.0D, y + 5.0D, z + 8.0D)).isEmpty();
        }

        //The protected/private members being referenced in this are in the access transformer. If they show up as unavailable
        //do gradlew clean, gradlew setupDecompWorkspace then the gradle command for your IDE (such as gradlew idea) to have it shown as public
        public void sleep(EntityPlayer player, int bedX, int bedY, int bedZ)
        {
            BlockPos bedLocation = new BlockPos(bedX, bedY, bedZ);

            if (player.isRiding())
            {
                player.mountEntity((Entity)null);
            }

            player.setSize(0.2F, 0.2F);

            if (player.worldObj.isBlockLoaded(bedLocation) && player.worldObj.getBlockState(bedLocation).getBlock().isBed(player.worldObj, bedLocation, player))
            {
                EnumFacing enumfacing = player.worldObj.getBlockState(bedLocation).getBlock().getBedDirection(player.worldObj, bedLocation);
                float f = 0.5F;
                float f1 = 0.5F;

                switch (enumfacing)
                {
                    case SOUTH:
                        f1 = 0.9F;
                        break;
                    case NORTH:
                        f1 = 0.1F;
                        break;
                    case WEST:
                        f = 0.1F;
                        break;
                    case EAST:
                        f = 0.9F;
                }

                player.func_175139_a(enumfacing);
                player.setPosition((double)((float)bedLocation.getX() + f), (double)((float)bedLocation.getY() + 0.6875F), (double)((float)bedLocation.getZ() + f1));
            }
            else
            {
                player.setPosition((double)((float)bedLocation.getX() + 0.5F), (double)((float)bedLocation.getY() + 0.6875F), (double)((float)bedLocation.getZ() + 0.5F));
            }

            player.sleeping = true;
            player.sleepTimer = 0;
            player.playerLocation = bedLocation;
            player.motionX = player.motionZ = player.motionY = 0.0D;

            if (!player.worldObj.isRemote)
            {
                player.worldObj.updateAllPlayersSleepingFlag();
            }
        }

        private class SpawnPoint {
            private BlockPos coordinates;
            private Boolean isForced;

            public SpawnPoint(BlockPos coordinates, Boolean isForced)
            {
                this.coordinates = coordinates;
                this.isForced = isForced;
            }
        }
    }

    public static class PlayerRespawnHandler
    {
        private HashMap<UUID, PlayerProperties> playerMap = new HashMap<UUID, PlayerProperties>();

        //We do not need to worry about if this actually kills them. If it doesn't, all it will do is update the HashMap. We just don't want to be updating a HashMap every tick
        @SubscribeEvent
        public void onHurt(LivingHurtEvent event)
        {
            if (event.entity instanceof EntityPlayer && SprinklesForVanilla.isOnServer)
            {
                EntityPlayer player = (EntityPlayer) event.entity;
                if (player.getHealth() - event.ammount <= 0)
                {
                    PlayerProperties props = (PlayerProperties) player.getExtendedProperties(ModInfo.MOD_NAME);
                    props.health = player.getHealth();
                    props.hunger = player.getFoodStats().getFoodLevel();
                    props.experience = player.experience;
                    props.experienceLevel = player.experienceLevel;
                    props.experienceTotal = player.experienceTotal;
                    playerMap.put(player.getPersistentID(), props);
                }
            }
        }

        @SubscribeEvent
        public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
        {
            if (SprinklesForVanilla.isOnServer)
            {
                PlayerProperties oldPlayerProps = playerMap.remove(event.player.getPersistentID());
                PlayerProperties props = (PlayerProperties) event.player.getExtendedProperties(ModInfo.MOD_NAME);
                EntityPlayer player = event.player;

                if (oldPlayerProps != null)
                {
                    props.health = oldPlayerProps.health;
                    props.hunger = oldPlayerProps.hunger;
                    props.experience = oldPlayerProps.experience;
                    props.experienceLevel = oldPlayerProps.experienceLevel;
                    props.experienceTotal = oldPlayerProps.experienceTotal;
                }

                if (Settings.playerKeepsHealthOnRespawn[1] >= 1)
                {
                    player.setHealth(Math.max(props.health, (float) Settings.playerKeepsHealthOnRespawn[1]));
                }
                if (Settings.playerKeepsHungerOnRespawn[1] >= 1)
                {
                    player.getFoodStats().addStats(Math.max(props.hunger, Settings.playerKeepsHungerOnRespawn[1]) - 20, 0);
                    if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
                    {
                        player.getFoodStats().setFoodLevel(Math.max(props.hunger, Settings.playerKeepsHungerOnRespawn[1]));
                    }
                }
                if (Settings.playerKeepsXPOnRespawn[1])
                {
                    player.experience = props.experience;
                    player.experienceLevel = props.experienceLevel;
                    player.experienceTotal = props.experienceTotal;
                }
            }
        }

        @SubscribeEvent
        public void onConstruction(EntityEvent.EntityConstructing event)
        {
            if (event.entity instanceof EntityPlayer && event.entity.getExtendedProperties(ModInfo.MOD_NAME) == null && SprinklesForVanilla.isOnServer)
            {
                event.entity.registerExtendedProperties(ModInfo.MOD_NAME, new PlayerProperties());
            }
        }

        public class PlayerProperties implements IExtendedEntityProperties
        {
            public float health;
            public int   hunger;
            public float experience;
            public int   experienceLevel;
            public int   experienceTotal;

            @Override
            public void saveNBTData(NBTTagCompound compound)
            {

            }

            @Override
            public void loadNBTData(NBTTagCompound compound)
            {

            }

            @Override
            public void init(Entity entity, World world)
            {

            }
        }

    }
}
