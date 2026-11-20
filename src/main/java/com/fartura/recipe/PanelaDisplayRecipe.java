package com.fartura.recipe;

import com.fartura.registry.ModRecipes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;

/**
 * Versao so de exibicao da receita da panela de barro, usada pelo guia (Patchouli) para mostrar a grade.
 * Nunca combina, para a bancada de trabalho nao craftar o item.
 */
public class PanelaDisplayRecipe extends ShapedRecipe {
    private final ShapedRecipePattern displayPattern;
    private final ItemStack displayResult;

    public PanelaDisplayRecipe(ShapedRecipePattern pattern, ItemStack result) {
        super("", CraftingBookCategory.MISC, pattern, result);
        this.displayPattern = pattern;
        this.displayResult = result;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.PANELA_DISPLAY_SERIALIZER.get();
    }

    public static final class Serializer implements RecipeSerializer<PanelaDisplayRecipe> {
        private static final MapCodec<PanelaDisplayRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.displayPattern),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.displayResult)
        ).apply(instance, PanelaDisplayRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, PanelaDisplayRecipe> STREAM_CODEC = StreamCodec.composite(
                ShapedRecipePattern.STREAM_CODEC, recipe -> recipe.displayPattern,
                ItemStack.STREAM_CODEC, recipe -> recipe.displayResult,
                PanelaDisplayRecipe::new
        );

        @Override
        public MapCodec<PanelaDisplayRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PanelaDisplayRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
