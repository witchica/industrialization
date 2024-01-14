package com.witchica.innovativeenergy.block;

import com.witchica.innovativeenergy.block.entity.base.BaseEnergyGeneratorBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.common.ModConfigSpec;

public class WindTurbineBlock extends BaseEnergyGeneratorBlock {
    public WindTurbineBlock(Properties pProperties, ModConfigSpec.ConfigValue<Integer> fePerTickConfig, ModConfigSpec.ConfigValue<Integer> feStorageConfig, ModConfigSpec.ConfigValue<Integer> feExtractConfig, BlockEntityType.BlockEntitySupplier<BaseEnergyGeneratorBlockEntity> blockEntitySupplier) {
        super(pProperties, fePerTickConfig, feStorageConfig, feExtractConfig, blockEntitySupplier);
    }
}
