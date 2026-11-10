package com.fartura.compat.jei;

import com.fartura.Fartura;
import com.fartura.recipe.FrigideiraRecipe;
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

public class FrigideiraRecipeCategory implements IRecipeCategory<FrigideiraRecipe> {
    public static final RecipeType<FrigideiraRecipe> RECIPE_TYPE =
            RecipeType.create(Fartura.MODID, "frigideira", FrigideiraRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;

    public FrigideiraRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(82, 18);
        this.icon = helper.createDrawableItemStack(new ItemStack(ModBlocks.IRON_FRYING_PAN.get()));
        this.arrow = helper.getRecipeArrow();
    }

    @Override
    public RecipeType<FrigideiraRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return ModBlocks.IRON_FRYING_PAN.get().getName();
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
    public void setRecipe(IRecipeLayoutBuilder builder, FrigideiraRecipe recipe, IFocusGroup focuses) {
        var registryAccess = Minecraft.getInstance().level.registryAccess();
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 55, 1).addItemStack(recipe.getResultItem(registryAccess));
    }

    @Override
    public void draw(FrigideiraRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 24, 0);
    }
}
