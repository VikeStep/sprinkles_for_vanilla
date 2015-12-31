package io.github.vikestep.sprinklesforvanilla.common.modules;

import io.github.vikestep.sprinklesforvanilla.client.modules.particles.ParticlesModule;
import io.github.vikestep.sprinklesforvanilla.client.modules.sounds.SoundsModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.beacons.BeaconsModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.blocks.BlocksModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.explosions.ExplosionsModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.mobs.MobsModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.players.PlayersModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.portals.PortalsModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.sleep.SleepModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.villagers.VillagersModule;

import java.util.ArrayList;
import java.util.List;

public class Modules
{
    public static List<IModule> modules = new ArrayList<>();

    static
    {
        // Client Side
        modules.add(ParticlesModule.getInstance());
        modules.add(SoundsModule.getInstance());
        // Server Side
        modules.add(BeaconsModule.getInstance());
        modules.add(BlocksModule.getInstance());
        modules.add(ExplosionsModule.getInstance());
        modules.add(MobsModule.getInstance());
        modules.add(PlayersModule.getInstance());
        modules.add(PortalsModule.getInstance());
        modules.add(SleepModule.getInstance());
        modules.add(VillagersModule.getInstance());
    }
}
