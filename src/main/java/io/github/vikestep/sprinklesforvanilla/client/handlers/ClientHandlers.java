package io.github.vikestep.sprinklesforvanilla.client.handlers;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import io.github.vikestep.sprinklesforvanilla.common.configuration.Settings;
import io.github.vikestep.sprinklesforvanilla.common.utils.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class ClientHandlers
{
    public static class SoundHandler
    {
        private static List<String> incorrectEntries = new ArrayList<String>();

        @SubscribeEvent
        public void onSound(PlaySoundEvent event)
        {
            ResourceLocation soundLocation = event.sound.getSoundLocation();
            String modName = soundLocation.getResourceDomain();
            String soundPath = soundLocation.getResourcePath();
            for (String sound : Settings.disabledSounds)
            {
                if (!sound.startsWith("#") && sound.split(":").length == 2)
                {
                    if (modName.equals(sound.split(":")[0]) && soundPath.equals(sound.split(":")[1]))
                    {
                        event.result = null;
                    }
                }
                else if (sound.split(":").length != 2 && !sound.startsWith("#") && !incorrectEntries.contains(sound))
                {
                    LogHelper.warn("INCORRECT CONFIG ENTRY:");
                    LogHelper.warn((sound.split(":").length < 2 ? "You do not have this sound in the format (modname:soundPath): " : "Too many colons in disabled sound: ") + sound);
                    incorrectEntries.add(sound);
                }
            }
        }
    }

    public static class GuiHandler
    {
        private static boolean hasClicked = false;

        @SubscribeEvent
        public void onGuiOpen(GuiOpenEvent event)
        {
            GuiScreen gui = event.gui;
            if (!Settings.autoRespawn)
            {
                return;
            }
            if (gui instanceof GuiGameOver && !hasClicked)
            {
                Minecraft mc = Minecraft.getMinecraft();
                if (!mc.theWorld.getWorldInfo().isHardcoreModeEnabled())
                {
                    hasClicked = true;
                    mc.thePlayer.respawnPlayer();
                    mc.displayGuiScreen((GuiScreen) null);
                    event.setCanceled(true);
                }
            }
            else if (!(gui instanceof GuiGameOver) && hasClicked)
            {
                hasClicked = false;
            }
        }
    }
}
