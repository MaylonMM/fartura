package com.fartura;

import com.fartura.registry.ModBlockEntities;
import com.fartura.registry.ModBlocks;
import com.fartura.registry.ModCreativeTabs;
import com.fartura.registry.ModEntities;
import com.fartura.registry.ModFeatures;
import com.fartura.registry.ModItems;
import com.fartura.registry.ModMenus;
import com.fartura.registry.ModRecipes;

//? if neoforge {
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
//?} else {
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import com.fartura.compat.ingredient.ForgeIngredients;
import com.fartura.compat.recipe.ModRecipeSerializer;
//?}

@Mod(Fartura.MODID)
public class Fartura {
    public static final String MODID = "fartura";

    //? if neoforge {
    public Fartura(IEventBus modEventBus) {
    //?} else {
    public Fartura() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    //?}
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
        ModRecipes.RECIPE_TYPES.register(modEventBus);
        ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModFeatures.FEATURES.register(modEventBus);
        //? if forge {
        ForgeIngredients.register(modEventBus);
        ModRecipeSerializer.register(modEventBus);
        //?}
    }
}
