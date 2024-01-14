package com.witchica.renewable;

import com.mojang.logging.LogUtils;
import com.witchica.renewable.block.BaseEnergyGeneratorBlock;
import com.witchica.renewable.block.HydroPowerTurbineBlock;
import com.witchica.renewable.block.entity.HydroPowerTurbineBlockEntity;
import com.witchica.renewable.block.entity.SolarPanelBlockEntity;
import com.witchica.renewable.block.entity.base.BaseEnergyGeneratorBlockEntity;
import com.witchica.renewable.client.screen.EnergyInterfaceScreen;
import com.witchica.renewable.energy.RenewableEnergyItemEnergyHandler;
import com.witchica.renewable.inventory.RenewableEnergyItemStackHandler;
import com.witchica.renewable.item.EnergyStorageItem;
import com.witchica.renewable.menu.EnergyInterfaceMenu;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(RenewableEnergy.MODID)
public class RenewableEnergy
{
    public static final String MODID = "renewable_energy";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.Blocks.createBlocks(MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.Items.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, MODID);

    public static DeferredBlock<Block> SOLAR_PANEL_MK_I = BLOCKS.register("solar_panel_mk_i", () ->
            new BaseEnergyGeneratorBlock(BlockBehaviour.Properties.of().dynamicShape().requiresCorrectToolForDrops().mapColor(DyeColor.BLUE).strength(3f, 3f).noOcclusion().sound(SoundType.METAL), Config.GENERATOR_MK_I_FE, Config.GENERATOR_MK_I_FE_STORAGE, Config.MK_I_FE_TRANSFER_RATE, SolarPanelBlockEntity::new));
    public static DeferredBlock<Block> SOLAR_PANEL_MK_II = BLOCKS.register("solar_panel_mk_ii", () ->
            new BaseEnergyGeneratorBlock(BlockBehaviour.Properties.of().dynamicShape().requiresCorrectToolForDrops().mapColor(DyeColor.BLUE).strength(3f, 3f).noOcclusion().sound(SoundType.METAL), Config.GENERATOR_MK_II_FE, Config.GENERATOR_MK_II_FE_STORAGE, Config.MK_II_FE_TRANSFER_RATE, SolarPanelBlockEntity::new));
    public static DeferredBlock<Block> SOLAR_PANEL_MK_III = BLOCKS.register("solar_panel_mk_iii", () ->
            new BaseEnergyGeneratorBlock(BlockBehaviour.Properties.of().dynamicShape().requiresCorrectToolForDrops().mapColor(DyeColor.BLUE).strength(3f, 3f).noOcclusion().sound(SoundType.METAL), Config.GENERATOR_MK_III_FE, Config.GENERATOR_MK_III_FE_STORAGE, Config.MK_III_FE_TRANSFER_RATE, SolarPanelBlockEntity::new));
    public static DeferredBlock<Block> HYDRO_POWER_TURBINE_MK_I = BLOCKS.register("hydro_power_turbine_mk_i", () ->
            new HydroPowerTurbineBlock(BlockBehaviour.Properties.of().dynamicShape().requiresCorrectToolForDrops().mapColor(DyeColor.BLUE).strength(3f, 3f).noOcclusion().sound(SoundType.METAL), Config.GENERATOR_MK_I_FE, Config.GENERATOR_MK_I_FE_STORAGE, Config.MK_I_FE_TRANSFER_RATE, Config.HYDRO_POWER_TURBINE_WATER_MK_I_MULTIPLIER, HydroPowerTurbineBlockEntity::new));
    public static DeferredBlock<Block> HYDRO_POWER_TURBINE_MK_II = BLOCKS.register("hydro_power_turbine_mk_ii", () ->
            new HydroPowerTurbineBlock(BlockBehaviour.Properties.of().dynamicShape().requiresCorrectToolForDrops().mapColor(DyeColor.BLUE).strength(3f, 3f).noOcclusion().sound(SoundType.METAL), Config.GENERATOR_MK_II_FE, Config.GENERATOR_MK_II_FE_STORAGE, Config.MK_II_FE_TRANSFER_RATE, Config.HYDRO_POWER_TURBINE_WATER_MK_II_MULTIPLIER,  HydroPowerTurbineBlockEntity::new));
    public static DeferredBlock<Block> HYDRO_POWER_TURBINE_MK_III = BLOCKS.register("hydro_power_turbine_mk_iii", () ->
            new HydroPowerTurbineBlock(BlockBehaviour.Properties.of().dynamicShape().requiresCorrectToolForDrops().mapColor(DyeColor.BLUE).strength(3f, 3f).noOcclusion().sound(SoundType.METAL), Config.GENERATOR_MK_III_FE, Config.GENERATOR_MK_III_FE_STORAGE, Config.MK_III_FE_TRANSFER_RATE,  Config.HYDRO_POWER_TURBINE_WATER_MK_III_MULTIPLIER, HydroPowerTurbineBlockEntity::new));
    public static DeferredBlock<Block> MACHINE_CASING = BLOCKS.register("machine_casing", () -> new Block(BlockBehaviour.Properties.of().dynamicShape().requiresCorrectToolForDrops().mapColor(DyeColor.LIGHT_GRAY).strength(3f, 3f).noOcclusion().sound(SoundType.METAL)));
    public static DeferredBlock<Block> SILICON_INFUSED_GLASS = BLOCKS.register("silicon_infused_glass", () -> new IronBarsBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().mapColor(DyeColor.WHITE).strength(1f, 1f).noOcclusion().sound(SoundType.GLASS)));

    public static DeferredItem<Item> SOLAR_PANEL_MK_I_ITEM = ITEMS.register("solar_panel_mk_i", () -> new BlockItem(SOLAR_PANEL_MK_I.get(), new Item.Properties()));
    public static DeferredItem<Item> SOLAR_PANEL_MK_II_ITEM = ITEMS.register("solar_panel_mk_ii", () -> new BlockItem(SOLAR_PANEL_MK_II.get(), new Item.Properties()));
    public static DeferredItem<Item> SOLAR_PANEL_MK_III_ITEM = ITEMS.register("solar_panel_mk_iii", () -> new BlockItem(SOLAR_PANEL_MK_III.get(), new Item.Properties()));
    public static DeferredItem<Item> HYDRO_POWER_TURBINE_MK_I_ITEM = ITEMS.register("hydro_power_turbine_mk_i", () -> new BlockItem(HYDRO_POWER_TURBINE_MK_I.get(), new Item.Properties()));
    public static DeferredItem<Item> HYDRO_POWER_TURBINE_MK_II_ITEM = ITEMS.register("hydro_power_turbine_mk_ii", () -> new BlockItem(HYDRO_POWER_TURBINE_MK_II.get(), new Item.Properties()));
    public static DeferredItem<Item> HYDRO_POWER_TURBINE_MK_III_ITEM = ITEMS.register("hydro_power_turbine_mk_iii", () -> new BlockItem(HYDRO_POWER_TURBINE_MK_III.get(), new Item.Properties()));
    public static DeferredItem<Item> MACHINE_CASING_ITEM = ITEMS.register("machine_casing", () -> new BlockItem(MACHINE_CASING.get(), new Item.Properties()));
    public static DeferredItem<Item> SILICON_INFUSED_GLASS_ITEM = ITEMS.register("silicon_infused_glass", () -> new BlockItem(SILICON_INFUSED_GLASS.get(), new Item.Properties()));

    public static DeferredHolder<MenuType<?>, MenuType<EnergyInterfaceMenu>> ENERGY_INTERFACE_MENU_TYPE = MENU_TYPES.register("energy_interface", () -> IMenuTypeExtension.create(EnergyInterfaceMenu::new));
    public static DeferredItem<Item> SILICON = ITEMS.register("silicon", () -> new Item(new Item.Properties()));
    public static DeferredItem<Item> CARBON = ITEMS.register("carbon", () -> new Item(new Item.Properties()));
    public static DeferredItem<Item> CARBON_SHEET = ITEMS.register("carbon_sheet", () ->new Item(new Item.Properties()));
    public static DeferredItem<Item> DENSE_CARBON_SHEET = ITEMS.register("dense_carbon_sheet", () -> new Item(new Item.Properties()));
    public static DeferredItem<Item> BATTERY_MK_I = ITEMS.register("battery_mk_i", () -> new EnergyStorageItem(new Item.Properties().stacksTo(1).setNoRepair(), Config.BATTERY_MK_I_FE_STORAGE, Config.MK_I_FE_TRANSFER_RATE));
    public static DeferredItem<Item> BATTERY_MK_II = ITEMS.register("battery_mk_ii", () -> new EnergyStorageItem(new Item.Properties().stacksTo(1).setNoRepair(), Config.BATTERY_MK_II_FE_STORAGE, Config.MK_II_FE_TRANSFER_RATE));
    public static DeferredItem<Item> BATTERY_MK_III = ITEMS.register("battery_mk_iii", () -> new EnergyStorageItem(new Item.Properties().stacksTo(1).setNoRepair(), Config.BATTERY_MK_III_FE_STORAGE, Config.MK_III_FE_TRANSFER_RATE));


    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<SolarPanelBlockEntity>> SOLAR_PANEL_BLOCK_ENTITY_TYPE =
            BLOCK_ENTITY_TYPES.register("solar_panel", () -> BlockEntityType.Builder.of(SolarPanelBlockEntity::new, SOLAR_PANEL_MK_I.get(), SOLAR_PANEL_MK_II.get(), SOLAR_PANEL_MK_III.get()).build(null));

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<HydroPowerTurbineBlockEntity>> HYDRO_POWER_TURBINE_BLOCK_ENTITY_TYPE =
            BLOCK_ENTITY_TYPES.register("hydro_power_turbine", () -> BlockEntityType.Builder.of(HydroPowerTurbineBlockEntity::new, HYDRO_POWER_TURBINE_MK_I.get(), HYDRO_POWER_TURBINE_MK_II.get(), HYDRO_POWER_TURBINE_MK_III.get()).build(null));
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RENEWABLE_ENERGY_GENERAL_TAB = CREATIVE_MODE_TABS.register("renewable_energy", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .title(Component.translatable("creativeModeTab.renewable_energy.general"))
            .icon(() -> new ItemStack(SOLAR_PANEL_MK_I_ITEM.get()))
            .displayItems((parameters, output) -> {
                output.accept(SOLAR_PANEL_MK_I.get());
                output.accept(SOLAR_PANEL_MK_II.get());
                output.accept(SOLAR_PANEL_MK_III.get());
                output.accept(HYDRO_POWER_TURBINE_MK_I.get());
                output.accept(HYDRO_POWER_TURBINE_MK_II.get());
                output.accept(HYDRO_POWER_TURBINE_MK_III.get());
                output.accept(MACHINE_CASING.get());
                output.accept(SILICON.get());
                output.accept(SILICON_INFUSED_GLASS.get());
                output.accept(CARBON.get());
                output.accept(CARBON_SHEET.get());
                output.accept(DENSE_CARBON_SHEET.get());
                output.accept(BATTERY_MK_I.get());
                output.accept(BATTERY_MK_II.get());
                output.accept(BATTERY_MK_III.get());
            }).build());

    public RenewableEnergy(IEventBus modEventBus)
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC, "renewable_energy.toml");
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerCapabilities);

        BLOCKS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        MENU_TYPES.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private void registerCapabilities(final RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.EnergyStorage.ITEM,
            (itemStack, context) -> {
                EnergyStorageItem item = (EnergyStorageItem) itemStack.getItem();
                return new RenewableEnergyItemEnergyHandler(itemStack, itemStack.getTag(), item.storageAmount.get(), item.transferRate.get());
            }, BATTERY_MK_I.get(), BATTERY_MK_II.get(), BATTERY_MK_III.get());

        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, SOLAR_PANEL_BLOCK_ENTITY_TYPE.get(), RenewableEnergy::getEnergyCapabiity);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, HYDRO_POWER_TURBINE_BLOCK_ENTITY_TYPE.get(), RenewableEnergy::getEnergyCapabiity);

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SOLAR_PANEL_BLOCK_ENTITY_TYPE.get(), RenewableEnergy::getItemCapability);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, HYDRO_POWER_TURBINE_BLOCK_ENTITY_TYPE.get(), RenewableEnergy::getItemCapability);
    }

    private static IEnergyStorage getEnergyCapabiity(BaseEnergyGeneratorBlockEntity entity, Direction side) {
        if(entity.getValidCapabilitySides().contains(side)) {
            return entity.energyStorage;
        } else {
            return null;
        }
    }

    private static IItemHandler getItemCapability(BaseEnergyGeneratorBlockEntity entity, Direction side) {
        if(entity.getValidCapabilitySides().contains(side)) {
            return entity.itemStorage;
        } else {
            return null;
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            ItemBlockRenderTypes.setRenderLayer(SILICON_INFUSED_GLASS.get(), RenderType.cutout());

            event.enqueueWork(() -> {
                MenuScreens.register(ENERGY_INTERFACE_MENU_TYPE.get(), EnergyInterfaceScreen::new);
            });
        }
    }
}
