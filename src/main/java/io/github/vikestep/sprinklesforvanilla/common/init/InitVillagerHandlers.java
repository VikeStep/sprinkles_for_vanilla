package io.github.vikestep.sprinklesforvanilla.common.init;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.VillagerRegistry;
import io.github.vikestep.sprinklesforvanilla.common.configuration.Settings;
import io.github.vikestep.sprinklesforvanilla.common.handlers.VillagerHandler;
import io.github.vikestep.sprinklesforvanilla.common.utils.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.util.Tuple;
import net.minecraft.village.MerchantRecipeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InitVillagerHandlers
{
    static final RegistryNamespaced itemRegistry = GameData.getItemRegistry();

    public static void initVillageHandlers()
    {
        HashMap<Integer, CustomRecipe> additionalRecipes = new HashMap<Integer, CustomRecipe>();

        for (String additionalRecipe : Settings.additionalVillagerTrades[1])
        {
            if (additionalRecipe.startsWith("#"))
            {
                continue;
            }
            String[] recipeData = additionalRecipe.replaceAll("\\s+", "").split(",");
            if (!(recipeData.length == 4 || recipeData.length == 5))
            {
                LogHelper.error("Invalid Villager Trade Addition: " + additionalRecipe + ". Needs either 3 or 4 parameters");
                continue;
            }
            int id;
            float chance;
            try
            {
                id = Integer.parseInt(recipeData[0]);
                chance = Float.parseFloat(recipeData[1]);
                if (chance < 0 || chance > 1)
                {
                    LogHelper.error("Invalid Villager Trade Addition: " + additionalRecipe + ". Second value should be a decimal between 0 and 1");
                    continue;
                }
            }
            catch (NumberFormatException e)
            {
                LogHelper.error("Invalid Villager Trade Addition: " + additionalRecipe + ". First value should be an integer and second value should be a decimal");
                continue;
            }
            String[] itemSelling = recipeData[2].split("\\(");
            ItemStack itemSellingStack = getItemStack(itemSelling[0]);
            String[] itemBuying = recipeData[3].split("\\(");
            ItemStack itemBuyingStack = getItemStack(itemBuying[0]);
            String[] itemBuyingSecond = {};
            ItemStack itemBuyingSecondStack = null;
            if (recipeData.length == 5)
            {
                itemBuyingSecond = recipeData[4].split("\\(");
                itemBuyingSecondStack = getItemStack(itemBuyingSecond[0]);
            }
            if (itemSellingStack != null && itemBuyingStack != null && !(recipeData.length == 5 && itemBuyingSecondStack == null))
            {
                if (itemSelling.length == 2 && itemBuying.length == 2 && !(recipeData.length == 5 && itemBuyingSecond.length != 2))
                {
                    String[] rangeSelling = itemSelling[1].replace(")","").split("-");
                    String[] rangeBuying = itemBuying[1].replace(")","").split("-");
                    String[] rangeBuyingSecond = {};
                    if (recipeData.length == 5)
                    {
                        rangeBuyingSecond = itemBuyingSecond[1].replace(")","").split("-");
                    }
                    if (rangeSelling.length == 2 && rangeBuying.length == 2 && !(recipeData.length == 5 && rangeBuyingSecond.length != 2))
                    {
                        try
                        {
                            Integer min = Integer.parseInt(rangeSelling[0]);
                            Integer max = Integer.parseInt(rangeSelling[1]);
                            EntityVillager.blacksmithSellingList.put(itemSellingStack.getItem(), new Tuple(min, max));
                            min = Integer.parseInt(rangeBuying[0]);
                            max = Integer.parseInt(rangeBuying[1]);
                            EntityVillager.villagersSellingList.put(itemBuyingStack.getItem(), new Tuple(min, max));
                            if (recipeData.length == 5)
                            {
                                min = Integer.parseInt(rangeBuyingSecond[0]);
                                max = Integer.parseInt(rangeBuyingSecond[1]);
                                EntityVillager.villagersSellingList.put(itemBuyingSecondStack.getItem(), new Tuple(min, max));
                            }
                            CustomRecipe customRecipe = additionalRecipes.get(id);
                            if (customRecipe == null)
                            {
                                //customRecipe = new CustomRecipe(new MerchantRecipeList(), new ArrayList<Float>());
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            LogHelper.error("");
                        }
                    }
                    else
                    {
                        LogHelper.error("");
                    }
                }
                else
                {
                    LogHelper.error("Invalid Villager Trade Addition: " + additionalRecipe + ". The Items/Blocks need to contain the name and the min and max. Refer to the comments for formatting.");
                }
            }
        }

        for(Map.Entry<Integer, CustomRecipe> entry : additionalRecipes.entrySet())
        {
            addVillagerTrades(entry.getKey(), entry.getValue().merchantRecipeList, entry.getValue().chanceList);
        }
    }

    public static ItemStack getItemStack(String itemName)
    {
        String[] itemParts = itemName.split(":");
        if (itemParts.length == 2 || itemParts.length == 3)
        {
            String fullItemName = itemParts[0] + itemParts[1];
            Block block = Block.getBlockFromName(fullItemName);
            Item item;
            if (block != null)
            {
                item = Item.getItemFromBlock(block);
            }
            else
            {
                if (itemRegistry.containsKey(fullItemName))
                {
                    item = (Item)itemRegistry.getObject(fullItemName);
                }
                else
                {
                    item = null;
                }
            }
            if (item == null)
            {
                LogHelper.error("Unable to find Item or Block with name " + fullItemName);
                return null;
            }
            if (itemParts.length == 2)
            {
                return new ItemStack(item, 1);
            }
            else
            {
                try
                {
                    int meta = Integer.parseInt(itemParts[2]);
                    return new ItemStack(item, 1, meta);
                }
                catch (NumberFormatException e)
                {
                    LogHelper.error("Invalid metadata for item/block: " + itemName);
                }
            }
        }
        else
        {
            LogHelper.error("Item/Block name should be in the form modname:itemname or modname:itemname:metadata. " + itemName);
        }
        return null;
    }

    public static void addVillagerTrades(int id, MerchantRecipeList merchantRecipeList, List<Float> chanceList)
    {
        VillagerRegistry.instance().registerVillageTradeHandler(id, new VillagerHandler(merchantRecipeList, chanceList));
    }

    class CustomRecipe
    {
        public MerchantRecipeList merchantRecipeList;
        public List<Float> chanceList;

        CustomRecipe(MerchantRecipeList merchantRecipeList, List<Float> chanceList)
        {
            this.merchantRecipeList = merchantRecipeList;
            this.chanceList = chanceList;
        }
    }
}
