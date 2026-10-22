package com.brasilfoods.registry;

import static com.brasilfoods.BrasilFoods.MODID;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BRASIL_FOODS = TABS.register("brasil_foods",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + MODID))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModItems.PAO_DE_QUEIJO.get().getDefaultInstance())
                    .displayItems((parameters, output) -> output.accept(ModItems.PAO_DE_QUEIJO.get()))
                    .build());

    private ModCreativeTabs() {
    }
}
