package com.fartura.block;

import com.fartura.registry.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
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
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Rice plant. Grows underwater on dirt-type ground until age 7, then can be harvested for 2 rice (rarely 3).
 * From {@link #TALL_AGE} on, a second block (HALF = UPPER) is placed above, like a double plant.
 * Only the lower block needs water; the upper block sits in the air above the water.
 */
public class RiceCropBlock extends BushBlock implements BonemealableBlock {
    public static final MapCodec<RiceCropBlock> CODEC = simpleCodec(RiceCropBlock::new);
    public static final int MAX_AGE = 7;
    public static final int TALL_AGE = 3;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    /** Lower block only: set when it cannot grow its second block (water above), so it stops random ticking. */
    public static final BooleanProperty STALLED = BooleanProperty.create("stalled");
    private static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 16, 13);
    /** Vanilla world event 2001: block break particles and sound. */
    private static final int BLOCK_BREAK_EVENT = 2001;

    public RiceCropBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(AGE, 0).setValue(HALF, DoubleBlockHalf.LOWER).setValue(STALLED, false));
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, HALF, STALLED);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER
                && state.getValue(AGE) < MAX_AGE
                && !state.getValue(STALLED);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.DIRT) || state.is(Blocks.FARMLAND) || state.is(Blocks.GRASS_BLOCK);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            BlockState below = level.getBlockState(pos.below());
            return below.is(this) && below.getValue(HALF) == DoubleBlockHalf.LOWER;
        }
        return super.canSurvive(state, level, pos) && level.getFluidState(pos).is(FluidTags.WATER);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        if (direction == Direction.UP && state.getValue(HALF) == DoubleBlockHalf.LOWER && state.getValue(STALLED)) {
            return state.setValue(STALLED, false);
        }
        return state;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            BlockPos otherPos = state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
            BlockState other = level.getBlockState(otherPos);
            if (other.is(this) && other.getValue(HALF) != state.getValue(HALF)) {
                level.setBlock(otherPos, Blocks.AIR.defaultBlockState(), 35);
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    private static BlockPos lowerPos(BlockPos pos, BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;
    }

    /** Sets the lower block's age and keeps the upper block in sync, placing it once the plant is tall enough. */
    private void grow(Level level, BlockPos lowerPos, BlockState lowerState, int age) {
        level.setBlock(lowerPos, lowerState.setValue(AGE, age), 2);
        BlockPos upperPos = lowerPos.above();
        BlockState upperState = level.getBlockState(upperPos);
        if (upperState.is(this)) {
            level.setBlock(upperPos, upperState.setValue(AGE, age), 2);
        } else if (age >= TALL_AGE && upperState.isAir()) {
            level.setBlock(upperPos, lowerState.setValue(AGE, age).setValue(HALF, DoubleBlockHalf.UPPER).setValue(STALLED, false), 2);
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        BlockPos lowerPos = lowerPos(pos, state);
        BlockState lowerState = level.getBlockState(lowerPos);
        if (!lowerState.is(this) || lowerState.getValue(AGE) < MAX_AGE) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            int count = level.random.nextInt(10) == 0 ? 3 : 2;
            popResource(level, lowerPos, new ItemStack(ModItems.RICE.get(), count));
            BlockPos upperPos = lowerPos.above();
            if (level.getBlockState(upperPos).is(this)) {
                level.levelEvent(BLOCK_BREAK_EVENT, upperPos, Block.getId(level.getBlockState(upperPos)));
                level.setBlock(upperPos, Blocks.AIR.defaultBlockState(), 2);
            }
            level.levelEvent(BLOCK_BREAK_EVENT, lowerPos, Block.getId(lowerState));
            level.setBlock(lowerPos, lowerState.setValue(AGE, 0), 2);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(HALF) != DoubleBlockHalf.LOWER) {
            return;
        }
        int age = state.getValue(AGE);
        if (age >= MAX_AGE) {
            return;
        }
        BlockState above = level.getBlockState(pos.above());
        if (age + 1 >= TALL_AGE && !above.isAir() && !above.is(this)) {
            level.setBlock(pos, state.setValue(STALLED, true), 2);
            return;
        }
        if (level.getRawBrightness(pos.above(), 0) >= 9 && random.nextInt(4) == 0) {
            grow(level, pos, state, age + 1);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        BlockState lowerState = level.getBlockState(lowerPos(pos, state));
        return lowerState.is(this) && lowerState.getValue(AGE) < MAX_AGE && !lowerState.getValue(STALLED);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos lowerPos = lowerPos(pos, state);
        BlockState lowerState = level.getBlockState(lowerPos);
        BlockState above = level.getBlockState(lowerPos.above());
        int maxAge = above.isAir() || above.is(this) ? MAX_AGE : TALL_AGE - 1;
        int age = Math.min(maxAge, lowerState.getValue(AGE) + Mth.nextInt(random, 2, 5));
        grow(level, lowerPos, lowerState, age);
    }
}
