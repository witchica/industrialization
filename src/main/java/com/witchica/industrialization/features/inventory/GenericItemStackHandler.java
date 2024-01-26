package com.witchica.industrialization.features.inventory;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class GenericItemStackHandler extends ItemStackHandler {
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
