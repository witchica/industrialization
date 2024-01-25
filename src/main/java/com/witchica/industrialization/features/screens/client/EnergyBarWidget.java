package com.witchica.industrialization.features.screens.client;

import com.witchica.industrialization.features.energy.IndustrializationEnergyStorage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class EnergyBarWidget extends AbstractWidget {
    private final IndustrializationEnergyStorage energyStorage;
    private static final ResourceLocation ENERGY_INTERFACE_TEXTURE = new ResourceLocation("industrialization", "textures/gui/energy_interface.png");

    //16 + 54
    public EnergyBarWidget(int pX, int pY, int pWidth, int pHeight, IndustrializationEnergyStorage energyStorage) {
        super(pX, pY, pWidth, pHeight, Component.empty());
        this.energyStorage = energyStorage;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        float energyLevel = energyStorage.getEnergyLevel();

        guiGraphics.blit(ENERGY_INTERFACE_TEXTURE, getX(), getY(), 222, 0, 18, 56);
        guiGraphics.blit(ENERGY_INTERFACE_TEXTURE, getX() + 1, getY() + 1 + 54 - (int) (energyLevel * 54), 240, 54- (int) (energyLevel * 54), 16, (int) (energyLevel * 54));

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}
