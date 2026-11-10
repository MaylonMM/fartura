package com.fartura.client.screen;

import com.fartura.Fartura;
import com.fartura.menu.BarbecueGrillMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BarbecueGrillScreen extends AbstractContainerScreen<BarbecueGrillMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Fartura.MODID, "textures/gui/barbecue_grill.png");
    private static final ResourceLocation LIT_PROGRESS_SPRITE =
            ResourceLocation.withDefaultNamespace("container/furnace/lit_progress");

    public BarbecueGrillScreen(BarbecueGrillMenu menu, Inventory playerInventory, Component title) {
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
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int flame = this.menu.getScaledLitProgress();
        if (flame > 0) {
            guiGraphics.blitSprite(LIT_PROGRESS_SPRITE, 14, 14, 0, 14 - flame,
                    this.leftPos + 81, this.topPos + 37 + 14 - flame, 14, flame);
        }
    }
}
