package com.witchica.industrialization.features.inventory;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class GenericItemStackHandler extends ItemStackHandler {
    public static GenericItemStackHandler acceptsAll(int size) {
        return new GenericItemStackHandler(size, (stack) -> true);
    }

    public static GenericItemStackHandler acceptsEnergyItemsOnly(int size) {
        return new GenericItemStackHandler(size, (stack) -> {
            IEnergyStorage storage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
            return storage != null;
        });
    }

    private final Predicate<ItemStack> isItemValid;

    public GenericItemStackHandler(int size, Predicate<ItemStack> isItemValid) {
        super(size);
        this.isItemValid = isItemValid;
    }

    public GenericItemStackHandler(int size) {
        this(size, (stack) -> true);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return isItemValid.test(stack);
    }
}
