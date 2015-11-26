package io.github.vikestep.sprinklesforvanilla.common.configuration;

import io.github.vikestep.sprinklesforvanilla.common.modules.IModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.Modules;
import io.github.vikestep.sprinklesforvanilla.common.utils.LogHelper;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationHandler
{
    private static Configuration config;

    public static void init(File file)
    {
        config = new Configuration(file);

        String category;
        String comment;
        String name;
        Object defaultValue;
        Property property;
        List<String> propOrder;

        for (IModule module : Modules.modules)
        {
            category = (module.getModuleSide() == Side.CLIENT ? "clientside" : "global") + module.getModuleName();
            propOrder = new ArrayList<String>();
            for (IProperty prop : module.getProperties())
            {
                comment = prop.getDescription();
                name = prop.getName();
                defaultValue = prop.getDefault();
                switch (prop.getType())
                {
                    case INT:
                        property = config.get(category, name, (Integer)defaultValue, comment);
                        prop.setValue(property.getInt((Integer)defaultValue));
                        break;
                    case STRING:
                        property = config.get(category, name, (String)defaultValue, comment);
                        prop.setValue(property.getString());
                        break;
                    case DOUBLE:
                        property = config.get(category, name, (Double)defaultValue, comment);
                        prop.setValue(property.getDouble((Double)defaultValue));
                        break;
                    case BOOLEAN:
                        property = config.get(category, name, (Boolean)defaultValue, comment);
                        prop.setValue(property.getBoolean((Boolean)defaultValue));
                        break;
                    case INTLIST:
                        property = config.get(category, name, (int[])defaultValue, comment);
                        prop.setValue(property.getIntList());
                        break;
                    case STRINGLIST:
                        property = config.get(category, name, (String[])defaultValue, comment);
                        prop.setValue(property.getStringList());
                        break;
                    case DOUBLELIST:
                        property = config.get(category, name, (double[])defaultValue, comment);
                        prop.setValue(property.getDoubleList());
                        break;
                    case BOOLEANLIST:
                        property = config.get(category, name, (boolean[])defaultValue, comment);
                        prop.setValue(property.getBooleanList());
                        break;
                    default:
                        LogHelper.error("Invalid Config Type " + name);
                        prop.setValue(defaultValue);
                        continue;
                }
                property.setRequiresMcRestart(prop.requiresMcRestart());
                property.setRequiresWorldRestart(prop.requiresWorldRestart());
                propOrder.add(name);
            }
            config.setCategoryPropertyOrder(category, propOrder);
        }

        config.save();
    }
}
