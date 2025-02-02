package net.Babychaosfloh.justvampires.color;

import net.Babychaosfloh.justvampires.JustVampires;
import net.Babychaosfloh.justvampires.config.JustVampiresCommonConfigs;
import net.Babychaosfloh.justvampires.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ColorHandler {
    private static final int defaultColor = 0xFFFFFF;

    public static void onBloodChange(ItemStack pStack, int pColor) {
        ItemColors itemColors = Minecraft.getInstance().getItemColors();
        //itemColors.register((stack, tintIndex) -> pColor, pStack.getItem());
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemColors itemColors = Minecraft.getInstance().getItemColors();
            itemColors.register((stack, tintIndex) -> defaultColor, ModItems.BLOOD_SYRINGE.get());
        });
    }
}
