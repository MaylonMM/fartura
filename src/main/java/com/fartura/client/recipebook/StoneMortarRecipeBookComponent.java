package com.fartura.client.recipebook;

import java.util.Set;

import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.world.item.Item;

/**
 * Livro de receitas do pilão. Usa o mesmo layout da fornalha, sem combustível.
 */
public class StoneMortarRecipeBookComponent extends AbstractFurnaceRecipeBookComponent {
    @Override
    protected Set<Item> getFuelItems() {
        return Set.of();
    }
}
