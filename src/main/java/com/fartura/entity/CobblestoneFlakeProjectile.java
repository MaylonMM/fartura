package com.fartura.entity;

import com.fartura.registry.ModEntities;
import com.fartura.registry.ModItems;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Thrown cobblestone flake. Deals a small hit to mobs and then disappears.
 */
public class CobblestoneFlakeProjectile extends ThrowableItemProjectile {
    private static final float MOB_DAMAGE = 2.0F;

    public CobblestoneFlakeProjectile(EntityType<? extends CobblestoneFlakeProjectile> type, Level level) {
        super(type, level);
    }

    public CobblestoneFlakeProjectile(EntityType<? extends CobblestoneFlakeProjectile> type, LivingEntity shooter, Level level) {
        super(type, shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.COBBLESTONE_FLAKE.get();
    }

    public static void throwFrom(Level level, Player player, ItemStack stack) {
        CobblestoneFlakeProjectile projectile = new CobblestoneFlakeProjectile(ModEntities.COBBLESTONE_FLAKE_PROJECTILE.get(), player, level);
        projectile.setItem(stack);
        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
        level.addFreshEntity(projectile);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!level().isClientSide) {
            result.getEntity().hurt(damageSources().thrown(this, getOwner()), MOB_DAMAGE);
            spawnCobblestoneImpact(result.getLocation());
            discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!level().isClientSide) {
            spawnCobblestoneImpact(result.getLocation());
            discard();
        }
    }

    private void spawnCobblestoneImpact(Vec3 pos) {
        BlockState stone = Blocks.COBBLESTONE.defaultBlockState();
        ((ServerLevel) level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, stone),
                pos.x, pos.y, pos.z, 8, 0.1, 0.1, 0.1, 0.05);
        level().playSound(null, pos.x, pos.y, pos.z, SoundType.STONE.getHitSound(),
                SoundSource.BLOCKS, 1.0F, 1.0F);
    }
}
