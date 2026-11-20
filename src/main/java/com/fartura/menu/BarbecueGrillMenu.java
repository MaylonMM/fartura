package com.fartura.menu;

import com.fartura.block.entity.BarbecueGrillBlockEntity;
import com.fartura.registry.ModMenus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
//? if neoforge {
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
//?} else {
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
//?}

public class BarbecueGrillMenu extends AbstractContainerMenu {
    private static final int PLAYER_INV_Y = 84;
    private static final int HOTBAR_Y = 142;

    private final ItemStackHandler inventory;
    private final ContainerData data;

    /** Construtor do cliente, usado ao abrir o menu pelo pacote. */
    public BarbecueGrillMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory, new ItemStackHandler(1 + BarbecueGrillBlockEntity.GRILL_SLOTS), new SimpleContainerData(2));
    }

    /** Construtor do servidor, ligado ao estado do bloco. */
    public BarbecueGrillMenu(int containerId, Inventory playerInventory, BarbecueGrillBlockEntity blockEntity) {
        this(containerId, playerInventory, blockEntity.getInventory(), blockEntity.getDataAccess());
    }

    private BarbecueGrillMenu(int containerId, Inventory playerInventory, ItemStackHandler inventory, ContainerData data) {
        super(ModMenus.BARBECUE_GRILL.get(), containerId);
        this.inventory = inventory;
        this.data = data;

        addSlot(new SlotItemHandler(inventory, BarbecueGrillBlockEntity.FUEL_SLOT, 80, 53));

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

    public int getScaledLitProgress() {
        int litTime = data.get(0);
        int litDuration = data.get(1);
        int flameHeight = 14;
        return litDuration != 0 && litTime != 0 ? litTime * flameHeight / litDuration : 0;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack copy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            copy = stack.copy();

            int fuelSlot = 0;
            int playerStart = 1;
            int playerEnd = 37;

            if (index == fuelSlot) {
                if (!moveItemStackTo(stack, playerStart, playerEnd, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (BarbecueGrillBlockEntity.getFuelBurnTime(stack) > 0) {
                if (!moveItemStackTo(stack, fuelSlot, fuelSlot + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                return ItemStack.EMPTY;
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
