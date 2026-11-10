package com.fartura.block.entity;

import com.fartura.menu.KettleMenu;
import com.fartura.recipe.PanelaRecipe;
import com.fartura.registry.ModBlockEntities;
import com.fartura.registry.ModRecipes;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;

public class KettleBlockEntity extends PanelaBlockEntity {
    public KettleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.KETTLE.get(), pos, state);
    }

    @Override
    protected RecipeType<PanelaRecipe> recipeType() {
        return ModRecipes.KETTLE_TYPE.get();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.fartura.kettle");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new KettleMenu(containerId, playerInventory, this);
    }
}
