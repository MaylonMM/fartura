package com.fartura.client.renderer;

import com.fartura.block.FrigideiraBlock;
import com.fartura.block.entity.FrigideiraBlockEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class FrigideiraRenderer implements BlockEntityRenderer<FrigideiraBlockEntity> {
    public FrigideiraRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(FrigideiraBlockEntity frigideira, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        float yaw = -frigideira.getBlockState().getValue(FrigideiraBlock.FACING).toYRot();

        for (int i = 0; i < FrigideiraBlockEntity.SLOTS; i++) {
            ItemStack stack = frigideira.getInventory().getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }

            poseStack.pushPose();
            poseStack.translate(0.5, 0.0, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
            poseStack.translate(-0.5, 0.0, -0.5);
            Vec3 pos = FrigideiraBlockEntity.SLOT_POSITIONS[i];
            poseStack.translate(pos.x, pos.y, pos.z);
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0f));
            poseStack.scale(0.18f, 0.18f, 0.18f);
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay,
                    poseStack, buffer, frigideira.getLevel(), i);
            poseStack.popPose();
        }
    }
}
