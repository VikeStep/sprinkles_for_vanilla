package com.vikestep.sprinklesforvanilla.client.handlers;

import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import com.vikestep.sprinklesforvanilla.common.util.LogHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.sound.PlaySoundEvent17;

import java.util.ArrayList;
import java.util.List;

public class SoundHandler
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
                LogHelper.log("INCORRECT CONFIG ENTRY:");
                LogHelper.log((sound.split(":").length < 2 ? "You do not have this sound in the format (modname:soundPath): " : "Too many colons in disabled sound: ") + sound);
                incorrectEntries.add(sound);
            }
        }
    }
}
