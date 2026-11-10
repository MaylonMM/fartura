package com.fartura.compat.jei;

import net.minecraft.world.item.ItemStack;

import java.util.List;

// Receita de corte: item de entrada + uma das ferramentas (facas) geram 1 item de saída. Não existe no RecipeManager, só para exibir no JEI.
public record CuttingBoardRecipe(ItemStack input, List<ItemStack> tools, ItemStack output) {
}
