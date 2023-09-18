package com.witchica.renewable.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.witchica.renewable.menu.EnergyInterfaceMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;

public class EnergyInterfaceScreen extends AbstractContainerScreen<EnergyInterfaceMenu> {
    public static final ResourceLocation ENERGY_INTERFACE_TEXTURE = new ResourceLocation("renewable_energy", "textures/gui/energy_interface.png");
    public static final ResourceLocation ENERGY_INTERFACE_ICONS_TEXTURE = new ResourceLocation("renewable_energy", "textures/gui/energy_interface_icons.png");

    private static final Component STORED_FE_TEXT = Component.translatable("text.renewable_energy.fe_stored");
    private static final Component PRODUCING_FE_TEXT = Component.translatable("text.renewable_energy.fe_producing");
    private static final Component SOLAR_NO_ISSUE = Component.translatable("text.renewable_energy.solar_no_issues");
    private static final Component SOLAR_RAINING = Component.translatable("text.renewable_energy.solar_raining");
    private static final Component SOLAR_NIGHT = Component.translatable("text.renewable_energy.solar_night");
    private static final Component SOLAR_BLOCKED = Component.translatable("text.renewable_energy.solar_blocked");
    private final Level level;

    public EnergyInterfaceScreen(EnergyInterfaceMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 166;
        
        this.level = pPlayerInventory.player.level();
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        super.renderLabels(pGuiGraphics, pMouseX, pMouseY);

        this.renderTooltip(pGuiGraphics, pMouseX-leftPos, pMouseY-topPos);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        this.renderBackground(pGuiGraphics);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, ENERGY_INTERFACE_TEXTURE);

        pGuiGraphics.blit(ENERGY_INTERFACE_TEXTURE, leftPos, topPos, 0, 0, getXSize(), getYSize());

        float energyLevel = menu.energyBlock.getEnergyLevel();
        pGuiGraphics.blit(ENERGY_INTERFACE_TEXTURE, leftPos + 152, topPos + 6 + (54 - (int) (energyLevel * 54)), 240, 54- (int) (energyLevel * 54), 16, (int) (energyLevel * 54));

        int weatherIconX = leftPos + 131;
        int weatherIconY = topPos + 5;

        if(menu.energyBlock.hasCurrentIcon()) {
            EnergyGeneratorIcon icon = menu.energyBlock.getCurrentIcon();
            pGuiGraphics.blit(ENERGY_INTERFACE_TEXTURE, weatherIconX, weatherIconY, 176, 32, 18, 18);

            if(icon != null) {
                pGuiGraphics.blit(ENERGY_INTERFACE_ICONS_TEXTURE, weatherIconX + 1, weatherIconY + 1, icon.uvX * 16, icon.uvY * 16, 16, 16, 64, 64);
            }
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        pGuiGraphics.drawString(this.font, STORED_FE_TEXT.getString().formatted(menu.energyBlock.getCurrentEnergyLevel(), menu.energyBlock.getMaximumEnergyLevel()), leftPos + 8, topPos + 18, 4210752, false);
        pGuiGraphics.drawString(this.font, PRODUCING_FE_TEXT.getString().formatted(menu.energyBlock.getCurrentFEPerTick()), leftPos + 8, topPos + 28, 4210752, false);
        pGuiGraphics.drawString(this.font, menu.energyBlock.getCurrentStatusText(), leftPos + 8, topPos + 38, 4210752, false);
    }
}
