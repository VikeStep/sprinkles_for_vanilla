package com.vikestep.sprinklesforvanilla.common.init;

import com.vikestep.sprinklesforvanilla.SprinklesForVanilla;
import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import com.vikestep.sprinklesforvanilla.common.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.util.ArrayList;
import java.util.List;

public class initFireInfo
{
    //TODO: Find a different way to do this, this is very convoluted and there is possibly a better way
    //I just couldn't find an easy way to do this at the time

    //If the user adds fire info for something that was registered by another mod, we should not remove it from the registry if the users joins a server without the mod
    private static boolean        defaultsChecked = false;
    private static List<String[]> otherDefaults   = new ArrayList<String[]>();
    private static List<String[]> checkedEntries  = new ArrayList<String[]>();

    //This will use configs if it is on server, use defaults if not (to allow this to be clientside only yet not have server desync
    //Will be called everytime the player connects to the server
    public static void addFireInfo()
    {
        List<String[]> defaultFlammableArr = new ArrayList<String[]>();
        List<String[]> flammableBlocksArr = new ArrayList<String[]>();
        for (int i = 0; i < Settings.defaultFlammable.length; i++)
        {
            defaultFlammableArr.add(Settings.defaultFlammable[i].split(", "));
        }
        for (int i = 0; i < Settings.flammableBlocks.length; i++)
        {
            String[] entry = Settings.flammableBlocks[i].split(", ");
            if (entry.length == 3)
            {
                flammableBlocksArr.add(entry);
            }
        }
        if (!defaultsChecked)
        {
            //We will see if anything from flammableBlocksArr has already been set
            //Also checks if one of the vanilla options has been altered by another mod to restore that
            for (String[] entry : flammableBlocksArr)
            {
                Block entryBlock = getBlockFromName(entry[0]);
                if (isValid(entry, true))
                {
                    int encouragement = Blocks.fire.getEncouragement(entryBlock);
                    int flammability = Blocks.fire.getFlammability(entryBlock);
                    int index = findVal(defaultFlammableArr, entry[0]);
                    if (encouragement != 0 || flammability != 0)
                    {
                        if (index == -1)
                        {
                            otherDefaults.add(new String[] {entry[0], String.valueOf(encouragement), String.valueOf(flammability)});
                        }
                        else if (!defaultFlammableArr.get(index)[1].equals(String.valueOf(encouragement)) && !defaultFlammableArr.get(index)[2].equals(String.valueOf(flammability)))
                        {
                            otherDefaults.add(new String[] {entry[0], String.valueOf(encouragement), String.valueOf(flammability)});
                        }
                    }
                }
            }
            defaultsChecked = true;
        }
        //This statement could be condensed to not have an else by using a conditional operator "a ? b : c" but it is expanded for the ease of reading it
        if (SprinklesForVanilla.isOnServer)
        {
            //Since the server has the mod, we will use flammableBlocksArr however we must remove things that have been removed from defaultFlammableArr
            for (String[] entry : defaultFlammableArr)
            {
                int index = findVal(flammableBlocksArr, entry[0]);
                if (index == -1 && isValid(entry, false))
                {
                    Blocks.fire.setFireInfo(getBlockFromName(entry[0]), 0, 0);
                }
            }
            registerFireArray(flammableBlocksArr);
        }
        else
        {
            //This occurs when we don't want the changes from the configs to apply. We will remove the changes if they have occurred and replace them with defaults
            for (String[] entry : flammableBlocksArr)
            {
                int index = findVal(defaultFlammableArr, entry[0]);
                int index2 = findVal(otherDefaults, entry[0]);
                if ((index == -1 || index2 == -1) && isValid(entry, true))
                {
                    Blocks.fire.setFireInfo(getBlockFromName(entry[0]), 0, 0);
                }
            }
            registerFireArray(defaultFlammableArr);
            registerFireArray(otherDefaults);
        }
        Blocks.fire.rebuildFireInfo();
    }

    private static int findVal(List<String[]> list, String string)
    {
        for (int i = 0; i < list.size(); i++)
        {
            if (list.get(i)[0].equals(string))
            {
                return i;
            }
        }
        return -1;
    }

    private static Block getBlockFromName(String name)
    {
        Object obj = Block.blockRegistry.getObject(name);
        if (obj instanceof Block)
        {
            return (Block) obj;
        }
        else
        {
            return null;
        }
    }

    private static void registerFireArray(List<String[]> list)
    {
        for (String[] entry : list)
        {
            //This will overwrite if Fire Info has already been set
            if (isValid(entry, !SprinklesForVanilla.isOnServer))
            {
                Blocks.fire.setFireInfo(getBlockFromName(entry[0]), Integer.parseInt(entry[1]), Integer.parseInt(entry[2]));
            }
        }
    }

    private static boolean isValid(String[] entry, boolean userFault)
    {
        if (getBlockFromName(entry[0]) != null && Integer.parseInt(entry[1]) >= 0 && Integer.parseInt(entry[2]) >= 0 && Integer.parseInt(entry[2]) <= 300)
        {
            return true;
        }
        else if (!checkedEntries.contains(entry))
        {
            String reason = getBlockFromName(entry[0]) == null ? "Invalid Block Name" : (Integer.parseInt(entry[1]) <= 0 ? "Negative value for speed" : "Flammability is not in between 0 and 300");
            if (userFault)
            {
                LogHelper.log("Invalid Flammable Block Config: " + entry[0] + entry[1] + entry[2] + ", Reason: " + reason);
            }
            else
            {
                LogHelper.log("Tell VikeStep he made a mistake: " + entry[0] + entry[1] + entry[2] + ", Reason: " + reason);
            }
            checkedEntries.add(entry);
        }
        return false;
    }
}
