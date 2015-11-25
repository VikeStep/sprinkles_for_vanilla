package io.github.vikestep.sprinklesforvanilla.common.utils;

import io.github.vikestep.sprinklesforvanilla.common.reference.ModInfo;
import net.minecraftforge.fml.common.ModMetadata;

public class MetadataHelper
{
    public static void transformMetadata(ModMetadata meta)
    {
        meta.authorList.clear();
        meta.authorList.add("VikeStep");
        meta.modId = ModInfo.MOD_ID;
        meta.name = ModInfo.MOD_NAME;
        meta.description = "This mod adds a config file for vanilla";
        meta.version = ModInfo.VERSION;
        meta.url = "http://minecraft.curseforge.com/mc-mods/226309-sprinkles_for_vanilla";
    }
}
