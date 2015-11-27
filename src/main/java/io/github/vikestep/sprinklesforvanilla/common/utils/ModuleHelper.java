package io.github.vikestep.sprinklesforvanilla.common.utils;

import io.github.vikestep.sprinklesforvanilla.common.modules.IModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.Modules;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.util.Arrays;

public class ModuleHelper
{
    public static String getIPropertyValueAsString(IProperty property)
    {
        switch (property.getType())
        {
            case INT:
            case STRING:
            case DOUBLE:
            case BOOLEAN:
                return property.getValue().toString();
            case INT_LIST:
            case STRING_LIST:
            case DOUBLE_LIST:
            case BOOLEAN_LIST:
                return null;
        }
        return null;
    }

    public static String[] getIPropertyValueAsStringArr(IProperty property)
    {
        String[] values;
        switch (property.getType())
        {
            case INT:
            case STRING:
            case DOUBLE:
            case BOOLEAN:
                return null;
            case INT_LIST:
                int[] ints = (int[])property.getValue();
                values = new String[ints.length];
                for (int i = 0; i < ints.length; i++)
                {
                    values[i] = Integer.toString(ints[i]);
                }
                return values;
            case STRING_LIST:
                return (String[]) property.getValue();
            case DOUBLE_LIST:
                double[] doubles = (double[])property.getValue();
                values = new String[doubles.length];
                for (int i = 0; i < doubles.length; i++)
                {
                    values[i] = Double.toString(doubles[i]);
                }
                return values;
            case BOOLEAN_LIST:
                boolean[] booleans = (boolean[])property.getValue();
                values = new String[booleans.length];
                for (int i = 0; i < booleans.length; i++)
                {
                    values[i] = Boolean.toString(booleans[i]);
                }
                return values;
        }
        return null;
    }

    public static Property getPropertyFromIProperty(IProperty prop, Configuration config, String category)
    {
        Property property;
        String comment = prop.getDescription();
        String name = prop.getName();
        Object defaultValue = prop.getDefault();
        switch (prop.getType())
        {
            case INT:
                property = config.get(category, name, (Integer)defaultValue, comment);
                break;
            case STRING:
                property = config.get(category, name, (String)defaultValue, comment);
                break;
            case DOUBLE:
                property = config.get(category, name, (Double)defaultValue, comment);
                break;
            case BOOLEAN:
                property = config.get(category, name, (Boolean)defaultValue, comment);
                break;
            case INT_LIST:
                property = config.get(category, name, (int[])defaultValue, comment);
                break;
            case STRING_LIST:
                property = config.get(category, name, (String[])defaultValue, comment);
                break;
            case DOUBLE_LIST:
                property = config.get(category, name, (double[])defaultValue, comment);
                break;
            case BOOLEAN_LIST:
                property = config.get(category, name, (boolean[])defaultValue, comment);
                break;
            default:
                LogHelper.error("Invalid Config Type " + name);
                prop.setValue(defaultValue);
                return null;
        }
        property.setRequiresMcRestart(prop.requiresMcRestart());
        property.setRequiresWorldRestart(prop.requiresWorldRestart());
        return property;
    }

    public static void setIPropertyValueFromProperty(IProperty prop, Property property)
    {
        Object defaultValue = prop.getDefault();
        switch (prop.getType())
        {
            case INT:
                prop.setValue(property.getInt((Integer)defaultValue));
                break;
            case STRING:
                prop.setValue(property.getString());
                break;
            case DOUBLE:
                prop.setValue(property.getDouble((Double)defaultValue));
                break;
            case BOOLEAN:
                prop.setValue(property.getBoolean((Boolean)defaultValue));
                break;
            case INT_LIST:
                prop.setValue(property.getIntList());
                break;
            case STRING_LIST:
                prop.setValue(property.getStringList());
                break;
            case DOUBLE_LIST:
                prop.setValue(property.getDoubleList());
                break;
            case BOOLEAN_LIST:
                prop.setValue(property.getBooleanList());
                break;
        }
    }

    public static void setIPropertyValueFromString(String value, String[] values, IProperty property)
    {
        switch (property.getType()){
            case INT:
                property.setValue(Integer.parseInt(value));
                break;
            case STRING:
                property.setValue(value);
                break;
            case DOUBLE:
                property.setValue(Double.parseDouble(value));
                break;
            case BOOLEAN:
                property.setValue(Boolean.parseBoolean(value));
                break;
            case INT_LIST:
                int[] ints = new int[values.length];
                for (int i = 0; i < values.length; i++)
                {
                    ints[i] = Integer.parseInt(values[i]);
                }
                property.setValue(ints);
                break;
            case STRING_LIST:
                property.setValue(values);
                break;
            case DOUBLE_LIST:
                double[] doubles = new double[values.length];
                for (int i = 0; i < values.length; i++)
                {
                    doubles[i] = Double.parseDouble(values[i]);
                }
                property.setValue(doubles);
                break;
            case BOOLEAN_LIST:
                boolean[] booleans = new boolean[values.length];
                for (int i = 0; i < values.length; i++)
                {
                    booleans[i] = Boolean.parseBoolean(values[i]);
                }
                property.setValue(booleans);
                break;
        }
    }

    public static IProperty getPropertyFromName(String moduleName, String propertyName)
    {
        IModule module = null;
        for (IModule mod : Modules.modules)
        {
            if (mod.getModuleName().equals(moduleName))
            {
                module = mod;
                break;
            }
        }
        if (module == null)
        {
            LogHelper.error("Unknown Module in ConfigPacket: " + moduleName);
            return null;

        }
        IProperty property = null;
        for (IProperty prop : module.getProperties())
        {
            if (prop.getName().equals(propertyName))
            {
                property = prop;
                break;
            }
        }
        if (property == null)
        {
            LogHelper.error("Unknown Property in ConfigPacket: " + moduleName + "." + propertyName);
        }
        return property;
    }

    public static boolean isPropertyDefault(IProperty property)
    {
        switch (property.getType())
        {
            case INT:
            case STRING:
            case DOUBLE:
            case BOOLEAN:
                return (property.getValue()).equals(property.getDefault());
            case INT_LIST:
                return Arrays.equals(((int[]) property.getValue()), (int[]) property.getDefault());
            case STRING_LIST:
                return Arrays.equals(((String[]) property.getValue()), (String[]) property.getDefault());
            case DOUBLE_LIST:
                return Arrays.equals(((double[]) property.getValue()), (double[]) property.getDefault());
            case BOOLEAN_LIST:
                return Arrays.equals(((boolean[]) property.getValue()), (boolean[]) property.getDefault());
        }
        return true;
    }

    public static void reloadModules()
    {
        for (IModule module : Modules.modules)
        {
            boolean isAllDefault = true;
            for (IProperty property : module.getProperties())
            {
                if (!isPropertyDefault(property))
                {
                    isAllDefault = false;
                }
            }
            if (isAllDefault)
            {
                module.disable();
            }
            else
            {
                module.enable();
            }
        }
    }
}
