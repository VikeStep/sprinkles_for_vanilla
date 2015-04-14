package io.github.vikestep.sprinklesforvanilla.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.github.vikestep.sprinklesforvanilla.common.configuration.Settings;
import io.github.vikestep.sprinklesforvanilla.common.utils.LogHelper;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigPacket implements IMessage
{
    private String key;
    private String value;

    public ConfigPacket()
    {

    }

    public ConfigPacket(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        String[] message = ByteBufUtils.readUTF8String(buf).split("::");
        if (message.length == 2)
        {
            this.key = message[0];
            this.value = message[1];
        }
        else
        {
            LogHelper.warn("A config message packet has been sent with an incorrect format: " + ByteBufUtils.readUTF8String(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, key + "::" + value);
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public static class Handler implements IMessageHandler<ConfigPacket, IMessage>
    {
        @Override
        public IMessage onMessage(ConfigPacket message, MessageContext ctx)
        {
            try
            {
                Field field = Settings.class.getDeclaredField(message.getKey());
                if (field == null)
                {
                    return null;
                }
                Object configValue = field.get(null);
                if (configValue instanceof int[])
                {
                    int[] newValue = new int[]{((int[]) configValue)[0], Integer.parseInt(message.getValue())};
                    field.set(null, newValue);
                }
                else if (configValue instanceof boolean[])
                {
                    boolean[] newValue = new boolean[]{((boolean[]) configValue)[0], Boolean.parseBoolean(message.getValue())};
                    field.set(null, newValue);
                }
                else if (configValue instanceof double[])
                {
                    double[] newValue = new double[]{((double[]) configValue)[0], Double.parseDouble(message.getValue())};
                    field.set(null, newValue);
                }
                else if (configValue instanceof double[][])
                {
                    String[] doubleStrings = message.getValue().split(";");
                    double[] doubleArr = new double[doubleStrings.length];
                    for(int i = 0; i < doubleStrings.length; i++)
                    {
                        doubleArr[i] = Double.parseDouble(doubleStrings[i]);
                    }
                    double[][] newValue = new double[][]{((double[][])configValue)[0], doubleArr};
                    field.set(null, newValue);
                }
                //Because Screw Type Erasure
                else if (configValue instanceof List<?>[])
                {
                    String[] stringListsNames  = new String[]{/*"flammableBlocks", */"beaconBaseBlocks", "explosionData", "mobSpawnRulesModifications"};
                    String[] booleanListsNames = new String[]{"mobGriefingConfigs", "mobConfigs"};
                    String[] intListNames      = new String[]{"damageSourceConfigs"};
                    if (Arrays.asList(stringListsNames).contains(message.getKey()))
                    {
                        String[] stringArr = message.getValue().split(";");
                        List<String>[] newValues = (ArrayList<String>[])new ArrayList[2];
                        newValues[0] = new ArrayList<String>();
                        newValues[1] = new ArrayList<String>();
                        for(String entry : stringArr)
                        {
                            newValues[1].add(entry);
                        }
                        for (String originalEntry : ((ArrayList<String>[]) configValue)[0])
                        {
                            newValues[0].add(originalEntry);
                        }
                        field.set(null, newValues);
                    }
                    else if (Arrays.asList(booleanListsNames).contains(message.getKey()))
                    {
                        String[] stringArr = message.getValue().split(";");
                        List<Boolean>[] newValues = (ArrayList<Boolean>[])new ArrayList[2];
                        newValues[0] = new ArrayList<Boolean>();
                        newValues[1] = new ArrayList<Boolean>();
                        for(String entry : stringArr)
                        {
                            newValues[1].add(Boolean.parseBoolean(entry));
                        }
                        for (Boolean originalEntry : ((ArrayList<Boolean>[]) configValue)[0])
                        {
                            newValues[0].add(originalEntry);
                        }
                        field.set(null, newValues);
                    }
                    else if (Arrays.asList(intListNames).contains(message.getKey()))
                    {
                        String[] stringArr = message.getValue().split(";");
                        List<Integer>[] newValues = (ArrayList<Integer>[])new ArrayList[2];
                        newValues[0] = new ArrayList<Integer>();
                        newValues[1] = new ArrayList<Integer>();
                        for(String entry : stringArr)
                        {
                            newValues[1].add(Integer.parseInt(entry));
                        }
                        for (Integer originalEntry : ((ArrayList<Integer>[]) configValue)[0])
                        {
                            newValues[0].add(originalEntry);
                        }
                        field.set(null, newValues);
                    }
                }
            }
            catch (ReflectiveOperationException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }
}