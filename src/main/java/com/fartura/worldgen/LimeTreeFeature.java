package com.fartura.worldgen;

import com.fartura.block.LimePodBlock;
import com.fartura.registry.ModBlocks;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Turns an existing oak tree near the origin into a lime tree by hanging a guaranteed lime under its leaves,
 * with a 50% chance of a second one. Trees already marked this way are skipped.
 * Other crops must call {@link #isLimeTree} to avoid reusing these trees.
 * Trees marked by {@link OrangeTreeFeature} are skipped as well.
 * Only works inside the origin chunk.
 */
public class LimeTreeFeature extends Feature<NoneFeatureConfiguration> {
    private static final int SEARCH_RADIUS = 4;
    private static final int SEARCH_DEPTH = 24;
    private static final int LEAF_RADIUS = 3;
    private static final int LEAF_DEPTH = 4;
    private static final int MAX_PODS_PER_TREE = 2;

    public LimeTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        return place(context.level(), context.origin(), context.random());
    }

    /** Returns true if a lime pod already hangs under this trunk's canopy, marking it as a lime tree. */
    public static boolean isLimeTree(LevelAccessor level, BlockPos trunk) {
        for (int dx = -LEAF_RADIUS; dx <= LEAF_RADIUS; dx++) {
            for (int dz = -LEAF_RADIUS; dz <= LEAF_RADIUS; dz++) {
                for (int dy = -LEAF_DEPTH - 1; dy <= 0; dy++) {
                    if (level.getBlockState(trunk.offset(dx, dy, dz)).is(ModBlocks.LIME_POD.get())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /** Picks a random growth stage so pods appear at every stage, not only the first one. */
    private static BlockState randomPod(RandomSource random) {
        return ModBlocks.LIME_POD.get().defaultBlockState()
                .setValue(LimePodBlock.AGE, random.nextInt(LimePodBlock.MAX_AGE + 1));
    }

    public static boolean place(LevelAccessor level, BlockPos origin, RandomSource random) {
        int chunkX = origin.getX() >> 4;
        int chunkZ = origin.getZ() >> 4;

        List<BlockPos> trunks = new ArrayList<>();
        for (int dx = -SEARCH_RADIUS; dx <= SEARCH_RADIUS; dx++) {
            for (int dz = -SEARCH_RADIUS; dz <= SEARCH_RADIUS; dz++) {
                int x = origin.getX() + dx;
                int z = origin.getZ() + dz;
                if ((x >> 4) != chunkX || (z >> 4) != chunkZ) {
                    continue;
                }
                int top = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
                int minY = Math.max(level.getMinBuildHeight(), top - SEARCH_DEPTH);
                for (int y = top; y >= minY; y--) {
                    BlockPos log = new BlockPos(x, y, z);
                    if (level.getBlockState(log).is(Blocks.OAK_LOG)
                            && level.getBlockState(log.above()).is(Blocks.OAK_LEAVES)) {
                        trunks.add(log);
                        break;
                    }
                }
            }
        }
        if (trunks.isEmpty()) {
            return false;
        }

        // Try trunks in random order until one yields a spot, so one bare tree doesn't cancel the feature.
        Collections.shuffle(trunks, new java.util.Random(random.nextLong()));
        for (BlockPos trunk : trunks) {
            if (isLimeTree(level, trunk) || OrangeTreeFeature.isOrangeTree(level, trunk)) {
                continue;
            }
            List<BlockPos> spots = new ArrayList<>();
            for (int dx = -LEAF_RADIUS; dx <= LEAF_RADIUS; dx++) {
                for (int dz = -LEAF_RADIUS; dz <= LEAF_RADIUS; dz++) {
                    for (int dy = -LEAF_DEPTH; dy <= 0; dy++) {
                        BlockPos leaves = trunk.offset(dx, dy, dz);
                        BlockPos pod = leaves.below();
                        if ((pod.getX() >> 4) != chunkX || (pod.getZ() >> 4) != chunkZ) {
                            continue;
                        }
                        if (level.getBlockState(leaves).is(Blocks.OAK_LEAVES)
                                && level.getBlockState(pod).canBeReplaced()) {
                            spots.add(pod);
                        }
                    }
                }
            }
            if (spots.isEmpty()) {
                continue;
            }

            Collections.shuffle(spots, new java.util.Random(random.nextLong()));
            level.setBlock(spots.get(0), randomPod(random), 2);
            int placed = 1;
            for (BlockPos extra : spots.subList(1, spots.size())) {
                if (placed >= MAX_PODS_PER_TREE) {
                    break;
                }
                if (random.nextBoolean()) {
                    level.setBlock(extra, randomPod(random), 2);
                    placed++;
                }
            }
            return true;
        }
        return false;
    }
}
