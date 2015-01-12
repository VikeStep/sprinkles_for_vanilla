package com.vikestep.sprinklesforvanilla.common.handlers;

import com.vikestep.sprinklesforvanilla.common.reference.ModInfo;
import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.HashMap;
import java.util.UUID;

public class RespawnHandler
{
    private HashMap<UUID, PlayerProperties> playerMap = new HashMap<UUID, PlayerProperties>();

    //We do not need to worry about if this actually kills them. If it doesn't, all it will do is update the HashMap. We just don't want to be updating a HashMap every tick
    @SubscribeEvent
    public void onHurt(LivingHurtEvent event)
    {
        if (event.entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.entity;
            if (player.getHealth() - event.ammount <= 0 && !event.entity.worldObj.isRemote )
            {
                PlayerProperties props = (PlayerProperties) player.getExtendedProperties(ModInfo.MOD_NAME);
                props.health = player.getHealth();
                props.hunger = player.getFoodStats().getFoodLevel();
                props.experience = player.experience;
                props.experienceLevel = player.experienceLevel;
                props.experienceTotal = player.experienceTotal;
                playerMap.put(player.getPersistentID(), props);
                System.out.println(FMLCommonHandler.instance().getEffectiveSide());
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
    {
        System.out.println(FMLCommonHandler.instance().getEffectiveSide());
        PlayerProperties oldPlayerProps = playerMap.remove(event.player.getPersistentID());
        PlayerProperties props = (PlayerProperties) event.player.getExtendedProperties(ModInfo.MOD_NAME);
        EntityPlayer player = event.player;

        if (oldPlayerProps != null)
        {
            props.health  = oldPlayerProps.health;
            props.hunger  = oldPlayerProps.hunger;
            props.experience = oldPlayerProps.experience;
            props.experienceLevel = oldPlayerProps.experienceLevel;
            props.experienceTotal = oldPlayerProps.experienceTotal;
        }

        if (Settings.keepHunger >= 0)
        {
            player.setHealth(Math.max(props.health, (float) Settings.keepHealth));
        }
        if (Settings.keepHunger >= 0)
        {
            player.getFoodStats().addStats(Math.max(props.hunger, Settings.keepHunger) - 20, 0);
            if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            {
                player.getFoodStats().setFoodLevel(Math.max(props.hunger, Settings.keepHunger));
            }
        }
        if (Settings.keepXP)
        {
            player.experience      = props.experience;
            player.experienceLevel = props.experienceLevel;
            player.experienceTotal = props.experienceTotal;
        }
    }

    @SubscribeEvent
    public void onConstruction(EntityEvent.EntityConstructing event)
    {
        if (event.entity instanceof EntityPlayer && event.entity.getExtendedProperties(ModInfo.MOD_NAME) == null)
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
