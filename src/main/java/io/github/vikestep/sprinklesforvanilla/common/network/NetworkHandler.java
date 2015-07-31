package io.github.vikestep.sprinklesforvanilla.common.network;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import io.github.vikestep.sprinklesforvanilla.SprinklesForVanilla;
import io.github.vikestep.sprinklesforvanilla.common.configuration.Settings;
import io.github.vikestep.sprinklesforvanilla.common.utils.LogHelper;
import net.minecraft.entity.player.EntityPlayerMP;

import org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ALL")
public class NetworkHandler
{
    private static final String[] blacklistedSettingsFields = new String[]
            {
                    "particleNames",
                    "defaultBeaconBaseBlocks",
                    //"defaultFlammable",
                    "damageSources",
                    "mobGriefingTypes",
                    "defaultExplosionData",
                    "defaultModifications",
                    "defaultHeights",
                    "defaultRates",
                    "defaultAdditionalTrades",
                    "defaultLightValues"
            };

    //Fired on the client when they are disconnected from a server
    @SubscribeEvent
    public void onClientDisconnectFromServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        SprinklesForVanilla.isOnServer = false;
    }

    //Fired when the client joins a server. SprinklesForVanilla.isOnServer will already be set with the correct value at this point
    @SubscribeEvent
    public void onClientConnectToServer(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        if (SprinklesForVanilla.isOnServer)
        {
            //SprinklesForVanilla.network.sendToServer(new ConfigPacket("",""));
            LogHelper.info("Client has Joined Server with sprinkles_for_vanilla");
        }
    }

    //Fire on the server when a player logs in
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        //This will send config data from the server to the client
        try
        {
            for (Field field : Settings.class.getDeclaredFields())
            {
                Object val = field.get(null);
                if (val.getClass().isArray() && !Arrays.asList(blacklistedSettingsFields).contains(field.getName()))
                {
                    sendConfigInfo(field.getName(), val, (EntityPlayerMP) event.player);
                }
            }
        }
        catch (ReflectiveOperationException e)
        {
            e.printStackTrace();
        }
    }

    private static void sendConfigInfo(String configName, Object configValue, EntityPlayerMP player)
    {
        String configSendValue = null;
        if (configValue instanceof int[])
        {
            configSendValue = Integer.toString(((int[]) configValue)[1]);
        }
        else if (configValue instanceof boolean[])
        {
            configSendValue = Boolean.toString(((boolean[]) configValue)[1]);
        }
        else if (configValue instanceof double[])
        {
            configSendValue = Double.toString(((double[]) configValue)[1]);
        }
        else if (configValue instanceof String[])
        {
            configSendValue = ((String[])configValue)[1];
        }
        else if (configValue instanceof float[])
        {
            configSendValue = Float.toString(((float[]) configValue)[1]);
        }
        else if (configValue instanceof double[][])
        {
            double[] sendList = ((double[][]) configValue)[1];
            StringBuilder sb = new StringBuilder();
            for (double d : sendList)
            {
                sb.append(d);
                sb.append(";");
            }
            configSendValue = sb.toString();
        }
        else if (configValue instanceof List<?>[])
        {
            List<?> sendList = ((List<?>[]) configValue)[1];
            if (sendList.size() == 0)
            {
                configSendValue = "EMPTY_LIST";
            }
            else
            {
                StringBuilder sb = new StringBuilder();
                for (Object obj : sendList)
                {
                    sb.append(obj);
                    sb.append(";");
                }
                configSendValue = sb.toString();
            }
        }

        if(configSendValue != null)
        {
            SprinklesForVanilla.network.sendTo(new ConfigPacket(configName, configSendValue), player);
        }
        else
        {
            LogHelper.warn("Failed to send config value " + configName + " of type " + Type.getInternalName(configValue.getClass()));
        }
    }
}
