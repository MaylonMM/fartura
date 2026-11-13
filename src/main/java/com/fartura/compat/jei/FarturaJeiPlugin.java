package com.fartura.compat.jei;

import com.fartura.Fartura;
import com.fartura.recipe.FrigideiraRecipe;
import com.fartura.recipe.MortarRecipe;
import com.fartura.recipe.PanelaRecipe;
import com.fartura.registry.ModBlocks;
import com.fartura.registry.ModItems;
import com.fartura.registry.ModRecipes;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

@JeiPlugin
public class FarturaJeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Fartura.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MortarRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new BarbecueGrillRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CuttingBoardRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new PanelaRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new KettleRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FrigideiraRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (Minecraft.getInstance().level == null) {
            return;
        }
        List<PanelaRecipe> panelaRecipes = Minecraft.getInstance().level.getRecipeManager()
                .getAllRecipesFor(ModRecipes.PANELA_TYPE.get()).stream()
                .map(RecipeHolder::value)
                .toList();
        registration.addRecipes(PanelaRecipeCategory.RECIPE_TYPE, panelaRecipes);
        List<PanelaRecipe> kettleRecipes = Minecraft.getInstance().level.getRecipeManager()
                .getAllRecipesFor(ModRecipes.KETTLE_TYPE.get()).stream()
                .map(RecipeHolder::value)
                .toList();
        registration.addRecipes(KettleRecipeCategory.KETTLE_RECIPE_TYPE, kettleRecipes);
        List<FrigideiraRecipe> frigideiraRecipes = Minecraft.getInstance().level.getRecipeManager()
                .getAllRecipesFor(ModRecipes.FRIGIDEIRA_TYPE.get()).stream()
                .map(RecipeHolder::value)
                .toList();
        registration.addRecipes(FrigideiraRecipeCategory.RECIPE_TYPE, frigideiraRecipes);
        List<MortarRecipe> recipes = Minecraft.getInstance().level.getRecipeManager()
                .getAllRecipesFor(ModRecipes.MORTAR_TYPE.get()).stream()
                .map(RecipeHolder::value)
                .toList();
        registration.addRecipes(MortarRecipeCategory.RECIPE_TYPE, recipes);

        List<CampfireCookingRecipe> campfireRecipes = Minecraft.getInstance().level.getRecipeManager()
                .getAllRecipesFor(net.minecraft.world.item.crafting.RecipeType.CAMPFIRE_COOKING).stream()
                .map(RecipeHolder::value)
                .toList();
        registration.addRecipes(BarbecueGrillRecipeCategory.RECIPE_TYPE, campfireRecipes);

        List<ItemStack> knives = List.of(
                new ItemStack(ModItems.KNIFE.get()),
                new ItemStack(ModItems.REDSTONE_KNIFE.get()),
                new ItemStack(ModItems.AMETHYST_KNIFE.get()),
                new ItemStack(ModItems.EMERALD_KNIFE.get()),
                new ItemStack(ModItems.GLOWSTONE_KNIFE.get()),
                new ItemStack(ModItems.QUARTZ_KNIFE.get()),
                new ItemStack(ModItems.LAPIS_KNIFE.get()));
        registration.addRecipes(CuttingBoardRecipeCategory.RECIPE_TYPE, List.of(
                new CuttingBoardRecipe(new ItemStack(ModItems.GREEN_COCONUT.get()),
                        knives,
                        new ItemStack(ModItems.COCONUT_WATER.get())),
                new CuttingBoardRecipe(new ItemStack(ModItems.DRY_COCONUT.get()),
                        knives,
                        new ItemStack(ModItems.COCONUT.get())),
                new CuttingBoardRecipe(new ItemStack(Items.PORKCHOP),
                        knives,
                        new ItemStack(ModItems.MINCED_PORK.get())),
                new CuttingBoardRecipe(new ItemStack(Items.BEEF),
                        knives,
                        new ItemStack(ModItems.MINCED_BEEF.get()))));
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        if (Minecraft.getInstance().level == null) {
            return;
        }
        // Receitas de exibicao do guia (Panela de Barro) nao devem aparecer como receitas de bancada no JEI.
        List<RecipeHolder<CraftingRecipe>> displayRecipes = Minecraft.getInstance().level.getRecipeManager()
                .getAllRecipesFor(net.minecraft.world.item.crafting.RecipeType.CRAFTING).stream()
                .filter(holder -> holder.id().getNamespace().equals(Fartura.MODID)
                        && holder.id().getPath().startsWith("display/"))
                .toList();
        jeiRuntime.getRecipeManager().hideRecipes(RecipeTypes.CRAFTING, displayRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItems.STONE_MORTAR.get()), MortarRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.BARBECUE_GRILL.get()), RecipeTypes.CAMPFIRE_COOKING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.BARBECUE_GRILL.get()), BarbecueGrillRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.BARBECUE_GRILL_FLOOR.get()), RecipeTypes.CAMPFIRE_COOKING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.BARBECUE_GRILL_FLOOR.get()), BarbecueGrillRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CUTTING_BOARD.get()), CuttingBoardRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.PANELA.get()), PanelaRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.KETTLE.get()), KettleRecipeCategory.KETTLE_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.IRON_FRYING_PAN.get()), FrigideiraRecipeCategory.RECIPE_TYPE);
    }
}

