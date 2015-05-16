package io.github.vikestep.sprinklesforvanilla.common.handlers;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import io.github.vikestep.sprinklesforvanilla.SprinklesForVanilla;
import io.github.vikestep.sprinklesforvanilla.common.configuration.Settings;
import io.github.vikestep.sprinklesforvanilla.common.reference.ModInfo;
import io.github.vikestep.sprinklesforvanilla.common.utils.LogHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;

import java.util.*;

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
            if ((Math.abs(player.posX - event.x) > distX || Math.abs(player.posY - event.y) > distY || Math.abs(player.posZ - event.z) > distZ) && Settings.distanceFromBedCancelsSleep[1])
            {
                //We manually set it because it may be close enough with vanilla code
                event.result = EntityPlayer.EnumStatus.TOO_FAR_AWAY;
                return;
            }

            //This checks for nearby mobs
            distX = Settings.nearbyMobDistance[1][0];
            distY = Settings.nearbyMobDistance[1][1];
            distZ = Settings.nearbyMobDistance[1][2];
            if (!(player.worldObj.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.getBoundingBox(event.x - distX, event.y - distY, event.z - distZ, event.x + distX, event.y + distY, event.z + distZ)).isEmpty()) && Settings.nearbyMobsCancelSleep[1])
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
                    ChunkCoordinates chunkCoordinates = new ChunkCoordinates(event.x, event.y, event.z);
                    ChunkCoordinates bedCoordinates = player.worldObj.getBlock(event.x, event.y, event.z).getBedSpawnPosition(player.worldObj, chunkCoordinates.posX, chunkCoordinates.posY, chunkCoordinates.posZ, player);
                    if (bedCoordinates == null) {
                        bedCoordinates = new ChunkCoordinates(chunkCoordinates.posX, chunkCoordinates.posY + 1, chunkCoordinates.posZ);
                    }

                    ChunkCoordinates verifiedCoordinates = EntityPlayer.verifyRespawnCoordinates(player.worldObj, bedCoordinates, false);

                    if (verifiedCoordinates == null) {
                        verifiedCoordinates = new ChunkCoordinates(chunkCoordinates.posX, chunkCoordinates.posY, chunkCoordinates.posZ);
                    }

                    player.setSpawnChunk(verifiedCoordinates, false); //!Settings.playerChecksBedRespawn[1]);
                }
                return;
            }

            boolean wouldSleep = wouldSleepInVanilla(player, event.x, event.y, event.z);
            //If the tests would be failed in vanilla then we sleep using our own method
            if (!wouldSleep)
            {
                sleep(player, event.x, event.y, event.z);
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
                        ChunkCoordinates bedLocation = player.getBedLocation(player.dimension);
                        boolean playerSpawnForced = player.isSpawnForced(player.dimension);
                        SpawnPoint mapValue = (SpawnPoint) spawnData.getValue();
                        if (entityPlayerUUID != spawnData.getKey() || bedLocation != mapValue.coordinates || playerSpawnForced != mapValue.isForced)
                        {
                            player.setSpawnChunk(mapValue.coordinates, mapValue.isForced);
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

            return player.worldObj.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.getBoundingBox(x - 8.0D, y - 5.0D, z - 8.0D, x + 8.0D, y + 5.0D, z + 8.0D)).isEmpty();
        }

        //The protected/private members being referenced in this are in the access transformer. If they show up as unavailable
        //do gradlew clean, gradlew setupDecompWorkspace then the gradle command for your IDE (such as gradlew idea) to have it shown as public
        public void sleep(EntityPlayer player, int bedX, int bedY, int bedZ)
        {
            if (player.isRiding())
            {
                player.mountEntity(null);
            }

            player.setSize(0.2F, 0.2F);
            player.yOffset = 0.2F;

            if (player.worldObj.blockExists(bedX, bedY, bedZ))
            {
                int l = player.worldObj.getBlock(bedX, bedY, bedZ).getBedDirection(player.worldObj, bedX, bedY, bedZ);
                float f1 = 0.5F;
                float f = 0.5F;

                switch (l)
                {
                    case 0:
                        f = 0.9F;
                        break;
                    case 1:
                        f1 = 0.1F;
                        break;
                    case 2:
                        f = 0.1F;
                        break;
                    case 3:
                        f1 = 0.9F;
                }

                player.func_71013_b(l);
                player.setPosition((double) ((float) bedX + f1), (double) ((float) bedY + 0.9375F), (double) ((float) bedZ + f));
            }
            else
            {
                player.setPosition((double) ((float) bedX + 0.5F), (double) ((float) bedY + 0.9375F), (double) ((float) bedZ + 0.5F));
            }

            player.sleeping = true;
            player.sleepTimer = Math.min(Math.max(100 - Settings.timeToSleep[1], 0), 100);
            player.playerLocation = new ChunkCoordinates(bedX, bedY, bedZ);
            player.motionX = player.motionZ = player.motionY = 0.0D;

            if (!player.worldObj.isRemote)
            {
                player.worldObj.updateAllPlayersSleepingFlag();
            }
        }

        private class SpawnPoint {
            private ChunkCoordinates coordinates;
            private Boolean isForced;

            public SpawnPoint(ChunkCoordinates coordinates, Boolean isForced)
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
