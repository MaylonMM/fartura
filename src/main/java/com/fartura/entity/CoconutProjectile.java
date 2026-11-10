package com.fartura.entity;

import com.fartura.Fartura;
import com.fartura.registry.ModEntities;
import com.fartura.registry.ModItems;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

/**
 * Thrown coconut. Breaks in half on impact and deals a small hit to mobs.
 */
public class CoconutProjectile extends ThrowableItemProjectile {
    private static final float MOB_DAMAGE = 2.0F;

    public CoconutProjectile(EntityType<? extends CoconutProjectile> type, Level level) {
        super(type, level);
    }

    public CoconutProjectile(EntityType<? extends CoconutProjectile> type, LivingEntity shooter, Level level) {
        super(type, shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.DRY_COCONUT.get();
    }

    public static void throwFrom(Level level, Player player, ItemStack stack) {
        CoconutProjectile projectile = new CoconutProjectile(ModEntities.COCONUT_PROJECTILE.get(), player, level);
        projectile.setItem(stack);
        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
        level.addFreshEntity(projectile);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!level().isClientSide) {
            result.getEntity().hurt(damageSources().thrown(this, getOwner()), MOB_DAMAGE);
            if (getItem().is(ModItems.DRY_COCONUT.get()) && getOwner() instanceof ServerPlayer player
                    && level().getServer() != null) {
                AdvancementHolder advancement = level().getServer().getAdvancements()
                        .get(ResourceLocation.fromNamespaceAndPath(Fartura.MODID, "hard_head"));
                if (advancement != null) {
                    player.getAdvancements().award(advancement, "hit_someone");
                }
            }
            breakApart();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!level().isClientSide) {
            breakApart();
        }
    }

    private void breakApart() {
        if (level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, getItem()),
                    getX(), getY(), getZ(), 8, 0.1D, 0.1D, 0.1D, 0.05D);
            if (getItem().is(ModItems.GREEN_COCONUT.get())) {
                ItemEntity juice = new ItemEntity(level(), getX(), getY(), getZ(),
                        new ItemStack(ModItems.COCONUT_WATER.get()));
                level().addFreshEntity(juice);
            } else if (getItem().is(ModItems.DRY_COCONUT.get())) {
                ItemEntity coconut = new ItemEntity(level(), getX(), getY(), getZ(),
                        new ItemStack(ModItems.COCONUT.get()));
                level().addFreshEntity(coconut);
            }
        }
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.WOOD_BREAK, SoundSource.NEUTRAL, 0.8F, 1.0F);
        discard();
    }
}
