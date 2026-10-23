package com.fartura.block.entity;

import com.fartura.menu.StoneMortarMenu;
import com.fartura.recipe.MortarRecipe;
import com.fartura.registry.ModBlockEntities;
import com.fartura.registry.ModRecipes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.Optional;

public class StoneMortarBlockEntity extends BlockEntity implements MenuProvider {
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    public static final int PROCESSING_TIME = 200;

    private final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private int progress;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return index == 0 ? progress : PROCESSING_TIME;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) {
                progress = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public StoneMortarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STONE_MORTAR.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public ContainerData getDataAccess() {
        return data;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, StoneMortarBlockEntity mortar) {
        Optional<RecipeHolder<MortarRecipe>> recipe = level.getRecipeManager().getRecipeFor(
                ModRecipes.MORTAR_TYPE.get(),
                new SingleRecipeInput(mortar.inventory.getStackInSlot(INPUT_SLOT)),
                level);

        if (recipe.isEmpty() || !mortar.canOutput(recipe.get().value().getResult())) {
            if (mortar.progress != 0) {
                mortar.progress = 0;
                mortar.setChanged();
            }
            return;
        }

        mortar.progress++;
        if (mortar.progress >= PROCESSING_TIME) {
            mortar.progress = 0;
            mortar.inventory.extractItem(INPUT_SLOT, 1, false);
            mortar.inventory.insertItem(OUTPUT_SLOT, recipe.get().value().getResult().copy(), false);
        }
        mortar.setChanged();
    }

    private boolean canOutput(ItemStack result) {
        return inventory.insertItem(OUTPUT_SLOT, result.copy(), true).isEmpty();
    }

    public void dropContents(Level level, BlockPos pos) {
        Container drops = new Container() {
            @Override
            public int getContainerSize() {
                return inventory.getSlots();
            }

            @Override
            public boolean isEmpty() {
                return inventory.getStackInSlot(INPUT_SLOT).isEmpty() && inventory.getStackInSlot(OUTPUT_SLOT).isEmpty();
            }

            @Override
            public ItemStack getItem(int slot) {
                return inventory.getStackInSlot(slot);
            }

            @Override
            public ItemStack removeItem(int slot, int amount) {
                return inventory.extractItem(slot, amount, false);
            }

            @Override
            public ItemStack removeItemNoUpdate(int slot) {
                ItemStack stack = inventory.getStackInSlot(slot);
                inventory.setStackInSlot(slot, ItemStack.EMPTY);
                return stack;
            }

            @Override
            public void setItem(int slot, ItemStack stack) {
                inventory.setStackInSlot(slot, stack);
            }

            @Override
            public void setChanged() {
            }

            @Override
            public boolean stillValid(Player player) {
                return true;
            }

            @Override
            public void clearContent() {
                for (int i = 0; i < inventory.getSlots(); i++) {
                    inventory.setStackInSlot(i, ItemStack.EMPTY);
                }
            }
        };
        Containers.dropContents(level, pos, drops);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.fartura.stone_mortar");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new StoneMortarMenu(containerId, playerInventory, this);
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
