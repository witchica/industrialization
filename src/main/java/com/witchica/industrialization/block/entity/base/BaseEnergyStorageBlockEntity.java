package com.witchica.industrialization.block.entity.base;

import com.witchica.industrialization.block.BaseEnergyGeneratorBlock;
import com.witchica.industrialization.block.base.BaseEnergyStorageBlock;
import com.witchica.industrialization.client.screen.EnergyGeneratorIcon;
import com.witchica.industrialization.energy.IndustrializationEnergyEnergyStorage;
import com.witchica.industrialization.inventory.IndustrializationItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;

import java.util.List;

public class BaseEnergyStorageBlockEntity extends BlockEntity {
    public IndustrializationEnergyEnergyStorage energyStorage;
    public IndustrializationItemStackHandler itemStorage;
    protected int feInputOutputPerTick;
    private int tickCount;
    protected BaseEnergyStorageBlock storageBlock;

    public BaseEnergyStorageBlockEntity(BlockEntityType<?> type, BlockPos pPos, BlockState pBlockState) {
        super(type, pPos, pBlockState);

        this.storageBlock = (BaseEnergyStorageBlock) pBlockState.getBlock();

        int maxCapcity = (int) storageBlock.getFeMaxStorage().get();
        int feInputOutputPerTick = (int) storageBlock.getFeInputOutput().get();

        energyStorage = new IndustrializationEnergyEnergyStorage(maxCapcity, feInputOutputPerTick, feInputOutputPerTick) {
            @Override
            public void onChanged() {
                setChanged();
            }
        };

        itemStorage = new IndustrializationItemStackHandler(2, this);

        this.feInputOutputPerTick = feInputOutputPerTick;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        if(energyStorage != null) {
            pTag.put("EnergyStorage", energyStorage.serializeNBT());
        }

        if(itemStorage != null) {
            pTag.put("ItemStorage", itemStorage.serializeNBT());
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if(energyStorage != null && pTag.contains("EnergyStorage")) {
            energyStorage.deserializeNBT(pTag.get("EnergyStorage"));
        }

        if(itemStorage != null && pTag.contains("ItemStorage")) {
            itemStorage.deserializeNBT(pTag.getCompound("ItemStorage"));
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
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
    }

    public static <T extends BlockEntity> void tickHelper(Level level, BlockPos pos, BlockState state, T energyStorageBlock) {
        ((BaseEnergyStorageBlockEntity) energyStorageBlock).tick();
    }

    public void tick() {
        if(level != null && !level.isClientSide()) {
            if(itemStorage != null) {
                if(!itemStorage.getStackInSlot(0).isEmpty()) {
                    ItemStack stack = itemStorage.getStackInSlot(0);
                    IEnergyStorage stackStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);

                    int extracted = energyStorage.extractEnergy(baseFePerTick, true);
                    int ableToTake = Math.min(extracted, stackStorage.getMaxEnergyStored() - stackStorage.getEnergyStored());
                    stackStorage.receiveEnergy((getCurrentEnergyLevel() == getMaximumEnergyLevel() && getCurrentFEPerTick() > 0) ? ableToTake : energyStorage.extractEnergy(ableToTake, false), false);
                }
            }
        }

        tickCount++;
    }

    public int getCurrentFEPerTick() {
        return currentFePerTick;
    }

    public abstract int updateCurrentFEPerTick();

    public float getEnergyLevel() {
        IndustrializationEnergyEnergyStorage energy = energyStorage;
        if(energy.getEnergyStored() == 0) {
            return 0;
        }

        return ((float)energy.getEnergyStored() / (float) energy.getMaxEnergyStored());
    }

    public int getCurrentEnergyLevel() {
        IndustrializationEnergyEnergyStorage energy = energyStorage;
        return energy.getEnergyStored();
    }

    public int getMaximumEnergyLevel() {
        IndustrializationEnergyEnergyStorage energy = energyStorage;
        return energy.getMaxEnergyStored();
    }

    @OnlyIn(Dist.CLIENT)
    public EnergyGeneratorIcon getCurrentIcon() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean hasCurrentIcon() {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public Component getCurrentStatusText() {
        return Component.literal("");
    }