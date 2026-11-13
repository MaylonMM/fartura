package com.fartura.item;

import com.fartura.block.CobblestoneFlakeBlock;
import com.fartura.entity.CobblestoneFlakeProjectile;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Cobblestone flake. Right-click a surface to place it, right-click in the air to throw it.
 */
public class CobblestoneFlakeItem extends BlockItem {
    public CobblestoneFlakeItem(CobblestoneFlakeBlock block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            CobblestoneFlakeProjectile.throwFrom(level, player, stack.copyWithCount(1));
        }
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
