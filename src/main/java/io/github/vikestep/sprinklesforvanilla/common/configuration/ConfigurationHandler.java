package io.github.vikestep.sprinklesforvanilla.common.configuration;

import io.github.vikestep.sprinklesforvanilla.common.modules.IModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.Modules;
import io.github.vikestep.sprinklesforvanilla.common.utils.ModuleHelper;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationHandler
{

    public static void init(File file)
    {
        Configuration config = new Configuration(file);

        String category;
        Property property;
        List<String> propOrder;

        for (IModule module : Modules.modules)
        {
            category = (module.getModuleSide() == Side.CLIENT ? "client-side." : "global.") + module.getModuleName();
            propOrder = new ArrayList<>();
            for (IProperty prop : module.getProperties())
            {
                property = ModuleHelper.getPropertyFromIProperty(prop, config, category);
                ModuleHelper.setIPropertyValueFromProperty(prop, property);
                propOrder.add(prop.getName());
            }
            config.setCategoryPropertyOrder(category, propOrder);
        }

        config.save();
    }
}
