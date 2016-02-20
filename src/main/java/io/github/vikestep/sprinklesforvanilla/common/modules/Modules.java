package io.github.vikestep.sprinklesforvanilla.common.modules;

import io.github.vikestep.sprinklesforvanilla.client.modules.misc.MiscClientModule;
import io.github.vikestep.sprinklesforvanilla.client.modules.particles.ParticlesModule;
import io.github.vikestep.sprinklesforvanilla.client.modules.sounds.SoundsModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.beacons.BeaconsModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.blocks.BlocksModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.explosions.ExplosionsModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.items.ItemsModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.mobs.MobsModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.players.PlayersModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.portals.PortalsModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.sleep.SleepModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.villagers.VillagersModule;

import java.util.ArrayList;
import java.util.List;

public class Modules
{
    public static final List<IModule> modules = new ArrayList<>();

    static
    {
        Modules.modules.add(BeaconsModule.getInstance());
        Modules.modules.add(BlocksModule.getInstance());
        Modules.modules.add(ExplosionsModule.getInstance());
        Modules.modules.add(MobsModule.getInstance());
        Modules.modules.add(PlayersModule.getInstance());
        Modules.modules.add(PortalsModule.getInstance());
        Modules.modules.add(SleepModule.getInstance());
        Modules.modules.add(VillagersModule.getInstance());
        Modules.modules.add(ItemsModule.getInstance());
        Modules.modules.add(ParticlesModule.getInstance());
        Modules.modules.add(SoundsModule.getInstance());
        Modules.modules.add(MiscClientModule.getInstance());
    }
}
