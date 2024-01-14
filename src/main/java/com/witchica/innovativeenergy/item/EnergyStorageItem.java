package com.witchica.innovativeenergy.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnergyStorageItem extends Item {
    public final ModConfigSpec.ConfigValue<Integer> storageAmount;
    public final ModConfigSpec.ConfigValue<Integer> transferRate;

    public EnergyStorageItem(Properties pProperties, ModConfigSpec.ConfigValue<Integer> feStorage, ModConfigSpec.ConfigValue<Integer> feTransferRate) {
        super(pProperties);
        this.storageAmount = feStorage;
        this.transferRate = feTransferRate;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return storageAmount.get();
    }

    @Override
    public int getDamage(ItemStack stack) {
        return storageAmount.get() - getCurrentlyStoredEnergy(stack);
    }

    public int getCurrentlyStoredEnergy(ItemStack stack) {
        if(stack.hasTag() && stack.getTag().contains("EnergyStorage")) {
            return stack.getTag().getInt("EnergyStorage");
        }
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        pTooltipComponents.add(Component.translatable("tooltip.renewable_energy.fe_amount", getCurrentlyStoredEnergy(pStack), storageAmount.get()).withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.ITALIC));
    }
}
