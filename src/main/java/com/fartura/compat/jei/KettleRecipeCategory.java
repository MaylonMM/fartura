package com.fartura.compat.jei;

import com.fartura.Fartura;
import com.fartura.recipe.PanelaRecipe;
import com.fartura.registry.ModBlocks;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;

public class KettleRecipeCategory extends PanelaRecipeCategory {
    public static final RecipeType<PanelaRecipe> KETTLE_RECIPE_TYPE =
            RecipeType.create(Fartura.MODID, "kettle", PanelaRecipe.class);

    public KettleRecipeCategory(IGuiHelper helper) {
        super(helper, KETTLE_RECIPE_TYPE, ModBlocks.KETTLE.get(), "jei.fartura.kettle.heat");
    }
}
