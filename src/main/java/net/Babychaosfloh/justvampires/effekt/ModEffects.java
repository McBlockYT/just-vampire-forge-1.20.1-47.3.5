package net.Babychaosfloh.justvampires.effekt;

import net.Babychaosfloh.justvampires.JustVampires;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS
            = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, JustVampires.MOD_ID);

    public static final RegistryObject<MobEffect> SEPSIS = MOB_EFFECTS.register("sepsis",
            () -> new SepsisEffect(MobEffectCategory.HARMFUL,3124687));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
