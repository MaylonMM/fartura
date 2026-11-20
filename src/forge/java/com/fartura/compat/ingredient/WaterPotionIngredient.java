package com.fartura.compat.ingredient;

import java.util.stream.Stream;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.ingredients.AbstractIngredient;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;

/** Matches a potion item whose contents are plain water. Replaces neoforge:components for Forge. */
public class WaterPotionIngredient extends AbstractIngredient {
    public static final WaterPotionIngredient INSTANCE = new WaterPotionIngredient();

    public static final IIngredientSerializer<WaterPotionIngredient> SERIALIZER = new IIngredientSerializer<>() {
        @Override
        public MapCodec<WaterPotionIngredient> codec() {
            return MapCodec.unit(INSTANCE);
        }

        @Override
        public void write(RegistryFriendlyByteBuf buffer, WaterPotionIngredient ingredient) {
        }

        @Override
        public WaterPotionIngredient read(RegistryFriendlyByteBuf buffer) {
            return INSTANCE;
        }
    };

    private WaterPotionIngredient() {
        super(Stream.of(new Ingredient.ItemValue(PotionContents.createItemStack(Items.POTION, Potions.WATER))));
    }

    @Override
    public boolean test(ItemStack stack) {
        if (stack == null || !stack.is(Items.POTION)) {
            return false;
        }
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        return contents != null && contents.is(Potions.WATER);
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> serializer() {
        return SERIALIZER;
    }
}
