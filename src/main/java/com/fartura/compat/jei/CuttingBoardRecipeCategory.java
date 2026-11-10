package com.fartura.compat.jei;

import com.fartura.Fartura;

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

public class CuttingBoardRecipeCategory implements IRecipeCategory<CuttingBoardRecipe> {
    public static final RecipeType<CuttingBoardRecipe> RECIPE_TYPE =
            RecipeType.create(Fartura.MODID, "cutting_board", CuttingBoardRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;

    public CuttingBoardRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(100, 18);
        this.icon = helper.createDrawableItemStack(new ItemStack(com.fartura.registry.ModBlocks.CUTTING_BOARD.get()));
        this.arrow = helper.getRecipeArrow();
    }

    @Override
    public RecipeType<CuttingBoardRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.fartura.cutting_board");
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
    public void setRecipe(IRecipeLayoutBuilder builder, CuttingBoardRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addItemStack(recipe.input());
        builder.addSlot(RecipeIngredientRole.INPUT, 29, 1).addItemStacks(recipe.tools());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 81, 1).addItemStack(recipe.output());
    }

    @Override
    public void draw(CuttingBoardRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.drawString(Minecraft.getInstance().font, "+", 20, 5, 0xFF404040, false);
        arrow.draw(guiGraphics, 48, 0);
    }
}
