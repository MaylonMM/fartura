package com.fartura.item;

import com.fartura.block.GuaranaCropBlock;
import com.fartura.registry.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Guaraná fruit and seed. Edible, and right-click the top of grass or farmland to plant a guaraná bush.
 */
public class GuaranaItem extends Item {
    public GuaranaItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos groundPos = context.getClickedPos();
        if (context.getClickedFace() != Direction.UP) {
            return InteractionResult.PASS;
        }
        BlockPos plantPos = groundPos.above();
        BlockState plant = ModBlocks.GUARANA_CROP.get().defaultBlockState();
        if (!level.getBlockState(plantPos).canBeReplaced() || !plant.canSurvive(level, plantPos)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            level.setBlock(plantPos, plant, 3);
            level.playSound(null, plantPos, plant.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
            Player player = context.getPlayer();
            if (player == null || !player.getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
