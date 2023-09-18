package com.witchica.renewable.energy;

import com.witchica.renewable.RenewableEnergy;
import com.witchica.renewable.inventory.RenewableEnergyItemStackHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RenewableEnergyItemEnergyHandler implements ICapabilityProvider {
    private final ItemStack stack;
    private LazyOptional<RenewableEnergyEnergyStorage> energyStorage;
    public RenewableEnergyItemEnergyHandler(ItemStack stack, CompoundTag tag, int maxAmount, int maxTransfer) {
        this.stack = stack;
        this.energyStorage = LazyOptional.of(() -> new RenewableEnergyEnergyStorage(maxAmount, maxTransfer, maxTransfer, this::onChanged));

        if (stack.hasTag() && stack.getTag().contains("EnergyStorage")) {
            this.energyStorage.orElse(null).deserializeNBT(stack.getTag().get("EnergyStorage"));
        }
    }

    public boolean onChanged() {
        if(!stack.hasTag()) {
            stack.setTag(new CompoundTag());
        }

        stack.getTag().put("EnergyStorage", energyStorage.orElse(null).serializeNBT());

        return true;
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return energyStorage.cast();
    }
}
