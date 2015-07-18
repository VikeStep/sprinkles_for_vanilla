package io.github.vikestep.sprinklesforvanilla.common.handlers;

import cpw.mods.fml.common.registry.VillagerRegistry;
import io.github.vikestep.sprinklesforvanilla.SprinklesForVanilla;
import io.github.vikestep.sprinklesforvanilla.common.utils.CustomMerchantRecipe;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import java.util.List;
import java.util.Random;

public class VillagerHandler implements VillagerRegistry.IVillageTradeHandler
{
    private MerchantRecipeList merchantRecipeList;
    private List<Float> chanceList;
    public List<List<Integer>> minList;
    public List<List<Integer>> maxList;

    public VillagerHandler(CustomMerchantRecipe customMerchantRecipe)
    {
        this.merchantRecipeList = customMerchantRecipe.merchantRecipeList;
        this.chanceList = customMerchantRecipe.chanceList;
        this.minList = customMerchantRecipe.minList;
        this.maxList = customMerchantRecipe.maxList;
    }

    @Override
    public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random)
    {
        if (SprinklesForVanilla.isOnServer)
        {
            for (int i = 0; i < merchantRecipeList.size(); i++)
            {

                MerchantRecipe merchantRecipe = (MerchantRecipe) merchantRecipeList.get(i);
                Float chance = chanceList.get(i);
                List<Integer> mins = minList.get(i);
                List<Integer> maxs = maxList.get(i);
                ItemStack itemToSell = merchantRecipe.getItemToSell();
                itemToSell.stackSize = getItemAmount(mins.get(0), maxs.get(0), random);
                ItemStack itemToBuy = merchantRecipe.getItemToBuy();
                itemToBuy.stackSize = getItemAmount(mins.get(1), maxs.get(1), random);
                ItemStack itemToBuySecond = merchantRecipe.getSecondItemToBuy();
                if (itemToBuySecond != null && mins.size() == 3 && maxs.size() == 3)
                {
                    itemToBuySecond.stackSize = getItemAmount(mins.get(2), maxs.get(2), random);
                }
                if (random.nextFloat() < chance)
                {
                    //I don't do addToListWithCheck because I allow multiple trades of the same item types
                    recipeList.add(merchantRecipe);
                }
            }
        }
    }

    private static int getItemAmount(Integer min, Integer max, Random rand)
    {
        return min >= max ? min : min + rand.nextInt(max - min);
    }
}
