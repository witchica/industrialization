package com.witchica.industrialization.features.generators;

import com.witchica.industrialization.Industrialization;
import com.witchica.industrialization.features.generators.base.BaseEnergyGeneratorBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
    public BlockEntityType<?> getBlockEntityType() {
        return Industrialization.HYDRO_POWER_TURBINE_BLOCK_ENTITY_TYPE.get();
    }
}
