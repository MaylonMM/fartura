package com.fartura.client;

import com.fartura.Fartura;
import com.fartura.client.renderer.BarbecueGrillRenderer;
import com.fartura.client.renderer.CuttingBoardRenderer;
import com.fartura.client.renderer.FrigideiraRenderer;
import com.fartura.client.screen.BarbecueGrillScreen;
import com.fartura.client.screen.KettleScreen;
import com.fartura.client.screen.PanelaScreen;
import com.fartura.client.screen.StoneMortarScreen;
import com.fartura.registry.ModBlockEntities;
import com.fartura.registry.ModBlocks;
import com.fartura.registry.ModEntities;
import com.fartura.registry.ModMenus;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.level.FoliageColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Fartura.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientSetup {
    private ClientSetup() {
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_CORN.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CORN_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.RICE_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_RICE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_TOMATO.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.TOMATO_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_BEAN.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.BEAN_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_PEANUT.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.PEANUT_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_STRAWBERRY.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.STRAWBERRY_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.BARBECUE_GRILL.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.BARBECUE_GRILL_FLOOR.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.COCONUT_LEAVES.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.COCONUT_SAPLING.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.BEIJINHO.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.BRIGADEIRO.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.PANELA.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.KETTLE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.IRON_FRYING_PAN.get(), RenderType.cutout());
        });
    }

    @SubscribeEvent
    static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> level != null && pos != null
                ? BiomeColors.getAverageFoliageColor(level, pos)
                : FoliageColor.getDefaultColor(), ModBlocks.COCONUT_LEAVES.get());
    }

    @SubscribeEvent
    static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> FoliageColor.getDefaultColor(), ModBlocks.COCONUT_LEAVES.get());
    }

    @SubscribeEvent
    static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.BARBECUE_GRILL.get(), BarbecueGrillRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.CUTTING_BOARD.get(), CuttingBoardRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.FRIGIDEIRA.get(), FrigideiraRenderer::new);
        event.registerEntityRenderer(ModEntities.COCONUT_PROJECTILE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.PARTY_POPPER_PROJECTILE.get(), ThrownItemRenderer::new);
    }

    @SubscribeEvent
    static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.STONE_MORTAR.get(), StoneMortarScreen::new);
        event.register(ModMenus.BARBECUE_GRILL.get(), BarbecueGrillScreen::new);
        event.register(ModMenus.PANELA.get(), PanelaScreen::new);
        event.register(ModMenus.KETTLE.get(), KettleScreen::new);
    }
}
