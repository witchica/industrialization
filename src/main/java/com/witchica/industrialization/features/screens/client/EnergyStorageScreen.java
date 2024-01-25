package com.witchica.industrialization.features.screens.client;

import com.witchica.industrialization.features.storage.EnergyStorageMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;


public class EnergyStorageScreen extends BaseIndustrializationScreen<EnergyStorageMenu> {
    public EnergyStorageScreen(EnergyStorageMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new EnergyBarWidget(leftPos + 151, topPos + 5, 18, 56, menu.energyBlock.energyStorage));

    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTicks, mouseX, mouseY);
        drawSlot(guiGraphics, leftPos + 151, topPos + 63);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}
