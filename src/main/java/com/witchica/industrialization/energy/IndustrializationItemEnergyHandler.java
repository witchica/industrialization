package com.witchica.industrialization.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class IndustrializationItemEnergyHandler extends IndustrializationEnergyStorage {
    private final ItemStack stack;
    public IndustrializationItemEnergyHandler(ItemStack stack, CompoundTag tag, int maxAmount, int maxTransfer) {
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
