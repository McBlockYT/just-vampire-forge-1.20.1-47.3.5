package net.Babychaosfloh.justvampires.effekt;

import net.minecraft.WorldVersion;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class SepsisEffect extends MobEffect {
    public SepsisEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        float pHealth = pLivingEntity.getHealth();
        if (!pLivingEntity.level().isClientSide) {
            if (pHealth > 1.0F) {
                pLivingEntity.hurt(pLivingEntity.damageSources().magic(), 1.0F * pAmplifier);
            }
        }
        if (pAmplifier > 0) {
            super.applyEffectTick(pLivingEntity, pAmplifier);
        } else {
            pAmplifier = 1;
            super.applyEffectTick(pLivingEntity, pAmplifier);
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
