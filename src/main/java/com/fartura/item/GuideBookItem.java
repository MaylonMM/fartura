package com.fartura.item;

import com.fartura.Fartura;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
//? if neoforge {
import net.neoforged.fml.ModList;
import vazkii.patchouli.api.PatchouliAPI;
//?} else {
import net.minecraftforge.fml.ModList;
//?}

/**
 * Opens the Fartura guide book. Patchouli is optional, so without it the player only gets a message.
 */
public class GuideBookItem extends Item {
    public static final ResourceLocation BOOK_ID = ResourceLocation.fromNamespaceAndPath(Fartura.MODID, "guide");

    public GuideBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!ModList.get().isLoaded("patchouli")) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable("message.fartura.guide_requires_patchouli"), true);
            }
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
        //? if neoforge {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            PatchouliAPI.get().openBookGUI(serverPlayer, BOOK_ID);
        }
        //?}
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
    }
}
