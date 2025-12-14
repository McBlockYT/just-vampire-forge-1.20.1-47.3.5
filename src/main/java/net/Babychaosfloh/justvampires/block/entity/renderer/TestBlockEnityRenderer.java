package net.Babychaosfloh.justvampires.block.entity.renderer;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.Babychaosfloh.justvampires.JustVampires;
import net.Babychaosfloh.justvampires.block.custom.TestBlock;
import net.Babychaosfloh.justvampires.block.entity.TestBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;


public class TestBlockEnityRenderer implements BlockEntityRenderer<TestBlockEntity> {

    public TestBlockEnityRenderer(BlockEntityRendererProvider.Context context) {

    }
    //private final ResourceLocation skin = new ResourceLocation("modid", "textures/block/custom_texture.png");

    @Override
    public void render(TestBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStackH = new ItemStack(Items.DIRT);
        ItemStack itemStackS = new ItemStack(Items.GRASS);

        Direction direction = pBlockEntity.getBlockState().getValue(TestBlock.FACING);
        float headRotation = switch (direction) {
            case NORTH -> -90;
            case SOUTH -> 90;
            case WEST -> 0; //default
            case EAST -> 180;
            default -> 0;
        };
        float syringeYrotation = switch (direction) {
            case NORTH -> -90;
            case SOUTH -> 90;
            case WEST -> 0; //default
            case EAST -> 180;
            default -> 0;
        };
        float syringeZrotation = switch (direction) {
            case NORTH -> 90;
            case SOUTH -> -90;
            case WEST -> 0; //default
            case EAST -> 180;
            default -> 0;
        };
        float xRotation = switch (direction) {
            case NORTH -> 0;
            case SOUTH -> 0;
            case WEST -> 90; //default
            case EAST -> 90;
            default -> 0;
        };

            if (pBlockEntity.getBlockPos() != null && "debug" == "true") {
                JustVampires.print("HEAD_SLOT: " + ((TestBlockEntity) pBlockEntity.getLevel().getBlockEntity(pBlockEntity.getBlockPos())).itemHandler.getStackInSlot(0));
                JustVampires.print("Itemstackhandeler: " + pBlockEntity.itemHandler);
                JustVampires.print("Level: " + pBlockEntity.itemHandler);
                JustVampires.print("STACK: " + pBlockEntity.getRenderStack());
                JustVampires.print("SYRINGE_SLOT: " + pBlockEntity.itemHandler.getStackInSlot(1));
                JustVampires.print("pBlockEntity: " + pBlockEntity.getLevel().getBlockEntity(pBlockEntity.getBlockPos()));
                JustVampires.print("pPos: " + pBlockEntity.getBlockPos());
            }

            if (pBlockEntity.getBlockState().hasProperty(TestBlock.HEAD) && pBlockEntity.getBlockState().getValue(TestBlock.HEAD)) {
                itemStackH = pBlockEntity.itemHandler.getStackInSlot(0);
                //JustVampires.print(itemStackH.getItem().toString());
                //JustVampires.print("HEAD Item: " + itemStackH.getItem());
            }
            if (pBlockEntity.getBlockState().hasProperty(TestBlock.SYRINGE) && pBlockEntity.getBlockState().getValue(TestBlock.SYRINGE)) {
                itemStackS = pBlockEntity.itemHandler.getStackInSlot(1);
                //JustVampires.print("itemStackS: " + pBlockEntity.itemHandler.getStackInSlot(TestBlockEntity.SYRINGE_SLOT));
                //JustVampires.print("SYRINGE Item: " + itemStackS.getItem());
            }

//head
        if (!itemStackH.isEmpty()) {
            pPoseStack.pushPose();

            switch (direction) {
                case NORTH -> pPoseStack.translate(0.5, 1.062, 0.6867);
                case SOUTH -> pPoseStack.translate(0.5, 1.062, 0.3133);
                case WEST -> pPoseStack.translate(0.6867, 1.062, 0.5); //default
                case EAST -> pPoseStack.translate(0.3133, 1.062, 0.5);
            }

            //pPoseStack.translate(0.6867, 1.062, 0.5);
            pPoseStack.mulPose(Axis.YP.rotationDegrees(headRotation));
            itemRenderer.renderStatic(itemStackH, ItemDisplayContext.FIXED, getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()),
                    OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pBlockEntity.getLevel(), 1);
            pPoseStack.popPose();
        }

//syringe
        if (!itemStackS.isEmpty()) {
            pPoseStack.pushPose();

            switch (direction) {
                case NORTH -> pPoseStack.translate(0.5, 1.0, -0.3);
                case SOUTH -> pPoseStack.translate(0.5, 1.0, 1.3);
                case WEST -> pPoseStack.translate(0, 1.0, 0.5); //default
                case EAST -> pPoseStack.translate(1.3, 1.0, 0.5);
            }

            //pPoseStack.translate(0, 1.0, 0.5);
            pPoseStack.scale(0.5F, 0.5F, 0.5F);
            pPoseStack.mulPose(Axis.XP.rotationDegrees(xRotation));
            pPoseStack.mulPose(Axis.ZP.rotationDegrees(syringeZrotation));
            pPoseStack.mulPose(Axis.YP.rotationDegrees(syringeYrotation));


            itemRenderer.renderStatic(itemStackS, ItemDisplayContext.FIXED, getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()),
                    OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pBlockEntity.getLevel(), 1);
            pPoseStack.popPose();
        }
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}

