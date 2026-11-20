package com.fartura.menu;

import com.fartura.block.entity.KettleBlockEntity;
import com.fartura.block.entity.PanelaBlockEntity;
import com.fartura.registry.ModMenus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.SimpleContainerData;
//? if neoforge {
import net.neoforged.neoforge.items.ItemStackHandler;
//?} else {
import net.minecraftforge.items.ItemStackHandler;
//?}

public class KettleMenu extends PanelaMenu {
    /** Construtor do cliente, usado ao abrir o menu pelo pacote. */
    public KettleMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory, new ItemStackHandler(PanelaBlockEntity.SLOT_COUNT), new SimpleContainerData(3));
    }

    /** Construtor do servidor, ligado ao estado do bloco. */
    public KettleMenu(int containerId, Inventory playerInventory, KettleBlockEntity blockEntity) {
        this(containerId, playerInventory, blockEntity.getInventory(), blockEntity.getDataAccess());
    }

    private KettleMenu(int containerId, Inventory playerInventory, ItemStackHandler inventory,
                         net.minecraft.world.inventory.ContainerData data) {
        super(ModMenus.KETTLE.get(), containerId, playerInventory, inventory, data);
    }
}
