package com.mrcrayfish.key.event;

import com.mrcrayfish.key.MrCrayfishKeyMod;
import com.mrcrayfish.key.lock.LockData;
import com.mrcrayfish.key.lock.LockManager;
import com.mrcrayfish.key.lock.WorldLockData;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.NeighborNotifyEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class KeyEvents {

	// we set this to lowest because we only want to remove the lock if no other
	// event listener cancels the event, otherwise we remove the lock and
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onBreakBlock(BreakEvent event) {
		if (event.getWorld().isRemote) {
			return;
		}

		BlockPos pos = LockManager.isLockAround(event.getWorld(), event.getPos());
		if (pos != null) {
			Block block = event.getWorld().getBlockState(pos).getBlock();
			TileEntity tileEntity = event.getWorld().getTileEntity(event.getPos());
			if (LockManager.onBreak(block, tileEntity, event.getPlayer(), event.getWorld(), event.getPos())) {
				event.setCanceled(true);
			} else {
				WorldLockData.get(event.getWorld()).removeLock(event.getPos());
			}
		}
	}

	@SubscribeEvent
	public void onNeighbourChange(NeighborNotifyEvent event) {
		// TODO: Need to somehow find if the door can be interacted with
		// since we wont know which player triggered the event
		BlockPos pos = LockManager.isLockAround(event.getWorld(), event.getPos());
		if (pos != null) {
			if ((event.getWorld().getBlockState(pos).getBlock() instanceof BlockDoor)
					&& (event.getWorld().getBlockState(pos).getBlock() != Blocks.IRON_DOOR)) {
				IBlockState state = event.getWorld().getBlockState(pos);
				if ((state.getValue(BlockDoor.HALF)) == BlockDoor.EnumDoorHalf.UPPER) {
					pos = pos.down();
				}
				WorldLockData worldLockData = WorldLockData.get(event.getWorld());
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
	public void onOpenContainer(PlayerContainerEvent event) {
		ItemStack current = event.getEntityPlayer().getHeldItemMainhand();
		if (current != null) {
			if (current.getItem() == MrCrayfishKeyMod.item_key_ring) {
				current.clearCustomName();
			}
		}
	}

	// again we only want to add a lock if no other event listen cancels the
	// event
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlaceBlock(PlaceEvent event) {
		if (event.getWorld().isRemote) {
			return;
		}
		if ((event.getPlacedBlock().getBlock() instanceof BlockDoor)
				&& (event.getWorld().getBlockState(event.getPos()).getBlock() != Blocks.IRON_DOOR)) {
			WorldLockData.get(event.getWorld()).addLock(event.getPos());
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getPos() != null) {
			Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
			TileEntity tileEntity = event.getWorld().getTileEntity(event.getPos());
			if (event instanceof PlayerInteractEvent.RightClickBlock) {
				event.setCanceled(LockManager.onInteract(block, tileEntity, event.getEntityPlayer(), event.getHand(),
						event.getWorld(), event.getPos()));
			}
		}
	}
}
