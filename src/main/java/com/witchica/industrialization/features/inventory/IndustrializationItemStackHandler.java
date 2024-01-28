package com.witchica.industrialization.features.inventory;

import com.witchica.industrialization.features.generators.base.BaseEnergyGeneratorBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

@Deprecated
public class IndustrializationItemStackHandler extends ItemStackHandler {

    private final BaseEnergyGeneratorBlockEntity parent;

    public IndustrializationItemStackHandler(int size, BaseEnergyGeneratorBlockEntity parent) {
        super(size);
        this.parent = parent;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        IEnergyStorage storage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        return super.isItemValid(slot, stack) && storage != null;
    }

    public BaseEnergyGeneratorBlockEntity getParenst() {
        return parent;
    }
}
