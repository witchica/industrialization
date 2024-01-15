package com.witchica.industrialization.block;

import com.witchica.industrialization.Industrialization;
import com.witchica.industrialization.block.entity.base.BaseEnergyGeneratorBlockEntity;
import com.witchica.industrialization.menu.EnergyInterfaceMenu;
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
import org.jetbrains.annotations.Nullable;

public class BaseEnergyGeneratorBlock extends Block implements EntityBlock {
    private final ModConfigSpec.ConfigValue<Integer> fePerTickConfig;
    private final ModConfigSpec.ConfigValue<Integer> feStorageConfig;
    private final ModConfigSpec.ConfigValue<Integer> feExtractConfig;
    private final BlockEntityType.BlockEntitySupplier<BaseEnergyGeneratorBlockEntity> blockEntitySupplier;

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public BaseEnergyGeneratorBlock(Properties pProperties, ModConfigSpec.ConfigValue<Integer> fePerTickConfig, ModConfigSpec.ConfigValue<Integer> feStorageConfig, ModConfigSpec.ConfigValue<Integer> feExtractConfig, BlockEntityType.BlockEntitySupplier<BaseEnergyGeneratorBlockEntity> blockEntitySupplier) {
        super(pProperties);
        this.fePerTickConfig = fePerTickConfig;
        this.feStorageConfig = feStorageConfig;
        this.feExtractConfig = feExtractConfig;
        this.blockEntitySupplier = blockEntitySupplier;
    }

    public BlockEntityType<?> getBlockEntityType() {
        return Industrialization.SOLAR_PANEL_BLOCK_ENTITY_TYPE.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext).setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return blockEntitySupplier.create(pPos, pState);
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
            BaseEnergyGeneratorBlockEntity solarPanelBlockEntity = (BaseEnergyGeneratorBlockEntity) pLevel.getBlockEntity(pPos);

            return new EnergyInterfaceMenu(pContainerId, pPlayerInventory, (BaseEnergyGeneratorBlockEntity) pLevel.getBlockEntity(pPos), ContainerLevelAccess.create(pLevel, pPos), this);
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

    public ModConfigSpec.ConfigValue<Integer> getFePerTickConfigValue() {
        return fePerTickConfig;
    }
    public ModConfigSpec.ConfigValue<Integer> getFeStorageConfigValue() {
        return feStorageConfig;
    }
    public ModConfigSpec.ConfigValue<Integer> getFeExtractConfigValue() {
        return feStorageConfig;
    }
}
