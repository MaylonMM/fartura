package com.fartura.registry;

import com.fartura.Fartura;
import com.fartura.entity.CoconutProjectile;
import com.fartura.entity.PartyPopperProjectile;
import com.fartura.entity.CobblestoneFlakeProjectile;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
//? if neoforge {
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
//?} else {
import com.fartura.compat.registry.DeferredHolder;
import com.fartura.compat.registry.DeferredRegister;
//?}

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

    public static final DeferredHolder<EntityType<?>, EntityType<CobblestoneFlakeProjectile>> COBBLESTONE_FLAKE_PROJECTILE =
            ENTITY_TYPES.register("cobblestone_flake_projectile", () -> EntityType.Builder
                    .<CobblestoneFlakeProjectile>of(CobblestoneFlakeProjectile::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("cobblestone_flake_projectile"));

    private ModEntities() {
    }
}
