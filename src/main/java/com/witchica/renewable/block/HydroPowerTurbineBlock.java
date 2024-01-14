package com.witchica.renewable.block;

import com.witchica.renewable.RenewableEnergy;
import com.witchica.renewable.block.entity.HydroPowerTurbineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec;

public class HydroPowerTurbineBlock extends BaseEnergyGeneratorBlock {

    private final ModConfigSpec.ConfigValue hydroPowerWaterMultiplier;

    public HydroPowerTurbineBlock(Properties pProperties, ModConfigSpec.ConfigValue fePerTickConfig, ModConfigSpec.ConfigValue feStorageConfig, ModConfigSpec.ConfigValue feExtractConfig, ModConfigSpec.ConfigValue hydroPowerWaterMultiplier, BlockEntityType.BlockEntitySupplier blockEntitySupplier) {
        super(pProperties, fePerTickConfig, feStorageConfig, feExtractConfig, blockEntitySupplier);
        this.hydroPowerWaterMultiplier = hydroPowerWaterMultiplier;
    }

    public ModConfigSpec.ConfigValue<Float> getWaterSurroundMultiplierConfig() {
        return hydroPowerWaterMultiplier;
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        super.neighborChanged(pState, pLevel, pPos, pNeighborBlock, pNeighborPos, pMovedByPiston);

        BlockEntity entity = pLevel.getBlockEntity(pPos);

        if(entity != null && entity instanceof HydroPowerTurbineBlockEntity hydroPowerTurbineBlockEntity) {
            hydroPowerTurbineBlockEntity.updateCurrentWaterSurroundState();
        }
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return RenewableEnergy.HYDRO_POWER_TURBINE_BLOCK_ENTITY_TYPE.get();
    }
}
