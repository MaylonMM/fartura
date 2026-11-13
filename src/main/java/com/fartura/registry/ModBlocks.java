package com.fartura.registry;

import com.fartura.Fartura;
import com.fartura.block.AcaiPodBlock;
import com.fartura.block.MaracujaVineBlock;
import com.fartura.block.LimePodBlock;
import com.fartura.block.OrangePodBlock;
import com.fartura.block.BarbecueGrillBlock;
import com.fartura.block.FrigideiraBlock;
import com.fartura.block.CoffeeCropBlock;
import com.fartura.block.GuaranaCropBlock;
import com.fartura.block.BarbecueGrillFloorBlock;
import com.fartura.block.CarrotCakeBlock;
import com.fartura.block.KettleBlock;
import com.fartura.block.CoconutPodBlock;
import com.fartura.block.CoconutSaplingBlock;
import com.fartura.block.CoconutWaterBlock;
import com.fartura.block.CuttingBoardBlock;
import com.fartura.block.PanelaBlock;
import com.fartura.block.SweetBlock;
import com.fartura.block.CornCakeBlock;
import com.fartura.block.MaracujaCakeBlock;
import com.fartura.block.CornCropBlock;
import com.fartura.block.CassavaCropBlock;
import com.fartura.block.PeanutCropBlock;
import com.fartura.block.RiceCropBlock;
import com.fartura.block.CobblestoneFlakeBlock;
import com.fartura.block.StoneMortarBlock;
import com.fartura.block.StrawberryCropBlock;
import com.fartura.block.TomatoCropBlock;
import com.fartura.block.WildBeanBlock;
import com.fartura.block.BeanCropBlock;
import com.fartura.block.WildCornBlock;
import com.fartura.block.WildPeanutBlock;
import com.fartura.block.WildRiceBlock;
import com.fartura.block.WildCassavaBlock;
import com.fartura.block.WildStrawberryBlock;
import com.fartura.block.WildTomatoBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Fartura.MODID);

    public static final DeferredBlock<WildCornBlock> WILD_CORN = BLOCKS.register("wild_corn",
            () -> new WildCornBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<CornCropBlock> CORN_CROP = BLOCKS.register("corn_crop",
            () -> new CornCropBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.CROP)
                    .offsetType(BlockBehaviour.OffsetType.NONE)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<RiceCropBlock> RICE_CROP = BLOCKS.register("rice_crop",
            () -> new RiceCropBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.CROP)
                    .offsetType(BlockBehaviour.OffsetType.NONE)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<WildRiceBlock> WILD_RICE = BLOCKS.register("wild_rice",
            () -> new WildRiceBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WATER)
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.WET_GRASS)
                    .offsetType(BlockBehaviour.OffsetType.NONE)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<WildTomatoBlock> WILD_TOMATO = BLOCKS.register("wild_tomato",
            () -> new WildTomatoBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<TomatoCropBlock> TOMATO_CROP = BLOCKS.register("tomato_crop",
            () -> new TomatoCropBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.CROP)
                    .offsetType(BlockBehaviour.OffsetType.NONE)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<WildBeanBlock> WILD_BEAN = BLOCKS.register("wild_bean",
            () -> new WildBeanBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<BeanCropBlock> BEAN_CROP = BLOCKS.register("bean_crop",
            () -> new BeanCropBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.CROP)
                    .offsetType(BlockBehaviour.OffsetType.NONE)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<WildPeanutBlock> WILD_PEANUT = BLOCKS.register("wild_peanut",
            () -> new WildPeanutBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.NONE)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<PeanutCropBlock> PEANUT_CROP = BLOCKS.register("peanut_crop",
            () -> new PeanutCropBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.CROP)
                    .offsetType(BlockBehaviour.OffsetType.NONE)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<WildStrawberryBlock> WILD_STRAWBERRY = BLOCKS.register("wild_strawberry",
            () -> new WildStrawberryBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<StrawberryCropBlock> STRAWBERRY_CROP = BLOCKS.register("strawberry_crop",
            () -> new StrawberryCropBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.CROP)
                    .offsetType(BlockBehaviour.OffsetType.NONE)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<WildCassavaBlock> WILD_CASSAVA = BLOCKS.register("wild_cassava",
            () -> new WildCassavaBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<CassavaCropBlock> CASSAVA_CROP = BLOCKS.register("cassava_crop",
            () -> new CassavaCropBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.CROP)
                    .offsetType(BlockBehaviour.OffsetType.NONE)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<GuaranaCropBlock> GUARANA_CROP = BLOCKS.register("guarana_crop",
            () -> new GuaranaCropBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.CROP)
                    .offsetType(BlockBehaviour.OffsetType.NONE)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<CoffeeCropBlock> COFFEE_CROP = BLOCKS.register("coffee_crop",
            () -> new CoffeeCropBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.CROP)
                    .offsetType(BlockBehaviour.OffsetType.NONE)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<CobblestoneFlakeBlock> COBBLESTONE_FLAKE = BLOCKS.register("cobblestone_flake",
            () -> new CobblestoneFlakeBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(0.1f)
                    .sound(SoundType.STONE)
                    .noOcclusion()
                    .noCollission()));

    public static final DeferredBlock<StoneMortarBlock> STONE_MORTAR = BLOCKS.register("stone_mortar",
            () -> new StoneMortarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(2.0f, 6.0f)
                    .sound(SoundType.STONE)
                    .noOcclusion()));

    public static final DeferredBlock<BarbecueGrillBlock> BARBECUE_GRILL = BLOCKS.register("barbecue_grill",
            () -> new BarbecueGrillBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0f, 6.0f)
                    .sound(SoundType.STONE)
                    .lightLevel(state -> state.getValue(BarbecueGrillBlock.LIT) ? 13 : 0)
                    .noOcclusion()));

    public static final DeferredBlock<BarbecueGrillFloorBlock> BARBECUE_GRILL_FLOOR = BLOCKS.register("barbecue_grill_floor",
            () -> new BarbecueGrillFloorBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0f, 6.0f)
                    .sound(SoundType.STONE)
                    .lightLevel(state -> state.getValue(BarbecueGrillBlock.LIT) ? 13 : 0)
                    .noOcclusion()));

    public static final DeferredBlock<CornCakeBlock> CORN_CAKE = BLOCKS.register("corn_cake",
            () -> new CornCakeBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.5f)
                    .sound(SoundType.WOOL)
                    .pushReaction(PushReaction.DESTROY)
                    .noOcclusion()));

    public static final DeferredBlock<CarrotCakeBlock> CARROT_CAKE = BLOCKS.register("carrot_cake",
            () -> new CarrotCakeBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.5f)
                    .sound(SoundType.WOOL)
                    .pushReaction(PushReaction.DESTROY)
                    .noOcclusion()));

    public static final DeferredBlock<MaracujaCakeBlock> MARACUJA_CAKE = BLOCKS.register("maracuja_cake",
            () -> new MaracujaCakeBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.5f)
                    .sound(SoundType.WOOL)
                    .pushReaction(PushReaction.DESTROY)
                    .noOcclusion()));

    public static final DeferredBlock<SweetBlock> BEIJINHO = BLOCKS.register("beijinho",
            () -> new SweetBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.5f)
                    .sound(SoundType.WOOL)
                    .pushReaction(PushReaction.DESTROY)
                    .noOcclusion()));

    public static final DeferredBlock<SweetBlock> BRIGADEIRO = BLOCKS.register("brigadeiro",
            () -> new SweetBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(0.5f)
                    .sound(SoundType.WOOL)
                    .pushReaction(PushReaction.DESTROY)
                    .noOcclusion()));

    public static final DeferredBlock<CoconutPodBlock> COCONUT_POD = BLOCKS.register("coconut_pod",
            () -> new CoconutPodBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .strength(0.2f)
                    .randomTicks()
                    .sound(SoundType.WOOD)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)));

    /** Crop sounds for steps, placing and hitting; harvest (break) uses the crop break sound. */
    private static final SoundType ACAI_SOUND = new SoundType(
            SoundType.WOOD.getVolume(), SoundType.WOOD.getPitch(),
            SoundEvents.CROP_BREAK,
            SoundType.WOOD.getStepSound(), SoundEvents.CROP_PLANTED,
            SoundType.WOOD.getHitSound(), SoundType.WOOD.getFallSound());

    public static final DeferredBlock<AcaiPodBlock> ACAI_POD = BLOCKS.register("acai_pod",
            () -> new AcaiPodBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .strength(0.2f)
                    .randomTicks()
                    .sound(ACAI_SOUND)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<MaracujaVineBlock> MARACUJA_VINE = BLOCKS.register("maracuja_vine",
            () -> new MaracujaVineBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .strength(0.2f)
                    .randomTicks()
                    .sound(ACAI_SOUND)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LimePodBlock> LIME_POD = BLOCKS.register("lime_pod",
            () -> new LimePodBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .strength(0.2f)
                    .randomTicks()
                    .sound(ACAI_SOUND)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<OrangePodBlock> ORANGE_POD = BLOCKS.register("orange_pod",
            () -> new OrangePodBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .strength(0.2f)
                    .randomTicks()
                    .sound(ACAI_SOUND)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<CoconutSaplingBlock> COCONUT_SAPLING = BLOCKS.register("coconut_sapling",
            () -> new CoconutSaplingBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<CoconutWaterBlock> COCONUT_WATER = BLOCKS.register("coconut_water",
            () -> new CoconutWaterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(0.2f)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<CuttingBoardBlock> CUTTING_BOARD = BLOCKS.register("cutting_board",
            () -> new CuttingBoardBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(1.0f, 3.0f)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<PanelaBlock> PANELA = BLOCKS.register("panela",
            () -> new PanelaBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_ORANGE)
                    .strength(1.0f, 3.0f)
                    .sound(SoundType.PACKED_MUD)
                    .noOcclusion()));

    public static final DeferredBlock<FrigideiraBlock> IRON_FRYING_PAN = BLOCKS.register("iron_frying_pan",
            () -> new FrigideiraBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(1.0f, 3.0f)
                    .sound(SoundType.METAL)
                    .lightLevel(state -> state.getValue(FrigideiraBlock.LIT) ? 13 : 0)
                    .noOcclusion()));

    public static final DeferredBlock<KettleBlock> KETTLE = BLOCKS.register("kettle",
            () -> new KettleBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_ORANGE)
                    .strength(1.0f, 3.0f)
                    .sound(SoundType.PACKED_MUD)
                    .noOcclusion()));

    private ModBlocks() {
    }
}
