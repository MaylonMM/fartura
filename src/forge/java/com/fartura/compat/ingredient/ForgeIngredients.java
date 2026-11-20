package com.fartura.compat.ingredient;

import com.fartura.Fartura;

import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ForgeIngredients {
    private static final DeferredRegister<IIngredientSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.INGREDIENT_SERIALIZERS, Fartura.MODID);

    static {
        SERIALIZERS.register("water_potion", () -> WaterPotionIngredient.SERIALIZER);
    }

    private ForgeIngredients() {
    }

    public static void register(IEventBus modEventBus) {
        SERIALIZERS.register(modEventBus);
    }
}
