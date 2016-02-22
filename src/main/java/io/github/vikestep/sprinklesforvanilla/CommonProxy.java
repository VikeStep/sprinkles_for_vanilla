package io.github.vikestep.sprinklesforvanilla;

import io.github.vikestep.sprinklesforvanilla.common.configuration.Settings;
import io.github.vikestep.sprinklesforvanilla.common.handlers.EntityHandlers;
import io.github.vikestep.sprinklesforvanilla.common.handlers.PlayerHandlers;
import io.github.vikestep.sprinklesforvanilla.common.handlers.WorldHandlers;
import io.github.vikestep.sprinklesforvanilla.common.init.InitLightLevels;
import io.github.vikestep.sprinklesforvanilla.common.init.InitMobRegistry;
import io.github.vikestep.sprinklesforvanilla.common.network.NetworkHandler;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
    public void init()
    {
        MinecraftForge.EVENT_BUS.register(new NetworkHandler());

        PlayerHandlers.PlayerSleepHandler sleepHandler = new PlayerHandlers.PlayerSleepHandler();
        PlayerHandlers.PlayerRespawnHandler respawnHandler = new PlayerHandlers.PlayerRespawnHandler();
        MinecraftForge.EVENT_BUS.register(sleepHandler);
        MinecraftForge.EVENT_BUS.register(respawnHandler);

        MinecraftForge.EVENT_BUS.register(new EntityHandlers.EnderPearlHandler());
        MinecraftForge.EVENT_BUS.register(new EntityHandlers.LivingAttackHandler());
        MinecraftForge.EVENT_BUS.register(new EntityHandlers.MobHandler());
        MinecraftForge.EVENT_BUS.register(new EntityHandlers.LivingHurtHandler());

        MinecraftForge.EVENT_BUS.register(new WorldHandlers.ExplosionHandler());
        MinecraftForge.EVENT_BUS.register(new WorldHandlers.WorldPotentialSpawnsHandler());


        InitMobRegistry.init();
        InitLightLevels.tweakLightValues();
        if (!Settings.enableSpawnFuzz[1])
        {
            ForgeModContainer.defaultHasSpawnFuzz = false;
        }
    }
}
