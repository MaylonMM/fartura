package com.fartura.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.block.Block;

/**
 * Coconut water: drinkable food, also placeable in the world as a block.
 */
public class CoconutWaterItem extends BlockItem {
    public CoconutWaterItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }
}
