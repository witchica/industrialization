package com.witchica.industrialization.features.transport;

import com.witchica.industrialization.features.transport.base.BasePipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;

public class EnergyPipeBlock extends BasePipeBlock {
    public EnergyPipeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canPipeConnectTo(BlockState otherBlockState, Level level, BlockPos pipePos, Direction connectingFrom) {
        return otherBlockState.getBlock() instanceof EnergyPipeBlock
            || level.getCapability(Capabilities.EnergyStorage.BLOCK, pipePos.relative(connectingFrom), connectingFrom.getOpposite()) != null;
    }
}
