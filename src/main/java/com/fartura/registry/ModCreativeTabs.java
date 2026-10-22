package com.fartura.registry;

import com.fartura.Fartura;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Fartura.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FARTURA = TABS.register("fartura",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.fartura"))
                    .icon(() -> new ItemStack(ModItems.CORN.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.CORN.get());
                        output.accept(ModItems.CORN_KERNELS.get());
                        output.accept(ModItems.WILD_CORN.get());
                    })
                    .build());

    private ModCreativeTabs() {
    }
}
