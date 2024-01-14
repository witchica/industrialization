package com.witchica.renewable.energy;

import com.witchica.renewable.RenewableEnergy;
import com.witchica.renewable.inventory.RenewableEnergyItemStackHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RenewableEnergyItemEnergyHandler extends RenewableEnergyEnergyStorage {
    private final ItemStack stack;
    public RenewableEnergyItemEnergyHandler(ItemStack stack, CompoundTag tag, int maxAmount, int maxTransfer) {
        super(maxAmount, maxTransfer, maxTransfer);
        this.stack = stack;

        if (stack.hasTag() && stack.getTag().contains("EnergyStorage")) {
            deserializeNBT(stack.getTag().get("EnergyStorage"));
        }
    }

    @Override
    public void onChanged() {
        if(!stack.hasTag()) {
            stack.setTag(new CompoundTag());
        }

        stack.getTag().put("EnergyStorage", serializeNBT());
    }
}
