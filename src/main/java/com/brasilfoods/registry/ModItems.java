package com.brasilfoods.registry;

import static com.brasilfoods.BrasilFoods.MODID;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<Item> PAO_DE_QUEIJO = ITEMS.registerSimpleItem("pao_de_queijo",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationModifier(0.6f)
                    .build()));

    private ModItems() {
    }
}
