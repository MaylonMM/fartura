package com.brasilfoods;

import com.brasilfoods.registry.ModCreativeTabs;
import com.brasilfoods.registry.ModItems;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(BrasilFoods.MODID)
public class BrasilFoods {
    public static final String MODID = "brasilfoods";

    public BrasilFoods(IEventBus modEventBus) {
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);
    }
}
