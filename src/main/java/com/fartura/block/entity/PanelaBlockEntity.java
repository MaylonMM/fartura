package com.fartura.block.entity;

import com.fartura.Fartura;
import com.fartura.menu.PanelaMenu;
import com.fartura.recipe.PanelaRecipe;
import com.fartura.registry.ModBlockEntities;
import com.fartura.registry.ModRecipes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
//? if neoforge {
import net.neoforged.neoforge.items.ItemStackHandler;
//?} else {
import net.minecraftforge.items.ItemStackHandler;
//?}

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PanelaBlockEntity extends BlockEntity implements MenuProvider {
    public static final int GRID_SIZE = 9;
    public static final int GRID_WIDTH = 3;
    public static final int OUTPUT_SLOT = 9;
    public static final int SLOT_COUNT = 10;

    public static final TagKey<Block> HEAT_SOURCES =
            TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Fartura.MODID, "heat_sources"));

    private static final int DATA_PROGRESS = 0;
    private static final int DATA_MAX_PROGRESS = 1;
    private static final int DATA_HEATED = 2;

    private final ItemStackHandler inventory = new ItemStackHandler(SLOT_COUNT) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private int progress;
    private int maxProgress = PanelaRecipe.DEFAULT_COOKING_TIME;
    private boolean heated;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case DATA_PROGRESS -> progress;
                case DATA_MAX_PROGRESS -> maxProgress;
                case DATA_HEATED -> heated ? 1 : 0;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case DATA_PROGRESS -> progress = value;
                case DATA_MAX_PROGRESS -> maxProgress = value;
                case DATA_HEATED -> heated = value != 0;
                default -> {
                }
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public PanelaBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.PANELA.get(), pos, state);
    }

    protected PanelaBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public ContainerData getDataAccess() {
        return data;
    }

    /** Fonte de calor embaixo da panela: fogo, fogueira, churrasqueira ou qualquer bloco da tag {@code fartura:heat_sources}. */
    public static boolean isHeated(Level level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        if (below.hasProperty(BlockStateProperties.LIT) && !below.getValue(BlockStateProperties.LIT)) {
            return false;
        }
        return below.is(HEAT_SOURCES) || below.getBlock() instanceof BaseFireBlock;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PanelaBlockEntity panela) {
        panela.heated = isHeated(level, pos);

        Optional<RecipeHolder<PanelaRecipe>> recipe = panela.findRecipe(level);

        if (!panela.heated || recipe.isEmpty() || !panela.canOutput(recipe.get().value().getResult())) {
            panela.progress = 0;
            return;
        }

        panela.maxProgress = recipe.get().value().getCookingTime();
        panela.progress++;
        if (panela.progress >= panela.maxProgress) {
            panela.progress = 0;
            panela.craft(level, recipe.get().value());
        }
    }

    /** Verdadeiro quando há uma receita válida e o resultado cabe na saída; usado pela churrasqueira para acender. */
    public boolean needsHeat(Level level) {
        Optional<RecipeHolder<PanelaRecipe>> recipe = findRecipe(level);
        return recipe.isPresent() && canOutput(recipe.get().value().getResult());
    }

    private Optional<RecipeHolder<PanelaRecipe>> findRecipe(Level level) {
        return level.getRecipeManager().getRecipeFor(recipeType(), craftingInput(), level);
    }

    /** Tipo de receita usado por este bloco; subclasses usam o próprio tipo. */
    protected RecipeType<PanelaRecipe> recipeType() {
        return ModRecipes.PANELA_TYPE.get();
    }

    private CraftingInput craftingInput() {
        List<ItemStack> items = new ArrayList<>(GRID_SIZE);
        for (int i = 0; i < GRID_SIZE; i++) {
            items.add(inventory.getStackInSlot(i));
        }
        return CraftingInput.of(GRID_WIDTH, GRID_WIDTH, items);
    }

    private boolean canOutput(ItemStack result) {
        return inventory.insertItem(OUTPUT_SLOT, result.copy(), true).isEmpty();
    }

    private void craft(Level level, PanelaRecipe recipe) {
        for (int i = 0; i < GRID_SIZE; i++) {
            ItemStack ingredient = inventory.getStackInSlot(i);
            if (ingredient.isEmpty()) {
                continue;
            }
            ItemStack remainder = ingredient.getCraftingRemainingItem();
            inventory.extractItem(i, 1, false);
            if (!remainder.isEmpty()) {
                ItemStack leftover = inventory.insertItem(i, remainder, false);
                if (!leftover.isEmpty()) {
                    Containers.dropItemStack(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5,
                            worldPosition.getZ() + 0.5, leftover);
                }
            }
        }
        inventory.insertItem(OUTPUT_SLOT, recipe.getResult().copy(), false);
        setChanged();
    }

    public void dropContents(Level level, BlockPos pos) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
            }
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.fartura.panela");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new PanelaMenu(containerId, playerInventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
        tag.putInt("Progress", progress);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        progress = tag.getInt("Progress");
    }
}
