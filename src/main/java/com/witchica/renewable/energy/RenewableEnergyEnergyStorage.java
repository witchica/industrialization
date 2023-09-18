package com.witchica.renewable.energy;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.EnergyStorage;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class RenewableEnergyEnergyStorage extends EnergyStorage {
    private final Supplier<Boolean> onChanged;

    public RenewableEnergyEnergyStorage(int capacity, int maxTransfer, int maxExtract, Supplier<Boolean> onChanged) {
        super(capacity, maxTransfer, maxExtract);
        this.onChanged = onChanged;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energyReceived = super.receiveEnergy(maxReceive, simulate);

        if(energyReceived != 0 && !simulate) {
            onChanged.get();
        }

        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energyExtracted = super.extractEnergy(maxExtract, simulate);

        if(energyExtracted != 0 && !simulate) {
            onChanged.get();
        }

        return energyExtracted;
    }

    public int addEnergy(int maxReceive, boolean simulate) {
        int energyReceived = Math.min(capacity - energy, maxReceive);

        if(energyReceived != 0 && !simulate) {
            onChanged.get();
        }

        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }
}
