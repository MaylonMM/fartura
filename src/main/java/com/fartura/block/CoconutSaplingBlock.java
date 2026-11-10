package com.fartura.block;

import com.fartura.worldgen.CoconutTreeFeature;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Coconut sapling. Grows into a coconut tree when planted on sand or dirt.
 */
public class CoconutSaplingBlock extends BushBlock implements BonemealableBlock {
    public static final MapCodec<CoconutSaplingBlock> CODEC = simpleCodec(CoconutSaplingBlock::new);

    // Same hitbox as the vanilla sapling.
    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 12, 14);

    public CoconutSaplingBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.SAND) || state.is(BlockTags.DIRT);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(3) != 0) {
            return;
        }
        grow(state, level, pos, random);
    }

    private static void grow(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.removeBlock(pos, false);
        if (!CoconutTreeFeature.place(level, pos, random)) {
            level.setBlock(pos, state, 2);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return random.nextFloat() < 0.45F;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        grow(state, level, pos, random);
    }
}
