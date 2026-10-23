package com.fartura.compat.jei;

import com.fartura.Fartura;
import com.fartura.recipe.MortarRecipe;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class MortarRecipeCategory implements IRecipeCategory<MortarRecipe> {
    public static final RecipeType<MortarRecipe> RECIPE_TYPE =
            RecipeType.create(Fartura.MODID, "stone_mortar", MortarRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;

    public MortarRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(72, 18);
        this.icon = helper.createDrawableItemStack(new net.minecraft.world.item.ItemStack(
                com.fartura.registry.ModItems.STONE_MORTAR.get()));
        this.arrow = helper.getRecipeArrow();
    }

    @Override
    public RecipeType<MortarRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.fartura.stone_mortar");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MortarRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 55, 1).addItemStack(recipe.getResult());
    }

    @Override
    public void draw(MortarRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 24, 0);
    }
}
