package io.github.vikestep.sprinklesforvanilla.client.modules.sounds;

import io.github.vikestep.sprinklesforvanilla.common.modules.DefaultProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.utils.LogHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class SoundHandler
{
    public static final IProperty soundsToStop;
    private static final List<String> incorrectEntries = new ArrayList<>();

    static
    {
        soundsToStop = new DefaultProperty(IProperty.Type.STRING_LIST, // type
                "soundsToStop", // name
                "In this list, include a list of sounds you wish to stop. Putting a '#' in front of a sound will act as a comment to enable the sound.", // comment
                false, // requiresWorldRestart
                false, // requiresMcRestart
                new String[]{"#minecraft:mob.wither.spawn", "#minecraft:mob.enderdragon.end", "#minecraft:portal.portal"} // defaultValue
        );
    }

    @SuppressWarnings("UnusedDeclaration")
    @SubscribeEvent
    public void onSound(PlaySoundEvent event)
    {
        ResourceLocation soundLocation = event.sound.getSoundLocation();
        String modName = soundLocation.getResourceDomain();
        String soundPath = soundLocation.getResourcePath();
        String[] sounds = (String[]) soundsToStop.getValue();
        for (String sound : sounds)
        {
            if (!sound.startsWith("#"))
            {
                String[] args = sound.split(":");
                if (args.length != 2)
                {
                    if (!incorrectEntries.contains(sound))
                    {
                        LogHelper.warn("INCORRECT CONFIG ENTRY:");
                        LogHelper.warn((args.length < 2 ? "You do not have this sound in the format (modname:soundPath): " : "Too many colons in disabled sound: ") + sound);
                        incorrectEntries.add(sound);
                    }
                }
                else if (modName.equals(args[0]) && soundPath.equals(args[1]))
                {
                    event.result = null;
                }
            }
        }
    }
}
