package com.fartura.event;

import com.fartura.Fartura;
import com.fartura.item.ColoredKnifeItem;
import com.fartura.item.ColoredKnifeItem.Perk;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;

@EventBusSubscriber(modid = Fartura.MODID)
public final class KnifeEvents {
    private static final int GLOWING_TICKS = 200;
    private static final int SPEED_TICKS = 200;
    private static final int REGEN_TICKS = 60;
    private static final float EMERALD_DOUBLE_DROP_CHANCE = 0.2F;
    private static final float LAPIS_XP_MULTIPLIER = 1.2F;

    private KnifeEvents() {
    }

    @SubscribeEvent
    public static void onDamagePost(LivingDamageEvent.Post event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }
        if (!(event.getSource().getDirectEntity() instanceof LivingEntity attacker)) {
            return;
        }
        if (heldPerk(attacker) == Perk.GLOWSTONE) {
            event.getEntity().addEffect(new MobEffectInstance(MobEffects.GLOWING, GLOWING_TICKS));
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }
        if (!(event.getSource().getDirectEntity() instanceof Player player)) {
            return;
        }
        Perk perk = heldPerk(player);
        if (perk == Perk.REDSTONE) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, SPEED_TICKS, 0));
        } else if (perk == Perk.QUARTZ) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, REGEN_TICKS, 0));
        }
    }

    @SubscribeEvent
    public static void onDrops(LivingDropsEvent event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }
        if (!(event.getSource().getDirectEntity() instanceof LivingEntity attacker)
                || heldPerk(attacker) != Perk.EMERALD) {
            return;
        }
        if (attacker.getRandom().nextFloat() >= EMERALD_DOUBLE_DROP_CHANCE) {
            return;
        }
        List<ItemEntity> copies = new ArrayList<>();
        for (ItemEntity drop : event.getDrops()) {
            copies.add(new ItemEntity(drop.level(), drop.getX(), drop.getY(), drop.getZ(), drop.getItem().copy()));
        }
        event.getDrops().addAll(copies);
    }

    @SubscribeEvent
    public static void onExperienceDrop(LivingExperienceDropEvent event) {
        Player player = event.getAttackingPlayer();
        if (player == null || heldPerk(player) != Perk.LAPIS) {
            return;
        }
        event.setDroppedExperience(Math.round(event.getDroppedExperience() * LAPIS_XP_MULTIPLIER));
    }

    private static Perk heldPerk(LivingEntity entity) {
        if (entity.getMainHandItem().getItem() instanceof ColoredKnifeItem knife) {
            return knife.getPerk();
        }
        return null;
    }
}
