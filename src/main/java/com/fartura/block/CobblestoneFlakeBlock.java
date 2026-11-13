package com.fartura.block;

import com.fartura.registry.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

/**
 * Cobblestone flakes placed on the ground. Right-clicking with more flakes stacks them, up to 4, like candles.
 * Breaking the block drops one flake per stacked flake.
 */
public class CobblestoneFlakeBlock extends Block {
    public static final MapCodec<CobblestoneFlakeBlock> CODEC = simpleCodec(CobblestoneFlakeBlock::new);
    public static final IntegerProperty FLAKES = IntegerProperty.create("flakes", 1, 4);
    public static final int MAX_FLAKES = 4;

    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 1, 14);

    public CobblestoneFlakeBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FLAKES, 1));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FLAKES);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        boolean canStack = !context.isSecondaryUseActive()
                && context.getItemInHand().is(ModItems.COBBLESTONE_FLAKE.get())
                && state.getValue(FLAKES) < MAX_FLAKES;
        return canStack || super.canBeReplaced(state, context);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState existing = context.getLevel().getBlockState(context.getClickedPos());
        if (existing.is(this)) {
            return existing.setValue(FLAKES, Math.min(MAX_FLAKES, existing.getValue(FLAKES) + 1));
        }
        return defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return List.of(new ItemStack(ModItems.COBBLESTONE_FLAKE.get(), state.getValue(FLAKES)));
    }
}
