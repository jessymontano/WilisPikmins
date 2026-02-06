package net.wili.wilispikmins.screen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.wili.wilispikmins.block.entity.OnionBlockEntity;
import net.wili.wilispikmins.data.OnionComponents;
import net.wili.wilispikmins.data.OnionData;
import net.wili.wilispikmins.entity.ModEntities;
import net.wili.wilispikmins.entity.custom.PikminEntity;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import net.wili.wilispikmins.item.custom.OnionUpgradeItem;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class OnionMenu extends AbstractContainerMenu {
    public final OnionBlockEntity blockEntity;
    private final Level level;
    private final Player player;

    private final Map<PikminType, Integer> pendingTakeOut = new EnumMap<>(PikminType.class);
    private final Map<PikminType, Integer> pendingPutIn = new EnumMap<>(PikminType.class);

    private static final int VALUES_PER_TYPE = 6;
    private static final int DATA_COUNT = PikminType.values().length * VALUES_PER_TYPE;
    private final ContainerData data;

    private static final int OFF_UNLOCKED = 0;
    private static final int OFF_STORED = 1;
    private static final int OFF_OUTSIDE = 2;
    private static final int OFF_PENDING_TAKE_OUT = 3;
    private static final int OFF_PENDING_PUT_IN = 4;
    private static final int OFF_CAPACITY = 5;

    private static final int INV_START_Y = 110;

    public OnionMenu(int pContainerId, Inventory inv, BlockEntity entity) {
        super(ModMenuTypes.ONION_MENU.get(), pContainerId);
        this.blockEntity = (OnionBlockEntity) entity;
        this.level = inv.player.level();
        this.player = inv.player;
        ItemStackHandler itemHandler = blockEntity.getItemHandler();

        for (PikminType type : PikminType.values()) {
            pendingTakeOut.put(type, 0);
            pendingPutIn.put(type, 0);
        }

        this.data = new SimpleContainerData(DATA_COUNT);

        if (!level.isClientSide) {
            updateDataFromPlayer();
        }

        this.addSlot(new SlotItemHandler(itemHandler, 0, 152, 87) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() instanceof OnionUpgradeItem;
            }

            @Override
            public int getMaxStackSize() {
                return 64;
            }
        });

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        addDataSlots(data);
    }

    public OnionMenu(int pContainerId, Inventory inv, FriendlyByteBuf buf) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(buf.readBlockPos()));
    }

    private OnionData getPlayerData() {
        return player.getData(OnionComponents.PLAYER_ONION_DATA);
    }

    private OnionData getBlockData() {
        return blockEntity.getOnionData();
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 1;  // must be the number of slots you have!
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return blockEntity != null
                && !blockEntity.isRemoved()
                && player.distanceToSqr(
                        blockEntity.getBlockPos().getX() + 0.5,
                blockEntity.getBlockPos().getY() + 0.5,
                blockEntity.getBlockPos().getZ() + 0.5
        ) <= 64.0;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, INV_START_Y + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 168));
        }
    }

    public boolean isUnlocked(PikminType type) {
       if (level.isClientSide) {
            int index = type.ordinal() * VALUES_PER_TYPE + OFF_UNLOCKED;
            return data.get(index) == 1;
        }
        return getPlayerData().hasUnlocked(type);
    }

    public int getStored(PikminType type) {
        if (level.isClientSide) {
            int index = type.ordinal() * VALUES_PER_TYPE + OFF_STORED;
            return data.get(index);
        }

        return getBlockData().getStored(type);
    }

    public int getCapacity(PikminType type) {
        if (level.isClientSide) {
            int index = type.ordinal() * VALUES_PER_TYPE + OFF_CAPACITY;
            return data.get(index);
        }
        return getPlayerData().getCapacity(type);
    }

    public int getOutside(PikminType type) {
        if (level.isClientSide) {
            int index = type.ordinal() * VALUES_PER_TYPE + OFF_OUTSIDE;
            return data.get(index);
        }
        return getPlayerData().getOutside(type);
    }

    public void takeOut(PikminType type) {
        if (level.isClientSide) return;

        OnionData blockData = getBlockData();

        int stored = blockData.getStored(type);
        int takeOut = pendingTakeOut.get(type);
        int putIn = pendingPutIn.get(type);

        if (putIn > 0) {
            pendingPutIn.put(type, putIn - 1);
        }

        if (stored - takeOut > 0 && getTotalPending() < 100) {
            pendingTakeOut.put(type, takeOut + 1);
        }
        sync();
    }

    public void putIn(PikminType type) {
        if (level.isClientSide) return;

        OnionData playerData = getPlayerData();

        int outside = playerData.getOutside(type);
        int capacity = playerData.getCapacity(type);
        int takeOut = pendingTakeOut.get(type);
        int putIn = pendingPutIn.get(type);

        if (takeOut > 0 && getTotalPending() < 100) {
            pendingTakeOut.put(type, takeOut - 1);
        }

        if (outside - putIn > 0 && putIn < capacity) {
            pendingPutIn.put(type, putIn + 1);
        }

        sync();
    }

    public int getTotalPending() {
        return pendingTakeOut.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int getPendingTakeOut(PikminType type) {
        if (level.isClientSide) {
            int i = type.ordinal() * VALUES_PER_TYPE;
            return data.get(i + OFF_PENDING_TAKE_OUT);
        }
        return pendingTakeOut.getOrDefault(type, 0);
    }

    public int getPendingPutIn(PikminType type) {
        if (level.isClientSide) {
            int i = type.ordinal() * VALUES_PER_TYPE;
            return data.get(i + OFF_PENDING_PUT_IN);
        }
        return pendingPutIn.getOrDefault(type, 0);
    }

    public void confirmOperations() {
        if (level.isClientSide) return;
        OnionData playerData = getPlayerData();
        OnionData updatedPlayerData = playerData;

        for (PikminType type : PikminType.values()) {
            int out = pendingTakeOut.get(type);
            int in  = pendingPutIn.get(type);

            if (out > 0 && playerData.canTakeOut(type, out)) {
                updatedPlayerData = updatedPlayerData
                        .addOutside(type, out)
                        .addStored(type, -out);

                spawnPikmin(type, out);
            }

            if (in > 0 && playerData.canPutIn(type, in)) {
                updatedPlayerData = updatedPlayerData
                        .addOutside(type, -in)
                        .addStored(type, in);

                recallPikmins(type, in);
            }
        }

        player.setData(OnionComponents.PLAYER_ONION_DATA, updatedPlayerData);

        blockEntity.setOnionData(updatedPlayerData);

        clearOperations();
        updateDataFromPlayer();
        broadcastChanges();
    }

    public void clearOperations() {
        for (PikminType type : PikminType.values()) {
            pendingTakeOut.put(type, 0);
            pendingPutIn.put(type, 0);
        }
        sync();
    }

    private void sync() {
        if (!level.isClientSide) {
            updateDataFromPlayer();
            broadcastChanges();
        }
    }

    public void recallAllPikmins() {
        if (level.isClientSide) return;
        if (!(level instanceof ServerLevel serverLevel)) return;

        OnionData playerData = getPlayerData();
        OnionData blockData = getBlockData();
        UUID playerId = player.getUUID();

        Map<PikminType, Integer> outsideCouts = new EnumMap<>(PikminType.class);
        for (PikminType type : PikminType.values()) {
            outsideCouts.put(type, 0);
        }

        for (Entity entity: serverLevel.getAllEntities()) {
            if (entity instanceof PikminEntity pikmin) {
                UUID ownerId = pikmin.getOwnerUUID();
                if (ownerId != null && ownerId.equals(playerId)) {
                    PikminType type = pikmin.getPikminType();
                    outsideCouts.put(type, outsideCouts.get(type) + 1);
                }
            }
        }

        for (PikminType type : PikminType.values()) {
            if (!playerData.hasUnlocked(type)) continue;

            int toRecall = outsideCouts.get(type);
            int stored = blockData.getStored(type);
            int capacity = playerData.getCapacity(type);
            int canStore = Math.min(toRecall, capacity - stored);

            if (canStore > 0) {
                OnionData newBlockData = blockData.addStored(type, canStore);
                blockEntity.setOnionData(newBlockData);
                blockData = newBlockData;

                OnionData newPlayerData = playerData.addOutside(type, -canStore);
                player.setData(OnionComponents.PLAYER_ONION_DATA, newPlayerData);
                playerData = newPlayerData;

                int recalled = 0;
                for (Entity entity: serverLevel.getAllEntities()) {
                    if (recalled >= canStore) break;
                    if (entity instanceof PikminEntity pikmin) {
                        UUID ownerId = pikmin.getOwnerUUID();
                        if (ownerId != null && ownerId.equals(playerId) && pikmin.getPikminType() == type) {
                            pikmin.discard();
                            recalled++;
                        }
                    }
                }
            }
        }
        updateDataFromPlayer();
        broadcastChanges();
    }

    private void spawnPikmin(PikminType type, int amount) {
        if (!(level instanceof ServerLevel server)) return;

        for (int i = 0; i < amount; i++) {
            PikminEntity pikmin = new PikminEntity(ModEntities.PIKMIN.get(), server);
            pikmin.setOwnerUUID(player.getUUID());
            pikmin.setPikminType(type);

            pikmin.moveTo(
                    blockEntity.getBlockPos().getX() + 0.5,
                    blockEntity.getBlockPos().getY() + 1,
                    blockEntity.getBlockPos().getZ() + 0.5,
                    level.random.nextFloat() * 360,
                    0
            );

            server.addFreshEntity(pikmin);
        }
    }

    private void recallPikmins(PikminType type, int amount) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (amount <= 0) return;

        UUID ownerId = player.getUUID();
        int recalled = 0;

        for (Entity entity : serverLevel.getAllEntities()) {
            if (recalled >= amount) break;

            if (entity instanceof PikminEntity pikmin) {
                if (ownerId.equals(pikmin.getOwnerUUID()) && pikmin.getPikminType() == type) {
                    pikmin.discard();
                    recalled++;
                }
            }
        }
    }

    private void updateDataFromPlayer() {
        OnionData playerData = getPlayerData();
        OnionData blockData = getBlockData();

        int index = 0;
        for (PikminType type : PikminType.values()) {
            data.set(index + OFF_UNLOCKED, playerData.hasUnlocked(type) ? 1 : 0);
            data.set(index + OFF_STORED, blockData.getStored(type));
            data.set(index + OFF_OUTSIDE, playerData.getOutside(type));
            data.set(index + OFF_PENDING_TAKE_OUT, pendingTakeOut.getOrDefault(type, 0));
            data.set(index + OFF_PENDING_PUT_IN, pendingPutIn.getOrDefault(type, 0));
            data.set(index + OFF_CAPACITY, playerData.getCapacity(type));
            index += VALUES_PER_TYPE;
        }
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        if (!level.isClientSide) {
            updateDataFromPlayer();
        }
    }

    @Override
    public void slotsChanged(@NotNull Container pContainer) {
        super.slotsChanged(pContainer);
        broadcastChanges();
    }

    public boolean hasUpgradeItem() {
        Slot slot = this.getSlot(0);
        return slot.hasItem() && slot.getItem().getItem() instanceof OnionUpgradeItem;
    }

    public void processUpgrade() {
        if (level.isClientSide) return;

        Slot slot = this.getSlot(0);
        if (hasUpgradeItem()) {
            ItemStack upgradeStack = slot.getItem();
            PikminType upgradeType = OnionUpgradeItem.getTypeFromStack(upgradeStack);
            int upgradeCount = upgradeStack.getCount();

            OnionData playerData = getPlayerData();
            OnionData newPlayerData = playerData.addCapacity(upgradeType, 20 * upgradeCount);
            player.setData(OnionComponents.PLAYER_ONION_DATA, newPlayerData);

            level.playSound(null, player.blockPosition(),
                    SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS,
                    1.0f, 1.0f);

            slot.set(ItemStack.EMPTY);

            broadcastChanges();
        }
    }
}
