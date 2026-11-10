package com.fartura.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Small sweet placed on the ground, stackable up to {@link #MAX_COUNT} pieces like candles.
 * Empty-hand right-click eats one piece at a time.
 */
public class SweetBlock extends Block {
    public static final int MAX_COUNT = 4;
    public static final IntegerProperty COUNT = IntegerProperty.create("count", 1, MAX_COUNT);

    private static final VoxelShape[] SHAPES = {
            box(6.5, 0, 6.5, 9.5, 3, 9.5),
            Shapes.or(box(8.5, 0, 8.5, 11.5, 3, 11.5), box(4.5, 0, 4.5, 7.5, 3, 7.5)),
            Shapes.or(box(8.5, 0, 4.5, 11.5, 3, 7.5), box(6.5, 0, 8.5, 9.5, 3, 11.5), box(4.5, 0, 4.5, 7.5, 3, 7.5)),
            Shapes.or(box(4.5, 0, 8.5, 7.5, 3, 11.5), box(8.5, 0, 8.5, 11.5, 3, 11.5),
                    box(4.5, 0, 4.5, 7.5, 3, 7.5), box(8.5, 0, 4.5, 11.5, 3, 7.5))
    };

    public SweetBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(COUNT, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COUNT);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(COUNT) - 1];
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(COUNT) - 1];
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                     LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN && !canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return context.getItemInHand().is(asItem()) && state.getValue(COUNT) < MAX_COUNT;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState existing = context.getLevel().getBlockState(context.getClickedPos());
        if (existing.is(this)) {
            return existing.setValue(COUNT, Math.min(MAX_COUNT, existing.getValue(COUNT) + 1));
        }
        return defaultBlockState();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hit) {
        if (!player.getMainHandItem().isEmpty() || !player.canEat(false)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            player.eat(level, new ItemStack(asItem()));
            int count = state.getValue(COUNT);
            if (count > 1) {
                level.setBlock(pos, state.setValue(COUNT, count - 1), 3);
            } else {
                level.removeBlock(pos, false);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
