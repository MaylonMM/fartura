package com.fartura.client.screen;

import com.fartura.Fartura;
import com.fartura.menu.PanelaMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PanelaScreen extends AbstractContainerScreen<PanelaMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Fartura.MODID, "textures/gui/panela.png");
    private static final ResourceLocation BURN_PROGRESS_SPRITE =
            ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");
    // Chama acesa da fornalha vanilla, desenhada sobre o ícone de calor da textura.
    private static final ResourceLocation LIT_SPRITE =
            ResourceLocation.withDefaultNamespace("container/furnace/lit_progress");

    private static final int ARROW_X = 90;
    private static final int ARROW_Y = 26;
    private static final int FLAME_X = 124;
    private static final int FLAME_Y = 50;

    public PanelaScreen(PanelaMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = 72;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int progress = this.menu.getScaledProgress();
        if (progress > 0) {
            guiGraphics.blitSprite(BURN_PROGRESS_SPRITE, 24, 16, 0, 0,
                    this.leftPos + ARROW_X, this.topPos + ARROW_Y, progress, 16);
        }

        if (this.menu.isHeated()) {
            guiGraphics.blitSprite(LIT_SPRITE, 14, 14, 0, 0, this.leftPos + FLAME_X, this.topPos + FLAME_Y, 14, 14);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
