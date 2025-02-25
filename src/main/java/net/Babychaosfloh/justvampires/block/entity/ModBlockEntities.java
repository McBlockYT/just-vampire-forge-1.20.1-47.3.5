package net.Babychaosfloh.justvampires.block.entity;

import net.Babychaosfloh.justvampires.JustVampires;
import net.Babychaosfloh.justvampires.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, JustVampires.MOD_ID);

    public static final RegistryObject<BlockEntityType<TestBlockEntity>> TEST_BE =
            BLOCK_ENTITIES.register("test_be", () ->
                    BlockEntityType.Builder.of(TestBlockEntity::new,
                            ModBlocks.TESTBLOCK.get()).build(null));




    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
