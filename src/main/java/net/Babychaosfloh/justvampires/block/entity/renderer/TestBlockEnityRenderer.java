package net.Babychaosfloh.justvampires.block.entity.renderer;


import com.mojang.blaze3d.vertex.PoseStack;
import net.Babychaosfloh.justvampires.JustVampires;
import net.Babychaosfloh.justvampires.block.custom.TestBlock;
import net.Babychaosfloh.justvampires.block.entity.TestBlockEntity;
import net.Babychaosfloh.justvampires.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;


public class TestBlockEnityRenderer implements BlockEntityRenderer<TestBlockEntity> {

    public TestBlockEnityRenderer(BlockEntityRendererProvider.Context context) {

    }
    private final ResourceLocation skin = new ResourceLocation("modid", "textures/block/custom_texture.png");

    @Override
    public void render(TestBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStackH = new ItemStack(Items.DIRT);
        ItemStack itemStackS = new ItemStack(Items.GRASS);

        pPoseStack.pushPose();
        // Position and scale transformations
        pPoseStack.translate(0.6867, 1.062, 0.5);
        //pPoseStack.scale(1.0F, -1.0F, -1.0F);


        JustVampires.print("HEAD_SLOT: " + pBlockEntity.itemHandler.getStackInSlot(TestBlockEntity.HEAD_SLOT));
        JustVampires.print("SYRINGE_SLOT: " + pBlockEntity.itemHandler.getStackInSlot(TestBlockEntity.SYRINGE_SLOT));
        JustVampires.print("pBlockEntity " + pBlockEntity);
        JustVampires.print("pPos " + pBlockEntity.getBlockPos());

        if (pBlockEntity.getBlockState().hasProperty(TestBlock.HEAD) && pBlockEntity.getBlockState().getValue(TestBlock.HEAD)) {
            itemStackH = pBlockEntity.itemHandler.getStackInSlot(0);
            JustVampires.print(itemStackH.getItem().toString());
            JustVampires.print("HEAD Item: " + itemStackH.getItem());
        }
        if (pBlockEntity.getBlockState().hasProperty(TestBlock.SYRINGE) && pBlockEntity.getBlockState().getValue(TestBlock.SYRINGE)) {
            itemStackS = pBlockEntity.itemHandler.getStackInSlot(TestBlockEntity.SYRINGE_SLOT);
            JustVampires.print("itemStackS: " + pBlockEntity.itemHandler.getStackInSlot(TestBlockEntity.SYRINGE_SLOT));
            JustVampires.print("SYRINGE Item: " + itemStackS.getItem());
        }
        //head
        itemRenderer.renderStatic(itemStackH, ItemDisplayContext.FIXED, getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()),
                OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pBlockEntity.getLevel(), 1);

        //syringe
        itemRenderer.renderStatic(itemStackS, ItemDisplayContext.FIXED, getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()),
               OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pBlockEntity.getLevel(), 1);
        pPoseStack.popPose();

    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}

