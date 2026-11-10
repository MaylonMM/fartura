package com.fartura.block;

import com.fartura.block.entity.CuttingBoardBlockEntity;
import com.fartura.item.ColoredKnifeItem;
import com.fartura.registry.ModItems;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CuttingBoardBlock extends BaseEntityBlock {
    public static final MapCodec<CuttingBoardBlock> CODEC = simpleCodec(CuttingBoardBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    // Mesmas caixas de cutting_board.json (tábua + cabo), em coordenadas de modelo de 16 unidades.
    private static final double[][] BOXES = {
            {4, 0, 4, 14, 1, 12},
            {2, 0, 4, 3, 1, 12},
            {3, 0, 4, 4, 1, 6},
            {3, 0, 10, 4, 1, 12}};
    // Uma forma por rotação de 90° (índice 0 = norte, 1 = leste, 2 = sul, 3 = oeste).
    private static final VoxelShape[] SHAPES = buildShapes();

    public CuttingBoardBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    private static VoxelShape[] buildShapes() {
        VoxelShape[] result = new VoxelShape[4];
        for (int rot = 0; rot < 4; rot++) {
            VoxelShape shape = Shapes.empty();
            for (double[] b : BOXES) {
                double[] r = rotateBox(b, rot);
                shape = Shapes.or(shape, Block.box(r[0], r[1], r[2], r[3], r[4], r[5]));
            }
            result[rot] = shape;
        }
        return result;
    }

    // Aplica a mesma rotação horizontal do blockstate (y = rot * 90, sentido horário visto de cima).
    private static double[] rotateBox(double[] b, int rot) {
        double x1 = b[0], z1 = b[2], x2 = b[3], z2 = b[5];
        for (int i = 0; i < rot; i++) {
            double nx1 = 16 - z2, nz1 = x1, nx2 = 16 - z1, nz2 = x2;
            x1 = nx1;
            z1 = nz1;
            x2 = nx2;
            z2 = nz2;
        }
        return new double[] {Math.min(x1, x2), b[1], Math.min(z1, z2), Math.max(x1, x2), b[4], Math.max(z1, z2)};
    }

    public static int rotationIndex(Direction facing) {
        return switch (facing) {
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
            default -> 0;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected MapCodec<CuttingBoardBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CuttingBoardBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack held, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        if (held.isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (!(level.getBlockEntity(pos) instanceof CuttingBoardBlockEntity board)) {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }

        // Corte: faca na mão + coco na tábua gera 1 item por clique (coco verde -> água de coco, coco seco -> coco).
        boolean isKnife = held.is(ModItems.KNIFE.get()) || held.getItem() instanceof ColoredKnifeItem;
        if (isKnife && !board.getStack().isEmpty()) {
            Item result = null;
            if (board.getStack().is(ModItems.GREEN_COCONUT.get())) {
                result = ModItems.COCONUT_WATER.get();
            } else if (board.getStack().is(ModItems.DRY_COCONUT.get())) {
                result = ModItems.COCONUT.get();
            } else if (board.getStack().is(Items.PORKCHOP)) {
                result = ModItems.MINCED_PORK.get();
            } else if (board.getStack().is(Items.BEEF)) {
                result = ModItems.MINCED_BEEF.get();
            }
            if (result != null) {
                if (!level.isClientSide) {
                    ItemStack coconuts = board.getStack();
                    int remaining = coconuts.getCount() - 1;
                    board.setStack(remaining > 0 ? coconuts.copyWithCount(remaining) : ItemStack.EMPTY);
                    // Sai pulando de cima da tábua, em vez de ir direto pro inventário.
                    ItemEntity drop = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                            new ItemStack(result));
                    drop.setDeltaMovement(0.0, 0.25, 0.0);
                    level.addFreshEntity(drop);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        ItemStack current = board.getStack();
        if (current.isEmpty()) {
            int moved = Math.min(held.getCount(), held.getMaxStackSize());
            if (!level.isClientSide) {
                board.setStack(held.copyWithCount(moved));
                held.shrink(moved);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (ItemStack.isSameItemSameComponents(current, held) && current.getCount() < current.getMaxStackSize()) {
            int moved = Math.min(held.getCount(), current.getMaxStackSize() - current.getCount());
            if (!level.isClientSide) {
                board.setStack(current.copyWithCount(current.getCount() + moved));
                held.shrink(moved);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof CuttingBoardBlockEntity board && !board.getStack().isEmpty()) {
            if (!level.isClientSide) {
                ItemStack taken = board.getStack().copy();
                board.setStack(ItemStack.EMPTY);
                player.getInventory().placeItemBackInInventory(taken);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof CuttingBoardBlockEntity board
                && !board.getStack().isEmpty()) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), board.getStack());
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[rotationIndex(state.getValue(FACING))];
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[rotationIndex(state.getValue(FACING))];
    }
}
