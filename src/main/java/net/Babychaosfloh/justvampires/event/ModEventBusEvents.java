package net.Babychaosfloh.justvampires.event;


import net.Babychaosfloh.justvampires.JustVampires;
import net.Babychaosfloh.justvampires.entity.ModEntities;
import net.Babychaosfloh.justvampires.entity.custom.Vampire_BatEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JustVampires.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.VAMPIRE_BAT.get(), Vampire_BatEntity.createAttributes().build());
    }
}
