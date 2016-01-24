package io.github.vikestep.sprinklesforvanilla.common.network;

import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.utils.ModuleHelper;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ConfigPacket implements IMessage
{
    private String   moduleName;
    private String   propertyName;
    private boolean  isArray;
    private String   value;
    private String[] values;

    @SuppressWarnings("UnusedDeclaration")
    public ConfigPacket()
    {

    }

    public ConfigPacket(String moduleName, String propertyName, boolean isArray, String value, String[] values)
    {
        this.moduleName = moduleName;
        this.propertyName = propertyName;
        this.isArray = isArray;
        this.value = value;
        this.values = values;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        moduleName = ByteBufUtils.readUTF8String(buf);
        propertyName = ByteBufUtils.readUTF8String(buf);
        isArray = buf.readBoolean();
        if (isArray)
        {
            int length = buf.readInt();
            values = new String[length];
            for (int i = 0; i < length; i++)
            {
                values[i] = ByteBufUtils.readUTF8String(buf);
            }
            value = null;
        }
        else
        {
            value = ByteBufUtils.readUTF8String(buf);
            values = null;
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, moduleName);
        ByteBufUtils.writeUTF8String(buf, propertyName);
        buf.writeBoolean(isArray);
        if (isArray)
        {
            buf.writeInt(values.length);
            for (String value : values)
            {
                ByteBufUtils.writeUTF8String(buf, value);
            }
        }
        else
        {
            ByteBufUtils.writeUTF8String(buf, value);
        }
    }

    public static class Handler implements IMessageHandler<ConfigPacket, IMessage>
    {
        @Override
        public IMessage onMessage(final ConfigPacket message, MessageContext ctx)
        {
            IProperty property = ModuleHelper.getPropertyFromName(message.moduleName, message.propertyName);
            if (property != null)
            {
                ModuleHelper.setIPropertyValueFromString(message.value, message.values, property);
            }
            return null;
        }
    }
}
