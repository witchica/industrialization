package com.witchica.industrialization.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.witchica.industrialization.client.screen.widgets.BaseIndustrializationScreen;
import com.witchica.industrialization.client.screen.widgets.EnergyBarWidget;
import com.witchica.industrialization.menu.EnergyInterfaceMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;


public class EnergyInterfaceScreen extends BaseIndustrializationScreen<EnergyInterfaceMenu> {
    public EnergyInterfaceScreen(EnergyInterfaceMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
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

        pGuiGraphics.drawString(this.font, STORED_FE_TEXT.getString().formatted(menu.energyBlock.getCurrentEnergyLevel(), menu.energyBlock.getMaximumEnergyLevel()), leftPos + 8, topPos + 18, 4210752, false);
        pGuiGraphics.drawString(this.font, PRODUCING_FE_TEXT.getString().formatted(menu.energyBlock.getCurrentFEPerTick()), leftPos + 8, topPos + 28, 4210752, false);
        pGuiGraphics.drawString(this.font, menu.energyBlock.getCurrentStatusText(), leftPos + 8, topPos + 38, 4210752, false);

        int weatherIconX = leftPos + 131;
        int weatherIconY = topPos + 5;

        if(menu.energyBlock.hasCurrentIcon()) {
            EnergyGeneratorIcon icon = menu.energyBlock.getCurrentIcon();
            drawSlot(pGuiGraphics, weatherIconX, weatherIconY);

            if(icon != null) {
                pGuiGraphics.blit(ENERGY_INTERFACE_ICONS_TEXTURE, weatherIconX + 1, weatherIconY + 1, icon.uvX * 16, icon.uvY * 16, 16, 16, 64, 64);
            }
        }
    }
}
