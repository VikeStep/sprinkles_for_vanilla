package com.vikestep.sprinklesforvanilla.common.handlers;

import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.block.BlockBed;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.Explosion;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.ExplosionEvent;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SleepHandler
{
    Field sleepTimer;
    Field sleeping;
    Field spawnChunk;
    Field spawnForced;

    public static boolean sleepingInHell = false;
    public static EntityPlayer playerSleepingInHell = (EntityPlayer) null;
    public static Explosion explosionCreated = (Explosion) null;

    public static boolean resetSpawn = false;
    public static boolean oldSpawnWasBed = false;
    public static EntityPlayer playerResettingSpawn = null;
    public static ChunkCoordinates spawnCoordinates;

    public static boolean createBed = false;
    public static int newBedX;
    public static int newBedY;
    public static int newBedZ;
    public static int newBedMeta;
    public static EntityPlayer playerSleeping;
    public static boolean shouldSleepInNewBed = false;

    public SleepHandler()
    {
        try
        {
            sleepTimer = EntityPlayer.class.getDeclaredField("sleepTimer");
            sleepTimer.setAccessible(true);
            sleeping = EntityPlayer.class.getDeclaredField("sleeping");
            sleeping.setAccessible(true);
            spawnChunk = EntityPlayer.class.getDeclaredField("spawnChunk");
            spawnChunk.setAccessible(true);
            spawnForced = EntityPlayer.class.getDeclaredField("spawnForced");
            spawnForced.setAccessible(true);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onPlayerSleep(PlayerSleepInBedEvent event)
    {
        boolean[] sleepChecks = getSleepChecks(event.entityPlayer, event.x, event.y, event.z);
        if (!sleepChecks[0])
        {
            event.result = EntityPlayer.EnumStatus.OTHER_PROBLEM;
        }
        else if (!sleepChecks[1] && Settings.playerMustSleepInOverworld)
        {
            event.result = EntityPlayer.EnumStatus.NOT_POSSIBLE_HERE;
        }
        else if (!sleepChecks[2] && Settings.dayCancelsSleep)
        {
            event.result = EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW;
        }
        else if (!sleepChecks[3] && Settings.distanceFromBedCancelsSleep)
        {
            event.result = EntityPlayer.EnumStatus.TOO_FAR_AWAY;
        }
        else if (!sleepChecks[4] && Settings.nearbyMobsCancelSleep)
        {
            event.result = EntityPlayer.EnumStatus.NOT_SAFE;
        }
        else
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
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (!Settings.playerMustSleepInOverworld && event.world.getBlock(event.x, event.y, event.z).isBed(event.world, event.x, event.y, event.z, event.entityPlayer) && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
        {
            BlockBed bed = (BlockBed) event.world.getBlock(event.x, event.y, event.z);
            int meta = event.world.getBlockMetadata(event.x, event.y, event.z);
            int bedX = event.x;
            int bedY = event.y;
            int bedZ = event.z;
            if (!bed.isBlockHeadOfBed(meta))
            {
                int direction = bed.getDirection(meta);
                bedX += bed.field_149981_a[direction][0];
                bedZ += bed.field_149981_a[direction][1];
                meta = event.world.getBlockMetadata(bedX, bedY, bedZ);
            }
            if (!event.world.isRemote && !event.world.provider.canRespawnHere() && event.world.getBiomeGenForCoords(bedX, bedZ) == BiomeGenBase.hell)
            {
                boolean occupied = false;
                if (bed.func_149976_c(meta))
                {
                    EntityPlayer entityplayer1 = null;
                    Iterator iterator = event.world.playerEntities.iterator();

                    while (iterator.hasNext())
                    {
                        EntityPlayer entityplayer2 = (EntityPlayer)iterator.next();

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
                        event.entityPlayer.addChatComponentMessage(new ChatComponentTranslation("tile.bed.occupied", new Object[0]));
                        occupied = true;
                    }

                    bed.func_149979_a(event.world, bedX, bedY, bedZ, false);
                }
                if (!occupied)
                {
                    EntityPlayer.EnumStatus enumstatus = event.entityPlayer.sleepInBedAt(bedX, bedY, bedZ);

                    if (enumstatus == EntityPlayer.EnumStatus.OK)
                    {
                        bed.func_149979_a(event.world, bedX, bedY, bedZ, true);
                        shouldSleepInNewBed = true;
                    }
                    else
                    {
                        if (enumstatus == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW)
                        {
                            event.entityPlayer.addChatComponentMessage(new ChatComponentTranslation("tile.bed.noSleep", new Object[0]));
                        }
                        else if (enumstatus == EntityPlayer.EnumStatus.NOT_SAFE)
                        {
                            event.entityPlayer.addChatComponentMessage(new ChatComponentTranslation("tile.bed.notSafe", new Object[0]));
                        }
                    }
                }
                sleepingInHell = true;
                playerSleepingInHell = event.entityPlayer;
                int k1 = bed.getDirection(meta);
                explosionCreated = new Explosion(event.entityPlayer.worldObj, (Entity) null, (double)((float)bedX + 0.5F + (float) bed.field_149981_a[k1][0]), (double)((float)bedY + 0.5F), (double)((float)bedZ + 0.5F + (float) bed.field_149981_a[k1][1]), 5.0F);
                newBedX = bedX;
                newBedY = bedY;
                newBedZ = bedZ;
                newBedMeta = k1;
                playerSleeping = event.entityPlayer;
            }
        }
    }

    @SubscribeEvent
    public void onExplosion(ExplosionEvent.Start event)
    {
        if (!Settings.playerMustSleepInOverworld && sleepingInHell && event.explosion.exploder == (Entity) null && event.explosion.explosionX == explosionCreated.explosionX && event.explosion.explosionY == explosionCreated.explosionY && event.explosion.explosionZ == explosionCreated.explosionZ && event.world == playerSleepingInHell.worldObj)
        {
            event.setCanceled(true);
            createBed = true;
            shouldSleepInNewBed = false;
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        try
        {
            if (resetSpawn && spawnCoordinates != spawnChunk.get(playerResettingSpawn))
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
        if (createBed)
        {
            List<EntityItem> itemList = playerSleeping.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(newBedX - 4, newBedY - 4, newBedZ - 4, newBedX + 4, newBedY + 4, newBedZ + 4));
            for (EntityItem item : itemList)
            {
                if (item.getEntityItem().getItem() == Items.bed)
                {
                    item.setDead();
                    break;
                }
            }
            playerSleeping.worldObj.setBlock(newBedX, newBedY, newBedZ, Blocks.bed, newBedMeta + 8, 3);
            playerSleeping.worldObj.setBlock(newBedX - BlockBed.field_149981_a[newBedMeta][0], newBedY, newBedZ - BlockBed.field_149981_a[newBedMeta][1], Blocks.bed, newBedMeta, 3);
            if (shouldSleepInNewBed)
            {
                playerSleeping.sleepInBedAt(newBedX, newBedY, newBedZ);
            }
            createBed = false;
        }
    }

    public boolean[] getSleepChecks(EntityPlayer player, int x, int y, int z)
    {
        boolean[] sleepChecks = new boolean[5];
        Arrays.fill(sleepChecks, true);

        if (!player.worldObj.isRemote)
        {
            sleepChecks[0] = !(player.isPlayerSleeping() || !player.isEntityAlive());
            sleepChecks[1] = player.worldObj.provider.isSurfaceWorld();
            sleepChecks[2] = !player.worldObj.isDaytime();
            int distX = Settings.distanceFromBed[0];
            int distY = Settings.distanceFromBed[1];
            int distZ = Settings.distanceFromBed[2];
            sleepChecks[3] = !(Math.abs(player.posX - x) > (double) distX || Math.abs(player.posY - y) > (double) distY || Math.abs(player.posZ - z) > (double) distZ);
            distX = Settings.nearbyMobDistance[0];
            distY = Settings.nearbyMobDistance[1];
            distZ = Settings.nearbyMobDistance[2];
            sleepChecks[4] = player.worldObj.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.getBoundingBox(x - distX, y - distY, z - distZ, x + distX, y + distY, z + distZ)).isEmpty();
        }
        else
        {
            Arrays.fill(sleepChecks, true);
        }

        return sleepChecks;
    }

    public void sleep(EntityPlayer player, int x, int y, int z)
    {
        if (player.isRiding())
        {
            player.mountEntity((Entity) null);
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
            sleepTimer.set(player, 100 - Settings.timeToSleep);
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

    public void setSpawn(EntityPlayer player, int x, int y, int z)
    {
        ChunkCoordinates chunkCoordinates = new ChunkCoordinates(x, y, z);
        ChunkCoordinates bedCoordinates = player.worldObj.getBlock(x, y, z).getBedSpawnPosition(player.worldObj, chunkCoordinates.posX, chunkCoordinates.posY, chunkCoordinates.posZ, player);
        if (bedCoordinates == null) {
            bedCoordinates = new ChunkCoordinates(chunkCoordinates.posX, chunkCoordinates.posY + 1, chunkCoordinates.posZ);
        }

        ChunkCoordinates verifiedCoordinates = EntityPlayer.verifyRespawnCoordinates(player.worldObj, bedCoordinates, false);

        if (verifiedCoordinates == null) {
            verifiedCoordinates = new ChunkCoordinates(chunkCoordinates.posX, chunkCoordinates.posY, chunkCoordinates.posZ);
        }

        player.setSpawnChunk(verifiedCoordinates, false);
    }


}
