package com.fartura.worldgen;

import com.fartura.block.CoconutLeavesBlock;
import com.fartura.registry.ModBlocks;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Coconut palm: a slightly crooked jungle log trunk with a mangrove leaf canopy.
 * Some canopy tips are coconut leaves. Needs sand or dirt below.
 */
public class CoconutTreeFeature extends Feature<NoneFeatureConfiguration> {
    public CoconutTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        return place(context.level(), context.origin(), context.random());
    }

    public static boolean place(LevelAccessor level, BlockPos origin, RandomSource random) {
        BlockState ground = level.getBlockState(origin.below());
        if (!ground.is(BlockTags.SAND) && !ground.is(BlockTags.DIRT)) {
            return false;
        }

        int height = 7 + random.nextInt(4);
        Direction lean = Direction.from2DDataValue(random.nextInt(4));
        List<BlockPos> trunk = new ArrayList<>();
        BlockPos cursor = origin;
        int leanSteps = 0;
        for (int i = 0; i < height; i++) {
            trunk.add(cursor);
            if (i >= 2 && i < height - 2 && leanSteps < 3 && random.nextInt(3) == 0) {
                cursor = cursor.relative(lean);
                trunk.add(cursor);
                leanSteps++;
            }
            if (i < height - 1) {
                cursor = cursor.above();
            }
        }

        for (BlockPos pos : trunk) {
            if (level.isOutsideBuildHeight(pos) || !level.getBlockState(pos).canBeReplaced()) {
                return false;
            }
        }
        for (BlockPos pos : trunk) {
            level.setBlock(pos, Blocks.JUNGLE_LOG.defaultBlockState(), 2);
        }

        BlockPos crown = cursor;
        // Variant B is disabled until it is corrected; re-enable with random.nextBoolean() ? CANOPY_A : CANOPY_B.
        List<CanopyLayer> layers = CANOPY_A;
        for (CanopyLayer layer : layers) {
            placeLayer(level, crown, layer, random);
        }
        placeMangroveLeaf(level, crown.above());
        updateLeafDistances(level, trunk, crown);
        return true;
    }

    /**
     * Sets each leaf's DISTANCE the same way vanilla does: a flood fill from the trunk logs through connected leaves.
     * Connected leaves are capped at 6 so they never decay while the trunk exists; unconnected leaves get 7.
     */
    private static void updateLeafDistances(LevelAccessor level, List<BlockPos> trunk, BlockPos crown) {
        Set<BlockPos> leaves = new HashSet<>();
        for (int dx = -4; dx <= 4; dx++) {
            for (int dy = -4; dy <= 3; dy++) {
                for (int dz = -4; dz <= 4; dz++) {
                    BlockPos pos = crown.offset(dx, dy, dz);
                    BlockState state = level.getBlockState(pos);
                    if (state.is(Blocks.MANGROVE_LEAVES) || state.is(ModBlocks.COCONUT_LEAVES.get())) {
                        leaves.add(pos.immutable());
                    }
                }
            }
        }

        Map<BlockPos, Integer> distances = new HashMap<>();
        Deque<BlockPos> queue = new ArrayDeque<>();
        for (BlockPos log : trunk) {
            distances.put(log, 0);
            queue.add(log);
        }
        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            int distance = distances.get(current);
            for (Direction direction : Direction.values()) {
                BlockPos next = current.relative(direction);
                if (leaves.contains(next) && !distances.containsKey(next)) {
                    distances.put(next, distance + 1);
                    queue.add(next);
                }
            }
        }

        for (BlockPos pos : leaves) {
            BlockState state = level.getBlockState(pos);
            int distance = distances.containsKey(pos) ? Math.min(distances.get(pos), 6) : 7;
            level.setBlock(pos, state.setValue(LeavesBlock.DISTANCE, distance), 2);
        }
    }

    private record CanopyLayer(int dy, int[][] offsets, boolean fronds) {}

    private static final int[][] CARDINAL_OUTER = {{0, -3}, {-3, 0}, {3, 0}, {0, 3}};
    private static final int[][] RING_OUTER = {{0, -3}, {3, -2}, {2, 3}, {-3, 2}, {-2, -3}, {-3, 0}, {3, 0}, {0, 3}};
    private static final int[][] TIPS = {{0, -3}, {-3, 0}, {3, 0}, {0, 3}, {-2, -2}, {2, -2}, {-2, 2}, {2, 2}};
    private static final int[][] CARDINAL_MID = {{0, -2}, {-2, 0}, {2, 0}, {0, 2}};
    private static final int[][] CARDINAL_INNER = {{0, -1}, {-1, 0}, {1, 0}, {0, 1}};
    private static final int[][] FULL_INNER = {{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};

    private static final int[][] MID_OUTER_FRONDS = {{0, -3}, {3, -2}, {-3, 0}, {3, 0}, {-3, 2}, {0, 3}, {2, 3}};
    private static final int[][] MID_OUTER_JUNGLE = {{-2, -3}, {-2, -2}, {0, -2}, {2, -2}, {-2, 0}, {2, 0}, {-2, 2}, {2, 2}, {2, 1}, {2, -1}, {-2, -1}, {-2, 1}, {-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}, {0, 2}};
    private static final int[][] CENTER_FRONDS = {{-2, -2}, {2, -2}, {-2, 2}, {2, 2}};
    private static final int[][] CENTER_JUNGLE = {{-1, -2}, {0, -2}, {-1, -1}, {0, -1}, {1, -1}, {2, -1}, {-2, 0}, {-1, 0}, {1, 0}, {2, 0}, {-2, 1}, {-1, 1}, {0, 1}, {1, 1}, {0, 2}, {1, 2}};

    /** Variant A: the canopy from the layer-by-layer reference photos (layer 1 = bottom, dy -3). */
    private static final List<CanopyLayer> CANOPY_A = List.of(
            new CanopyLayer(-3, CARDINAL_OUTER, true),
            new CanopyLayer(-2, RING_OUTER, true),
            new CanopyLayer(-2, CARDINAL_INNER, false),
            new CanopyLayer(-1, MID_OUTER_FRONDS, true),
            new CanopyLayer(-1, MID_OUTER_JUNGLE, false),
            new CanopyLayer(0, CENTER_FRONDS, true),
            new CanopyLayer(0, CENTER_JUNGLE, false),
            new CanopyLayer(1, CARDINAL_INNER, false)
    );

    /** Variant B: the reshaped canopy from the reference photos. */
    private static final List<CanopyLayer> CANOPY_B = List.of(
            new CanopyLayer(-3, CARDINAL_OUTER, true),
            new CanopyLayer(-2, TIPS, true),
            new CanopyLayer(-1, TIPS, true),
            new CanopyLayer(-1, CARDINAL_INNER, false),
            new CanopyLayer(0, FULL_INNER, false),
            new CanopyLayer(0, CARDINAL_MID, true),
            new CanopyLayer(1, CARDINAL_INNER, false)
    );

    private static void placeLayer(LevelAccessor level, BlockPos crown, CanopyLayer layer, RandomSource random) {
        for (int[] offset : layer.offsets()) {
            BlockPos pos = crown.offset(offset[0], layer.dy(), offset[1]);
            if (layer.fronds()) {
                placeFrond(level, pos, random);
            } else {
                placeMangroveLeaf(level, pos);
            }
        }
    }

    private static void placeMangroveLeaf(LevelAccessor level, BlockPos pos) {
        if (level.isOutsideBuildHeight(pos) || !level.getBlockState(pos).canBeReplaced()) {
            return;
        }
        level.setBlock(pos, Blocks.MANGROVE_LEAVES.defaultBlockState(), 2);
    }

    private static void placeFrond(LevelAccessor level, BlockPos pos, RandomSource random) {
        if (level.isOutsideBuildHeight(pos) || !level.getBlockState(pos).canBeReplaced()) {
            return;
        }
        if (random.nextInt(4) == 0) {
            BlockState state = ModBlocks.COCONUT_LEAVES.get().defaultBlockState()
                    .setValue(CoconutLeavesBlock.AGE, random.nextInt(CoconutLeavesBlock.MAX_AGE + 1));
            level.setBlock(pos, state, 2);
        } else {
            placeMangroveLeaf(level, pos);
        }
    }
}
