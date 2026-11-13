package com.fartura.worldgen;

import com.fartura.block.AcaiPodBlock;
import com.fartura.registry.ModBlocks;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds açaí clusters to an existing jungle tree near the origin, without generating a new tree.
 * Finds the trunk log directly under the canopy (topmost non-leaf log with leaves above) and hangs
 * a cluster on each free horizontal side with a 75% chance. Only works inside the origin chunk.
 */
public class AcaiTreeFeature extends Feature<NoneFeatureConfiguration> {
    private static final int SEARCH_RADIUS = 4;
    private static final int SEARCH_DEPTH = 24;

    public AcaiTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        return place(context.level(), context.origin(), context.random());
    }

    public static boolean place(LevelAccessor level, BlockPos origin, RandomSource random) {
        int chunkX = origin.getX() >> 4;
        int chunkZ = origin.getZ() >> 4;

        List<BlockPos> trunkTops = new ArrayList<>();
        for (int dx = -SEARCH_RADIUS; dx <= SEARCH_RADIUS; dx++) {
            for (int dz = -SEARCH_RADIUS; dz <= SEARCH_RADIUS; dz++) {
                int x = origin.getX() + dx;
                int z = origin.getZ() + dz;
                if ((x >> 4) != chunkX || (z >> 4) != chunkZ) {
                    continue;
                }
                // Scan down from the canopy top to the first jungle log with leaves directly above it.
                int top = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
                int minY = Math.max(level.getMinBuildHeight(), top - SEARCH_DEPTH);
                for (int y = top; y >= minY; y--) {
                    BlockPos log = new BlockPos(x, y, z);
                    if (level.getBlockState(log).is(BlockTags.JUNGLE_LOGS)
                            && level.getBlockState(log.above()).is(BlockTags.LEAVES)) {
                        trunkTops.add(log);
                        break;
                    }
                }
            }
        }
        if (trunkTops.isEmpty()) {
            return false;
        }

        // Try trunks in random order until one has a free side, so one blocked trunk doesn't cancel the feature.
        java.util.Collections.shuffle(trunkTops, new java.util.Random(random.nextLong()));
        for (BlockPos log : trunkTops) {
            boolean placed = false;
            for (Direction side : Direction.Plane.HORIZONTAL) {
                BlockPos podPos = log.relative(side);
                if ((podPos.getX() >> 4) != chunkX || (podPos.getZ() >> 4) != chunkZ) {
                    continue;
                }
                BlockState existing = level.getBlockState(podPos);
                if (!existing.canBeReplaced() || random.nextFloat() >= 0.75F) {
                    continue;
                }
                BlockState pod = ModBlocks.ACAI_POD.get().defaultBlockState()
                        .setValue(AcaiPodBlock.FACING, side.getOpposite())
                        .setValue(AcaiPodBlock.AGE, random.nextInt(AcaiPodBlock.MAX_AGE + 1));
                level.setBlock(podPos, pod, 2);
                placed = true;
            }
            if (placed) {
                return true;
            }
        }
        return false;
    }
}
