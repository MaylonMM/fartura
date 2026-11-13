package com.fartura.worldgen;

import com.fartura.block.MaracujaVineBlock;
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

/**
 * Adds maracujá vines to an existing jungle tree near the origin, without generating a new tree.
 * Finds the trunk log directly under the canopy and hangs a vine on a free horizontal side.
 * The vine gets a random growth stage, so any stage can spawn, like cocoa. Only works inside the origin chunk.
 */
public class MaracujaVineFeature extends Feature<NoneFeatureConfiguration> {
    private static final int SEARCH_RADIUS = 4;
    private static final int SEARCH_DEPTH = 24;

    public MaracujaVineFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        LevelAccessor level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        int chunkX = origin.getX() >> 4;
        int chunkZ = origin.getZ() >> 4;

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
                        if (tryPlace(level, log, random, chunkX, chunkZ)) {
                            return true;
                        }
                        break;
                    }
                }
            }
        }
        return false;
    }

    private static boolean tryPlace(LevelAccessor level, BlockPos log, RandomSource random, int chunkX, int chunkZ) {
        for (Direction side : Direction.Plane.HORIZONTAL) {
            BlockPos vinePos = log.relative(side);
            if ((vinePos.getX() >> 4) != chunkX || (vinePos.getZ() >> 4) != chunkZ) {
                continue;
            }
            BlockState existing = level.getBlockState(vinePos);
            if (!existing.canBeReplaced() || random.nextFloat() >= 0.5F) {
                continue;
            }
            BlockState vine = ModBlocks.MARACUJA_VINE.get().defaultBlockState()
                    .setValue(MaracujaVineBlock.FACING, side.getOpposite())
                    .setValue(MaracujaVineBlock.AGE, random.nextInt(MaracujaVineBlock.MAX_AGE + 1));
            level.setBlock(vinePos, vine, 2);
            return true;
        }
        return false;
    }
}
