package com.fartura.recipe;

import com.fartura.registry.ModRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;

/**
 * Receita com formato de bancada (grade 3x3), feita apenas na panela de barro enquanto houver fogo embaixo.
 */
public class PanelaRecipe implements Recipe<CraftingInput> {
    public static final int DEFAULT_COOKING_TIME = 200;

    private final ShapedRecipePattern pattern;
    private final ItemStack result;
    private final int cookingTime;

    public PanelaRecipe(ShapedRecipePattern pattern, ItemStack result, int cookingTime) {
        this.pattern = pattern;
        this.result = result;
        this.cookingTime = cookingTime;
    }

    public ShapedRecipePattern getPattern() {
        return pattern;
    }

    public ItemStack getResult() {
        return result;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return pattern.matches(input);
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= pattern.width() && height >= pattern.height();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.PANELA_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.PANELA_TYPE.get();
    }

    public static final class Serializer implements RecipeSerializer<PanelaRecipe> {
        private static final MapCodec<PanelaRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ShapedRecipePattern.MAP_CODEC.forGetter(PanelaRecipe::getPattern),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(PanelaRecipe::getResult),
                Codec.INT.optionalFieldOf("cookingtime", DEFAULT_COOKING_TIME).forGetter(PanelaRecipe::getCookingTime)
        ).apply(instance, PanelaRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, PanelaRecipe> STREAM_CODEC = StreamCodec.composite(
                ShapedRecipePattern.STREAM_CODEC, PanelaRecipe::getPattern,
                ItemStack.STREAM_CODEC, PanelaRecipe::getResult,
                ByteBufCodecs.VAR_INT, PanelaRecipe::getCookingTime,
                PanelaRecipe::new
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
