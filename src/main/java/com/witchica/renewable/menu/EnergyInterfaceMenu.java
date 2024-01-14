package com.witchica.renewable.menu;

import com.witchica.renewable.RenewableEnergy;
import com.witchica.renewable.block.entity.base.BaseEnergyGeneratorBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class EnergyInterfaceMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess levelAccess;
    private final Block blockType;
    private final IItemHandler inventory;

    public final BaseEnergyGeneratorBlockEntity energyBlock;

    public EnergyInterfaceMenu(int pContainerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(pContainerId, playerInventory, (BaseEnergyGeneratorBlockEntity) playerInventory.player.level().getBlockEntity(buf.readBlockPos()), ContainerLevelAccess.NULL, null);
    }
    
    public EnergyInterfaceMenu(int pContainerId, Inventory playerInventory, BaseEnergyGeneratorBlockEntity solarPanelBlockEntity, ContainerLevelAccess levelAccess, Block blockType) {
        super(RenewableEnergy.ENERGY_INTERFACE_MENU_TYPE.get(), pContainerId);
        this.inventory = solarPanelBlockEntity.itemStorage;
        this.levelAccess = levelAccess;
        this.blockType = blockType;
        this.energyBlock = solarPanelBlockEntity;

        this.addSlot(new SlotItemHandler(inventory, 0, 152, 64));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, i * 9 + j + 9, 8 + (j * 18), 84 + i * 18));
            }
        }


        for (int j = 0; j < 9; j++) {
            this.addSlot(new Slot(playerInventory, j, 8 + (j * 18), 142));
        }
        }

        @Override
        public ItemStack quickMoveStack(Player player, int quickMovedSlotIndex) {
            ItemStack newStack = ItemStack.EMPTY;
            final Slot slot = this.slots.get(quickMovedSlotIndex);
            if (slot != null && slot.hasItem()) {

                final ItemStack originalStack = slot.getItem();
                newStack = originalStack.copy();

                if (quickMovedSlotIndex < this.inventory.getSlots()) {
                    if (!this.moveItemStackTo(originalStack, this.inventory.getSlots(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getSlots(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return newStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return AbstractContainerMenu.stillValid(levelAccess, pPlayer, blockType);
    }
}
