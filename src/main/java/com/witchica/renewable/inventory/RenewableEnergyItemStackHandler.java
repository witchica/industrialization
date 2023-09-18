package com.witchica.renewable.inventory;

import com.witchica.renewable.block.entity.base.BaseEnergyGeneratorBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class RenewableEnergyItemStackHandler extends ItemStackHandler {

    private final BaseEnergyGeneratorBlockEntity parent;

    public RenewableEnergyItemStackHandler(int size, BaseEnergyGeneratorBlockEntity parent) {
        super(size);
        this.parent = parent;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return super.isItemValid(slot, stack) && stack.getCapability(ForgeCapabilities.ENERGY).isPresent();
    }

    public BaseEnergyGeneratorBlockEntity getParent() {
        return parent;
    }
}
