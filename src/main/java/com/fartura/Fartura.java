package com.fartura;

import com.fartura.registry.ModBlockEntities;
import com.fartura.registry.ModBlocks;
import com.fartura.registry.ModCreativeTabs;
import com.fartura.registry.ModEntities;
import com.fartura.registry.ModFeatures;
import com.fartura.registry.ModItems;
import com.fartura.registry.ModMenus;
import com.fartura.registry.ModRecipes;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Fartura.MODID)
public class Fartura {
    public static final String MODID = "fartura";

    public Fartura(IEventBus modEventBus) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
        ModRecipes.RECIPE_TYPES.register(modEventBus);
        ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModFeatures.FEATURES.register(modEventBus);
    }
}
