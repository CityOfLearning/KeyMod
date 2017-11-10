package com.mrcrayfish.key.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerKeyRack extends Container {
	private IInventory inventoryKeyRack;

	public ContainerKeyRack(IInventory inventoryPlayer, IInventory inventoryKeyRack) {
		this.inventoryKeyRack = inventoryKeyRack;

		for (int i = 0; i < 4; i++) {
			addSlotToContainer(new SlotKeyRack(inventoryKeyRack, i, (i * 18) + 53, 17));
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
		return inventoryKeyRack.isUsableByPlayer(playerIn);
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNum) {
		ItemStack itemCopy = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(slotNum);

		if ((slot != null) && slot.getHasStack()) {
			ItemStack item = slot.getStack();
			itemCopy = item.copy();

			if (slotNum < inventoryKeyRack.getSizeInventory()) {
				if (!mergeItemStack(item, inventoryKeyRack.getSizeInventory(), inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(item, 0, inventoryKeyRack.getSizeInventory(), false)) {
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
