package net.Babychaosfloh.justvampires.event;

import net.Babychaosfloh.justvampires.JustVampires;
import net.Babychaosfloh.justvampires.block.entity.ModBlockEntities;
import net.Babychaosfloh.justvampires.block.entity.renderer.TestBlockEnityRenderer;
import net.Babychaosfloh.justvampires.entity.client.ModModelLayers;
import net.Babychaosfloh.justvampires.entity.client.Vampire_BatModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JustVampires.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.TEST_BE.get(), TestBlockEnityRenderer::new);
    }
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.VAMP_BAT_LAYER, Vampire_BatModel::createBodyLayer);
    }
}