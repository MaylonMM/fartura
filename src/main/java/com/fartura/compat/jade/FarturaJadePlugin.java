package com.fartura.compat.jade;

import java.util.ArrayList;
import java.util.List;

import com.fartura.block.BarbecueGrillBlock;
import com.fartura.block.FrigideiraBlock;
import com.fartura.block.entity.BarbecueGrillBlockEntity;
import com.fartura.block.entity.FrigideiraBlockEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.ITooltip;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

@WailaPlugin
public class FarturaJadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(GrillProvider.INSTANCE, BarbecueGrillBlock.class);
        registration.registerBlockDataProvider(FrigideiraProvider.INSTANCE, FrigideiraBlock.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(GrillProvider.INSTANCE, BarbecueGrillBlock.class);
        registration.registerBlockComponent(FrigideiraProvider.INSTANCE, FrigideiraBlock.class);
    }

    private static final class FrigideiraProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
        static final FrigideiraProvider INSTANCE = new FrigideiraProvider();
        private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath("fartura", "iron_frying_pan");
        private static final String KEY = "RemainingCookingTicks";

        @Override
        public ResourceLocation getUid() {
            return UID;
        }

        @Override
        public void appendServerData(CompoundTag data, BlockAccessor accessor) {
            BlockEntity blockEntity = accessor.getBlockEntity();
            if (blockEntity instanceof FrigideiraBlockEntity frigideira) {
                data.putIntArray(KEY, frigideira.getRemainingCookingTicks());
            }
        }

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            if (!accessor.getServerData().contains(KEY)) {
                return;
            }
            BlockEntity blockEntity = accessor.getBlockEntity();
            if (!(blockEntity instanceof FrigideiraBlockEntity frigideira)) {
                return;
            }
            int[] remaining = accessor.getServerData().getIntArray(KEY);
            IElementHelper helper = IElementHelper.get();
            List<IElement> row = new ArrayList<>();
            for (int i = 0; i < remaining.length && i < FrigideiraBlockEntity.SLOTS; i++) {
                if (remaining[i] < 0) {
                    continue;
                }
                ItemStack stack = frigideira.getInventory().getStackInSlot(i);
                int seconds = (remaining[i] + 19) / 20;
                row.add(helper.smallItem(stack));
                row.add(helper.text(Component.literal(seconds + "s")));
                row.add(helper.spacer(4, 0));
            }
            if (!row.isEmpty()) {
                tooltip.add(row);
            }
        }
    }

    private static final class GrillProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
        static final GrillProvider INSTANCE = new GrillProvider();
        private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath("fartura", "barbecue_grill");
        private static final String KEY = "RemainingCookingTicks";

        @Override
        public ResourceLocation getUid() {
            return UID;
        }

        @Override
        public void appendServerData(CompoundTag data, BlockAccessor accessor) {
            BlockEntity blockEntity = accessor.getBlockEntity();
            if (blockEntity instanceof BarbecueGrillBlockEntity grill) {
                data.putIntArray(KEY, grill.getRemainingCookingTicks());
            }
        }

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            if (!accessor.getServerData().contains(KEY)) {
                return;
            }
            BlockEntity blockEntity = accessor.getBlockEntity();
            if (!(blockEntity instanceof BarbecueGrillBlockEntity grill)) {
                return;
            }
            int[] remaining = accessor.getServerData().getIntArray(KEY);
            IElementHelper helper = IElementHelper.get();
            List<IElement> row = new ArrayList<>();
            for (int i = 0; i < remaining.length && i < BarbecueGrillBlockEntity.GRILL_SLOTS; i++) {
                if (remaining[i] < 0) {
                    continue;
                }
                ItemStack stack = grill.getInventory().getStackInSlot(BarbecueGrillBlockEntity.FIRST_GRILL_SLOT + i);
                if (stack.isEmpty()) {
                    continue;
                }
                int seconds = (remaining[i] + 19) / 20;
                row.add(helper.smallItem(stack));
                row.add(helper.text(Component.literal(seconds + "s")));
                row.add(helper.spacer(4, 0));
            }
            if (!row.isEmpty()) {
                tooltip.add(row);
            }
        }
    }
}
