package com.fartura.compat.jei;

import com.fartura.Fartura;
import com.fartura.recipe.PanelaRecipe;
import com.fartura.registry.ModBlocks;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class PanelaRecipeCategory implements IRecipeCategory<PanelaRecipe> {
    public static final RecipeType<PanelaRecipe> RECIPE_TYPE =
            RecipeType.create(Fartura.MODID, "panela", PanelaRecipe.class);

    private static final ResourceLocation LIT_SPRITE =
            ResourceLocation.withDefaultNamespace("container/furnace/lit_progress");
    private static final ResourceLocation SLOT_SPRITE =
            ResourceLocation.withDefaultNamespace("container/slot");

    private static final int PANELA_GRID = 3;
    private static final int FLAME_X = 95;
    private static final int FLAME_Y = 36;
    private static final int ARROW_X = 62;
    private static final int ARROW_Y = 19;
    private static final int OUTPUT_X = 94;
    private static final int OUTPUT_Y = 18;

    private final RecipeType<PanelaRecipe> recipeType;
    private final Block block;
    private final String heatTooltipKey;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;

    public PanelaRecipeCategory(IGuiHelper helper) {
        this(helper, RECIPE_TYPE, ModBlocks.PANELA.get(), "jei.fartura.panela.heat");
    }

    protected PanelaRecipeCategory(IGuiHelper helper, RecipeType<PanelaRecipe> recipeType, Block block, String heatTooltipKey) {
        this.recipeType = recipeType;
        this.block = block;
        this.heatTooltipKey = heatTooltipKey;
        this.background = helper.createBlankDrawable(116, 54);
        this.icon = helper.createDrawableItemStack(new ItemStack(block));
        this.arrow = helper.getRecipeArrow();
    }

    @Override
    public RecipeType<PanelaRecipe> getRecipeType() {
        return recipeType;
    }

    @Override
    public Component getTitle() {
        return block.getName();
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
    public void setRecipe(IRecipeLayoutBuilder builder, PanelaRecipe recipe, IFocusGroup focuses) {
        var pattern = recipe.getPattern();
        int width = pattern.width();
        int height = pattern.height();
        var ingredients = pattern.ingredients();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Ingredient ingredient = ingredients.get(row * width + col);
                if (ingredient.isEmpty()) {
                    continue;
                }
                builder.addSlot(RecipeIngredientRole.INPUT, 1 + col * 18, 1 + row * 18).addIngredients(ingredient);
            }
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, OUTPUT_Y).addItemStack(recipe.getResult());
    }

    @Override
    public void draw(PanelaRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        for (int row = 0; row < PANELA_GRID; row++) {
            for (int col = 0; col < PANELA_GRID; col++) {
                drawSlot(guiGraphics, 1 + col * 18, 1 + row * 18);
            }
        }
        drawSlot(guiGraphics, OUTPUT_X, OUTPUT_Y);
        guiGraphics.blitSprite(LIT_SPRITE, 14, 14, 0, 0, FLAME_X, FLAME_Y, 14, 14);
        arrow.draw(guiGraphics, ARROW_X, ARROW_Y);
    }

    private static void drawSlot(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blitSprite(SLOT_SPRITE, 18, 18, 0, 0, x - 1, y - 1, 18, 18);
    }

    @Override
    public List<Component> getTooltipStrings(PanelaRecipe recipe, IRecipeSlotsView slotsView, double mouseX, double mouseY) {
        if (mouseX >= FLAME_X && mouseX < FLAME_X + 14 && mouseY >= FLAME_Y && mouseY < FLAME_Y + 14) {
            return List.of(Component.translatable(heatTooltipKey));
        }
        return List.of();
    }
}
