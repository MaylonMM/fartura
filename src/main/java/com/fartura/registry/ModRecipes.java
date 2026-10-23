package com.fartura.registry;

import com.fartura.Fartura;
import com.fartura.recipe.MortarRecipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Fartura.MODID);

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Fartura.MODID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<MortarRecipe>> MORTAR_TYPE =
            RECIPE_TYPES.register("mortar", () -> new RecipeType<MortarRecipe>() {
                @Override
                public String toString() {
                    return Fartura.MODID + ":mortar";
                }
            });

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MortarRecipe>> MORTAR_SERIALIZER =
            RECIPE_SERIALIZERS.register("mortar", MortarRecipe.Serializer::new);

    private ModRecipes() {
    }
}
