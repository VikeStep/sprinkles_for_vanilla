package io.github.vikestep.sprinklesforvanilla.common.utils;

import net.minecraft.village.MerchantRecipeList;

import java.util.List;

public class CustomMerchantRecipe
{
    public MerchantRecipeList  merchantRecipeList;
    public List<Float>         chanceList;
    public List<List<Integer>> minList;
    public List<List<Integer>> maxList;

    public CustomMerchantRecipe(MerchantRecipeList merchantRecipeList, List<Float> chanceList, List<List<Integer>> minList, List<List<Integer>> maxList)
    {
        this.merchantRecipeList = merchantRecipeList;
        this.chanceList = chanceList;
        this.minList = minList;
        this.maxList = maxList;
    }
}
