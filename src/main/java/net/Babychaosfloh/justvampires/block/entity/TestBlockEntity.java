package net.Babychaosfloh.justvampires.block.entity;

import net.Babychaosfloh.justvampires.JustVampires;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.*;

public class TestBlockEntity extends BlockEntity {
    public final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                JustVampires.print("slot " + slot + " changed!");
            }
        }
    };

    public static final int HEAD_SLOT = 0;
    public static final int SYRINGE_SLOT = 1;

    private LazyOptional lazyItemHandler = LazyOptional.empty();

    public SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());

    public TestBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.TEST_BE.get(), pPos, pBlockState);
    }

//    public ItemStack getHeadStack() {
//        if(!itemHandler.getStackInSlot(0).isEmpty()) {
//            return itemHandler.getStackInSlot(0);
//        } else {
//            return new ItemStack(Items.BARRIER);
//        }
//    }
//
//    public ItemStack getSyringeStack() {
//        if(!itemHandler.getStackInSlot(1).isEmpty()) {
//            return itemHandler.getStackInSlot(1);
//        } else {
//            return new ItemStack(Items.BARRIER);
//        }
//    }

    public ItemStack getRenderStack() {
        if(!itemHandler.getStackInSlot(0).isEmpty()) {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                inventory.setItem(i, itemHandler.getStackInSlot(i));
            }
            return itemHandler.getStackInSlot(0);
        } else {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                inventory.setItem(i, itemHandler.getStackInSlot(i));
            }
            return itemHandler.getStackInSlot(1);
        }
    }

    public void sync() {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().chunkMap.getPlayers(new ChunkPos(this.worldPosition), false)
                    .forEach(player -> player.connection.send(this.getUpdatePacket()));
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    public void drops() {
        //SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
    }
}
