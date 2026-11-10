package com.fartura.registry;

import com.fartura.Fartura;
import com.fartura.block.entity.BarbecueGrillBlockEntity;
import com.fartura.block.entity.KettleBlockEntity;
import com.fartura.block.entity.CuttingBoardBlockEntity;
import com.fartura.block.entity.FrigideiraBlockEntity;
import com.fartura.block.entity.PanelaBlockEntity;
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

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BarbecueGrillBlockEntity>> BARBECUE_GRILL =
            BLOCK_ENTITIES.register("barbecue_grill", () -> BlockEntityType.Builder
                    .of(BarbecueGrillBlockEntity::new, ModBlocks.BARBECUE_GRILL.get(), ModBlocks.BARBECUE_GRILL_FLOOR.get())
                    .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PanelaBlockEntity>> PANELA =
            BLOCK_ENTITIES.register("panela", () -> BlockEntityType.Builder
                    .of(PanelaBlockEntity::new, ModBlocks.PANELA.get())
                    .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FrigideiraBlockEntity>> FRIGIDEIRA =
            BLOCK_ENTITIES.register("iron_frying_pan", () -> BlockEntityType.Builder
                    .of(FrigideiraBlockEntity::new, ModBlocks.IRON_FRYING_PAN.get())
                    .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<KettleBlockEntity>> KETTLE =
            BLOCK_ENTITIES.register("kettle", () -> BlockEntityType.Builder
                    .of(KettleBlockEntity::new, ModBlocks.KETTLE.get())
                    .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CuttingBoardBlockEntity>> CUTTING_BOARD =
            BLOCK_ENTITIES.register("cutting_board", () -> BlockEntityType.Builder
                    .of(CuttingBoardBlockEntity::new, ModBlocks.CUTTING_BOARD.get())
                    .build(null));
}
