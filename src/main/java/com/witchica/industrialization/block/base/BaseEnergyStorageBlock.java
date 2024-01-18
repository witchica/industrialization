package com.witchica.industrialization.block.base;

import com.witchica.industrialization.Industrialization;
import com.witchica.industrialization.block.entity.base.BaseEnergyGeneratorBlockEntity;
import com.witchica.industrialization.block.entity.base.BaseEnergyStorageBlockEntity;
import com.witchica.industrialization.menu.EnergyInterfaceMenu;
import com.witchica.industrialization.menu.EnergyStorageMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.energy.EnergyStorage;
import org.jetbrains.annotations.Nullable;

public class BaseEnergyStorageBlock extends Block implements EntityBlock {
    private final ModConfigSpec.ConfigValue<Integer> feInputOutput;
    private final ModConfigSpec.ConfigValue<Integer> feMaxStorage;

    public BaseEnergyStorageBlock(Properties pProperties, ModConfigSpec.ConfigValue<Integer> feInputOutput, ModConfigSpec.ConfigValue<Integer> feMaxStorage) {
        super(pProperties);
        this.feInputOutput = feInputOutput;
        this.feMaxStorage = feMaxStorage;
    }

    public BlockEntityType<?> getBlockEntityType() {
        return Industrialization.ENERGY_STORAGE_BLOCK_ENTITY_TYPE.get();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BaseEnergyStorageBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pBlockEntityType == getBlockEntityType() ? BaseEnergyGeneratorBlockEntity::tickHelper : null;
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new SimpleMenuProvider((pContainerId, pPlayerInventory, pPlayer) -> {
            BaseEnergyStorageBlockEntity storageBlock = (BaseEnergyStorageBlockEntity) pLevel.getBlockEntity(pPos);

            return new EnergyStorageMenu(pContainerId, pPlayerInventory, storageBlock, ContainerLevelAccess.create(pLevel, pPos), this);
        }, getName());
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide && pPlayer instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(pState.getMenuProvider(pLevel, pPos), friendlyByteBuf -> {
                friendlyByteBuf.writeBlockPos(pPos);
            });
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    public ModConfigSpec.ConfigValue<Integer> getFeInputOutput() {
        return feInputOutput;
    }
    public ModConfigSpec.ConfigValue<Integer> getFeMaxStorage() {
        return feMaxStorage;
    }
}
