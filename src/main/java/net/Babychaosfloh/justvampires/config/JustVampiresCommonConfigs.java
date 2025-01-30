package net.Babychaosfloh.justvampires.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class JustVampiresCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> BLOOD_TYPE_ENTITY_TAGS;

    static {
        BUILDER.push("Common configs for JustVampires");

        BLOOD_TYPE_ENTITY_TAGS = BUILDER.defineList(
                "BLOOD_TYPE_ENTITY_TAGS", Arrays.asList(
                        "justvampires:bloodtype_normal",
                        "justvampires:bloodtype_insect",
                        "justvampires:bloodtype_slime"
                        //"justvampires:bloodtype_zombie",
                ), // Standart values
                o -> o instanceof String // validating
        );

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}