package com.witchica.industrialization.features.capabilities;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.energy.IEnergyStorage;

public interface EnergyCapabilityProvider {
    public IEnergyStorage getEnergyCapability();
    public boolean isSideValidForEnergy(Direction side);
}
