package com.fartura.recipe;

import com.fartura.registry.ModRecipes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

/**
 * Receita de um item para outro no pilão de pedra. Cada item de entrada gera um item de saída.
 */
public class MortarRecipe implements Recipe<SingleRecipeInput> {
    private final Ingredient ingredient;
    private final ItemStack result;

    public MortarRecipe(Ingredient ingredient, ItemStack result) {
        this.ingredient = ingredient;
        this.result = result;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public ItemStack getResult() {
        return result;
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return ingredient.test(input.getItem(0));
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result;
    }

    @Override
    public RecipeSerializer<MortarRecipe> getSerializer() {
        return ModRecipes.MORTAR_SERIALIZER.get();
    }

    @Override
    public RecipeType<MortarRecipe> getType() {
        return ModRecipes.MORTAR_TYPE.get();
    }

    public static final class Serializer implements RecipeSerializer<MortarRecipe> {
        private static final MapCodec<MortarRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(MortarRecipe::getIngredient),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(MortarRecipe::getResult)
        ).apply(instance, MortarRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, MortarRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, MortarRecipe::getIngredient,
                ItemStack.STREAM_CODEC, MortarRecipe::getResult,
                MortarRecipe::new
        );

        @Override
        public MapCodec<MortarRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MortarRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
