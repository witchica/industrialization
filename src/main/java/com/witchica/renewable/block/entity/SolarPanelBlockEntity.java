package com.witchica.renewable.block.entity;

import com.witchica.renewable.RenewableEnergy;
import com.witchica.renewable.block.entity.base.BaseEnergyGeneratorBlockEntity;
import com.witchica.renewable.client.screen.EnergyGeneratorIcon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class SolarPanelBlockEntity extends BaseEnergyGeneratorBlockEntity {
    private static final List<Direction> VALID_CAPABILITY_SIDES = List.of(Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);
    public SolarPanelBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RenewableEnergy.SOLAR_PANEL_BLOCK_ENTITY_TYPE.get(), pPos, pBlockState);
    }

    public boolean isDayTime() {
        float timeOfDay = level.getTimeOfDay(0);
        return (timeOfDay >= 0f && timeOfDay < 0.25f) || timeOfDay >= 0.78f;
    }

    public boolean isRaining() {
        return level.isRaining();
    }

    public boolean canSeeSky() {
        return level.canSeeSky(getBlockPos().above());
    }

    @Override
    public List<Direction> getValidCapabilitySides() {
        return VALID_CAPABILITY_SIDES;
    }

    @Override
    public int getCurrentFEPerTick() {
        if(!canSeeSky() || !isDayTime()) {
            return 0;
        }

        return (isRaining() ? this.fePerTick / 2 : this.fePerTick);
    }

    @Override
    public EnergyGeneratorIcon getCurrentIcon() {
        if(canSeeSky()) {
            if(!isDayTime()) {
                return EnergyGeneratorIcon.MOON;
            } else {
                if(isRaining()) {
                    return EnergyGeneratorIcon.RAIN;
                } else {
                    return EnergyGeneratorIcon.SUN;
                }
            }
        } else {
            return EnergyGeneratorIcon.BLOCKED;
        }
    }

    @Override
    public boolean hasCurrentIcon() {
        return true;
    }

    @Override
    public Component getCurrentStatusText() {
        if(canSeeSky()) {
            if(!isDayTime()) {
                return Component.translatable("text.renewable_energy.solar_night");
            } else {
                if(isRaining()) {
                    return Component.translatable("text.renewable_energy.solar_raining");
                } else {
                    return Component.translatable("text.renewable_energy.solar_no_issues");
                }
            }
        } else {
            return Component.translatable("text.renewable_energy.solar_blocked");
        }
    }
}
