package com.vikestep.sprinklesforvanilla.common.handlers;

import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MobHandler
{
    //Will only check for mobs which were false
    private HashMap<String, Class<?>> classList = new HashMap<String, Class<?>>();

    public MobHandler()
    {
        Iterator iter = Settings.mobClasses.entrySet().iterator();
        int mobIndex = 0;
        while (iter.hasNext())
        {
            Map.Entry entry = (Map.Entry) iter.next();
            if (!Settings.mobNameConfigs[mobIndex])
            {
                classList.put((String) entry.getKey(), (Class<?>) entry.getValue());
            }
            mobIndex++;
        }
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event)
    {
        Class<?> EntityClass = event.entity.getClass();
        if (classList.containsValue(EntityClass))
        {
            if (EntityClass == EntityZombie.class)
            {
                if (classList.containsKey("zombieVillager") && ((EntityZombie) event.entity).isVillager())
                {
                    event.setCanceled(true);
                }
                else if (classList.containsKey("zombie") && !((EntityZombie) event.entity).isVillager())
                {
                    event.setCanceled(true);
                }
            }
            else if (EntityClass == EntitySkeleton.class)
            {
                if (classList.containsKey("skeleton") && ((EntitySkeleton) event.entity).getSkeletonType() == 0)
                {
                    event.setCanceled(true);
                }
                else if (classList.containsKey("witherSkeleton") && ((EntitySkeleton) event.entity).getSkeletonType() == 1)
                {
                    event.setCanceled(true);
                }
            }
            else
            {
                event.setCanceled(true);
            }
        }
    }
}
