package net.Babychaosfloh.justvampires.item;

import net.Babychaosfloh.justvampires.JustVampires;
import net.Babychaosfloh.justvampires.item.Custom.BloodSyringeItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, JustVampires.MOD_ID);

    public static final RegistryObject<Item> BLOOD_SYRINGE = ITEMS.register("blood_syringe",
            () -> new BloodSyringeItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}