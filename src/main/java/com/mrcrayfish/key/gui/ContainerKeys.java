package com.mrcrayfish.key.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerKeys extends Container {
	private IInventory inventoryKeys;

	public ContainerKeys(IInventory inventoryPlayer, IInventory inventoryKeys) {
		this.inventoryKeys = inventoryKeys;
		inventoryKeys.openInventory(null);

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 3; ++j) {
				addSlotToContainer(new SlotKey(inventoryKeys, j + (i * 3), (j * 18) + 62, (i * 18) + 17));
			}
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(inventoryPlayer, j + (i * 9) + 9, (j * 18) + 8, (i * 18) + 84));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, (i * 18) + 8, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return inventoryKeys.isUsableByPlayer(playerIn);
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		inventoryKeys.closeInventory(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNum) {
		ItemStack itemCopy = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(slotNum);

		if ((slot != null) && slot.getHasStack()) {
			ItemStack item = slot.getStack();
			itemCopy = item.copy();

			if (slotNum < 6) {
				if (!mergeItemStack(item, 6, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(item, 0, 6, false)) {
				return ItemStack.EMPTY;
			}

			if (item.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}

		return itemCopy;
	}

}
