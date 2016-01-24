package io.github.vikestep.sprinklesforvanilla.common.network;

import io.github.vikestep.sprinklesforvanilla.SprinklesForVanilla;
import io.github.vikestep.sprinklesforvanilla.common.modules.IModule;
import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.Modules;
import io.github.vikestep.sprinklesforvanilla.common.utils.ModuleHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class NetworkHandler
{
    //Fired on the client when they are disconnected from a server
    @SuppressWarnings("UnusedDeclaration")
    @SubscribeEvent
    public void onClientDisconnectFromServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        SprinklesForVanilla.setIsOnServer(false);
    }

    // Send config info to players when they join
    @SuppressWarnings("UnusedDeclaration")
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        for (IModule module : Modules.modules)
        {
            for (IProperty property : module.getProperties())
            {
                boolean isArray = property.getType().isArray;
                String value = ModuleHelper.getIPropertyValueAsString(property);
                String[] values = ModuleHelper.getIPropertyValueAsStringArr(property);
                ConfigPacket packet = new ConfigPacket(module.getModuleName(), property.getName(), isArray, value, values);
                SprinklesForVanilla.network.sendTo(packet, (EntityPlayerMP) event.player);
            }
        }
        ModuleHelper.reloadModules();
    }
}
