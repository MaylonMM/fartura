package com.fartura.registry;

import com.fartura.Fartura;
import com.fartura.menu.StoneMortarMenu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Fartura.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<StoneMortarMenu>> STONE_MORTAR =
            MENUS.register("stone_mortar", () -> IMenuTypeExtension.create(StoneMortarMenu::new));
}
