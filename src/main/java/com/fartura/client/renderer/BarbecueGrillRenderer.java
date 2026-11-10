package com.fartura.client.renderer;

import com.fartura.block.BarbecueGrillBlock;
import com.fartura.block.entity.BarbecueGrillBlockEntity;

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

public class BarbecueGrillRenderer implements BlockEntityRenderer<BarbecueGrillBlockEntity> {
    private static final Vec3[] SLOT_POSITIONS = {
            new Vec3(0.3125, 1.015625, 0.3125),
            new Vec3(0.6875, 1.015625, 0.3125),
            new Vec3(0.3125, 1.015625, 0.6875),
            new Vec3(0.6875, 1.015625, 0.6875)
    };

    public BarbecueGrillRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BarbecueGrillBlockEntity grill, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        float height = grill.getBlockState().getBlock() instanceof BarbecueGrillBlock block ? block.getGrillHeight() : 1.0F;

        for (int i = 0; i < BarbecueGrillBlockEntity.GRILL_SLOTS; i++) {
            ItemStack stack = grill.getInventory().getStackInSlot(BarbecueGrillBlockEntity.FIRST_GRILL_SLOT + i);
            if (stack.isEmpty()) {
                continue;
            }

            poseStack.pushPose();
            Vec3 pos = SLOT_POSITIONS[i];
            poseStack.translate(pos.x, pos.y - (1.0 - height), pos.z);
            poseStack.mulPose(Axis.YP.rotationDegrees(90.0f));
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
            poseStack.scale(0.4f, 0.4f, 0.4f);
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay,
                    poseStack, buffer, grill.getLevel(), i);
            poseStack.popPose();
        }
    }
}
