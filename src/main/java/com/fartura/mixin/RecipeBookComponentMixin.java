package com.fartura.mixin;

import java.util.List;

import com.fartura.client.recipebook.StoneMortarRecipeBookComponent;
import com.fartura.recipe.MortarRecipe;

import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.world.item.crafting.RecipeHolder;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * No livro do pilão, mostra apenas as coleções que contêm receitas do pilão.
 * O livro da vanilla usa o mesmo {@code RecipeBookType.FURNACE}, então sem este filtro
 * as receitas da fornalha apareceriam aqui também.
 */
@Mixin(RecipeBookComponent.class)
public abstract class RecipeBookComponentMixin {
    @ModifyArg(
            method = "updateCollections",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/recipebook/RecipeBookPage;updateCollections(Ljava/util/List;Z)V"
            ),
            index = 0
    )
    private List<RecipeCollection> fartura$onlyMortarRecipes(List<RecipeCollection> collections) {
        if ((Object) this instanceof StoneMortarRecipeBookComponent) {
            return collections.stream()
                    .filter(collection -> collection.getRecipes().stream().anyMatch(RecipeBookComponentMixin::isMortarRecipe))
                    .toList();
        }
        return collections.stream()
                .filter(collection -> !collection.getRecipes().stream().allMatch(RecipeBookComponentMixin::isMortarRecipe))
                .toList();
    }

    private static boolean isMortarRecipe(RecipeHolder<?> holder) {
        return holder.value() instanceof MortarRecipe;
    }
}
