package com.fartura.item;

import com.fartura.registry.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Lime fruit. Edible, and right-click the underside of an oak leaf block to plant it as a hanging lime.
 */
public class LimeItem extends Item {
    public LimeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos leavesPos = context.getClickedPos();
        if (context.getClickedFace() != Direction.DOWN || !level.getBlockState(leavesPos).is(Blocks.OAK_LEAVES)) {
            return InteractionResult.PASS;
        }
        BlockPos podPos = leavesPos.below();
        BlockState pod = ModBlocks.LIME_POD.get().defaultBlockState();
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
}
