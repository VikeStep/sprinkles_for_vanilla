package com.vikestep.sprinklesforvanilla.common.handlers;

import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class MobHandler
{
    private static int findMobIndex(String searchTerm, int col)
    {
        int mobIndex = -1;
        for (int i = 0; i < Settings.mobNames.length; i++)
        {
            if (Settings.mobNames[i][col].equals(searchTerm))
            {
                mobIndex = i;
                break;
            }
        }
        return mobIndex;
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event)
    {
        //Gets the class of the entity as a string
        String CLASS_NAME = event.entity.getClass().getName();
        //Removes path before and including the last "."
        String TRUNCATED_CLASS_NAME = CLASS_NAME.substring(CLASS_NAME.lastIndexOf(".") + 1);
        //Finds the class name in the mobNames Array. Will return -1 if the entity is not in the list
        int mobIndex = findMobIndex(TRUNCATED_CLASS_NAME, 1);
        if (mobIndex != -1)
        {
            if (TRUNCATED_CLASS_NAME.equals("EntityZombie"))
            {
                if (((EntityZombie) event.entity).isVillager())
                {
                    event.setCanceled(!Settings.mobNameConfigs[findMobIndex("zombieVillager", 0)]);
                }
                else
                {
                    event.setCanceled(Settings.mobNameConfigs[findMobIndex("zombie", 0)]);
                }
            }
            else if (TRUNCATED_CLASS_NAME.equals("EntitySkeleton"))
            {
                if (((EntitySkeleton) event.entity).getSkeletonType() == 0)
                {
                    event.setCanceled(!Settings.mobNameConfigs[findMobIndex("skeleton", 0)]);
                }
                else
                {
                    event.setCanceled(!Settings.mobNameConfigs[findMobIndex("witherSkeleton", 0)]);
                }
            }
            else
            {
                event.setCanceled(!Settings.mobNameConfigs[mobIndex]);
            }
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.WorldTickEvent event)
    {
        if (event.world.getGameRules().getGameRuleBooleanValue("mobGriefing") && Settings.mobGriefingIsForcedFalse)
        {
            event.world.getGameRules().setOrCreateGameRule("mobGriefing", "false");
        }
    }
}
