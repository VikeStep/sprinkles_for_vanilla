package io.github.vikestep.sprinklesforvanilla.common.configuration;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigurationHandler
{
    private static Configuration config;

    public static void init(File file)
    {
        config = new Configuration(file);
        config.save();
    }
}
