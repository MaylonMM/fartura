package com.fartura.client.renderer;

import com.fartura.block.CuttingBoardBlock;
import com.fartura.block.entity.CuttingBoardBlockEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CuttingBoardRenderer implements BlockEntityRenderer<CuttingBoardBlockEntity> {
    public CuttingBoardRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CuttingBoardBlockEntity board, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ItemStack stack = board.getStack();
        if (stack.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 0.07, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0f * CuttingBoardBlock.rotationIndex(
                board.getBlockState().getValue(CuttingBoardBlock.FACING))));
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0f));
        poseStack.scale(0.3f, 0.3f, 0.3f);
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, packedLight,
                packedOverlay, poseStack, buffer, board.getLevel(), 0);
        poseStack.popPose();
    }
}
