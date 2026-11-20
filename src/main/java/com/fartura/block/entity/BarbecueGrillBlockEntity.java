package com.fartura.block.entity;

import com.fartura.block.BarbecueGrillBlock;
import com.fartura.menu.BarbecueGrillMenu;
import com.fartura.registry.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
//? if neoforge {
import net.neoforged.neoforge.items.ItemStackHandler;
//?} else {
import net.minecraftforge.items.ItemStackHandler;
//?}
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;

import java.util.Optional;

public class BarbecueGrillBlockEntity extends BlockEntity implements MenuProvider {
    public static final int FUEL_SLOT = 0;
    public static final int FIRST_GRILL_SLOT = 1;
    public static final int GRILL_SLOTS = 4;

    private final ItemStackHandler inventory = new ItemStackHandler(1 + GRILL_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            syncToClient();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return slot == FUEL_SLOT && getFuelBurnTime(stack) > 0;
        }
    };

    // Forge returns -1 from ItemStack#getBurnTime for vanilla fuels; ForgeHooks resolves them.
    public static int getFuelBurnTime(ItemStack stack) {
        //? if neoforge {
        return stack.getBurnTime(RecipeType.SMELTING);
        //?} else {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
        //?}
    }

    private final int[] cookingProgress = new int[GRILL_SLOTS];
    private final int[] cookingTime = new int[GRILL_SLOTS];
    private int litTime;
    private int litDuration;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return index == 0 ? litTime : litDuration;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) {
                litTime = value;
            } else {
                litDuration = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public BarbecueGrillBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BARBECUE_GRILL.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public ContainerData getDataAccess() {
        return data;
    }

    public int[] getCookingProgress() {
        return cookingProgress;
    }

    /** Ticks restantes por slot da grelha; -1 quando o slot está vazio. */
    public int[] getRemainingCookingTicks() {
        int[] remaining = new int[GRILL_SLOTS];
        for (int i = 0; i < GRILL_SLOTS; i++) {
            remaining[i] = inventory.getStackInSlot(FIRST_GRILL_SLOT + i).isEmpty()
                    ? -1
                    : Math.max(0, cookingTime[i] - cookingProgress[i]);
        }
        return remaining;
    }

    /** Panela com receita válida ou frigideira com comida em cima da churrasqueira: a grelha deve acender mesmo sem comida. */
    private static boolean panelaNeedsHeat(Level level, BlockPos pos) {
        BlockEntity above = level.getBlockEntity(pos.above());
        if (above instanceof PanelaBlockEntity panela) {
            return panela.needsHeat(level);
        }
        return above instanceof FrigideiraBlockEntity frigideira && frigideira.hasCookableFood();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BarbecueGrillBlockEntity grill) {
        boolean burning = grill.litTime > 0;
        if (burning) {
            grill.litTime--;
        }

        if (!burning && (grill.hasFood() || panelaNeedsHeat(level, pos)) && grill.hasFuel()) {
            ItemStack fuel = grill.inventory.extractItem(FUEL_SLOT, 1, false);
            int burnTime = getFuelBurnTime(fuel);
            grill.litDuration = burnTime;
            grill.litTime = burnTime;
            burning = true;
            grill.setChanged();
        }

        if (burning) {
            grill.cook(level, pos);
        } else {
            grill.cool();
        }

        if (state.getValue(BarbecueGrillBlock.LIT) != burning) {
            level.setBlock(pos, state.setValue(BarbecueGrillBlock.LIT, burning), 3);
        }
    }

    private void cook(Level level, BlockPos pos) {
        for (int i = 0; i < GRILL_SLOTS; i++) {
            int slot = FIRST_GRILL_SLOT + i;
            if (inventory.getStackInSlot(slot).isEmpty()) {
                continue;
            }
            cookingProgress[i]++;
            if (cookingProgress[i] >= cookingTime[i]) {
                cookingProgress[i] = 0;
                ItemStack result = findResult(inventory.getStackInSlot(slot));
                inventory.setStackInSlot(slot, ItemStack.EMPTY);
                Containers.dropItemStack(level, pos.getX(), pos.getY() + 1, pos.getZ(), result);
            }
        }
        setChanged();
    }

    private void cool() {
        boolean changed = false;
        for (int i = 0; i < GRILL_SLOTS; i++) {
            if (cookingProgress[i] > 0) {
                cookingProgress[i] = Math.max(0, cookingProgress[i] - 2);
                changed = true;
            }
        }
        if (changed) {
            setChanged();
        }
    }

    public boolean tryAddItem(ItemStack stack) {
        if (level == null || stack.isEmpty()) {
            return false;
        }
        Optional<RecipeHolder<CampfireCookingRecipe>> recipe = findRecipe(stack);
        if (recipe.isEmpty()) {
            return false;
        }
        for (int i = 0; i < GRILL_SLOTS; i++) {
            int slot = FIRST_GRILL_SLOT + i;
            if (inventory.getStackInSlot(slot).isEmpty()) {
                inventory.setStackInSlot(slot, stack);
                cookingProgress[i] = 0;
                cookingTime[i] = recipe.get().value().getCookingTime();
                return true;
            }
        }
        return false;
    }

    private Optional<RecipeHolder<CampfireCookingRecipe>> findRecipe(ItemStack stack) {
        return level.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SingleRecipeInput(stack), level);
    }

    private ItemStack findResult(ItemStack input) {
        return findRecipe(input)
                .map(holder -> holder.value().assemble(new SingleRecipeInput(input), level.registryAccess()))
                .orElse(input.copy());
    }

    private boolean hasFuel() {
        return !inventory.getStackInSlot(FUEL_SLOT).isEmpty();
    }

    public boolean hasFood() {
        for (int i = 0; i < GRILL_SLOTS; i++) {
            if (!inventory.getStackInSlot(FIRST_GRILL_SLOT + i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public ItemStack takeFood() {
        for (int i = 0; i < GRILL_SLOTS; i++) {
            int slot = FIRST_GRILL_SLOT + i;
            ItemStack stack = inventory.getStackInSlot(slot);
            if (!stack.isEmpty()) {
                cookingProgress[i] = 0;
                inventory.setStackInSlot(slot, ItemStack.EMPTY);
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private void syncToClient() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void dropContents(Level level, BlockPos pos) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
            }
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.fartura.barbecue_grill");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new BarbecueGrillMenu(containerId, playerInventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
        tag.putIntArray("CookingProgress", cookingProgress);
        tag.putIntArray("CookingTime", cookingTime);
        tag.putInt("LitTime", litTime);
        tag.putInt("LitDuration", litDuration);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        int[] progress = tag.getIntArray("CookingProgress");
        int[] times = tag.getIntArray("CookingTime");
        for (int i = 0; i < GRILL_SLOTS; i++) {
            cookingProgress[i] = i < progress.length ? progress[i] : 0;
            cookingTime[i] = i < times.length ? times[i] : 0;
        }
        litTime = tag.getInt("LitTime");
        litDuration = tag.getInt("LitDuration");
    }
}
