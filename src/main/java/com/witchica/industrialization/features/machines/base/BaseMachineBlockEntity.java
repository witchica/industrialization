package com.witchica.industrialization.features.machines.base;

import com.witchica.industrialization.features.energy.IndustrializationEnergyStorage;
import com.witchica.industrialization.features.inventory.GenericItemStackHandler;
import com.witchica.industrialization.features.inventory.IndustrializationItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseMachineBlockEntity extends BlockEntity {

    public IndustrializationEnergyStorage energyStorage;
    public GenericItemStackHandler inputItems;
    public GenericItemStackHandler outputItems;
    public GenericItemStackHandler upgradeItems;

    public int feStorageCapacity;
    public int feConsumedPerTick;
    public int feTransferRate;

    public BaseMachineBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, ModConfigSpec.ConfigValue<Integer> feStorageCapacity, ModConfigSpec.ConfigValue<Integer> feConsumedPerTick, ModConfigSpec.ConfigValue<Integer> feTransferRate) {
        super(pType, pPos, pBlockState);

        this.inputItems = new GenericItemStackHandler(getMachineInputs(), this::isInputValid);
        this.outputItems = new GenericItemStackHandler(getMachineOutputs());
        this.upgradeItems = new GenericItemStackHandler(4);

        this.feStorageCapacity = feStorageCapacity.get();
        this.feConsumedPerTick = feConsumedPerTick.get();
        this.feTransferRate = feTransferRate.get();

        this.energyStorage = new IndustrializationEnergyStorage(this.feStorageCapacity, this.feTransferRate, 0) {
            @Override
            public void onChanged() {
                setChanged();
            }
        };
    }

    public abstract int getMachineInputs();
    public abstract int getMachineOutputs();
    public abstract boolean isInputValid(ItemStack stack);
    public abstract List<Direction> getValidCapabilitySides();

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        if(inputItems != null) {
            pTag.put("InputItems", inputItems.serializeNBT());
        }

        if(outputItems != null) {
            pTag.put("OutputItems", outputItems.serializeNBT());
        }

        if(upgradeItems != null) {
            pTag.put("UpgradeItems", upgradeItems.serializeNBT());
        }

        if(energyStorage != null) {
            pTag.put("EnergyStorage", energyStorage.serializeNBT());
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if(inputItems != null && pTag.contains("InputItems")) {
            inputItems.deserializeNBT(pTag.getCompound("InputItems"));
        }

        if(outputItems != null && pTag.contains("OutputItems")) {
            outputItems.deserializeNBT(pTag.getCompound("OutputItems"));
        }

        if(upgradeItems != null && pTag.contains("UpgradeItems")) {
            upgradeItems.deserializeNBT(pTag.getCompound("UpgradeItems"));
        }

        if(energyStorage != null && pTag.contains("EnergyStorage")) {
            energyStorage.deserializeNBT(pTag.getCompound("EnergyStorage"));
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
    }
}
