package io.github.vikestep.sprinklesforvanilla.common.handlers;

import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import java.util.List;
import java.util.Random;

public class VillagerHandler implements VillagerRegistry.IVillageTradeHandler
{
    private MerchantRecipeList merchantRecipeList;
    private List<Float> chanceList;

    public VillagerHandler(MerchantRecipeList merchantRecipeList, List<Float> chanceList)
    {
        this.merchantRecipeList = merchantRecipeList;
        this.chanceList = chanceList;
    }

    @Override
    public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random)
    {
        for (Object recipesToAdd : merchantRecipeList)
        {
            MerchantRecipe merchantRecipe = (MerchantRecipe) recipesToAdd;
            recipeList.addToListWithCheck(merchantRecipe);
        }
    }
}
