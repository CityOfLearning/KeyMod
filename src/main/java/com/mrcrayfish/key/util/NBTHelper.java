package com.mrcrayfish.key.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

/**
 * NBTHelper
 *
 * @author pahimar
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class NBTHelper {
	// boolean
	public static boolean getBoolean(ItemStack itemStack, String keyName) {

		NBTHelper.initNBTTagCompound(itemStack);

		if (!itemStack.getTagCompound().hasKey(keyName)) {
			NBTHelper.setBoolean(itemStack, keyName, false);
		}

		return itemStack.getTagCompound().getBoolean(keyName);
	}

	// byte
	public static byte getByte(ItemStack itemStack, String keyName) {

		NBTHelper.initNBTTagCompound(itemStack);

		if (!itemStack.getTagCompound().hasKey(keyName)) {
			NBTHelper.setByte(itemStack, keyName, (byte) 0);
		}

		return itemStack.getTagCompound().getByte(keyName);
	}

	public static NBTTagCompound getCompoundTag(ItemStack itemStack, String tagName) {
		NBTHelper.initNBTTagCompound(itemStack);

		if (!itemStack.getTagCompound().hasKey(tagName)) {
			itemStack.getTagCompound().setTag(tagName, new NBTTagCompound());
		}
		return itemStack.getTagCompound().getCompoundTag(tagName);
	}

	// double
	public static double getDouble(ItemStack itemStack, String keyName) {

		NBTHelper.initNBTTagCompound(itemStack);

		if (!itemStack.getTagCompound().hasKey(keyName)) {
			NBTHelper.setDouble(itemStack, keyName, 0);
		}

		return itemStack.getTagCompound().getDouble(keyName);
	}

	// float
	public static float getFloat(ItemStack itemStack, String keyName) {

		NBTHelper.initNBTTagCompound(itemStack);

		if (!itemStack.getTagCompound().hasKey(keyName)) {
			NBTHelper.setFloat(itemStack, keyName, 0);
		}

		return itemStack.getTagCompound().getFloat(keyName);
	}

	// int
	public static int getInt(ItemStack itemStack, String keyName) {

		NBTHelper.initNBTTagCompound(itemStack);

		if (!itemStack.getTagCompound().hasKey(keyName)) {
			NBTHelper.setInteger(itemStack, keyName, 0);
		}

		return itemStack.getTagCompound().getInteger(keyName);
	}

	// long
	public static long getLong(ItemStack itemStack, String keyName) {

		NBTHelper.initNBTTagCompound(itemStack);

		if (!itemStack.getTagCompound().hasKey(keyName)) {
			NBTHelper.setLong(itemStack, keyName, 0);
		}

		return itemStack.getTagCompound().getLong(keyName);
	}

	// short
	public static short getShort(ItemStack itemStack, String keyName) {

		NBTHelper.initNBTTagCompound(itemStack);

		if (!itemStack.getTagCompound().hasKey(keyName)) {
			NBTHelper.setShort(itemStack, keyName, (short) 0);
		}

		return itemStack.getTagCompound().getShort(keyName);
	}

	// String
	public static String getString(ItemStack itemStack, String keyName) {

		NBTHelper.initNBTTagCompound(itemStack);

		if (!itemStack.getTagCompound().hasKey(keyName)) {
			NBTHelper.setString(itemStack, keyName, "");
		}

		return itemStack.getTagCompound().getString(keyName);
	}

	public static NBTTagList getTagList(ItemStack itemStack, String tagName) {
		NBTHelper.initNBTTagCompound(itemStack);

		if (!itemStack.getTagCompound().hasKey(tagName)) {
			NBTHelper.setTagList(itemStack, tagName, new NBTTagList());
		}

		return itemStack.getTagCompound().getTagList(tagName, Constants.NBT.TAG_STRING);
	}

	public static boolean hasTag(ItemStack itemStack, String keyName) {

		if (itemStack.getTagCompound() != null) {
			return itemStack.getTagCompound().hasKey(keyName);
		}

		return false;
	}

	/**
	 * Initializes the NBT Tag Compound for the given ItemStack if it is null
	 *
	 * @param itemStack
	 *            The ItemStack for which its NBT Tag Compound is being checked for
	 *            initialization
	 */
	private static void initNBTTagCompound(ItemStack itemStack) {

		if (itemStack.getTagCompound() == null) {
			itemStack.setTagCompound(new NBTTagCompound());
		}
	}

	public static void removeTag(ItemStack itemStack, String keyName) {

		if (itemStack.getTagCompound() != null) {
			itemStack.getTagCompound().removeTag(keyName);
		}
	}

	public static void setBoolean(ItemStack itemStack, String keyName, boolean keyValue) {

		NBTHelper.initNBTTagCompound(itemStack);

		itemStack.getTagCompound().setBoolean(keyName, keyValue);
	}

	public static void setByte(ItemStack itemStack, String keyName, byte keyValue) {

		NBTHelper.initNBTTagCompound(itemStack);

		itemStack.getTagCompound().setByte(keyName, keyValue);
	}

	public static void setCompoundTag(ItemStack itemStack, String tagName, NBTTagCompound tagValue) {
		NBTHelper.initNBTTagCompound(itemStack);

		itemStack.getTagCompound().setTag(tagName, tagValue);
	}

	public static void setDouble(ItemStack itemStack, String keyName, double keyValue) {

		NBTHelper.initNBTTagCompound(itemStack);

		itemStack.getTagCompound().setDouble(keyName, keyValue);
	}

	public static void setFloat(ItemStack itemStack, String keyName, float keyValue) {

		NBTHelper.initNBTTagCompound(itemStack);

		itemStack.getTagCompound().setFloat(keyName, keyValue);
	}

	public static void setInteger(ItemStack itemStack, String keyName, int keyValue) {

		NBTHelper.initNBTTagCompound(itemStack);

		itemStack.getTagCompound().setInteger(keyName, keyValue);
	}

	public static void setLong(ItemStack itemStack, String keyName, long keyValue) {

		NBTHelper.initNBTTagCompound(itemStack);

		itemStack.getTagCompound().setLong(keyName, keyValue);
	}

	public static void setShort(ItemStack itemStack, String keyName, short keyValue) {

		NBTHelper.initNBTTagCompound(itemStack);

		itemStack.getTagCompound().setShort(keyName, keyValue);
	}

	public static void setString(ItemStack itemStack, String keyName, String keyValue) {

		NBTHelper.initNBTTagCompound(itemStack);

		itemStack.getTagCompound().setString(keyName, keyValue);
	}

	public static void setTagList(ItemStack itemStack, String keyName, NBTTagList keyValue) {
		NBTHelper.initNBTTagCompound(itemStack);

		itemStack.getTagCompound().setTag(keyName, keyValue);
	}
}