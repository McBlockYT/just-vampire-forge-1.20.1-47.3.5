package net.Babychaosfloh.justvampires.color;

import net.Babychaosfloh.justvampires.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ColorHandler {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> {
            if (tintIndex == 0 && stack.hasTag() && stack.getTag().contains("CustomColor")) {
                return stack.getTag().getInt("CustomColor");
            }
            return 0xFFFFFF; // Standardfarbe
        }, ModItems.BLOOD_SYRINGE.get()); // Item statt ItemStack übergeben
    }
}

