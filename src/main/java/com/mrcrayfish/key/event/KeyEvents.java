package com.mrcrayfish.key.event;

import com.mrcrayfish.key.items.KeyItems;
import com.mrcrayfish.key.lock.LockManager;
import com.mrcrayfish.key.lock.WorldLockData;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.NeighborNotifyEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class KeyEvents {
	@SubscribeEvent
	public void onBreakBlock(BreakEvent event) {
		if (event.world.isRemote) {
			return;
		}

		BlockPos pos = LockManager.isLockAround(event.world, event.pos);
		if (pos != null) {
			Block block = event.world.getBlockState(pos).getBlock();
			TileEntity tileEntity = event.world.getTileEntity(event.pos);
			if (LockManager.onBreak(block, tileEntity, event.getPlayer(), event.world, event.pos)) {
				event.setCanceled(true);
				WorldLockData.get(event.world).removeLock(event.pos);
			}

		}
	}

	@SubscribeEvent
	public void onNeighbourChange(NeighborNotifyEvent event) {
		BlockPos pos = LockManager.isLockAround(event.world, event.pos);
		if (pos != null) {
			event.world.getBlockState(pos).getBlock();
			event.world.getTileEntity(pos);
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onOpenContainer(PlayerOpenContainerEvent event) {
		ItemStack current = event.entityPlayer.getCurrentEquippedItem();
		if (current != null) {
			if (current.getItem() == KeyItems.item_key_ring) {
				current.clearCustomName();
			}
		}
	}

	@SubscribeEvent
	public void onPlaceBlock(PlaceEvent event) {
		if (event.world.isRemote) {
			return;
		}
		if ((event.placedBlock.getBlock() instanceof BlockDoor)
				&& (event.world.getBlockState(event.pos).getBlock() != Blocks.iron_door)) {
			WorldLockData.get(event.world).addLock(event.pos);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.pos != null) {
			Block block = event.world.getBlockState(event.pos).getBlock();
			TileEntity tileEntity = event.world.getTileEntity(event.pos);
			if (event.action == Action.RIGHT_CLICK_BLOCK) {
				event.setCanceled(
						LockManager.onInteract(block, tileEntity, event.entityPlayer, event.world, event.pos));
			}
		}
	}
}
