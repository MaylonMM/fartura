package com.fartura.compat.registry;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

/** Forge equivalent of NeoForge's DeferredHolder, backed by a RegistryObject. */
public class DeferredHolder<R, T extends R> implements Supplier<T> {
    protected final RegistryObject<T> delegate;

    DeferredHolder(RegistryObject<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T get() {
        return delegate.get();
    }

    public ResourceLocation getId() {
        return delegate.getId();
    }
}
