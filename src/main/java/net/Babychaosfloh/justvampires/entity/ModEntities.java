package net.Babychaosfloh.justvampires.entity;

import net.Babychaosfloh.justvampires.JustVampires;
import net.Babychaosfloh.justvampires.entity.custom.Vampire_BatEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, JustVampires.MOD_ID);

    public static final RegistryObject<EntityType<Vampire_BatEntity>> VAMPIRE_BAT =
            ENTITY_TYPES.register("vampire_bat", () -> EntityType.Builder.of(Vampire_BatEntity::new, MobCategory.MONSTER)
                    .sized(0.5f, 0.9f).build("vampire_bat"));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
