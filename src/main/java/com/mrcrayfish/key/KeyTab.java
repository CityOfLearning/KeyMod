package com.mrcrayfish.key;

import com.mrcrayfish.key.items.KeyItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class KeyTab extends CreativeTabs {
	public KeyTab(String label) {
		super(label);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(KeyItems.item_key);
	}
}
