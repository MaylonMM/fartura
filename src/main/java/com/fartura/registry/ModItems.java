package com.fartura.registry;

import com.fartura.Fartura;
import com.fartura.item.ColoredKnifeItem;
import com.fartura.item.CandiedAppleItem;
import com.fartura.item.CoconutWaterItem;
import com.fartura.item.MilkCupItem;
import com.fartura.item.PartyPopperItem;
import com.fartura.item.ThrowableCoconutItem;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Fartura.MODID);

    public static final DeferredItem<Item> CORN = ITEMS.registerSimpleItem("corn",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationModifier(0.1f)
                    .build()));

    // Named after the lower plant block so the item's name comes from its own translation key.
    public static final DeferredItem<ItemNameBlockItem> CORN_KERNELS = ITEMS.register("corn_kernels",
            () -> new ItemNameBlockItem(ModBlocks.CORN_CROP.get(), new Item.Properties()));

    public static final DeferredItem<SwordItem> KNIFE = ITEMS.register("knife",
            () -> new SwordItem(Tiers.IRON, new Item.Properties()
                    .attributes(SwordItem.createAttributes(Tiers.IRON, 3, -2.4F))));

    public static final DeferredItem<ColoredKnifeItem> REDSTONE_KNIFE = ITEMS.register("redstone_knife",
            () -> new ColoredKnifeItem(ColoredKnifeItem.Perk.REDSTONE, 3));

    public static final DeferredItem<ColoredKnifeItem> AMETHYST_KNIFE = ITEMS.register("amethyst_knife",
            () -> new ColoredKnifeItem(ColoredKnifeItem.Perk.AMETHYST, 4));

    public static final DeferredItem<ColoredKnifeItem> EMERALD_KNIFE = ITEMS.register("emerald_knife",
            () -> new ColoredKnifeItem(ColoredKnifeItem.Perk.EMERALD, 3));

    public static final DeferredItem<ColoredKnifeItem> GLOWSTONE_KNIFE = ITEMS.register("glowstone_knife",
            () -> new ColoredKnifeItem(ColoredKnifeItem.Perk.GLOWSTONE, 3));

    public static final DeferredItem<ColoredKnifeItem> QUARTZ_KNIFE = ITEMS.register("quartz_knife",
            () -> new ColoredKnifeItem(ColoredKnifeItem.Perk.QUARTZ, 3));

    public static final DeferredItem<ColoredKnifeItem> LAPIS_KNIFE = ITEMS.register("lapis_knife",
            () -> new ColoredKnifeItem(ColoredKnifeItem.Perk.LAPIS, 3));

    public static final DeferredItem<Item> GREEN_CORN_HUSK = ITEMS.registerSimpleItem("green_corn_husk",
            new Item.Properties());

    public static final DeferredItem<BlockItem> CUTTING_BOARD =
            ITEMS.registerSimpleBlockItem("cutting_board", ModBlocks.CUTTING_BOARD);

    public static final DeferredItem<BlockItem> PANELA =
            ITEMS.registerSimpleBlockItem("panela", ModBlocks.PANELA);

    public static final DeferredItem<BlockItem> KETTLE =
            ITEMS.registerSimpleBlockItem("kettle", ModBlocks.KETTLE);

    public static final DeferredItem<BlockItem> WILD_CORN =
            ITEMS.registerSimpleBlockItem("wild_corn", ModBlocks.WILD_CORN);

    // Named after the plant block so the rice's name comes from its own translation key.
    public static final DeferredItem<ItemNameBlockItem> ARROZ = ITEMS.register("arroz",
            () -> new ItemNameBlockItem(ModBlocks.RICE_CROP.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> WILD_RICE =
            ITEMS.registerSimpleBlockItem("wild_rice", ModBlocks.WILD_RICE);

    public static final DeferredItem<BlockItem> WILD_TOMATO =
            ITEMS.registerSimpleBlockItem("wild_tomato", ModBlocks.WILD_TOMATO);

    // Named after the plant block so the seed's name comes from its own translation key.
    public static final DeferredItem<ItemNameBlockItem> TOMATO_SEEDS = ITEMS.register("tomato_seeds",
            () -> new ItemNameBlockItem(ModBlocks.TOMATO_CROP.get(), new Item.Properties()));

    public static final DeferredItem<Item> TOMATO = ITEMS.registerSimpleItem("tomato",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationModifier(0.1f)
                    .build()));

    public static final DeferredItem<BlockItem> WILD_BEAN =
            ITEMS.registerSimpleBlockItem("wild_bean", ModBlocks.WILD_BEAN);

    // Bean is both food and seed, so it places the plant. Overrides the name so it shows "Feijão" instead of the plant's name.
    public static final DeferredItem<ItemNameBlockItem> BEAN = ITEMS.<ItemNameBlockItem>register("bean",
            () -> new ItemNameBlockItem(ModBlocks.BEAN_CROP.get(), new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(3)
                    .saturationModifier(0.3f)
                    .build())) {
                @Override
                public String getDescriptionId() {
                    return "item.fartura.bean";
                }
            });

    public static final DeferredItem<BlockItem> WILD_PEANUT =
            ITEMS.registerSimpleBlockItem("wild_peanut", ModBlocks.WILD_PEANUT);

    // Peanut is both food and seed, so it places the plant. Overrides the name so it shows "Amendoim" instead of the plant's name.
    public static final DeferredItem<ItemNameBlockItem> PEANUT = ITEMS.<ItemNameBlockItem>register("peanut",
            () -> new ItemNameBlockItem(ModBlocks.PEANUT_CROP.get(), new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationModifier(0.1f)
                    .build())) {
                @Override
                public String getDescriptionId() {
                    return "item.fartura.peanut";
                }
            });

    public static final DeferredItem<BlockItem> WILD_STRAWBERRY =
            ITEMS.registerSimpleBlockItem("wild_strawberry", ModBlocks.WILD_STRAWBERRY);

    // Named after the plant block so the seed's name comes from its own translation key.
    public static final DeferredItem<ItemNameBlockItem> STRAWBERRY_SEEDS = ITEMS.register("strawberry_seeds",
            () -> new ItemNameBlockItem(ModBlocks.STRAWBERRY_CROP.get(), new Item.Properties()) {
                @Override
                public String getDescriptionId() {
                    return "item.fartura.strawberry_seeds";
                }
            });

    public static final DeferredItem<Item> STRAWBERRY = ITEMS.registerSimpleItem("strawberry",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationModifier(0.1f)
                    .build()));

    public static final DeferredItem<Item> CORN_FLOUR = ITEMS.registerSimpleItem("corn_flour",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationModifier(0.1f)
                    .build()));

    public static final DeferredItem<Item> COOKED_CORN = ITEMS.registerSimpleItem("cooked_corn",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationModifier(0.6f)
                    .build()));

    public static final DeferredItem<Item> POPCORN = ITEMS.registerSimpleItem("popcorn",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationModifier(0.3f)
                    .build()));

    public static final DeferredItem<Item> CUSCUZ = ITEMS.registerSimpleItem("cuscuz",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(6)
                    .saturationModifier(0.6f)
                    .build()));

    public static final DeferredItem<Item> FRIED_POTATO = ITEMS.registerSimpleItem("fried_potato",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(5)
                    .saturationModifier(0.6f)
                    .build()));

    public static final DeferredItem<BlockItem> CORN_CAKE = ITEMS.register("corn_cake",
            () -> new BlockItem(ModBlocks.CORN_CAKE.get(), new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(8)
                    .saturationModifier(0.6f)
                    .build())));

    public static final DeferredItem<BlockItem> CARROT_CAKE = ITEMS.register("carrot_cake",
            () -> new BlockItem(ModBlocks.CARROT_CAKE.get(), new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(8)
                    .saturationModifier(0.6f)
                    .build())));

    public static final DeferredItem<Item> PAMONHA = ITEMS.registerSimpleItem("pamonha",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(6)
                    .saturationModifier(0.6f)
                    .build()));

    public static final DeferredItem<Item> CORN_CURAU = ITEMS.registerSimpleItem("corn_curau",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(6)
                    .saturationModifier(0.6f)
                    .build()));

    public static final DeferredItem<Item> COOKED_RICE = ITEMS.registerSimpleItem("cooked_rice",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(8)
                    .saturationModifier(0.6f)
                    .build()));

    public static final DeferredItem<Item> BEEF_PASTA = ITEMS.registerSimpleItem("beef_pasta",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(16)
                    .saturationModifier(0.9f)
                    .build()));

    public static final DeferredItem<Item> COMBO_PLATE = ITEMS.registerSimpleItem("combo_plate",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(16)
                    .saturationModifier(0.9f)
                    .build()));

    public static final DeferredItem<Item> MACARONI = ITEMS.registerSimpleItem("macaroni",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(12)
                    .saturationModifier(0.8f)
                    .build()));

    public static final DeferredItem<Item> CHICKEN_STROGANOFF = ITEMS.registerSimpleItem("chicken_stroganoff",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(16)
                    .saturationModifier(0.8f)
                    .build()));
    public static final DeferredItem<Item> CHEESE = ITEMS.registerSimpleItem("cheese",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationModifier(0.5f)
                    .build()));
    public static final DeferredItem<Item> SALT = ITEMS.registerSimpleItem("salt",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1)
                    .saturationModifier(0.1f)
                    .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 200), 1.0f)
                    .build()));

    public static final DeferredItem<BlockItem> BEIJINHO = ITEMS.register("beijinho",
            () -> new BlockItem(ModBlocks.BEIJINHO.get(), new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationModifier(0.2f)
                    .build())));

    public static final DeferredItem<BlockItem> BRIGADEIRO = ITEMS.register("brigadeiro",
            () -> new BlockItem(ModBlocks.BRIGADEIRO.get(), new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationModifier(0.2f)
                    .build())));

    public static final DeferredItem<ThrowableCoconutItem> GREEN_COCONUT = ITEMS.register("green_coconut",
            () -> new ThrowableCoconutItem(new Item.Properties()));

    public static final DeferredItem<CoconutWaterItem> COCONUT_WATER = ITEMS.register("coconut_water",
            () -> new CoconutWaterItem(ModBlocks.COCONUT_WATER.get(), new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationModifier(0.3f)
                    .alwaysEdible()
                    .build())));

    public static final DeferredItem<PartyPopperItem> PARTY_POPPER = ITEMS.register("party_popper",
            () -> new PartyPopperItem(new Item.Properties()));

    public static final DeferredItem<ThrowableCoconutItem> DRY_COCONUT = ITEMS.register("dry_coconut",
            () -> new ThrowableCoconutItem(new Item.Properties()));

    public static final DeferredItem<Item> MINCED_PORK = ITEMS.register("minced_pork",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(3)
                    .saturationModifier(0.3f)
                    .build())));

    public static final DeferredItem<Item> MINCED_BEEF = ITEMS.register("minced_beef",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(3)
                    .saturationModifier(0.3f)
                    .build())));

    public static final DeferredItem<Item> RAW_SAUSAGE = ITEMS.register("raw_sausage",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(3)
                    .saturationModifier(0.3f)
                    .build())));

    public static final DeferredItem<Item> COOKED_SAUSAGE = ITEMS.register("cooked_sausage",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(10)
                    .saturationModifier(0.8f)
                    .build())));

    public static final DeferredItem<Item> RAW_BEEF_SKEWER = ITEMS.register("raw_beef_skewer",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationModifier(0.4f)
                    .build())));

    public static final DeferredItem<Item> COOKED_BEEF_SKEWER = ITEMS.register("cooked_beef_skewer",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(14)
                    .saturationModifier(0.8f)
                    .build())));

    public static final DeferredItem<Item> RAW_CHICKEN_SKEWER = ITEMS.register("raw_chicken_skewer",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationModifier(0.4f)
                    .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600), 0.3f)
                    .build())));

    public static final DeferredItem<Item> COOKED_CHICKEN_SKEWER = ITEMS.register("cooked_chicken_skewer",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(12)
                    .saturationModifier(0.6f)
                    .build())));

    public static final DeferredItem<Item> RAW_SAUSAGE_SKEWER = ITEMS.register("raw_sausage_skewer",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationModifier(0.4f)
                    .build())));

    public static final DeferredItem<Item> COOKED_SAUSAGE_SKEWER = ITEMS.register("cooked_sausage_skewer",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(14)
                    .saturationModifier(0.8f)
                    .build())));

    public static final DeferredItem<Item> RAW_CHEESE_SKEWER = ITEMS.register("raw_cheese_skewer",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationModifier(0.4f)
                    .build())));

    public static final DeferredItem<Item> COOKED_CHEESE_SKEWER = ITEMS.register("cooked_cheese_skewer",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(12)
                    .saturationModifier(0.6f)
                    .build())));

    public static final DeferredItem<Item> WHEAT_DOUGH = ITEMS.registerSimpleItem("wheat_dough");

    public static final DeferredItem<Item> MEAT_PASTEL = ITEMS.register("meat_pastel",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(14)
                    .saturationModifier(0.8f)
                    .build())));

    public static final DeferredItem<Item> CHEESE_PASTEL = ITEMS.register("cheese_pastel",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(12)
                    .saturationModifier(0.6f)
                    .build())));

    public static final DeferredItem<Item> RAW_VEGETABLE_SKEWER = ITEMS.register("raw_vegetable_skewer",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationModifier(0.4f)
                    .build())));

    public static final DeferredItem<Item> COOKED_VEGETABLE_SKEWER = ITEMS.register("cooked_vegetable_skewer",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(12)
                    .saturationModifier(0.6f)
                    .build())));

    public static final DeferredItem<Item> COCONUT = ITEMS.register("coconut",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(3)
                    .saturationModifier(0.3f)
                    .build())));

    public static final DeferredItem<Item> BEEF_EMPANADA = ITEMS.register("beef_empanada",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(14)
                    .saturationModifier(0.8f)
                    .build())));

    public static final DeferredItem<Item> PASTEL_DE_NATA = ITEMS.register("pastel_de_nata",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(8)
                    .saturationModifier(0.4f)
                    .build())));

    public static final DeferredItem<Item> PACOCA = ITEMS.register("pacoca",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationModifier(0.2f)
                    .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200), 1.0f)
                    .build())));

    public static final DeferredItem<Item> CANDIED_APPLE = ITEMS.register("candied_apple",
            () -> new CandiedAppleItem(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(6)
                    .saturationModifier(0.3f)
                    .build())));

    public static final DeferredItem<Item> CANDIED_STRAWBERRY = ITEMS.register("candied_strawberry",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(6)
                    .saturationModifier(0.3f)
                    .build())));

    public static final DeferredItem<Item> MILK_CUP = ITEMS.register("milk_cup",
            () -> new MilkCupItem(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(3)
                    .saturationModifier(0.3f)
                    .build())));

    public static final DeferredItem<BlockItem> COCONUT_LEAVES =
            ITEMS.registerSimpleBlockItem("coconut_leaves", ModBlocks.COCONUT_LEAVES);

    public static final DeferredItem<BlockItem> COCONUT_SAPLING =
            ITEMS.registerSimpleBlockItem("coconut_sapling", ModBlocks.COCONUT_SAPLING);

    public static final DeferredItem<BlockItem> STONE_MORTAR =
            ITEMS.registerSimpleBlockItem("stone_mortar", ModBlocks.STONE_MORTAR);

    public static final DeferredItem<BlockItem> BARBECUE_GRILL =
            ITEMS.registerSimpleBlockItem("barbecue_grill", ModBlocks.BARBECUE_GRILL);

    public static final DeferredItem<BlockItem> BARBECUE_GRILL_FLOOR =
            ITEMS.registerSimpleBlockItem("barbecue_grill_floor", ModBlocks.BARBECUE_GRILL_FLOOR);

    public static final DeferredItem<BlockItem> IRON_FRYING_PAN =
            ITEMS.registerSimpleBlockItem("iron_frying_pan", ModBlocks.IRON_FRYING_PAN);

    private ModItems() {
    }
}
