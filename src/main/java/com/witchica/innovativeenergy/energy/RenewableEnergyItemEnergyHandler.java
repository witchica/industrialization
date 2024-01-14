package com.witchica.innovativeenergy.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

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
