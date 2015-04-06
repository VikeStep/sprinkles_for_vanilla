package io.github.vikestep.sprinklesforvanilla;

import cpw.mods.fml.common.FMLCommonHandler;
import io.github.vikestep.sprinklesforvanilla.common.handlers.EntityHandlers;
import io.github.vikestep.sprinklesforvanilla.common.handlers.PlayerHandlers;
import io.github.vikestep.sprinklesforvanilla.common.handlers.WorldHandlers;
import io.github.vikestep.sprinklesforvanilla.common.init.InitMobRegistry;
import io.github.vikestep.sprinklesforvanilla.common.network.NetworkHandler;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
    public void init()
    {
        FMLCommonHandler.instance().bus().register(new NetworkHandler());

        PlayerHandlers.PlayerSleepHandler sleepHandler = new PlayerHandlers.PlayerSleepHandler();
        PlayerHandlers.PlayerRespawnHandler respawnHandler = new PlayerHandlers.PlayerRespawnHandler();
        FMLCommonHandler.instance().bus().register(sleepHandler);
        MinecraftForge.EVENT_BUS.register(sleepHandler);
        FMLCommonHandler.instance().bus().register(respawnHandler);
        MinecraftForge.EVENT_BUS.register(respawnHandler);

        MinecraftForge.EVENT_BUS.register(new EntityHandlers.EnderPearlHandler());
        MinecraftForge.EVENT_BUS.register(new EntityHandlers.LivingAttackHandler());
        MinecraftForge.EVENT_BUS.register(new EntityHandlers.MobHandler());

        MinecraftForge.EVENT_BUS.register(new WorldHandlers.ExplosionHandler());
        MinecraftForge.EVENT_BUS.register(new WorldHandlers.WorldLoadHandler());

        InitMobRegistry.init();
    }
}
