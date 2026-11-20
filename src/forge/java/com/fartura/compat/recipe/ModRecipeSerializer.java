package com.fartura.compat.recipe;

import com.fartura.Fartura;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Wraps a recipe that depends on an optional mod. The inner recipe is only parsed when that mod is loaded;
 * otherwise an inert placeholder is used, so the inner recipe type (e.g. create:milling) is never resolved.
 */
public final class ModRecipeSerializer implements RecipeSerializer<Recipe<?>> {
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Fartura.MODID);

    private static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Fartura.MODID);

    // Dedicated type so JEI and other recipe-type consumers never treat a placeholder as a crafting recipe.
    private static final RecipeType<Recipe<?>> PLACEHOLDER_TYPE =
            RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Fartura.MODID, "mod_recipe_placeholder"));

    public static final ModRecipeSerializer INSTANCE = new ModRecipeSerializer();

    private static final Recipe<?> PLACEHOLDER = new Placeholder();

    private record Wrapper(String mod, Dynamic<?> recipe) {
    }

    private static final MapCodec<Wrapper> WRAPPER_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("mod").forGetter(Wrapper::mod),
            Codec.PASSTHROUGH.fieldOf("recipe").forGetter(Wrapper::recipe)
    ).apply(instance, Wrapper::new));

    private static final MapCodec<Recipe<?>> CODEC = WRAPPER_CODEC.flatXmap(
            wrapper -> ModList.get().isLoaded(wrapper.mod())
                    ? parse(wrapper.recipe())
                    : DataResult.success(PLACEHOLDER),
            recipe -> DataResult.error(() -> "fartura:mod_recipe cannot be encoded")
    );

    private static final StreamCodec<RegistryFriendlyByteBuf, Recipe<?>> STREAM_CODEC = StreamCodec.unit(PLACEHOLDER);

    private ModRecipeSerializer() {
    }

    private static <T> DataResult<Recipe<?>> parse(Dynamic<T> dynamic) {
        return Recipe.CODEC.parse(dynamic.getOps(), dynamic.getValue());
    }

    @Override
    public MapCodec<Recipe<?>> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, Recipe<?>> streamCodec() {
        return STREAM_CODEC;
    }

    public static void register(IEventBus modEventBus) {
        SERIALIZERS.register("mod_recipe", () -> INSTANCE);
        SERIALIZERS.register(modEventBus);
        TYPES.register("mod_recipe_placeholder", () -> PLACEHOLDER_TYPE);
        TYPES.register(modEventBus);
    }

    private static final class Placeholder implements Recipe<RecipeInput> {
        @Override
        public boolean matches(RecipeInput input, Level level) {
            return false;
        }

        @Override
        public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
            return ItemStack.EMPTY;
        }

        @Override
        public boolean canCraftInDimensions(int width, int height) {
            return false;
        }

        @Override
        public ItemStack getResultItem(HolderLookup.Provider registries) {
            return ItemStack.EMPTY;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return INSTANCE;
        }

        @Override
        public RecipeType<?> getType() {
            return PLACEHOLDER_TYPE;
        }
    }
}
