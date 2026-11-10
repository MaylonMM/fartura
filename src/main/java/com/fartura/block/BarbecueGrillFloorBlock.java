package com.fartura.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BarbecueGrillFloorBlock extends BarbecueGrillBlock {
    public static final MapCodec<BarbecueGrillFloorBlock> CODEC = simpleCodec(BarbecueGrillFloorBlock::new);

    private static final VoxelShape FLOOR_SHAPE = Block.box(0, 0, 0, 16, 8, 16);

    public BarbecueGrillFloorBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BarbecueGrillBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return FLOOR_SHAPE;
    }

    @Override
    public float getGrillHeight() {
        return 0.5F;
    }
}
