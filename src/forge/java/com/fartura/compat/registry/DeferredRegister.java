package com.fartura.compat.registry;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;

/** Forge adapter exposing the NeoForge DeferredRegister API used by this mod. */
public class DeferredRegister<T> {
    protected final net.minecraftforge.registries.DeferredRegister<T> delegate;

    protected DeferredRegister(net.minecraftforge.registries.DeferredRegister<T> delegate) {
        this.delegate = delegate;
    }

    public static <T> DeferredRegister<T> create(ResourceKey<? extends Registry<T>> registryKey, String modid) {
        return new DeferredRegister<>(net.minecraftforge.registries.DeferredRegister.create(registryKey, modid));
    }

    public static Items createItems(String modid) {
        return new Items(modid);
    }

    public static Blocks createBlocks(String modid) {
        return new Blocks(modid);
    }

    public <I extends T> DeferredHolder<T, I> register(String name, Supplier<? extends I> sup) {
        return new DeferredHolder<>(delegate.register(name, sup));
    }

    public void register(IEventBus bus) {
        delegate.register(bus);
    }

    public static class Items extends DeferredRegister<Item> {
        private Items(String modid) {
            super(net.minecraftforge.registries.DeferredRegister.create(Registries.ITEM, modid));
        }

        @Override
        public <I extends Item> DeferredItem<I> register(String name, Supplier<? extends I> sup) {
            return new DeferredItem<>(delegate.register(name, sup));
        }

        public DeferredItem<Item> registerSimpleItem(String name) {
            return registerSimpleItem(name, new Item.Properties());
        }

        public DeferredItem<Item> registerSimpleItem(String name, Item.Properties props) {
            return register(name, () -> new Item(props));
        }

        public DeferredItem<BlockItem> registerSimpleBlockItem(String name, Supplier<? extends Block> block) {
            return register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        }
    }

    public static class Blocks extends DeferredRegister<Block> {
        private Blocks(String modid) {
            super(net.minecraftforge.registries.DeferredRegister.create(Registries.BLOCK, modid));
        }

        @Override
        public <B extends Block> DeferredBlock<B> register(String name, Supplier<? extends B> sup) {
            return new DeferredBlock<>(delegate.register(name, sup));
        }
    }
}
