package com.fartura.registry;

import com.fartura.Fartura;
import com.fartura.block.entity.StoneMortarBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Fartura.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StoneMortarBlockEntity>> STONE_MORTAR =
            BLOCK_ENTITIES.register("stone_mortar", () -> BlockEntityType.Builder
                    .of(StoneMortarBlockEntity::new, ModBlocks.STONE_MORTAR.get())
                    .build(null));
}
