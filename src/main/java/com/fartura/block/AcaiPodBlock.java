package com.fartura.block;

import com.fartura.registry.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Açaí cluster growing on the side of a jungle trunk, modeled on the coconut pod.
 * FACING points from the cluster toward the log it hangs on. Ripe clusters (age 2) drop 1-3 açaí
 * when right-clicked; right-click also resets the cluster to age 0.
 */
public class AcaiPodBlock extends HorizontalDirectionalBlock implements BonemealableBlock {
    public static final MapCodec<AcaiPodBlock> CODEC = simpleCodec(AcaiPodBlock::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
    public static final int MAX_AGE = 2;

    // Boxes authored for FACING = SOUTH (same frame as the block models): the cluster and the stalk up to the log.
    // All ages share the same model geometry, so they share the same hitbox.
    private static final double[][] SOUTH_BOXES = {
            {3, 0, 4, 13, 10, 14},
            {7, 10, 8, 9, 14, 16}
    };
    private static final VoxelShape[][] SHAPES = buildShapes();

    private static VoxelShape[][] buildShapes() {
        Direction[] dirs = {Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.EAST};
        VoxelShape[][] result = new VoxelShape[4][MAX_AGE + 1];
        for (int d = 0; d < dirs.length; d++) {
            VoxelShape shape = Shapes.empty();
            for (double[] box : SOUTH_BOXES) {
                shape = Shapes.or(shape, rotatedBox(box, dirs[d]));
            }
            for (int age = 0; age <= MAX_AGE; age++) {
                result[d][age] = shape;
            }
        }
        return result;
    }

    /** Maps a box from the south-facing frame to the given facing. */
    private static VoxelShape rotatedBox(double[] b, Direction facing) {
        double ax0 = b[0], ax1 = b[3], az0 = b[2], az1 = b[5];
        double x0, x1, z0, z1;
        switch (facing) {
            case WEST -> { x0 = 16 - az1; x1 = 16 - az0; z0 = ax0; z1 = ax1; }
            case NORTH -> { x0 = 16 - ax1; x1 = 16 - ax0; z0 = 16 - az1; z1 = 16 - az0; }
            case EAST -> { x0 = az0; x1 = az1; z0 = 16 - ax1; z1 = 16 - ax0; }
            default -> { x0 = ax0; x1 = ax1; z0 = az0; z1 = az1; }
        }
        return Block.box(x0, b[1], z0, x1, b[4], z1);
    }

    public AcaiPodBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(AGE, 0));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, AGE);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[facingIndex(state.getValue(FACING))][state.getValue(AGE)];
    }

    private static int facingIndex(Direction facing) {
        return switch (facing) {
            case WEST -> 1;
            case NORTH -> 2;
            case EAST -> 3;
            default -> 0;
        };
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == state.getValue(FACING) && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.relative(state.getValue(FACING))).is(BlockTags.JUNGLE_LOGS);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < MAX_AGE;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            level.removeBlock(pos, false);
            return;
        }
        int age = state.getValue(AGE);
        if (age < MAX_AGE && random.nextInt(5) == 0) {
            level.setBlock(pos, state.setValue(AGE, age + 1), 2);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(AGE) < MAX_AGE;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(AGE, state.getValue(AGE) + 1), 2);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (state.getValue(AGE) < MAX_AGE) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            level.levelEvent(2001, pos, Block.getId(state));
            popResource(level, pos, new ItemStack(ModItems.ACAI.get(), 1 + level.getRandom().nextInt(3)));
            level.setBlock(pos, state.setValue(AGE, 0), 2);
        }
        return InteractionResult.SUCCESS;
    }
}
