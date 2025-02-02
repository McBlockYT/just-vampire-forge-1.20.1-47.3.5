package net.Babychaosfloh.justvampires.config;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Style;
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
                        "justvampires:bloodtype_normal:0xDC143C",
                        "justvampires:bloodtype_insect:0xF4E04D",
                        //"justvampires:bloodtype_slime:0xFF0000"
                        "justvampires:bloodtype_zombie:0x9c3c16"
                ), // Standart values
                o -> o instanceof String // validating
        );

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}