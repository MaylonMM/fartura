package com.fartura.item;

import java.util.List;
import java.util.Locale;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;

/**
 * Diamond-tier knife with a mineral-specific perk, applied by {@link com.fartura.event.KnifeEvents}.
 */
public class ColoredKnifeItem extends SwordItem {
    public enum Perk {
        REDSTONE,
        AMETHYST,
        EMERALD,
        GLOWSTONE,
        QUARTZ,
        LAPIS
    }

    private final Perk perk;

    public ColoredKnifeItem(Perk perk, int attackDamage) {
        super(Tiers.DIAMOND, new Item.Properties()
                .attributes(SwordItem.createAttributes(Tiers.DIAMOND, attackDamage, -2.4F)));
        this.perk = perk;
    }

    public Perk getPerk() {
        return perk;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip,
            TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.fartura.knife." + perk.name().toLowerCase(Locale.ROOT))
                .withStyle(ChatFormatting.GRAY));
    }
}
