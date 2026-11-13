package com.fartura.item;

import com.fartura.block.MaracujaVineBlock;
import com.fartura.registry.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Maracujá fruit, which is also the seed. Edible, and right-click a jungle log side to plant it as a vine.
 */
public class MaracujaItem extends Item {
    public MaracujaItem(Properties properties) {
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
        BlockPos vinePos = logPos.relative(face);
        BlockState vine = ModBlocks.MARACUJA_VINE.get().defaultBlockState()
                .setValue(MaracujaVineBlock.FACING, face.getOpposite());
        if (!level.getBlockState(vinePos).canBeReplaced() || !vine.canSurvive(level, vinePos)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            level.setBlock(vinePos, vine, 3);
            level.playSound(null, vinePos, vine.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
            Player player = context.getPlayer();
            if (player == null || !player.getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
