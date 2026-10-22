package com.fartura;

import com.fartura.registry.ModCreativeTabs;
import com.fartura.registry.ModItems;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Fartura.MODID)
public class Fartura {
    public static final String MODID = "fartura";

    public Fartura(IEventBus modEventBus) {
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);
    }
}
