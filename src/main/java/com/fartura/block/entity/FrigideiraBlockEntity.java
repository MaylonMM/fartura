package com.fartura.block.entity;

import com.fartura.block.FrigideiraBlock;
import com.fartura.recipe.FrigideiraRecipe;
import com.fartura.registry.ModBlockEntities;
import com.fartura.registry.ModRecipes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.Optional;

/** Frigideira de ferro: sem GUI, até 4 itens visíveis, cada um cozinha na sua receita da frigideira. */
public class FrigideiraBlockEntity extends BlockEntity {
    public static final int SLOTS = 4;
    /** Posição de cada slot dentro do bloco (x, y, z em blocos). */
    public static final Vec3[] SLOT_POSITIONS = {
            new Vec3(0.4125, 0.0625, 0.4125),
            new Vec3(0.5875, 0.0625, 0.4125),
            new Vec3(0.4125, 0.0625, 0.5875),
            new Vec3(0.5875, 0.0625, 0.5875)
    };

    private final ItemStackHandler inventory = new ItemStackHandler(SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            syncToClient();
        }
    };

    private final int[] cookingProgress = new int[SLOTS];
    private final int[] cookingTime = new int[SLOTS];

    public FrigideiraBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FRIGIDEIRA.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FrigideiraBlockEntity frigideira) {
        boolean burning = PanelaBlockEntity.isHeated(level, pos) && frigideira.hasCookableFood();

        if (burning) {
            frigideira.cook();
        } else {
            frigideira.cool();
        }

        if (state.getValue(FrigideiraBlock.LIT) != burning) {
            level.setBlock(pos, state.setValue(FrigideiraBlock.LIT, burning), 3);
        }
    }

    private void cook() {
        for (int i = 0; i < SLOTS; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            Optional<RecipeHolder<FrigideiraRecipe>> recipe = findRecipe(stack);
            if (recipe.isEmpty()) {
                continue;
            }
            cookingProgress[i]++;
            if (cookingProgress[i] >= cookingTime[i]) {
                cookingProgress[i] = 0;
                ItemStack result = recipe.get().value().assemble(new SingleRecipeInput(stack), level.registryAccess());
                inventory.setStackInSlot(i, ItemStack.EMPTY);
                ejectItem(i, result);
            }
        }
        setChanged();
    }

    /** Faz o item pular para fora do slot, com um pequeno empurrão para longe do centro. */
    private void ejectItem(int slot, ItemStack stack) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        Vec3 offset = SLOT_POSITIONS[slot];
        double dx = offset.x - 0.5;
        double dz = offset.z - 0.5;
        double x = worldPosition.getX() + offset.x;
        double y = worldPosition.getY() + offset.y + 0.1;
        double z = worldPosition.getZ() + offset.z;
        ItemEntity entity = new ItemEntity(serverLevel, x, y, z, stack);
        entity.setDeltaMovement(dx * 0.4, 0.3, dz * 0.4);
        entity.setPickUpDelay(10);
        serverLevel.addFreshEntity(entity);
    }

    private void cool() {
        boolean changed = false;
        for (int i = 0; i < SLOTS; i++) {
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
        Optional<RecipeHolder<FrigideiraRecipe>> recipe = findRecipe(stack);
        if (recipe.isEmpty()) {
            return false;
        }
        for (int i = 0; i < SLOTS; i++) {
            if (inventory.getStackInSlot(i).isEmpty()) {
                inventory.setStackInSlot(i, stack);
                cookingProgress[i] = 0;
                cookingTime[i] = recipe.get().value().getCookingTime();
                return true;
            }
        }
        return false;
    }

    private Optional<RecipeHolder<FrigideiraRecipe>> findRecipe(ItemStack stack) {
        if (level == null || stack.isEmpty()) {
            return Optional.empty();
        }
        return level.getRecipeManager().getRecipeFor(ModRecipes.FRIGIDEIRA_TYPE.get(), new SingleRecipeInput(stack), level);
    }

    /** Verdadeiro quando algum item ainda tem receita da frigideira; usado pela churrasqueira para acender. */
    public boolean hasCookableFood() {
        for (int i = 0; i < SLOTS; i++) {
            if (findRecipe(inventory.getStackInSlot(i)).isPresent()) {
                return true;
            }
        }
        return false;
    }

    /** Ticks restantes por slot; -1 quando o slot está vazio ou sem receita. */
    public int[] getRemainingCookingTicks() {
        int[] remaining = new int[SLOTS];
        for (int i = 0; i < SLOTS; i++) {
            remaining[i] = findRecipe(inventory.getStackInSlot(i)).isPresent()
                    ? Math.max(0, cookingTime[i] - cookingProgress[i])
                    : -1;
        }
        return remaining;
    }

    public boolean hasFood() {
        for (int i = 0; i < SLOTS; i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public ItemStack takeFood() {
        for (int i = 0; i < SLOTS; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                cookingProgress[i] = 0;
                inventory.setStackInSlot(i, ItemStack.EMPTY);
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
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
        tag.putIntArray("CookingProgress", cookingProgress);
        tag.putIntArray("CookingTime", cookingTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        int[] progress = tag.getIntArray("CookingProgress");
        int[] times = tag.getIntArray("CookingTime");
        for (int i = 0; i < SLOTS; i++) {
            cookingProgress[i] = i < progress.length ? progress[i] : 0;
            cookingTime[i] = i < times.length ? times[i] : 0;
        }
    }
}
