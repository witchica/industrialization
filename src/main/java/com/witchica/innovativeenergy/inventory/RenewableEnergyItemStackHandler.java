package com.witchica.innovativeenergy.inventory;

import com.witchica.innovativeenergy.block.entity.base.BaseEnergyGeneratorBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class RenewableEnergyItemStackHandler extends ItemStackHandler {

    private final BaseEnergyGeneratorBlockEntity parent;

    public RenewableEnergyItemStackHandler(int size, BaseEnergyGeneratorBlockEntity parent) {
        super(size);
        this.parent = parent;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        IEnergyStorage storage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        return super.isItemValid(slot, stack) && storage != null;
    }

    public BaseEnergyGeneratorBlockEntity getParent() {
        return parent;
    }
}
