package com.fartura.item;

import com.fartura.Fartura;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Maçã do Amor: awards the "love in the air" advancement the first time it is eaten.
 */
public class CandiedAppleItem extends Item {
    public CandiedAppleItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (entity instanceof ServerPlayer serverPlayer && serverPlayer.getServer() != null) {
            AdvancementHolder advancement = serverPlayer.getServer().getAdvancements()
                    .get(ResourceLocation.fromNamespaceAndPath(Fartura.MODID, "love_in_the_air"));
            if (advancement != null) {
                serverPlayer.getAdvancements().award(advancement, "ate_candied_apple");
            }
        }
        return result;
    }
}
