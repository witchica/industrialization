package com.witchica.industrialization.client.screen.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.witchica.industrialization.menu.EnergyInterfaceMenu;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;

public abstract class BaseIndustrializationScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements MenuAccess<T> {
    public static final ResourceLocation ENERGY_INTERFACE_TEXTURE = new ResourceLocation("industrialization", "textures/gui/energy_interface.png");
    public static final ResourceLocation ENERGY_INTERFACE_ICONS_TEXTURE = new ResourceLocation("industrialization", "textures/gui/energy_interface_icons.png");

    public static final Component STORED_FE_TEXT = Component.translatable("text.industrialization.fe_stored");
    public static final Component PRODUCING_FE_TEXT = Component.translatable("text.industrialization.fe_producing");
    public static final Component SOLAR_NO_ISSUE = Component.translatable("text.industrialization.solar_no_issues");
    public static final Component SOLAR_RAINING = Component.translatable("text.industrialization.solar_raining");
    public static final Component SOLAR_NIGHT = Component.translatable("text.industrialization.solar_night");
    public static final Component SOLAR_BLOCKED = Component.translatable("text.industrialization.solar_blocked");
    public final Level level;

    public BaseIndustrializationScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        this(pMenu, pPlayerInventory, pTitle, 176, 166);
    }

    public BaseIndustrializationScreen(T pMenu, Inventory pPlayerInventory, Component pTitle, int width, int height) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = width;
        this.imageHeight = height;
        this.level = pPlayerInventory.player.level();
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        super.renderLabels(pGuiGraphics, pMouseX, pMouseY);
        this.renderTooltip(pGuiGraphics, pMouseX-leftPos, pMouseY-topPos);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, ENERGY_INTERFACE_TEXTURE);

        guiGraphics.blit(ENERGY_INTERFACE_TEXTURE, leftPos, topPos, 0, 0, getXSize(), getYSize());
    }

    protected void drawSlot(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(ENERGY_INTERFACE_TEXTURE, x, y, 176, 32, 18, 18);
    }
}
