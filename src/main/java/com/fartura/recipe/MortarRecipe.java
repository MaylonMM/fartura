package com.fartura.recipe;

import com.fartura.registry.ModRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * Receita de um item para outro no pilão de pedra. Cada item de entrada gera um item de saída.
 * Estende {@link AbstractCookingRecipe} para que o livro de receitas da fornalha reconheça a categoria.
 */
public class MortarRecipe extends AbstractCookingRecipe {
    public static final int DEFAULT_COOKING_TIME = 200;

    public MortarRecipe(Ingredient ingredient, ItemStack result, int cookingTime, float experience) {
        super(ModRecipes.MORTAR_TYPE.get(), "", CookingBookCategory.FOOD, ingredient, result, experience, cookingTime);
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public ItemStack getResult() {
        return result;
    }

    @Override
    public RecipeSerializer<MortarRecipe> getSerializer() {
        return ModRecipes.MORTAR_SERIALIZER.get();
    }

    public static final class Serializer implements RecipeSerializer<MortarRecipe> {
        private static final MapCodec<MortarRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(MortarRecipe::getIngredient),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(MortarRecipe::getResult),
                Codec.INT.optionalFieldOf("cookingtime", DEFAULT_COOKING_TIME).forGetter(MortarRecipe::getCookingTime),
                Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(MortarRecipe::getExperience)
        ).apply(instance, MortarRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, MortarRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, MortarRecipe::getIngredient,
                ItemStack.STREAM_CODEC, MortarRecipe::getResult,
                ByteBufCodecs.VAR_INT, MortarRecipe::getCookingTime,
                ByteBufCodecs.FLOAT, MortarRecipe::getExperience,
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
