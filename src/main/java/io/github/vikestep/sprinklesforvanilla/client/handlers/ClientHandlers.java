package io.github.vikestep.sprinklesforvanilla.client.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import io.github.vikestep.sprinklesforvanilla.common.configuration.Settings;
import io.github.vikestep.sprinklesforvanilla.common.utils.LogHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.sound.PlaySoundEvent17;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class ClientHandlers
{
    public static class SoundHandler
    {
        private static List<String> incorrectEntries = new ArrayList<String>();

        @SubscribeEvent
        public void onSound(PlaySoundEvent17 event)
        {
            ResourceLocation soundLocation = event.sound.getPositionedSoundLocation();
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
}
