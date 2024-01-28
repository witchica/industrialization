package com.witchica.industrialization.features.capabilities;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

public interface ItemHandlerCapabilityProvider {
    public IItemHandler getItemHandlerCapability();
    public boolean isSideValidForItemHandler(Direction side);
}
