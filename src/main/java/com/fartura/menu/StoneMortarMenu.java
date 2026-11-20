package com.fartura.menu;

import com.fartura.block.entity.StoneMortarBlockEntity;
import com.fartura.recipe.MortarRecipe;
import com.fartura.registry.ModMenus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
//? if neoforge {
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
//?} else {
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
//?}

public class StoneMortarMenu extends RecipeBookMenu<SingleRecipeInput, MortarRecipe> {
    private static final int PLAYER_INV_Y = 84;
    private static final int HOTBAR_Y = 142;

    private final ItemStackHandler inventory;
    private final ContainerData data;

    /** Construtor do cliente, usado ao abrir o menu pelo pacote. */
    public StoneMortarMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory, new ItemStackHandler(2), new SimpleContainerData(2));
    }

    /** Construtor do servidor, ligado ao estado do bloco. */
    public StoneMortarMenu(int containerId, Inventory playerInventory, StoneMortarBlockEntity blockEntity) {
        this(containerId, playerInventory, blockEntity.getInventory(), blockEntity.getDataAccess());
    }

    private StoneMortarMenu(int containerId, Inventory playerInventory, ItemStackHandler inventory, ContainerData data) {
        super(ModMenus.STONE_MORTAR.get(), containerId);
        this.inventory = inventory;
        this.data = data;

        addSlot(new SlotItemHandler(inventory, StoneMortarBlockEntity.INPUT_SLOT, 56, 35));
        addSlot(new SlotItemHandler(inventory, StoneMortarBlockEntity.OUTPUT_SLOT, 116, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, PLAYER_INV_Y + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, HOTBAR_Y));
        }

        addDataSlots(data);
    }

    public int getScaledProgress() {
        int progress = data.get(0);
        int maxProgress = data.get(1);
        int arrowWidth = 24;
        return maxProgress != 0 && progress != 0 ? progress * arrowWidth / maxProgress : 0;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents contents) {
        contents.accountStack(inventory.getStackInSlot(StoneMortarBlockEntity.INPUT_SLOT));
    }

    @Override
    public void clearCraftingContent() {
        inventory.setStackInSlot(StoneMortarBlockEntity.INPUT_SLOT, ItemStack.EMPTY);
    }

    @Override
    public boolean recipeMatches(RecipeHolder<MortarRecipe> recipe) {
        return recipe.value().getIngredient().test(inventory.getStackInSlot(StoneMortarBlockEntity.INPUT_SLOT));
    }

    @Override
    public int getResultSlotIndex() {
        return StoneMortarBlockEntity.OUTPUT_SLOT;
    }

    @Override
    public int getGridWidth() {
        return 1;
    }

    @Override
    public int getGridHeight() {
        return 1;
    }

    @Override
    public int getSize() {
        return 2;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.FURNACE;
    }

    @Override
    public boolean shouldMoveToInventory(int index) {
        return index != getResultSlotIndex();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack copy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            copy = stack.copy();

            int outputSlot = StoneMortarBlockEntity.OUTPUT_SLOT;
            int inputSlot = StoneMortarBlockEntity.INPUT_SLOT;
            int playerStart = 2;
            int playerEnd = 38;

            if (index == outputSlot) {
                if (!moveItemStackTo(stack, playerStart, playerEnd, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, copy);
            } else if (index == inputSlot) {
                if (!moveItemStackTo(stack, playerStart, playerEnd, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!moveItemStackTo(stack, inputSlot, inputSlot + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (stack.getCount() == copy.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, stack);
        }
        return copy;
    }
}
