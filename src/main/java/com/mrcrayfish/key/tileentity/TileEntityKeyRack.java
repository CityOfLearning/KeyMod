package com.mrcrayfish.key.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;

public class TileEntityKeyRack extends TileEntity implements IInventory {
	private InventoryBasic inventory = new InventoryBasic("Key Rack", true, 4);

	@Override
	public void clear() {
		inventory.clear();
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return inventory.decrStackSize(index, count);
	}

	@Override
	public ITextComponent getDisplayName() {
		return inventory.getDisplayName();
	}

	@Override
	public int getField(int id) {
		return inventory.getField(id);
	}

	@Override
	public int getFieldCount() {
		return inventory.getFieldCount();
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public String getName() {
		return inventory.getName();
	}

	@Override
	public int getSizeInventory() {
		return inventory.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return inventory.getStackInSlot(index);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound tagCom = new NBTTagCompound();
		writeToNBT(tagCom);
		return new SPacketUpdateTileEntity(pos, getBlockMetadata(), tagCom);
	}

	@Override
	public boolean hasCustomName() {
		return inventory.hasCustomName();
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return inventory.isItemValidForSlot(index, stack);
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return inventory.isUsableByPlayer(player);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound tagCom = pkt.getNbtCompound();
		readFromNBT(tagCom);
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		if (tagCompound.hasKey("Items")) {
			NBTTagList tagList = (NBTTagList) tagCompound.getTag("Items");
			inventory.clear();

			for (int i = 0; i < tagList.tagCount(); ++i) {
				NBTTagCompound nbt = tagList.getCompoundTagAt(i);
				byte slot = nbt.getByte("Slot");

				if ((slot >= 0) && (slot < inventory.getSizeInventory())) {
					inventory.setInventorySlotContents(slot, new ItemStack(nbt));
				}
			}
		}
		if (tagCompound.hasKey("CustomName", 8)) {
			inventory.setCustomName(tagCompound.getString("CustomName"));
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return inventory.removeStackFromSlot(index);
	}

	@Override
	public void setField(int id, int value) {
		inventory.setField(id, value);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		inventory.setInventorySlotContents(index, stack);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		NBTTagList tagList = new NBTTagList();
		for (int slot = 0; slot < inventory.getSizeInventory(); ++slot) {
			if (inventory.getStackInSlot(slot) != null) {
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setByte("Slot", (byte) slot);
				inventory.getStackInSlot(slot).writeToNBT(nbt);
				tagList.appendTag(nbt);
			}
		}
		tagCompound.setTag("Items", tagList);

		if (hasCustomName()) {
			tagCompound.setString("CustomName", inventory.getName());
		}
		return tagCompound;
	}
}
