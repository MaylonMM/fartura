package com.fartura.client.screen;

import com.fartura.Fartura;
import com.fartura.menu.StoneMortarMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class StoneMortarScreen extends AbstractContainerScreen<StoneMortarMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Fartura.MODID, "textures/gui/stone_mortar.png");
    private static final ResourceLocation BURN_PROGRESS_SPRITE =
            ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");

    public StoneMortarScreen(StoneMortarMenu menu, Inventory playerInventory, Component title) {
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
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        int progress = menu.getScaledProgress();
        if (progress > 0) {
            guiGraphics.enableScissor(x + 79, y + 34, x + 79 + progress, y + 34 + 16);
            guiGraphics.blitSprite(BURN_PROGRESS_SPRITE, x + 79, y + 34, 24, 16);
            guiGraphics.disableScissor();
        }
    }
}
