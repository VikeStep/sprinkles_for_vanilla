package io.github.vikestep.sprinklesforvanilla.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class GenericPacket implements IMessage
{
    private String message;

    public GenericPacket(String message)
    {
        this.message = message;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        message = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, message);
    }

    public static class Handler implements IMessageHandler<GenericPacket, IMessage>
    {

        @Override
        public IMessage onMessage(GenericPacket message, MessageContext ctx)
        {
            return null;
        }
    }
}
