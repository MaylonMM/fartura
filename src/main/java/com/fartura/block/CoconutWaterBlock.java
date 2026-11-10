package com.fartura.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Coconut water placed in the world. Each click drinks one level, giving half of the item's nutrition.
 */
public class CoconutWaterBlock extends Block {
    public static final MapCodec<CoconutWaterBlock> CODEC = simpleCodec(CoconutWaterBlock::new);

    private static final int MAX_LEVEL = 2;
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 1, MAX_LEVEL);
    private static final int NUTRITION_PER_LEVEL = 2;
    private static final float SATURATION_PER_LEVEL = 1.2F;

    // 6x6x6 px cube, centered horizontally and resting on the floor.
    private static final VoxelShape SHAPE = Block.box(5, 0, 5, 11, 6, 11);

    public CoconutWaterBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(LEVEL, MAX_LEVEL));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
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
        if (!player.canEat(true)) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        player.getFoodData().eat(NUTRITION_PER_LEVEL, SATURATION_PER_LEVEL);
        level.playSound(null, pos, SoundEvents.GENERIC_DRINK, SoundSource.BLOCKS, 1.0F, 1.0F);

        int remaining = state.getValue(LEVEL) - 1;
        if (remaining <= 0) {
            level.destroyBlock(pos, false);
        } else {
            level.setBlock(pos, state.setValue(LEVEL, remaining), Block.UPDATE_ALL);
        }
        level.gameEvent(player, GameEvent.EAT, pos);
        return InteractionResult.SUCCESS;
    }
}
