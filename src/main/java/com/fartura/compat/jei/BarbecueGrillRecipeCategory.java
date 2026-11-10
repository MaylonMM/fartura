package com.fartura.compat.jei;

import com.fartura.Fartura;
import com.fartura.registry.ModBlocks;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;

public class BarbecueGrillRecipeCategory implements IRecipeCategory<CampfireCookingRecipe> {
    public static final RecipeType<CampfireCookingRecipe> RECIPE_TYPE =
            RecipeType.create(Fartura.MODID, "barbecue_grill", CampfireCookingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;

    public BarbecueGrillRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(82, 18);
        this.icon = helper.createDrawableItemStack(new ItemStack(ModBlocks.BARBECUE_GRILL.get()));
        this.arrow = helper.getRecipeArrow();
    }

    @Override
    public RecipeType<CampfireCookingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return ModBlocks.BARBECUE_GRILL.get().getName();
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
    public void setRecipe(IRecipeLayoutBuilder builder, CampfireCookingRecipe recipe, IFocusGroup focuses) {
        var registryAccess = Minecraft.getInstance().level.registryAccess();
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 55, 1).addItemStack(recipe.getResultItem(registryAccess));
    }

    @Override
    public void draw(CampfireCookingRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 24, 0);
    }
}
