package com.fartura.registry;

import com.fartura.Fartura;
import com.fartura.worldgen.AcaiTreeFeature;
import com.fartura.worldgen.CoconutTreeFeature;
import com.fartura.worldgen.LimeTreeFeature;
import com.fartura.worldgen.MaracujaVineFeature;
import com.fartura.worldgen.OrangeTreeFeature;

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

    public static final DeferredHolder<Feature<?>, AcaiTreeFeature> ACAI_TREE =
            FEATURES.register("acai_tree", () -> new AcaiTreeFeature(NoneFeatureConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>, MaracujaVineFeature> MARACUJA_VINE_FEATURE =
            FEATURES.register("maracuja_vine", () -> new MaracujaVineFeature(NoneFeatureConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>, LimeTreeFeature> LIME_TREE =
            FEATURES.register("lime_tree", () -> new LimeTreeFeature(NoneFeatureConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>, OrangeTreeFeature> ORANGE_TREE =
            FEATURES.register("orange_tree", () -> new OrangeTreeFeature(NoneFeatureConfiguration.CODEC));

    private ModFeatures() {
    }
}
