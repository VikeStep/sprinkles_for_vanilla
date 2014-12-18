package com.vikestep.sprinklesforvanilla.common.handlers;

import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.util.Arrays;

public class MobHandler
{
    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event)
    {
        //Gets the class of the entity as a string
        String CLASS_NAME = event.entity.getClass().getName();
        //Removes path before and including the last "."
        String TRUNCATED_CLASS_NAME = CLASS_NAME.substring(CLASS_NAME.lastIndexOf(".") + 1);
        //Finds the class name in the mobNames Array. Will return -1 if the entity is not in the list
        int mobNum = Arrays.asList(Settings.mobNames).indexOf(TRUNCATED_CLASS_NAME);
        if (mobNum != -1)
        {
            mobNum = (mobNum - 1) / 2;
            if (TRUNCATED_CLASS_NAME.equals("EntityZombie"))
            {
                if (((EntityZombie) event.entity).isVillager())
                {
                    event.setCanceled(!Settings.mobNameConfigs[Arrays.asList(Settings.mobNames).indexOf("zombieVillagers") / 2]);
                }
                else
                {
                    event.setCanceled(Settings.mobNameConfigs[Arrays.asList(Settings.mobNames).indexOf("zombies") / 2]);
                }
            }
            else if (TRUNCATED_CLASS_NAME.equals("EntitySkeleton"))
            {
                if (((EntitySkeleton) event.entity).getSkeletonType() == 0)
                {
                    event.setCanceled(!Settings.mobNameConfigs[Arrays.asList(Settings.mobNames).indexOf("skeletons") / 2]);
                }
                else
                {
                    event.setCanceled(!Settings.mobNameConfigs[Arrays.asList(Settings.mobNames).indexOf("witherSkeletons") / 2]);
                }
            }
            else
            {
                event.setCanceled(!Settings.mobNameConfigs[mobNum]);
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
