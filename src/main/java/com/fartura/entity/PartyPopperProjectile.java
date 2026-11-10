package com.fartura.entity;

import com.fartura.registry.ModEntities;
import com.fartura.registry.ModItems;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

/**
 * Estalinho de festa. Não causa dano, mas faz o mob reagir como se tivesse sido atingido, com um estalo ao colidir.
 */
public class PartyPopperProjectile extends ThrowableItemProjectile {
    public PartyPopperProjectile(EntityType<? extends PartyPopperProjectile> type, Level level) {
        super(type, level);
    }

    public PartyPopperProjectile(EntityType<? extends PartyPopperProjectile> type, LivingEntity shooter, Level level) {
        super(type, shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.PARTY_POPPER.get();
    }

    public static void throwFrom(Level level, Player player, ItemStack stack) {
        PartyPopperProjectile projectile = new PartyPopperProjectile(ModEntities.PARTY_POPPER_PROJECTILE.get(), player, level);
        projectile.setItem(stack);
        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
        level.addFreshEntity(projectile);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!level().isClientSide) {
            // Dano zero: aplica só a animação de dano, igual à bola de neve.
            result.getEntity().hurt(damageSources().thrown(this, getOwner()), 0.0F);
            pop();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!level().isClientSide) {
            pop();
        }
    }

    private void pop() {
        if (level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.FIREWORK, getX(), getY(), getZ(), 6, 0.1D, 0.1D, 0.1D, 0.05D);
        }
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.FIREWORK_ROCKET_TWINKLE,
                SoundSource.NEUTRAL, 0.6F, 1.4F);
        discard();
    }
}
