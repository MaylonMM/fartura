package com.fartura.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CornCakeBlock extends Block {
    public static final MapCodec<CornCakeBlock> CODEC = simpleCodec(CornCakeBlock::new);
    public static final IntegerProperty BITES = BlockStateProperties.BITES;

    // Same as the vanilla cake: 7 slices, the last bite removes the block.
    private static final int MAX_BITES = 6;
    private static final int NUTRITION_PER_BITE = 1;
    private static final float SATURATION_PER_BITE = 0.1f;
    private static final VoxelShape[] SHAPE_BY_BITE = createShapes();

    public CornCakeBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(BITES, 0));
    }

    private static VoxelShape[] createShapes() {
        VoxelShape[] shapes = new VoxelShape[MAX_BITES + 1];
        for (int bites = 0; bites <= MAX_BITES; bites++) {
            shapes[bites] = Block.box(1 + 2 * bites, 0, 1, 15, 8, 15);
        }
        return shapes;
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BITES);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_BITE[state.getValue(BITES)];
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                     LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hitResult) {
        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        eatSlice(level, pos, state, player);
        return InteractionResult.SUCCESS;
    }

    private static void eatSlice(Level level, BlockPos pos, BlockState state, Player player) {
        player.getFoodData().eat(NUTRITION_PER_BITE, SATURATION_PER_BITE);
        player.awardStat(Stats.EAT_CAKE_SLICE);
        level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.BLOCKS, 1.0f, 1.0f);

        int bites = state.getValue(BITES) + 1;
        if (bites > MAX_BITES) {
            level.removeBlock(pos, false);
        } else {
            level.setBlock(pos, state.setValue(BITES, bites), Block.UPDATE_ALL);
        }
        level.gameEvent(player, GameEvent.EAT, pos);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return (MAX_BITES + 1 - state.getValue(BITES)) * 2;
    }
}
