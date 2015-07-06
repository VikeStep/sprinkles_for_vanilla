package io.github.vikestep.sprinklesforvanilla.common.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import io.github.vikestep.sprinklesforvanilla.SprinklesForVanilla;
import io.github.vikestep.sprinklesforvanilla.common.configuration.Settings;
import io.github.vikestep.sprinklesforvanilla.common.utils.LogHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("ALL")
public class EntityHandlers
{
    public static class EnderPearlHandler
    {
        @SubscribeEvent
        public void onPlayerInteract(PlayerInteractEvent event)
        {
            if (event.entityPlayer.getCurrentEquippedItem() == null)
            {
                return;
            }
            if (!Settings.enderPearlsTeleport[1] && SprinklesForVanilla.isOnServer && event.entityPlayer.getCurrentEquippedItem().getItem() == Items.ender_pearl && event.action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)
            {
                event.setCanceled(true);
            }
        }
    }

    public static class LivingAttackHandler
    {
        private HashMap<String, Integer> stoppedDamageSources = new HashMap<String, Integer>();

        public LivingAttackHandler()
        {
            stoppedDamageSources.clear();
            for (int i = 0; i < Settings.damageSourceConfigs[1].size(); i++)
            {
                int val = Settings.damageSourceConfigs[1].get(i);
                String damageSource = Settings.damageSources[i];
                if (val != 0)
                {
                    stoppedDamageSources.put(damageSource, val);
                }
            }
        }

        @SubscribeEvent
        public void onLivingAttack(LivingAttackEvent event)
        {
            if (stoppedDamageSources.keySet().contains(event.source.damageType) && SprinklesForVanilla.isOnServer)
            {
                int value = stoppedDamageSources.get(event.source.damageType);
                switch(value)
                {
                    case 1:
                        if (!(event.entity instanceof EntityPlayer))
                            break;
                    case 2:
                        event.setCanceled(true);
                        break;
                    default:
                        LogHelper.warn("Found incorrect value in stoppedDamageSources: " + event.source.damageType + ", " + value);
                        break;
                }
            }
        }
    }

    public static class MobHandler
    {
        private HashMap<String, Class<?>> blockedMobClasses = new HashMap<String, Class<?>>();

        public MobHandler()
        {
            blockedMobClasses.clear();
            Iterator iter = Settings.mobClasses.entrySet().iterator();
            int mobIndex = 0;
            while (iter.hasNext())
            {
                Map.Entry entry = (Map.Entry) iter.next();
                if (!Settings.mobConfigs[1].get(mobIndex))
                {
                    blockedMobClasses.put((String) entry.getKey(), (Class<?>) entry.getValue());
                }
                mobIndex++;
            }
        }

        @SubscribeEvent
        public void onEntityJoin(EntityJoinWorldEvent event)
        {
            Entity entity = event.entity;
            Class<?> entityClass = entity.getClass();
            if (blockedMobClasses.containsValue(entityClass) && SprinklesForVanilla.isOnServer)
            {
                if (entityClass == EntityZombie.class)
                {
                    if (blockedMobClasses.containsKey("zombieVillager") && ((EntityZombie) entity).isVillager())
                    {
                        event.setCanceled(true);
                    }
                    else if (blockedMobClasses.containsKey("zombie") && !((EntityZombie) entity).isVillager())
                    {
                        event.setCanceled(true);
                    }
                }
                else if (entityClass == EntitySkeleton.class)
                {
                    if (blockedMobClasses.containsKey("skeleton") && ((EntitySkeleton) entity).getSkeletonType() == 0)
                    {
                        event.setCanceled(true);
                    }
                    else if (blockedMobClasses.containsKey("witherSkeleton") && ((EntitySkeleton) entity).getSkeletonType() == 1)
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
}
