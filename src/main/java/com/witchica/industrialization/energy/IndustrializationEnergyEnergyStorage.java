package com.witchica.industrialization.energy;

import net.neoforged.neoforge.energy.EnergyStorage;

public abstract class IndustrializationEnergyEnergyStorage extends EnergyStorage {

    public IndustrializationEnergyEnergyStorage(int capacity, int maxTransfer, int maxExtract) {
        super(capacity, maxTransfer, maxExtract);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energyReceived = super.receiveEnergy(maxReceive, simulate);

        if(energyReceived != 0 && !simulate) {
            onChanged();
        }

        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energyExtracted = super.extractEnergy(maxExtract, simulate);

        if(energyExtracted != 0 && !simulate) {
            onChanged();
        }

        return energyExtracted;
    }

    public int addEnergy(int maxReceive, boolean simulate) {
        int energyReceived = Math.min(capacity - energy, maxReceive);

        if(energyReceived != 0 && !simulate) {
            onChanged();
        }

        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }

    public abstract void onChanged();
}
