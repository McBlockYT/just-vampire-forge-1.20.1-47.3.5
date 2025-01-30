package net.Babychaosfloh.justvampires.config;

import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class JustVampiresClientConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    static {
        BUILDER.push("Client configs for JustVampires");

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}