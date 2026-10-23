package com.fartura.client.screen;

import com.fartura.client.recipebook.StoneMortarRecipeBookComponent;
import com.fartura.menu.StoneMortarMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

/**
 * Tela do pilão, espelhando a {@code AbstractFurnaceScreen} da vanilla: mesma textura da fornalha,
 * botão do livro de receitas e comportamento do livro ao clicar na seta.
 */
public class StoneMortarScreen extends AbstractContainerScreen<StoneMortarMenu> implements RecipeUpdateListener {
    private static final ResourceLocation FURNACE_TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/gui/container/furnace.png");
    private static final ResourceLocation BURN_PROGRESS_SPRITE =
            ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");
    private static final int BACKGROUND_COLOR = 0xFFC6C6C6;

    private final StoneMortarRecipeBookComponent recipeBookComponent = new StoneMortarRecipeBookComponent();
    private boolean widthTooNarrow;
    private ImageButton recipeButton;

    public StoneMortarScreen(StoneMortarMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        this.recipeButton = new ImageButton(this.leftPos + 20, this.height / 2 - 49, 20, 18,
                RecipeBookComponent.RECIPE_BUTTON_SPRITES, button -> {
                    this.recipeBookComponent.toggleVisibility();
                    this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
                    this.recipeButton.setPosition(this.leftPos + 20, this.height / 2 - 49);
                });
        this.addRenderableWidget(this.recipeButton);
        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = 72;
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.recipeBookComponent.tick();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
            this.recipeBookComponent.render(guiGraphics, mouseX, mouseY, partialTick);
        } else {
            this.recipeBookComponent.render(guiGraphics, mouseX, mouseY, partialTick);
            super.render(guiGraphics, mouseX, mouseY, partialTick);
        }
        this.recipeBookComponent.renderGhostRecipe(guiGraphics, this.leftPos, this.topPos, true, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        this.recipeBookComponent.renderTooltip(guiGraphics, this.leftPos, this.topPos, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(FURNACE_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // O pilão não tem combustível: apaga os espaços de chama e de combustível da textura da fornalha.
        guiGraphics.fill(this.leftPos + 56, this.topPos + 36, this.leftPos + 70, this.topPos + 50, BACKGROUND_COLOR);
        guiGraphics.fill(this.leftPos + 55, this.topPos + 52, this.leftPos + 73, this.topPos + 70, BACKGROUND_COLOR);

        int progress = this.menu.getScaledProgress();
        if (progress > 0) {
            guiGraphics.blitSprite(BURN_PROGRESS_SPRITE, 24, 16, 0, 0,
                    this.leftPos + 79, this.topPos + 34, progress, 16);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.recipeBookComponent.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        if (this.widthTooNarrow && this.recipeBookComponent.isVisible()) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void slotClicked(Slot slot, int slotId, int mouseButton, ClickType type) {
        super.slotClicked(slot, slotId, mouseButton, type);
        this.recipeBookComponent.slotClicked(slot);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.recipeBookComponent.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (this.recipeBookComponent.charTyped(codePoint, modifiers)) {
            return true;
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeft, int guiTop, int mouseButton) {
        boolean outside = this.recipeBookComponent.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos,
                this.imageWidth, this.imageHeight, mouseButton);
        return outside && super.hasClickedOutside(mouseX, mouseY, guiLeft, guiTop, mouseButton);
    }

    @Override
    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    @Override
    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }
}
