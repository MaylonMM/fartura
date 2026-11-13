package com.fartura.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class MaracujaCakeBlock extends CornCakeBlock {
    public static final MapCodec<MaracujaCakeBlock> CODEC = simpleCodec(MaracujaCakeBlock::new);

    public MaracujaCakeBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends CornCakeBlock> codec() {
        return CODEC;
    }
}
