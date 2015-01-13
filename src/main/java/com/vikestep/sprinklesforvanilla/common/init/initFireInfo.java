package com.vikestep.sprinklesforvanilla.common.init;

import com.vikestep.sprinklesforvanilla.common.reference.Settings;
import com.vikestep.sprinklesforvanilla.common.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class initFireInfo
{
    public static void init()
    {
        String[][] defaultFlammableArr = new String[Settings.defaultFlammable.length][3];
        String[][] flammableBlocksArr = new String[Settings.flammableBlocks.length][3];
        for (int i = 0; i < Settings.defaultFlammable.length; i++)
        {
            defaultFlammableArr[i] = Settings.defaultFlammable[i].split(", ");
        }
        for (int i = 0; i < Settings.flammableBlocks.length; i++)
        {
            String[] entry = Settings.flammableBlocks[i].split(", ");
            if (entry.length == 3)
            {
                flammableBlocksArr[i] = entry;
            }
        }
        for (String[] entry : defaultFlammableArr)
        {
            int index = findVal(flammableBlocksArr, 0, entry[0]);
            if (index == -1)
            {
                if (Block.blockRegistry.getObject(entry[0]) != null)
                {
                    Blocks.fire.setFireInfo((Block) Block.blockRegistry.getObject(entry[0]), 0, 0);
                }
            }
        }
        for (String[] entry : flammableBlocksArr)
        {
            //This will overwrite if Fire Info has already been set
            if (Block.blockRegistry.getObject(entry[0]) != null && Integer.parseInt(entry[1]) >= 0 && Integer.parseInt(entry[2]) >= 0 && Integer.parseInt(entry[2]) <= 300)
            {
                Blocks.fire.setFireInfo((Block) Block.blockRegistry.getObject(entry[0]), Integer.parseInt(entry[1]), Integer.parseInt(entry[2]));
            }
            else
            {
                String reason = Block.blockRegistry.getObject(entry[0]) == null ? "Invalid Block Name" : (Integer.parseInt(entry[1]) <= 0 ? "Negative value for speed" : "Flammability is not in between 0 and 300");
                LogHelper.log("Invalid Flammable Block Config: " + entry[0] + entry[1] + entry[2] + ", Reason: " + reason);
            }
        }
    }

    private static int findVal(String[][] arr, int col, String string)
    {
        for (int i = 0; i < arr.length; i++)
        {
            if (arr[i][col].equals(string))
            {
                return i;
            }
        }
        return -1;
    }
}
