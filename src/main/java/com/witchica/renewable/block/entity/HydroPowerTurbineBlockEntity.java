package com.witchica.renewable.block.entity;

import com.witchica.renewable.RenewableEnergy;
import com.witchica.renewable.block.HydroPowerTurbineBlock;
import com.witchica.renewable.block.entity.base.BaseEnergyGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.joml.Math;

import java.util.List;

public class HydroPowerTurbineBlockEntity extends BaseEnergyGeneratorBlockEntity {
    private static final List<Direction> VALID_CAPABILITY_SIDES = List.of(Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);
    private final float waterSurroundMultiplier;
    private int currentlySurroundedWaterBlocks;

    public HydroPowerTurbineBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RenewableEnergy.HYDRO_POWER_TURBINE_BLOCK_ENTITY_TYPE.get(), pPos, pBlockState);

        HydroPowerTurbineBlock hydroPowerTurbineBlock = (HydroPowerTurbineBlock) this.generatorBlock;
        this.waterSurroundMultiplier = hydroPowerTurbineBlock.getWaterSurroundMultiplierConfig().get();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        updateCurrentWaterSurroundState();
    }

    public void updateCurrentWaterSurroundState() {
        this.currentlySurroundedWaterBlocks = getCurrentlySurroundingWaterBlocks();
    }

    public int getCurrentlySurroundingWaterBlocks() {
        if(level == null) {
            return 0;
        }

        BlockPos min = getBlockPos().subtract(new Vec3i(1,1,1));
        int surrounded = 0;

        for(int x = 0; x < 3; x++) {
            for(int y = 0; y < 3; y++) {
                for(int z = 0; z < 3; z++) {
                    BlockPos check = min.offset(x,y,z);
                    FluidState fluidState = level.getFluidState(check);

                    if(!fluidState.isEmpty() && (fluidState.is(Fluids.FLOWING_WATER) || fluidState.is(Fluids.WATER))) {
                        surrounded += 1;
                    }
                }
            }
        }

        return surrounded;
    }

    @Override
    public List<Direction> getValidCapabilitySides() {
        return VALID_CAPABILITY_SIDES;
    }

    @Override
    public int updateCurrentFEPerTick() {
        return Math.round(getCurrentlySurroundingWaterBlocks() * waterSurroundMultiplier);
    }
}
