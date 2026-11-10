package com.fartura.menu;

import com.fartura.block.entity.PanelaBlockEntity;
import com.fartura.Fartura;
import com.fartura.registry.ModMenus;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class PanelaMenu extends AbstractContainerMenu {
    private static final int GRID_X = 30;
    private static final int GRID_Y = 17;
    private static final int OUTPUT_X = 124;
    private static final int OUTPUT_Y = 26;
    private static final int PLAYER_INV_Y = 84;
    private static final int HOTBAR_Y = 142;

    private final ItemStackHandler inventory;
    private final ContainerData data;

    /** Construtor do cliente, usado ao abrir o menu pelo pacote. */
    public PanelaMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
        this(containerId, playerInventory, new ItemStackHandler(PanelaBlockEntity.SLOT_COUNT), new SimpleContainerData(3));
    }

    /** Construtor do servidor, ligado ao estado do bloco. */
    public PanelaMenu(int containerId, Inventory playerInventory, PanelaBlockEntity blockEntity) {
        this(containerId, playerInventory, blockEntity.getInventory(), blockEntity.getDataAccess());
    }

    private PanelaMenu(int containerId, Inventory playerInventory, ItemStackHandler inventory, ContainerData data) {
        this(ModMenus.PANELA.get(), containerId, playerInventory, inventory, data);
    }

    protected PanelaMenu(MenuType<?> type, int containerId, Inventory playerInventory, ItemStackHandler inventory, ContainerData data) {
        super(type, containerId);
        this.inventory = inventory;
        this.data = data;

        for (int row = 0; row < PanelaBlockEntity.GRID_WIDTH; row++) {
            for (int col = 0; col < PanelaBlockEntity.GRID_WIDTH; col++) {
                int index = col + row * PanelaBlockEntity.GRID_WIDTH;
                addSlot(new SlotItemHandler(inventory, index, GRID_X + col * 18, GRID_Y + row * 18));
            }
        }

        addSlot(new SlotItemHandler(inventory, PanelaBlockEntity.OUTPUT_SLOT, OUTPUT_X, OUTPUT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                super.onTake(player, stack);
                if (player instanceof ServerPlayer serverPlayer && serverPlayer.getServer() != null) {
                    AdvancementHolder advancement = serverPlayer.getServer().getAdvancements()
                            .get(ResourceLocation.fromNamespaceAndPath(Fartura.MODID, "good_smell"));
                    if (advancement != null) {
                        serverPlayer.getAdvancements().award(advancement, "took_from_pot");
                    }
                }
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

    public boolean isHeated() {
        return data.get(2) != 0;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack copy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return copy;
        }

        ItemStack stack = slot.getItem();
        copy = stack.copy();

        int outputIndex = PanelaBlockEntity.OUTPUT_SLOT;
        int playerStart = PanelaBlockEntity.SLOT_COUNT;
        int playerEnd = playerStart + 36;

        if (index == outputIndex) {
            if (!moveItemStackTo(stack, playerStart, playerEnd, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(stack, copy);
        } else if (index < outputIndex) {
            if (!moveItemStackTo(stack, playerStart, playerEnd, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!moveItemStackTo(stack, 0, PanelaBlockEntity.GRID_SIZE, false)) {
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
        return copy;
    }
}
