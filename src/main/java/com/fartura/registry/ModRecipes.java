package com.fartura.registry;

import com.fartura.Fartura;
import com.fartura.recipe.FrigideiraRecipe;
import com.fartura.recipe.KettleRecipe;
import com.fartura.recipe.MortarRecipe;
import com.fartura.recipe.PanelaDisplayRecipe;
import com.fartura.recipe.PanelaRecipe;

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

    public static final DeferredHolder<RecipeType<?>, RecipeType<PanelaRecipe>> PANELA_TYPE =
            RECIPE_TYPES.register("panela", () -> new RecipeType<PanelaRecipe>() {
                @Override
                public String toString() {
                    return Fartura.MODID + ":panela";
                }
            });

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PanelaRecipe>> PANELA_SERIALIZER =
            RECIPE_SERIALIZERS.register("panela", PanelaRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PanelaDisplayRecipe>> PANELA_DISPLAY_SERIALIZER =
            RECIPE_SERIALIZERS.register("panela_display", PanelaDisplayRecipe.Serializer::new);

    public static final DeferredHolder<RecipeType<?>, RecipeType<PanelaRecipe>> KETTLE_TYPE =
            RECIPE_TYPES.register("kettle", () -> new RecipeType<PanelaRecipe>() {
                @Override
                public String toString() {
                    return Fartura.MODID + ":kettle";
                }
            });

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PanelaRecipe>> KETTLE_SERIALIZER =
            RECIPE_SERIALIZERS.register("kettle", KettleRecipe.Serializer::new);

    public static final DeferredHolder<RecipeType<?>, RecipeType<FrigideiraRecipe>> FRIGIDEIRA_TYPE =
            RECIPE_TYPES.register("frigideira", () -> new RecipeType<FrigideiraRecipe>() {
                @Override
                public String toString() {
                    return Fartura.MODID + ":frigideira";
                }
            });

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FrigideiraRecipe>> FRIGIDEIRA_SERIALIZER =
            RECIPE_SERIALIZERS.register("frigideira", FrigideiraRecipe.Serializer::new);

    private ModRecipes() {
    }
}
