package net.Babychaosfloh.justvampires;

import com.mojang.logging.LogUtils;
import net.Babychaosfloh.justvampires.block.ModBlocks;
import net.Babychaosfloh.justvampires.block.entity.ModBlockEntities;
import net.Babychaosfloh.justvampires.config.JustVampiresClientConfigs;
import net.Babychaosfloh.justvampires.config.JustVampiresCommonConfigs;
import net.Babychaosfloh.justvampires.effekt.ModEffects;
import net.Babychaosfloh.justvampires.entity.ModEntities;
import net.Babychaosfloh.justvampires.entity.client.Vampire_BatRenderer;
import net.Babychaosfloh.justvampires.entity.custom.Vampire_BatEntity;
import net.Babychaosfloh.justvampires.item.ModCreativeModTabs;
import net.Babychaosfloh.justvampires.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.*;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(JustVampires.MOD_ID)
public class JustVampires {
    public static final String MOD_ID = "justvampires";
    private static final Logger LOGGER = LogUtils.getLogger();


    public JustVampires() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModTabs.register(modEventBus);

        ModEffects.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModBlockEntities.register(modEventBus);
        ModEntities.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, JustVampiresCommonConfigs.SPEC, "justvampries-common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, JustVampiresClientConfigs.SPEC, "justvampries-client.toml");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.BLOOD_SYRINGE);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
            EntityRenderers.register(ModEntities.VAMPIRE_BAT.get(), Vampire_BatRenderer::new);
        }
    }

    public static void print(String pPrint) {
        LOGGER.info(pPrint);
    }
}
