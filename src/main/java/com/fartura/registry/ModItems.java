package com.fartura.registry;

import com.fartura.Fartura;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Fartura.MODID);

    public static final DeferredItem<Item> CORN = ITEMS.registerSimpleItem("corn",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1)
                    .saturationModifier(0.1f)
                    .build()));

    // Named after the lower plant block so the item's name comes from its own translation key.
    public static final DeferredItem<ItemNameBlockItem> CORN_KERNELS = ITEMS.register("corn_kernels",
            () -> new ItemNameBlockItem(ModBlocks.CORN_CROP.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> WILD_CORN =
            ITEMS.registerSimpleBlockItem("wild_corn", ModBlocks.WILD_CORN);

    public static final DeferredItem<Item> CORN_FLOUR = ITEMS.registerSimpleItem("corn_flour",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1)
                    .saturationModifier(0.1f)
                    .build()));

    public static final DeferredItem<BlockItem> STONE_MORTAR =
            ITEMS.registerSimpleBlockItem("stone_mortar", ModBlocks.STONE_MORTAR);

    private ModItems() {
    }
}
