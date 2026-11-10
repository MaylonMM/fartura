package com.fartura.block.entity;

import com.fartura.registry.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CuttingBoardBlockEntity extends BlockEntity {
    private ItemStack stack = ItemStack.EMPTY;

    public CuttingBoardBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CUTTING_BOARD.get(), pos, state);
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
        syncToClient();
    }

    private void syncToClient() {
        setChanged();
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            // Enviado direto: sem mudança de estado do bloco, o cliente não recebe a atualização de dados sozinho.
            Packet<ClientGamePacketListener> packet = getUpdatePacket();
            for (ServerPlayer player : serverLevel.players()) {
                if (worldPosition.closerThan(player.blockPosition(), 128)) {
                    player.connection.send(packet);
                }
            }
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

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        // Sempre gravado (mesmo vazio): senão o cliente não limpa o item ao receber a atualização.
        tag.put("Item", stack.saveOptional(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        stack = ItemStack.parseOptional(registries, tag.getCompound("Item"));
    }
}
