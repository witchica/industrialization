package com.witchica.renewable.block.entity.base;

import com.witchica.renewable.RenewableEnergy;
import com.witchica.renewable.block.BaseEnergyGeneratorBlock;
import com.witchica.renewable.client.screen.EnergyGeneratorIcon;
import com.witchica.renewable.energy.RenewableEnergyEnergyStorage;
import com.witchica.renewable.inventory.RenewableEnergyItemStackHandler;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;

import java.util.List;

public abstract class BaseEnergyGeneratorBlockEntity extends BlockEntity {
    private LazyOptional<RenewableEnergyEnergyStorage> energyStorage;
    public LazyOptional<RenewableEnergyItemStackHandler> itemStorage;
    protected int fePerTick;
    protected BaseEnergyGeneratorBlock generatorBlock;

    public BaseEnergyGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pPos, BlockState pBlockState) {
        super(type, pPos, pBlockState);

        this.generatorBlock = (BaseEnergyGeneratorBlock) pBlockState.getBlock();

        int maxCapcity = (int) generatorBlock.getFeStorageConfigValue().get();
        int maxExtract = (int) generatorBlock.getFeExtractConfigValue().get();
        int fePerTick = (int) generatorBlock.getFePerTickConfigValue().get();

        energyStorage = LazyOptional.of(() -> new RenewableEnergyEnergyStorage(maxCapcity, 0, maxExtract, () -> {
            setChanged();
            return true;
        }));
        itemStorage = LazyOptional.of(() -> new RenewableEnergyItemStackHandler(1, this));

        this.fePerTick = fePerTick;
    }

    public abstract List<Direction> getValidCapabilitySides();

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        if(energyStorage.isPresent()) {
            RenewableEnergyEnergyStorage energyStorage = this.energyStorage.orElse(null);
            pTag.put("EnergyStorage", energyStorage.serializeNBT());
        }

        if(itemStorage.isPresent()) {
            ItemStackHandler itemStackHandler = itemStorage.orElse(null);
            pTag.put("ItemStorage", itemStackHandler.serializeNBT());
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if(energyStorage.isPresent() && pTag.contains("EnergyStorage")) {
            RenewableEnergyEnergyStorage energyStorage = this.energyStorage.orElse(null);
            energyStorage.deserializeNBT(pTag.get("EnergyStorage"));
        }

        if(itemStorage.isPresent() && pTag.contains("ItemStorage")) {
            ItemStackHandler itemStackHandler = itemStorage.orElse(null);
            itemStackHandler.deserializeNBT(pTag.getCompound("ItemStorage"));
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

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if(cap == ForgeCapabilities.ENERGY) {
            return energyStorage.cast();
        } else if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemStorage.cast();
        }

        return super.getCapability(cap);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(side == null || getValidCapabilitySides().contains(side)) {
            if(cap == ForgeCapabilities.ENERGY) {
                return energyStorage.cast();
            }
            else if(cap == ForgeCapabilities.ITEM_HANDLER) {
                return itemStorage.cast();
            }
        }

        return super.getCapability(cap, side);
    }

    public static <T extends BlockEntity> void tickHelper(Level level, BlockPos pos, BlockState state, T energyGeneratorBlock) {
        ((BaseEnergyGeneratorBlockEntity) energyGeneratorBlock).tick();
    }

    public void tick() {
        if(level != null && !level.isClientSide()) {
            if(getCurrentEnergyLevel() < getMaximumEnergyLevel()) {
                energyStorage.orElse(null).addEnergy(getCurrentFEPerTick(), false);
            }

            if(itemStorage.isPresent()) {
                RenewableEnergyItemStackHandler stackHandler = itemStorage.orElse(null);
                EnergyStorage storage = energyStorage.orElse(null);

                if(!stackHandler.getStackInSlot(0).isEmpty()) {
                    ItemStack stack = stackHandler.getStackInSlot(0);
                    IEnergyStorage stackStorage = stack.getCapability(ForgeCapabilities.ENERGY).orElse(null);

                    int extracted = storage.extractEnergy(fePerTick, true);
                    int ableToTake = Math.min(extracted, stackStorage.getMaxEnergyStored() - stackStorage.getEnergyStored());
                    stackStorage.receiveEnergy((getCurrentEnergyLevel() == getMaximumEnergyLevel() && getCurrentFEPerTick() > 0) ? ableToTake : storage.extractEnergy(ableToTake, false), false);
                }
            }
        }
    }

    public abstract int getCurrentFEPerTick();

    public float getEnergyLevel() {
        RenewableEnergyEnergyStorage energy = energyStorage.orElse(null);
        if(energy.getEnergyStored() == 0) {
            return 0;
        }

        return ((float)energy.getEnergyStored() / (float) energy.getMaxEnergyStored());
    }

    public int getCurrentEnergyLevel() {
        RenewableEnergyEnergyStorage energy = energyStorage.orElse(null);
        return energy.getEnergyStored();
    }

    public int getMaximumEnergyLevel() {
        RenewableEnergyEnergyStorage energy = energyStorage.orElse(null);
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
}
