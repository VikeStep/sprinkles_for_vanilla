package com.vikestep.sprinklesforvanilla.common.handlers;

import com.vikestep.sprinklesforvanilla.SprinklesForVanilla;
import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.block.BlockBed;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;

import java.lang.reflect.Field;

public class SleepHandler
{
    private static final String SLEEP_TIMER_DEOBF  = "sleepTimer";
    private static final String SLEEP_TIMER_OBF    = "field_71076_b";
    private static final String SLEEPING_DEOBF     = "sleeping";
    private static final String SLEEPING_OBF       = "field_71083_bS";
    private static final String SPAWN_CHUNK_DEOBF  = "spawnChunk";
    private static final String SPAWN_CHUNK_OBF    = "field_71077_c";
    private static final String SPAWN_FORCED_DEOBF = "spawnForced";
    private static final String SPAWN_FORCED_OBF   = "field_82248_d";
    private static boolean          resetSpawn;
    private static boolean          oldSpawnWasBed;
    private static EntityPlayer     playerResettingSpawn;
    private static ChunkCoordinates spawnCoordinates;
    private        Field            sleepTimer;
    private        Field            sleeping;
    private        Field            spawnChunk;
    private        Field            spawnForced;

    public SleepHandler()
    {
        try
        {
            sleepTimer = EntityPlayer.class.getDeclaredField(SLEEP_TIMER_OBF);
            sleepTimer.setAccessible(true);
            sleeping = EntityPlayer.class.getDeclaredField(SLEEPING_OBF);
            sleeping.setAccessible(true);
            spawnChunk = EntityPlayer.class.getDeclaredField(SPAWN_CHUNK_OBF);
            spawnChunk.setAccessible(true);
            spawnForced = EntityPlayer.class.getDeclaredField(SPAWN_FORCED_OBF);
            spawnForced.setAccessible(true);
        }
        catch (NoSuchFieldException e)
        {
            try
            {
                sleepTimer = EntityPlayer.class.getDeclaredField(SLEEP_TIMER_DEOBF);
                sleepTimer.setAccessible(true);
                sleeping = EntityPlayer.class.getDeclaredField(SLEEPING_DEOBF);
                sleeping.setAccessible(true);
                spawnChunk = EntityPlayer.class.getDeclaredField(SPAWN_CHUNK_DEOBF);
                spawnChunk.setAccessible(true);
                spawnForced = EntityPlayer.class.getDeclaredField(SPAWN_FORCED_DEOBF);
                spawnForced.setAccessible(true);
            }
            catch (NoSuchFieldException f)
            {
                f.printStackTrace();
            }
        }
    }

    //This will cause problems with mods like Insomnia and PerfectSpawn which also override this data. To resolve disable sleepOverhaul in configs
    @SubscribeEvent
    public void onPlayerSleep(PlayerSleepInBedEvent event)
    {
        if (getEnumStatus(event.entityPlayer, event.x, event.y, event.z) == null && SprinklesForVanilla.isOnServer)
        {
            if (!Settings.sleepIsEnabled)
            {
                event.result = EntityPlayer.EnumStatus.OTHER_PROBLEM;
                if (Settings.bedSetsSpawn)
                {
                    setSpawn(event.entityPlayer, event.x, event.y, event.z);
                }
            }
            else
            {
                event.result = EntityPlayer.EnumStatus.OK;
                sleep(event.entityPlayer, event.x, event.y, event.z);
                if (!Settings.bedSetsSpawn && !event.entityPlayer.worldObj.isRemote)
                {
                    playerResettingSpawn = event.entityPlayer;
                    resetSpawn = true;
                    try
                    {
                        spawnCoordinates = (ChunkCoordinates) spawnChunk.get(event.entityPlayer);
                        oldSpawnWasBed = (EntityPlayer.verifyRespawnCoordinates(event.entityPlayer.worldObj, spawnCoordinates, false) != null);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        else
        {
            event.result = getEnumStatus(event.entityPlayer, event.x, event.y, event.z);
        }
    }

    //This will cause problems with other mods that also replace the PlayerInteractEvent under conditions which mine fall under. To resolve disable sleepOverhaul in configs
    @SubscribeEvent
    public void onBedActivated(PlayerInteractEvent event)
    {
        if (!Settings.playerMustSleepInOverworld && event.world.getBlock(event.x, event.y, event.z).isBed(event.world, event.x, event.y, event.z, event.entityPlayer) && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && SprinklesForVanilla.isOnServer)
        {
            if (!event.world.isRemote && !event.world.provider.canRespawnHere() && event.world.getBiomeGenForCoords(event.x, event.z) == BiomeGenBase.hell)
            {
                event.setCanceled(true);
                int bedX = event.x;
                int bedY = event.y;
                int bedZ = event.z;
                int meta = event.world.getBlockMetadata(bedX, bedY, bedZ);
                boolean occupied = false;
                if (!BlockBed.isBlockHeadOfBed(meta))
                {
                    int dir = BlockBed.getDirection(meta);
                    bedX += BlockBed.field_149981_a[dir][0];
                    bedZ += BlockBed.field_149981_a[dir][1];
                    meta = event.world.getBlockMetadata(bedX, bedY, bedZ);
                }
                if (BlockBed.func_149976_c(meta))
                {
                    EntityPlayer entityplayer1 = null;
                    for (Object playerEntity : event.world.playerEntities)
                    {
                        EntityPlayer entityplayer2 = (EntityPlayer) playerEntity;
                        if (entityplayer2.isPlayerSleeping())
                        {
                            ChunkCoordinates chunkcoordinates = entityplayer2.playerLocation;
                            if (chunkcoordinates.posX == bedX && chunkcoordinates.posY == bedY && chunkcoordinates.posZ == bedZ)
                            {
                                entityplayer1 = entityplayer2;
                            }
                        }
                    }
                    if (entityplayer1 != null)
                    {
                        event.entityPlayer.addChatComponentMessage(new ChatComponentTranslation("tile.bed.occupied"));
                        occupied = true;
                    }
                    BlockBed.func_149979_a(event.world, bedX, bedY, bedZ, false);
                }
                if (!occupied)
                {
                    EntityPlayer.EnumStatus enumstatus = event.entityPlayer.sleepInBedAt(bedX, bedY, bedZ);
                    if (enumstatus == EntityPlayer.EnumStatus.OK)
                    {
                        BlockBed.func_149979_a(event.world, bedX, bedY, bedZ, true);
                    }
                    else if (enumstatus == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW)
                    {
                        event.entityPlayer.addChatComponentMessage(new ChatComponentTranslation("tile.bed.noSleep"));
                    }
                    else if (enumstatus == EntityPlayer.EnumStatus.NOT_SAFE)
                    {
                        event.entityPlayer.addChatComponentMessage(new ChatComponentTranslation("tile.bed.notSafe"));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        try
        {
            if (resetSpawn && spawnCoordinates != spawnChunk.get(playerResettingSpawn) && SprinklesForVanilla.isOnServer)
            {
                playerResettingSpawn.setSpawnChunk(spawnCoordinates, true);
                if (oldSpawnWasBed)
                {
                    spawnForced.set(playerResettingSpawn, true);
                }
                resetSpawn = false;
                playerResettingSpawn = null;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    EntityPlayer.EnumStatus getEnumStatus(EntityPlayer player, int x, int y, int z)
    {
        if (!player.worldObj.isRemote)
        {
            if (player.isPlayerSleeping() || !player.isEntityAlive())
            {
                return EntityPlayer.EnumStatus.OTHER_PROBLEM;
            }
            if (!player.worldObj.provider.isSurfaceWorld() && Settings.playerMustSleepInOverworld)
            {
                return EntityPlayer.EnumStatus.NOT_POSSIBLE_HERE;
            }
            if (player.worldObj.isDaytime() && Settings.dayCancelsSleep)
            {
                return EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW;
            }
            int distX = Settings.distanceFromBed[0];
            int distY = Settings.distanceFromBed[1];
            int distZ = Settings.distanceFromBed[2];
            if ((Math.abs(player.posX - x) > (double) distX || Math.abs(player.posY - y) > (double) distY || Math.abs(player.posZ - z) > (double) distZ) && Settings.distanceFromBedCancelsSleep)
            {
                return EntityPlayer.EnumStatus.TOO_FAR_AWAY;
            }
            distX = Settings.nearbyMobDistance[0];
            distY = Settings.nearbyMobDistance[1];
            distZ = Settings.nearbyMobDistance[2];
            if (!(player.worldObj.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.getBoundingBox(x - distX, y - distY, z - distZ, x + distX, y + distY, z + distZ)).isEmpty()) && Settings.nearbyMobsCancelSleep)
            {
                return EntityPlayer.EnumStatus.NOT_SAFE;
            }
        }
        return null;
    }

    void sleep(EntityPlayer player, int x, int y, int z)
    {
        if (player.isRiding())
        {
            player.mountEntity(null);
        }

        player.width = 0.2F;
        player.height = 0.2F;
        player.boundingBox.maxX = player.boundingBox.minX + (double) 0.2F;
        player.boundingBox.maxZ = player.boundingBox.minZ + (double) 0.2F;
        player.boundingBox.maxY = player.boundingBox.minY + (double) 0.2F;
        player.myEntitySize = Entity.EnumEntitySize.SIZE_1;
        player.yOffset = 0.2F;

        if (player.worldObj.blockExists(x, y, z))
        {
            int l = player.worldObj.getBlock(x, y, z).getBedDirection(player.worldObj, x, y, z);
            float f1 = 0.5F;
            float f = 0.5F;
            player.field_71079_bU = 0.0F;
            player.field_71089_bV = 0.0F;

            switch (l)
            {
                case 0:
                    f = 0.9F;
                    player.field_71089_bV = -1.8F;
                    break;
                case 1:
                    f1 = 0.1F;
                    player.field_71079_bU = 1.8F;
                    break;
                case 2:
                    f = 0.1F;
                    player.field_71089_bV = 1.8F;
                    break;
                case 3:
                    f1 = 0.9F;
                    player.field_71079_bU = -1.8F;
            }

            player.setPosition((double) ((float) x + f1), (double) ((float) y + 0.9375F), (double) ((float) z + f));
        }
        else
        {
            player.setPosition((double) ((float) x + 0.5F), (double) ((float) y + 0.9375F), (double) ((float) z + 0.5F));
        }

        try
        {
            sleepTimer.set(player, Math.min(Math.max(100 - Settings.timeToSleep, 0), 100));
            sleeping.set(player, true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        player.playerLocation = new ChunkCoordinates(x, y, z);
        player.motionX = player.motionZ = player.motionY = 0.0D;

        if (!player.worldObj.isRemote)
        {
            player.worldObj.updateAllPlayersSleepingFlag();
        }
    }

    void setSpawn(EntityPlayer player, int x, int y, int z)
    {
        ChunkCoordinates chunkCoordinates = new ChunkCoordinates(x, y, z);
        ChunkCoordinates bedCoordinates = player.worldObj.getBlock(x, y, z).getBedSpawnPosition(player.worldObj, chunkCoordinates.posX, chunkCoordinates.posY, chunkCoordinates.posZ, player);
        if (bedCoordinates == null)
        {
            bedCoordinates = new ChunkCoordinates(chunkCoordinates.posX, chunkCoordinates.posY + 1, chunkCoordinates.posZ);
        }

        ChunkCoordinates verifiedCoordinates = EntityPlayer.verifyRespawnCoordinates(player.worldObj, bedCoordinates, false);

        if (verifiedCoordinates == null)
        {
            verifiedCoordinates = new ChunkCoordinates(chunkCoordinates.posX, chunkCoordinates.posY, chunkCoordinates.posZ);
        }

        player.setSpawnChunk(verifiedCoordinates, false);
    }
}
