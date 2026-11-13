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
                        // Plantas Selvagens
                        output.accept(ModItems.WILD_PEANUT.get());
                        output.accept(ModItems.WILD_RICE.get());
                        output.accept(ModItems.WILD_BEAN.get());
                        output.accept(ModItems.WILD_CASSAVA.get());
                        output.accept(ModItems.WILD_CORN.get());
                        output.accept(ModItems.WILD_STRAWBERRY.get());
                        output.accept(ModItems.WILD_TOMATO.get());

                        // Mudas
                        output.accept(ModItems.COCONUT_SAPLING.get());

                        // Sementes
                        output.accept(ModItems.STRAWBERRY_SEEDS.get());
                        output.accept(ModItems.TOMATO_SEEDS.get());


                        // Verduras, Legumes e Frutas
                        output.accept(ModItems.CORN.get());
                        output.accept(ModItems.CORN_KERNELS.get());
                        output.accept(ModItems.GREEN_CORN_HUSK.get());
                        output.accept(ModItems.CASSAVA.get());

                        output.accept(ModItems.RICE.get());
                        output.accept(ModItems.BEAN.get());

                        output.accept(ModItems.PEANUT.get());
                        output.accept(ModItems.COCONUT.get());
                        output.accept(ModItems.DRY_COCONUT.get());
                        output.accept(ModItems.GREEN_COCONUT.get());
                        output.accept(ModItems.COFFEE_BEAN.get());

                        output.accept(ModItems.GUARANA.get());
                        output.accept(ModItems.ACAI.get());
                        output.accept(ModItems.ORANGE.get());
                        output.accept(ModItems.LIME.get());
                        output.accept(ModItems.MARACUJA.get());
                        output.accept(ModItems.STRAWBERRY.get());
                        output.accept(ModItems.TOMATO.get());

                        // Carnes
                        output.accept(ModItems.MINCED_PORK.get());
                        output.accept(ModItems.MINCED_BEEF.get());
                        output.accept(ModItems.DRIED_BEEF.get());
                        output.accept(ModItems.COOKED_SAUSAGE.get());
                        output.accept(ModItems.RAW_SAUSAGE.get());

                        output.accept(ModItems.COOKED_BEEF_SKEWER.get());
                        output.accept(ModItems.RAW_BEEF_SKEWER.get());
                        output.accept(ModItems.COOKED_CHICKEN_SKEWER.get());
                        output.accept(ModItems.RAW_CHICKEN_SKEWER.get());
                        output.accept(ModItems.COOKED_SAUSAGE_SKEWER.get());
                        output.accept(ModItems.RAW_SAUSAGE_SKEWER.get());
                        output.accept(ModItems.COOKED_CHEESE_SKEWER.get());
                        output.accept(ModItems.RAW_CHEESE_SKEWER.get());
                        output.accept(ModItems.COOKED_VEGETABLE_SKEWER.get());
                        output.accept(ModItems.RAW_VEGETABLE_SKEWER.get());

                        // Alimentos e Pratos de Comida
                        output.accept(ModItems.FRIED_POTATO.get());
                        output.accept(ModItems.COXINHA.get());
                        output.accept(ModItems.CUSCUZ.get());
                        output.accept(ModItems.BEEF_EMPANADA.get());
                        output.accept(ModItems.MEAT_PASTEL.get());
                        output.accept(ModItems.CHEESE_PASTEL.get());
                        output.accept(ModItems.CHEESE_TAPIOCA.get());
                        output.accept(ModItems.COOKED_CORN.get());
                        output.accept(ModItems.PAMONHA.get());
                        output.accept(ModItems.POPCORN.get());
                        output.accept(ModItems.PAO_DE_QUEIJO.get());


                        output.accept(ModItems.DRIED_BEEF_ESCONDIDINHO.get());
                        output.accept(ModItems.COOKED_RICE.get());
                        output.accept(ModItems.CHICKEN_STROGANOFF.get());
                        output.accept(ModItems.PRATO_FEITO.get());
                        output.accept(ModItems.FEIJOADA.get());
                        output.accept(ModItems.BEEF_PASTA.get());
                        output.accept(ModItems.PASTA.get());
                        output.accept(ModItems.CHICKEN_NOODLES.get());
                        output.accept(ModItems.TOMATO_NOODLES.get());
                        output.accept(ModItems.SALADA_MAIONESE.get());

                        output.accept(ModItems.FAROFA_BACON.get());
                        output.accept(ModItems.FAROFA_OVO.get());
                        output.accept(ModItems.CASSAVA_FLOUR.get());
                        output.accept(ModItems.CORN_FLOUR.get());
                        output.accept(ModItems.TAPIOCA_FLOUR.get());

                        output.accept(ModItems.WHEAT_DOUGH.get());
                        output.accept(ModItems.CHEESE.get());
                        output.accept(ModItems.SALT.get());



                        // Sobremesas
                        output.accept(ModItems.ACAI_NA_TIGELA.get());

                        output.accept(ModItems.BEIJINHO.get());
                        output.accept(ModItems.BRIGADEIRO.get());

                        output.accept(ModItems.CARROT_CAKE.get());
                        output.accept(ModItems.MARACUJA_CAKE.get());
                        output.accept(ModItems.CORN_CAKE.get());

                        output.accept(ModItems.CORN_CURAU.get());
                        output.accept(ModItems.CANDIED_APPLE.get());
                        output.accept(ModItems.CANDIED_STRAWBERRY.get());
                        output.accept(ModItems.PACOCA.get());
                        output.accept(ModItems.PASTEL_DE_NATA.get());
                        output.accept(ModItems.PUDDING.get());

                        // Sucos
                        output.accept(ModItems.COCONUT_WATER.get());

                        output.accept(ModItems.SUGARCANE_JUICE.get());
                        output.accept(ModItems.LEMONADE.get());
                        output.accept(ModItems.GUARANA_JUICE.get());
                        output.accept(ModItems.ORANGE_JUICE.get());
                        output.accept(ModItems.MARACUJA_JUICE.get());

                        output.accept(ModItems.MILK_CUP.get());
                        output.accept(ModItems.CHOCOLATE_MILK.get());

                        // Bebidas Quentes
                        output.accept(ModItems.COFFEE.get());
                        output.accept(ModItems.COFFEE_WITH_MILK.get());
                        output.accept(ModItems.HOT_CHOCOLATE.get());

                        // Blocos Funcionais
                        output.accept(ModItems.KETTLE.get());
                        output.accept(ModItems.BARBECUE_GRILL.get());
                        output.accept(ModItems.BARBECUE_GRILL_FLOOR.get());
                        output.accept(ModItems.IRON_FRYING_PAN.get());
                        output.accept(ModItems.PANELA.get());
                        output.accept(ModItems.STONE_MORTAR.get());
                        output.accept(ModItems.CUTTING_BOARD.get());

                        // Facas
                        output.accept(ModItems.KNIFE.get());
                        output.accept(ModItems.AMETHYST_KNIFE.get());
                        output.accept(ModItems.EMERALD_KNIFE.get());
                        output.accept(ModItems.GLOWSTONE_KNIFE.get());
                        output.accept(ModItems.LAPIS_KNIFE.get());
                        output.accept(ModItems.QUARTZ_KNIFE.get());
                        output.accept(ModItems.REDSTONE_KNIFE.get());

                        // Guia
                        output.accept(ModItems.GUIDE_BOOK.get());

                        // Pedra e Estalinho
                        output.accept(ModItems.PARTY_POPPER.get());
                        output.accept(ModItems.COBBLESTONE_FLAKE.get());
                    })
                    .build());

    private ModCreativeTabs() {
    }
}
