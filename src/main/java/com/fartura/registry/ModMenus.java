package com.fartura.registry;

import com.fartura.Fartura;
import com.fartura.menu.BarbecueGrillMenu;
import com.fartura.menu.KettleMenu;
import com.fartura.menu.PanelaMenu;
import com.fartura.menu.StoneMortarMenu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
//? if neoforge {
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
//?} else {
import net.minecraftforge.common.extensions.IForgeMenuType;
import com.fartura.compat.registry.DeferredHolder;
import com.fartura.compat.registry.DeferredRegister;
//?}

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Fartura.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<StoneMortarMenu>> STONE_MORTAR =
            //? if neoforge {
            MENUS.register("stone_mortar", () -> IMenuTypeExtension.create(StoneMortarMenu::new));
//?} else {
            MENUS.register("stone_mortar", () -> IForgeMenuType.create(StoneMortarMenu::new));
//?}

    public static final DeferredHolder<MenuType<?>, MenuType<PanelaMenu>> PANELA =
            //? if neoforge {
            MENUS.register("panela", () -> IMenuTypeExtension.create(PanelaMenu::new));
//?} else {
            MENUS.register("panela", () -> IForgeMenuType.create(PanelaMenu::new));
//?}

    public static final DeferredHolder<MenuType<?>, MenuType<KettleMenu>> KETTLE =
            //? if neoforge {
            MENUS.register("kettle", () -> IMenuTypeExtension.create(KettleMenu::new));
//?} else {
            MENUS.register("kettle", () -> IForgeMenuType.create(KettleMenu::new));
//?}

    public static final DeferredHolder<MenuType<?>, MenuType<BarbecueGrillMenu>> BARBECUE_GRILL =
            //? if neoforge {
            MENUS.register("barbecue_grill", () -> IMenuTypeExtension.create(BarbecueGrillMenu::new));
//?} else {
            MENUS.register("barbecue_grill", () -> IForgeMenuType.create(BarbecueGrillMenu::new));
//?}
}
