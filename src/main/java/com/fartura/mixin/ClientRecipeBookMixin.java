package com.fartura.mixin;

import com.fartura.recipe.FrigideiraRecipe;
import com.fartura.recipe.MortarRecipe;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeHolder;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * A vanilla só categoriza receitas de fornalha pelo tipo (smelting, blasting, smoking, campfire).
 * Receitas com tipo próprio (pilão e frigideira) cairiam em "Unknown recipe category" e sumiriam do livro.
 * Aqui elas são colocadas na aba de comida da fornalha.
 */
@Mixin(ClientRecipeBook.class)
public abstract class ClientRecipeBookMixin {
    @Inject(method = "getCategory", at = @At("HEAD"), cancellable = true)
    private static void fartura$categorizeMortarRecipe(RecipeHolder<?> holder,
                                                       CallbackInfoReturnable<RecipeBookCategories> cir) {
        if (holder.value() instanceof MortarRecipe || holder.value() instanceof FrigideiraRecipe) {
            cir.setReturnValue(RecipeBookCategories.FURNACE_FOOD);
        }
    }
}
