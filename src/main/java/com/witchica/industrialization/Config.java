package com.witchica.industrialization;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = Industrialization.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static ModConfigSpec.ConfigValue<Integer> GENERATOR_MK_I_FE;
    public static ModConfigSpec.ConfigValue<Integer> GENERATOR_MK_II_FE;
    public static ModConfigSpec.ConfigValue<Integer> GENERATOR_MK_III_FE;
    public static ModConfigSpec.ConfigValue<Integer> GENERATOR_MK_I_FE_STORAGE;
    public static ModConfigSpec.ConfigValue<Integer> GENERATOR_MK_II_FE_STORAGE;
    public static ModConfigSpec.ConfigValue<Integer> GENERATOR_MK_III_FE_STORAGE;
    public static ModConfigSpec.ConfigValue<Integer> MK_I_FE_TRANSFER_RATE;
    public static ModConfigSpec.ConfigValue<Integer> MK_II_FE_TRANSFER_RATE;
    public static ModConfigSpec.ConfigValue<Integer> MK_III_FE_TRANSFER_RATE;
    public static ModConfigSpec.ConfigValue<Integer> BATTERY_MK_I_FE_STORAGE;
    public static ModConfigSpec.ConfigValue<Integer> BATTERY_MK_II_FE_STORAGE;
    public static ModConfigSpec.ConfigValue<Integer> BATTERY_MK_III_FE_STORAGE;
    public static ModConfigSpec.ConfigValue<Float> HYDRO_POWER_TURBINE_WATER_MK_I_MULTIPLIER;
    public static ModConfigSpec.ConfigValue<Float> HYDRO_POWER_TURBINE_WATER_MK_II_MULTIPLIER;
    public static ModConfigSpec.ConfigValue<Float> HYDRO_POWER_TURBINE_WATER_MK_III_MULTIPLIER;
    public static ModConfigSpec.ConfigValue<Integer> ENERGY_STORAGE_MK_I_STORAGE_AMOUNT;
    public static ModConfigSpec.ConfigValue<Integer> ENERGY_STORAGE_MK_II_STORAGE_AMOUNT;
    public static ModConfigSpec.ConfigValue<Integer> ENERGY_STORAGE_MK_III_STORAGE_AMOUNT;

    static ModConfigSpec SPEC;

    static {
        BUILDER.push("Generator FE Production");

        BUILDER.comment("How many FE/t does a MK I generator make?");
        GENERATOR_MK_I_FE = BUILDER.define("Generator MK I FE/t", 2);

        BUILDER.comment("How many FE/t does a MK II generator make");
        GENERATOR_MK_II_FE = BUILDER.define("Generator MK II FE/t", 4);

        BUILDER.comment("How many FE/t does a MK III generator make");
        GENERATOR_MK_III_FE = BUILDER.define("Generator MK III FE/t", 8);

        BUILDER.pop();

        BUILDER.push("Generator FE Storage");

        BUILDER.comment("How many FE can a MK I Generator hold?");
        GENERATOR_MK_I_FE_STORAGE = BUILDER.define("Generator MK I FE Storage", 1024);

        BUILDER.comment("How many FE can a MK II Generator hold?");
        GENERATOR_MK_II_FE_STORAGE = BUILDER.define("Generator MK II FE Storage", 2048);

        BUILDER.comment("How many FE can a MK III Generator hold?");
        GENERATOR_MK_III_FE_STORAGE = BUILDER.define("Generator MK III FE Storage", 4096);

        BUILDER.pop();

        BUILDER.push("Generator FE/t Extract");

        BUILDER.comment("How many FE can a MK I Generator put out per tick?");
        MK_I_FE_TRANSFER_RATE = BUILDER.define("Generator MK I FE/t Extract", 64);

        BUILDER.comment("How many FE can a MK II Generator put out per tick?");
        MK_II_FE_TRANSFER_RATE = BUILDER.define("Generator MK II FE/t Extract", 128);

        BUILDER.comment("How many FE can a MK III Generator put out per tick?");
        MK_III_FE_TRANSFER_RATE = BUILDER.define("Generator MK III FE/t Extract", 256);

        BUILDER.pop();

        BUILDER.push("Battery FE Storage");

        BUILDER.comment("How many FE can a MK I Battery hold?");
        BATTERY_MK_I_FE_STORAGE = BUILDER.define("Battery MK I FE Storage", 4096);

        BUILDER.comment("How many FE can a MK II Battery hold?");
        BATTERY_MK_II_FE_STORAGE = BUILDER.define("Battery MK II FE Storage", 8192);

        BUILDER.comment("How many FE can a MK III Battery hold?");
        BATTERY_MK_III_FE_STORAGE = BUILDER.define("Battery MK III FE Storage", 16384);

        BUILDER.pop();

        BUILDER.push("Hydro Power Turbine Water Multipliers");

        BUILDER.comment("Max Surround is 25 blocks, for each block how many FE should we generate? eg: 25 * multiplier (0.2) = 5");
        HYDRO_POWER_TURBINE_WATER_MK_I_MULTIPLIER = BUILDER.define("Hydro Turbine MK I Water Multiplier", 0.2f);

        BUILDER.comment("Max Surround is 25 blocks, for each block how many FE should we generate? eg: 25 * multiplier (0.4) = 10");
        HYDRO_POWER_TURBINE_WATER_MK_II_MULTIPLIER = BUILDER.define("Hydro Turbine MK II Water Multiplier", 0.4f);

        BUILDER.comment("Max Surround is 25 blocks, for each block how many FE should we generate? eg: 25 * multiplier (0.6) = 15");
        HYDRO_POWER_TURBINE_WATER_MK_III_MULTIPLIER = BUILDER.define("Hydro Turbine MK III Water Multiplier", 0.6f);

        BUILDER.pop();

        BUILDER.push("Energy Storage Max Storage Amounts");

        BUILDER.comment("How much energy can an Energy Storage MK I hold?");
        ENERGY_STORAGE_MK_I_STORAGE_AMOUNT = BUILDER.define("Energy Storage MK I Max Storage Amount", 8192);

        BUILDER.comment("How much energy can an Energy Storage MK II hold?");
        ENERGY_STORAGE_MK_II_STORAGE_AMOUNT = BUILDER.define("Energy Storage MK II Max Storage Amount", 16384);

        BUILDER.comment("How much energy can an Energy Storage MK III hold?");
        ENERGY_STORAGE_MK_III_STORAGE_AMOUNT = BUILDER.define("Energy Storage MK III Max Storage Amount", 32768);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {

    }
}
