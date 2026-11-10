package com.fartura.registry;

import com.fartura.Fartura;
import com.fartura.entity.CoconutProjectile;
import com.fartura.entity.PartyPopperProjectile;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, Fartura.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<CoconutProjectile>> COCONUT_PROJECTILE =
            ENTITY_TYPES.register("coconut_projectile", () -> EntityType.Builder
                    .<CoconutProjectile>of(CoconutProjectile::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("coconut_projectile"));

    public static final DeferredHolder<EntityType<?>, EntityType<PartyPopperProjectile>> PARTY_POPPER_PROJECTILE =
            ENTITY_TYPES.register("party_popper_projectile", () -> EntityType.Builder
                    .<PartyPopperProjectile>of(PartyPopperProjectile::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("party_popper_projectile"));

    private ModEntities() {
    }
}
