package com.witchica.industrialization.block.entity;

import com.witchica.industrialization.block.entity.base.BaseEnergyGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.stream.Stream;

public class WindTurbineBlockEntity extends BaseEnergyGeneratorBlockEntity {
    private static final List<Direction> VALID_CAPABILITY_SIDES = List.of(Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

    private AABB clearArea;

    public WindTurbineBlockEntity(BlockEntityType<?> type, BlockPos pPos, BlockState pBlockState) {
        super(type, pPos, pBlockState);

        Direction direction = pBlockState.getValue(HorizontalDirectionalBlock.FACING);
        clearArea = new AABB(pPos.relative(direction).getCenter(), pPos.relative(direction, 32).getCenter());
    }

    @Override
    public List<Direction> getValidCapabilitySides() {
        return VALID_CAPABILITY_SIDES;
    }

    private boolean isAreaInFrontClear() {
        if(level == null) {
            return false;
        }

        Stream<BlockState> blocks = level.getBlockStates(clearArea);
        return blocks.allMatch((block) -> block.getBlock() == Blocks.AIR);
    }

    @Override
    public int updateCurrentFEPerTick() {
        return isAreaInFrontClear() ? baseFePerTick : 0;
    }
}
