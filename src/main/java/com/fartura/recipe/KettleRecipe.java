package com.fartura.recipe;

import com.fartura.registry.ModRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

/**
 * Receita da chaleira. Tem o mesmo formato da receita da panela, mas um tipo separado,
 * para que as duas se mantenham independentes.
 */
public class KettleRecipe extends PanelaRecipe {
    public KettleRecipe(ShapedRecipePattern pattern, ItemStack result, int cookingTime) {
        super(pattern, result, cookingTime);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.KETTLE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.KETTLE_TYPE.get();
    }

    public static final class Serializer implements RecipeSerializer<PanelaRecipe> {
        private static final MapCodec<PanelaRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ShapedRecipePattern.MAP_CODEC.forGetter(PanelaRecipe::getPattern),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(PanelaRecipe::getResult),
                Codec.INT.optionalFieldOf("cookingtime", DEFAULT_COOKING_TIME).forGetter(PanelaRecipe::getCookingTime)
        ).apply(instance, KettleRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, PanelaRecipe> STREAM_CODEC = StreamCodec.composite(
                ShapedRecipePattern.STREAM_CODEC, PanelaRecipe::getPattern,
                ItemStack.STREAM_CODEC, PanelaRecipe::getResult,
                ByteBufCodecs.VAR_INT, PanelaRecipe::getCookingTime,
                KettleRecipe::new
        );

        @Override
        public MapCodec<PanelaRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PanelaRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
