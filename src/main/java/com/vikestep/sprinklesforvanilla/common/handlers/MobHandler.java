package com.vikestep.sprinklesforvanilla.common.handlers;

import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.boss.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class MobHandler
{
    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event)
    {
        boolean allowed = true;
        if (event.entity instanceof EntityChicken)
        {
            allowed = Settings.chickensAreEnabled;
        }
        else if (event.entity instanceof EntityCow)
        {
            allowed = Settings.cowsAreEnabled;
        }
        else if (event.entity instanceof EntityHorse)
        {
            allowed = Settings.horsesAreEnabled;
        }
        else if (event.entity instanceof EntityOcelot)
        {
            allowed = Settings.ocelotsAreEnabled;
        }
        else if (event.entity instanceof EntityPig)
        {
            allowed = Settings.pigsAreEnabled;
        }
        else if (event.entity instanceof EntitySheep)
        {
            allowed = Settings.sheepAreEnabled;
        }
        else if (event.entity instanceof EntityBat)
        {
            allowed = Settings.batsAreEnabled;
        }
        else if (event.entity instanceof EntityMooshroom)
        {
            allowed = Settings.mooshroomsAreEnabled;
        }
        else if (event.entity instanceof EntitySquid)
        {
            allowed = Settings.squidsAreEnabled;
        }
        else if (event.entity instanceof EntityVillager)
        {
            allowed = Settings.villagersAreEnabled;
        }
        else if (event.entity instanceof EntityCaveSpider)
        {
            allowed = Settings.caveSpidersAreEnabled;
        }
        else if (event.entity instanceof EntityEnderman)
        {
            allowed = Settings.endermenAreEnabled;
        }
        else if (event.entity instanceof EntitySpider)
        {
            allowed = Settings.spidersAreEnabled;
        }
        else if (event.entity instanceof EntityWolf)
        {
            allowed = Settings.wolvesAreEnabled;
        }
        else if (event.entity instanceof EntityPigZombie)
        {
            allowed = Settings.zombiePigmenAreEnabled;
        }
        else if (event.entity instanceof EntityBlaze)
        {
            allowed = Settings.blazesAreEnabled;
        }
        else if (event.entity instanceof EntityCreeper)
        {
            allowed = Settings.creepersAreEnabled;
        }
        else if (event.entity instanceof EntityGhast)
        {
            allowed = Settings.ghastsAreEnabled;
        }
        else if (event.entity instanceof EntityMagmaCube)
        {
            allowed = Settings.magmaCubesAreEnabled;
        }
        else if (event.entity instanceof EntitySilverfish)
        {
            allowed = Settings.silverfishAreEnabled;
        }
        else if (event.entity instanceof EntitySkeleton)
        {
            if(((EntitySkeleton) event.entity).getSkeletonType() == 0)
            {
                allowed = Settings.skeletonsAreEnabled;
            }
            else
            {
                allowed = Settings.witherSkeletonsAreEnabled;
            }
        }
        else if (event.entity instanceof EntitySlime)
        {
            allowed = Settings.slimesAreEnabled;
        }
        else if (event.entity instanceof EntityWitch)
        {
            allowed = Settings.witchesAreEnabled;
        }
        else if (event.entity instanceof EntityZombie)
        {
            if (((EntityZombie) event.entity).isVillager())
            {
                allowed = Settings.zombieVillagersAreEnabled;
            }
            else
            {
                allowed = Settings.zombiesAreEnabled;
            }
        }
        else if (event.entity instanceof EntitySnowman)
        {
            allowed = Settings.snowGolemsAreEnabled;
        }
        else if (event.entity instanceof EntityIronGolem)
        {
            allowed = Settings.ironGolemsAreEnabled;
        }
        else if (event.entity instanceof EntityWither)
        {
            allowed = Settings.withersAreEnabled;
        }
        else if (event.entity instanceof EntityDragon)
        {
            allowed = Settings.enderDragonsAreEnabled;
        }
        if (!allowed)
        {
            event.setCanceled(true);
        }
    }
}
