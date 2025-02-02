package net.Babychaosfloh.justvampires.color;

import net.Babychaosfloh.justvampires.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ColorHandler {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemColors itemColors = Minecraft.getInstance().getItemColors();
            itemColors.register((stack, tintIndex) -> 0xDC143C, ModItems.BLOOD_SYRINGE.get());
        });
    }
}
