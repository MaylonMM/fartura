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
 * Receita feita na frigideira de ferro. Cada item colocado na frigideira gera um item de saída.
 */
public class FrigideiraRecipe extends AbstractCookingRecipe {
    public static final int DEFAULT_COOKING_TIME = 200;

    public FrigideiraRecipe(Ingredient ingredient, ItemStack result, int cookingTime, float experience) {
        super(ModRecipes.FRIGIDEIRA_TYPE.get(), "", CookingBookCategory.FOOD, ingredient, result, experience, cookingTime);
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public ItemStack getResult() {
        return result;
    }

    @Override
    public RecipeSerializer<FrigideiraRecipe> getSerializer() {
        return ModRecipes.FRIGIDEIRA_SERIALIZER.get();
    }

    public static final class Serializer implements RecipeSerializer<FrigideiraRecipe> {
        private static final MapCodec<FrigideiraRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(FrigideiraRecipe::getIngredient),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(FrigideiraRecipe::getResult),
                Codec.INT.optionalFieldOf("cookingtime", DEFAULT_COOKING_TIME).forGetter(FrigideiraRecipe::getCookingTime),
                Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(FrigideiraRecipe::getExperience)
        ).apply(instance, FrigideiraRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, FrigideiraRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, FrigideiraRecipe::getIngredient,
                ItemStack.STREAM_CODEC, FrigideiraRecipe::getResult,
                ByteBufCodecs.VAR_INT, FrigideiraRecipe::getCookingTime,
                ByteBufCodecs.FLOAT, FrigideiraRecipe::getExperience,
                FrigideiraRecipe::new
        );

        @Override
        public MapCodec<FrigideiraRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FrigideiraRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
