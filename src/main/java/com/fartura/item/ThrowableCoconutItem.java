package com.fartura.item;

import com.fartura.block.CoconutPodBlock;
import com.fartura.entity.CoconutProjectile;
import com.fartura.registry.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Green coconut. Right-click a jungle log side to plant it as a growing coconut pod.
 * Right-click in the air to throw it.
 */
public class ThrowableCoconutItem extends Item {
    public ThrowableCoconutItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos logPos = context.getClickedPos();
        Direction face = context.getClickedFace();
        if (face.getAxis().isVertical() || !level.getBlockState(logPos).is(BlockTags.JUNGLE_LOGS)) {
            return InteractionResult.PASS;
        }
        BlockPos podPos = logPos.relative(face);
        BlockState pod = ModBlocks.COCONUT_POD.get().defaultBlockState()
                .setValue(CoconutPodBlock.FACING, face.getOpposite());
        if (!level.getBlockState(podPos).canBeReplaced() || !pod.canSurvive(level, podPos)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            level.setBlock(podPos, pod, 3);
            level.playSound(null, podPos, pod.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
            Player player = context.getPlayer();
            if (player == null || !player.getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            CoconutProjectile.throwFrom(level, player, stack.copyWithCount(1));
        }
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
