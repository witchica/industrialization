package com.witchica.renewable;

import com.mojang.logging.LogUtils;
import com.witchica.renewable.block.BaseEnergyGeneratorBlock;
import com.witchica.renewable.block.HydroPowerTurbineBlock;
import com.witchica.renewable.block.entity.HydroPowerTurbineBlockEntity;
import com.witchica.renewable.block.entity.SolarPanelBlockEntity;
import com.witchica.renewable.block.entity.base.BaseEnergyGeneratorBlockEntity;
import com.witchica.renewable.client.screen.EnergyInterfaceScreen;
import com.witchica.renewable.item.EnergyStorageItem;
import com.witchica.renewable.menu.EnergyInterfaceMenu;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.ArrayList;

@Mod(RenewableEnergy.MODID)
public class RenewableEnergy
{
    public static final String MODID = "renewable_energy";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, MODID);

    public static RegistryObject<Block> SOLAR_PANEL_MK_I = BLOCKS.register("solar_panel_mk_i", () ->
            new BaseEnergyGeneratorBlock(BlockBehaviour.Properties.of().dynamicShape().requiresCorrectToolForDrops().mapColor(DyeColor.BLUE).strength(3f, 3f).noOcclusion().sound(SoundType.METAL), Config.GENERATOR_MK_I_FE, Config.GENERATOR_MK_I_FE_STORAGE, Config.MK_I_FE_TRANSFER_RATE, SolarPanelBlockEntity::new));
    public static RegistryObject<Block> SOLAR_PANEL_MK_II = BLOCKS.register("solar_panel_mk_ii", () ->
            new BaseEnergyGeneratorBlock(BlockBehaviour.Properties.of().dynamicShape().requiresCorrectToolForDrops().mapColor(DyeColor.BLUE).strength(3f, 3f).noOcclusion().sound(SoundType.METAL), Config.GENERATOR_MK_II_FE, Config.GENERATOR_MK_II_FE_STORAGE, Config.MK_II_FE_TRANSFER_RATE, SolarPanelBlockEntity::new));
    public static RegistryObject<Block> SOLAR_PANEL_MK_III = BLOCKS.register("solar_panel_mk_iii", () ->
            new BaseEnergyGeneratorBlock(BlockBehaviour.Properties.of().dynamicShape().requiresCorrectToolForDrops().mapColor(DyeColor.BLUE).strength(3f, 3f).noOcclusion().sound(SoundType.METAL), Config.GENERATOR_MK_III_FE, Config.GENERATOR_MK_III_FE_STORAGE, Config.MK_III_FE_TRANSFER_RATE, SolarPanelBlockEntity::new));
    public static RegistryObject<Block> HYDRO_POWER_TURBINE_MK_I = BLOCKS.register("hydro_power_turbine_mk_i", () ->
            new HydroPowerTurbineBlock(BlockBehaviour.Properties.of().dynamicShape().requiresCorrectToolForDrops().mapColor(DyeColor.BLUE).strength(3f, 3f).noOcclusion().sound(SoundType.METAL), Config.GENERATOR_MK_I_FE, Config.GENERATOR_MK_I_FE_STORAGE, Config.MK_I_FE_TRANSFER_RATE, Config.HYDRO_POWER_TURBINE_WATER_MK_I_MULTIPLIER, HydroPowerTurbineBlockEntity::new));
    public static RegistryObject<Block> HYDRO_POWER_TURBINE_MK_II = BLOCKS.register("hydro_power_turbine_mk_ii", () ->
            new HydroPowerTurbineBlock(BlockBehaviour.Properties.of().dynamicShape().requiresCorrectToolForDrops().mapColor(DyeColor.BLUE).strength(3f, 3f).noOcclusion().sound(SoundType.METAL), Config.GENERATOR_MK_II_FE, Config.GENERATOR_MK_II_FE_STORAGE, Config.MK_II_FE_TRANSFER_RATE, Config.HYDRO_POWER_TURBINE_WATER_MK_II_MULTIPLIER,  HydroPowerTurbineBlockEntity::new));
    public static RegistryObject<Block> HYDRO_POWER_TURBINE_MK_III = BLOCKS.register("hydro_power_turbine_mk_iii", () ->
            new HydroPowerTurbineBlock(BlockBehaviour.Properties.of().dynamicShape().requiresCorrectToolForDrops().mapColor(DyeColor.BLUE).strength(3f, 3f).noOcclusion().sound(SoundType.METAL), Config.GENERATOR_MK_III_FE, Config.GENERATOR_MK_III_FE_STORAGE, Config.MK_III_FE_TRANSFER_RATE,  Config.HYDRO_POWER_TURBINE_WATER_MK_III_MULTIPLIER, HydroPowerTurbineBlockEntity::new));
    public static RegistryObject<Block> MACHINE_CASING = BLOCKS.register("machine_casing", () -> new Block(BlockBehaviour.Properties.of().dynamicShape().requiresCorrectToolForDrops().mapColor(DyeColor.LIGHT_GRAY).strength(3f, 3f).noOcclusion().sound(SoundType.METAL)));
    public static RegistryObject<Block> SILICON_INFUSED_GLASS = BLOCKS.register("silicon_infused_glass", () -> new IronBarsBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().mapColor(DyeColor.WHITE).strength(1f, 1f).noOcclusion().sound(SoundType.GLASS)));

    public static RegistryObject<Item> SOLAR_PANEL_MK_I_ITEM = ITEMS.register("solar_panel_mk_i", () -> new BlockItem(SOLAR_PANEL_MK_I.get(), new Item.Properties()));
    public static RegistryObject<Item> SOLAR_PANEL_MK_II_ITEM = ITEMS.register("solar_panel_mk_ii", () -> new BlockItem(SOLAR_PANEL_MK_II.get(), new Item.Properties()));
    public static RegistryObject<Item> SOLAR_PANEL_MK_III_ITEM = ITEMS.register("solar_panel_mk_iii", () -> new BlockItem(SOLAR_PANEL_MK_III.get(), new Item.Properties()));
    public static RegistryObject<Item> HYDRO_POWER_TURBINE_MK_I_ITEM = ITEMS.register("hydro_power_turbine_mk_i", () -> new BlockItem(HYDRO_POWER_TURBINE_MK_I.get(), new Item.Properties()));
    public static RegistryObject<Item> HYDRO_POWER_TURBINE_MK_II_ITEM = ITEMS.register("hydro_power_turbine_mk_ii", () -> new BlockItem(HYDRO_POWER_TURBINE_MK_II.get(), new Item.Properties()));
    public static RegistryObject<Item> HYDRO_POWER_TURBINE_MK_III_ITEM = ITEMS.register("hydro_power_turbine_mk_iii", () -> new BlockItem(HYDRO_POWER_TURBINE_MK_III.get(), new Item.Properties()));
    public static RegistryObject<Item> MACHINE_CASING_ITEM = ITEMS.register("machine_casing", () -> new BlockItem(MACHINE_CASING.get(), new Item.Properties()));
    public static RegistryObject<Item> SILICON_INFUSED_GLASS_ITEM = ITEMS.register("silicon_infused_glass", () -> new BlockItem(SILICON_INFUSED_GLASS.get(), new Item.Properties()));

    public static RegistryObject<MenuType<EnergyInterfaceMenu>> ENERGY_INTERFACE_MENU_TYPE = MENU_TYPES.register("energy_interface", () -> IForgeMenuType.create(EnergyInterfaceMenu::new));
    public static RegistryObject<Item> SILICON = ITEMS.register("silicon", () -> new Item(new Item.Properties()));
    public static RegistryObject<Item> CARBON = ITEMS.register("carbon", () -> new Item(new Item.Properties()));
    public static RegistryObject<Item> CARBON_SHEET = ITEMS.register("carbon_sheet", () ->new Item(new Item.Properties()));
    public static RegistryObject<Item> DENSE_CARBON_SHEET = ITEMS.register("dense_carbon_sheet", () -> new Item(new Item.Properties()));
    public static RegistryObject<Item> BATTERY_MK_I = ITEMS.register("battery_mk_i", () -> new EnergyStorageItem(new Item.Properties().stacksTo(1).setNoRepair(), Config.BATTERY_MK_I_FE_STORAGE, Config.MK_I_FE_TRANSFER_RATE));
    public static RegistryObject<Item> BATTERY_MK_II = ITEMS.register("battery_mk_ii", () -> new EnergyStorageItem(new Item.Properties().stacksTo(1).setNoRepair(), Config.BATTERY_MK_II_FE_STORAGE, Config.MK_II_FE_TRANSFER_RATE));
    public static RegistryObject<Item> BATTERY_MK_III = ITEMS.register("battery_mk_iii", () -> new EnergyStorageItem(new Item.Properties().stacksTo(1).setNoRepair(), Config.BATTERY_MK_III_FE_STORAGE, Config.MK_III_FE_TRANSFER_RATE));


    public static RegistryObject<BlockEntityType<SolarPanelBlockEntity>> SOLAR_PANEL_BLOCK_ENTITY_TYPE =
            BLOCK_ENTITY_TYPES.register("solar_panel", () -> BlockEntityType.Builder.of(SolarPanelBlockEntity::new, SOLAR_PANEL_MK_I.get(), SOLAR_PANEL_MK_II.get(), SOLAR_PANEL_MK_III.get()).build(null));

    public static RegistryObject<BlockEntityType<HydroPowerTurbineBlockEntity>> HYDRO_POWER_TURBINE_BLOCK_ENTITY_TYPE =
            BLOCK_ENTITY_TYPES.register("hydro_power_turbine", () -> BlockEntityType.Builder.of(HydroPowerTurbineBlockEntity::new, HYDRO_POWER_TURBINE_MK_I.get(), HYDRO_POWER_TURBINE_MK_II.get(), HYDRO_POWER_TURBINE_MK_III.get()).build(null));
    public static final RegistryObject<CreativeModeTab> RENEWABLE_ENERGY_GENERAL_TAB = CREATIVE_MODE_TABS.register("renewable_energy", () -> CreativeModeTab.builder()
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

    public RenewableEnergy()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC, "renewable_energy.toml");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        MENU_TYPES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

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
