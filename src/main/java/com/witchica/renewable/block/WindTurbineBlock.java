package com.witchica.renewable.block;

import com.witchica.renewable.block.entity.base.BaseEnergyGeneratorBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.ForgeConfigSpec;

public class WindTurbineBlock extends BaseEnergyGeneratorBlock {
    public WindTurbineBlock(Properties pProperties, ForgeConfigSpec.ConfigValue<Integer> fePerTickConfig, ForgeConfigSpec.ConfigValue<Integer> feStorageConfig, ForgeConfigSpec.ConfigValue<Integer> feExtractConfig, BlockEntityType.BlockEntitySupplier<BaseEnergyGeneratorBlockEntity> blockEntitySupplier) {
        super(pProperties, fePerTickConfig, feStorageConfig, feExtractConfig, blockEntitySupplier);
    }
}
