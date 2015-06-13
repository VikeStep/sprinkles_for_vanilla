package io.github.vikestep.sprinklesforvanilla.server.utils;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class ServerHelper
{
    public static WorldServer getWorldServer(Entity entity)
    {
        return MinecraftServer.getServer().worldServerForDimension(entity.dimension);
    }
}
