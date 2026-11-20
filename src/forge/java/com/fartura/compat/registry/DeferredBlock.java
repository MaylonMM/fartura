package com.fartura.compat.registry;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

/** Forge equivalent of NeoForge's DeferredBlock. */
public class DeferredBlock<T extends Block> extends DeferredHolder<Block, T> {
    DeferredBlock(RegistryObject<T> delegate) {
        super(delegate);
    }
}
