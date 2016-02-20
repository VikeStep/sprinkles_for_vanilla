package io.github.vikestep.sprinklesforvanilla.client.modules.particles;

import io.github.vikestep.sprinklesforvanilla.common.modules.DefaultProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;

import java.util.ArrayList;
import java.util.List;

public class ParticlesHandler
{
    public static final List<IProperty> particleConfigs;

    static
    {
        particleConfigs = new ArrayList<>();
        String description = "Each of these config options can either be set to true or false. true means that the " +
                "particle will be shown, \nfalse means it won't. To see what each of these particle types are, go to " +
                "\nhttp://minecraft.gamepedia.com/Particles";
        boolean first = false;
        for (String particle_name : EnumParticleTypes.getParticleNames())
        {
            particleConfigs.add(getParticleProperty(particle_name, first ? "" : description));
            first = true;
        }
    }

    public static IProperty getParticleProperty(String name, String description)
    {
        return new DefaultProperty(IProperty.Type.BOOLEAN, name, description, true, true, true);
    }

    public void initParticleRules()
    {
        for (IProperty particleProperty : particleConfigs)
        {
            if ((Boolean)particleProperty.getValue())
            {
                continue;
            }
            String name = particleProperty.getName();
            for (EnumParticleTypes particle : EnumParticleTypes.values())
            {
                if (particle.getParticleName().equals(name))
                {
                    Minecraft.getMinecraft().effectRenderer.registerParticle(particle.getParticleID(), null);
                }
            }
        }
    }
}
