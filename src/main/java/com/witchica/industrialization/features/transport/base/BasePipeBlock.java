package com.witchica.industrialization.features.transport.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public abstract class BasePipeBlock extends Block implements EntityBlock {
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final  BooleanProperty EAST = BooleanProperty.create("east");
    public static final  BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final  BooleanProperty WEST = BooleanProperty.create("west");
    public static final  BooleanProperty UP = BooleanProperty.create("up");
    public static final  BooleanProperty DOWN = BooleanProperty.create("down");


    public BasePipeBlock(Properties properties) {
        super(properties.dynamicShape().noOcclusion());
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    private BlockState updateConnectedSides(BlockState state, Level level, BlockPos pos) {
        return state.setValue(NORTH, canPipeConnectTo(level.getBlockState(pos.north()), level, pos, Direction.NORTH))
            .setValue(EAST, canPipeConnectTo(level.getBlockState(pos.east()), level, pos, Direction.EAST))
            .setValue(SOUTH, canPipeConnectTo(level.getBlockState(pos.south()), level, pos, Direction.SOUTH))
            .setValue(WEST, canPipeConnectTo(level.getBlockState(pos.west()), level, pos, Direction.WEST))
            .setValue(UP, canPipeConnectTo(level.getBlockState(pos.above()), level, pos, Direction.UP))
            .setValue(DOWN, canPipeConnectTo(level.getBlockState(pos.below()), level, pos, Direction.DOWN));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return updateConnectedSides(super.getStateForPlacement(pContext), pContext.getLevel(), pContext.getClickedPos());
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        super.neighborChanged(pState, pLevel, pPos, pNeighborBlock, pNeighborPos, pMovedByPiston);
        pLevel.setBlock(pPos, updateConnectedSides(pState, pLevel, pPos), 3);

        //TODO: update block entities here
    }

    public abstract boolean canPipeConnectTo(BlockState state, Level level, BlockPos pos, Direction connectingFrom);


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;
    }
}
