package com.fartura.compat.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

/** Forge equivalent of NeoForge's DeferredItem. */
public class DeferredItem<T extends Item> extends DeferredHolder<Item, T> {
    DeferredItem(RegistryObject<T> delegate) {
        super(delegate);
    }
}
