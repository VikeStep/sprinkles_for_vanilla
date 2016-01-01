package io.github.vikestep.sprinklesforvanilla;

import io.github.vikestep.sprinklesforvanilla.client.modules.particles.ParticlesModule;
import io.github.vikestep.sprinklesforvanilla.client.modules.sounds.SoundsModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.Modules;

@SuppressWarnings("UnusedDeclaration")
public class ClientProxy extends CommonProxy
{
    @Override
    public void init()
    {
        Modules.modules.add(ParticlesModule.getInstance());
        Modules.modules.add(SoundsModule.getInstance());
        super.init();
    }
}
