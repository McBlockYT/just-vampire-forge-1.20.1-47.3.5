package net.Babychaosfloh.justvampires.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.Babychaosfloh.justvampires.JustVampires;
import net.Babychaosfloh.justvampires.entity.custom.Vampire_BatEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.BatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ambient.Bat;

public class Vampire_BatRenderer extends MobRenderer<Vampire_BatEntity, Vampire_BatModel<Vampire_BatEntity>> {
    public Vampire_BatRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new Vampire_BatModel(pContext.bakeLayer(ModModelLayers.VAMP_BAT_LAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(Vampire_BatEntity pEntity) {
        return new ResourceLocation(JustVampires.MOD_ID, "textures/entity/bat.png");
    }

    @Override
    public void render(Vampire_BatEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
    protected void scale(Vampire_BatEntity pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime) {
        pMatrixStack.scale(0.35F, 0.35F, 0.35F);
    }

    protected void setupRotations(Vampire_BatEntity pEntityLiving, PoseStack pMatrixStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
        if (pEntityLiving.isResting()) {
            pMatrixStack.translate(0.0F, -0.1F, 0.0F);
        } else {
            pMatrixStack.translate(0.0F, Mth.cos(pAgeInTicks * 0.3F) * 0.1F, 0.0F);
        }

        super.setupRotations(pEntityLiving, pMatrixStack, pAgeInTicks, pRotationYaw, pPartialTicks);
    }
}
