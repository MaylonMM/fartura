package com.fartura.registry;

import com.fartura.Fartura;
import com.fartura.worldgen.CoconutTreeFeature;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, Fartura.MODID);

    public static final DeferredHolder<Feature<?>, CoconutTreeFeature> COCONUT_TREE =
            FEATURES.register("coconut_tree", () -> new CoconutTreeFeature(NoneFeatureConfiguration.CODEC));

    private ModFeatures() {
    }
}
