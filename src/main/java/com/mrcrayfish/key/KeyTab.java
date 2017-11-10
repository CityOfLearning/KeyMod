package com.mrcrayfish.key;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class KeyTab extends CreativeTabs {
	public KeyTab(String label) {
		super(label);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(MrCrayfishKeyMod.item_key);
	}
}
