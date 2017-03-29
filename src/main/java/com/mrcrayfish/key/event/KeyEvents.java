package com.mrcrayfish.key.event;

import com.mrcrayfish.key.items.KeyItems;
import com.mrcrayfish.key.lock.LockData;
import com.mrcrayfish.key.lock.LockManager;
import com.mrcrayfish.key.lock.WorldLockData;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
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

	//we set this to lowest because we only want to remove the lock if no other event listener cancels the event, otherwise we remove the lock and 
	@SubscribeEvent(priority = EventPriority.LOWEST)
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
			} else {
				WorldLockData.get(event.world).removeLock(event.pos);
			}
		}
	}

	@SubscribeEvent
	public void onNeighbourChange(NeighborNotifyEvent event) {
		// TODO: Need to somehow find if the door can be interacted with
		//since we wont know which player triggered the event
		BlockPos pos = LockManager.isLockAround(event.world, event.pos);
		if (pos != null) {
			if ((event.world.getBlockState(pos).getBlock() instanceof BlockDoor)
					&& (event.world.getBlockState(pos).getBlock() != Blocks.iron_door)) {
				IBlockState state = event.world.getBlockState(pos);
				if ((state.getValue(BlockDoor.HALF)) == BlockDoor.EnumDoorHalf.UPPER) {
					pos = pos.down();
				}
				WorldLockData worldLockData = WorldLockData.get(event.world);
				LockData lockedDoor = worldLockData.getLock(pos);
				if (lockedDoor != null) {
					if (lockedDoor.isLocked()) {
						event.setCanceled(true);
					}
				}
			}
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

	//again we only want to add a lock if no other event listen cancels the event
	@SubscribeEvent(priority = EventPriority.LOWEST)
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
