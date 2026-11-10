package com.fartura.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class CarrotCakeBlock extends CornCakeBlock {
    public static final MapCodec<CarrotCakeBlock> CODEC = simpleCodec(CarrotCakeBlock::new);

    public CarrotCakeBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends CornCakeBlock> codec() {
        return CODEC;
    }
}
