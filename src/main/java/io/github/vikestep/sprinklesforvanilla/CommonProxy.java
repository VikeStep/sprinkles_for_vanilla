package io.github.vikestep.sprinklesforvanilla;

import io.github.vikestep.sprinklesforvanilla.common.modules.IModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.Modules;
import io.github.vikestep.sprinklesforvanilla.common.modules.beacons.BeaconsModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.blocks.BlocksModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.explosions.ExplosionsModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.mobs.MobsModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.players.PlayersModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.portals.PortalsModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.sleep.SleepModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.villagers.VillagersModule;
import io.github.vikestep.sprinklesforvanilla.common.network.NetworkHandler;
import io.github.vikestep.sprinklesforvanilla.common.utils.ModuleHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommonProxy
{
    public void init()
    {
        Modules.modules.add(BeaconsModule.getInstance());
        Modules.modules.add(BlocksModule.getInstance());
        Modules.modules.add(ExplosionsModule.getInstance());
        Modules.modules.add(MobsModule.getInstance());
        Modules.modules.add(PlayersModule.getInstance());
        Modules.modules.add(PortalsModule.getInstance());
        Modules.modules.add(SleepModule.getInstance());
        Modules.modules.add(VillagersModule.getInstance());

        FMLCommonHandler.instance().bus().register(new NetworkHandler());
        Modules.modules.forEach(IModule::init);
        ModuleHelper.reloadModules();
    }
}
