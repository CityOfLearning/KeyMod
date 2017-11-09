package com.mrcrayfish.key.lock.type;

import com.mrcrayfish.key.items.KeyItems;
import com.mrcrayfish.key.lock.ILock;
import com.mrcrayfish.key.lock.LockData;
import com.mrcrayfish.key.lock.LockManager;
import com.mrcrayfish.key.lock.WorldLockData;
import com.mrcrayfish.key.util.MessageUtil;
import com.mrcrayfish.key.util.NBTHelper;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;

public class LockDoor implements ILock {
	@Override
	public BlockPos fix(World world, BlockPos pos) {
		if ((world.getBlockState(pos).getBlock() instanceof BlockDoor)
				&& (world.getBlockState(pos).getBlock() != Blocks.IRON_DOOR)) {
			IBlockState state = world.getBlockState(pos);
			if (state.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.UPPER) {
				return pos.down();
			}
		}
		return pos;
	}

	@Override
	public LockCode getLockCode(World world, BlockPos pos) {
		WorldLockData worldLockData = WorldLockData.get(world);
		LockData lockData = worldLockData.getLock(pos);
		if (lockData != null) {
			return lockData.getLockCode();
		}
		return null;
	}

	@Override
	public boolean handleBreak(EntityPlayer player, World world, BlockPos pos) {
		if ((world.getBlockState(pos).getBlock() instanceof BlockDoor)
				&& (world.getBlockState(pos).getBlock() != Blocks.IRON_DOOR)) {
			IBlockState state = world.getBlockState(pos);
			if ((state.getValue(BlockDoor.HALF)) == BlockDoor.EnumDoorHalf.UPPER) {
				pos = pos.down();
			}

			WorldLockData worldLockData = WorldLockData.get(world);
			LockData lockedDoor = worldLockData.getLock(pos);
			if (lockedDoor != null) {
				if (lockedDoor.isLocked()) {
					if (!LockManager.isKeyInInvetory(player, lockedDoor.getLockCode())
							&& !LockManager.isMasterKeyInInvetory(player)) {
						MessageUtil.sendSpecial(player, TextFormatting.YELLOW
								+ "You need to have correct key in your inventory to destroy this block.");
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean handleInteract(EntityPlayer player, EnumHand hand, World world, BlockPos pos) {
		if ((world.getBlockState(pos).getBlock() instanceof BlockDoor)
				&& (world.getBlockState(pos).getBlock() != Blocks.IRON_DOOR)) {
			IBlockState state = world.getBlockState(pos);
			if ((state.getValue(BlockDoor.HALF)) == BlockDoor.EnumDoorHalf.UPPER) {
				pos = pos.down();
			}

			WorldLockData worldLockData = WorldLockData.get(world);
			LockData lockedDoor = worldLockData.getLock(pos);
			ItemStack current = player.getHeldItem(hand);

			if (lockedDoor != null) {
				if (lockedDoor.isLocked()) {
					if ((current != null) && (current.getItem() == KeyItems.item_master_key)) {
						return false;
					} else if ((current != null) && (current.getItem() == KeyItems.item_key)) {
						if (!lockedDoor.getLockCode().getLock().equals(current.getDisplayName())) {
							world.playSoundAtEntity(player, "fire.ignite", 1.0F, 1.0F);
							MessageUtil.sendSpecial(player,
									TextFormatting.YELLOW + "This key does not fit the lock.");
							world.markBlockForUpdate(pos);
							return true;
						}
					} else if ((current != null) && (current.getItem() == KeyItems.item_key_ring)) {
						boolean hasCorrectKey = false;
						NBTTagList keys = (NBTTagList) NBTHelper.getCompoundTag(current, "KeyRing").getTag("Keys");
						if (keys != null) {
							for (int i = 0; i < keys.tagCount(); i++) {
								ItemStack key = new ItemStack(keys.getCompoundTagAt(i));
								if (lockedDoor.getLockCode().getLock().equals(key.getDisplayName())) {
									hasCorrectKey = true;
									break;
								}
							}
						}
						if (!hasCorrectKey) {
							world.playSoundAtEntity(player, "fire.ignite", 1.0F, 1.0F);
							MessageUtil.sendSpecial(player,
									TextFormatting.YELLOW + "None of the keys fit the lock.");
							world.markBlockForUpdate(pos);
							return true;
						}
					} else {
						world.playSoundAtEntity(player, "random.wood_click", 1.0F, 1.0F);
						MessageUtil.sendSpecial(player, TextFormatting.YELLOW + "This door is locked with a key.");
						world.markBlockForUpdate(pos);
						return true;
					}
				} else {
					if ((current != null) && (current.getItem() == KeyItems.item_key)) {
						if (!current.getDisplayName().equals(
								StatCollector.translateToLocal(current.getItem().getUnlocalizedName() + ".name"))) {
							lockedDoor.setLockCode(new LockCode(current.getDisplayName()));
							world.playSoundAtEntity(player, "random.click", 1.0F, 1.0F);
							MessageUtil.sendSpecial(player,
									TextFormatting.GREEN + "Successfully locked the door with the key: "
											+ TextFormatting.RESET + current.getDisplayName());
							worldLockData.markDirty();
						} else {
							MessageUtil.sendSpecial(player, TextFormatting.YELLOW
									+ "The key needs to be renamed before you can lock this door.");
							world.markBlockForUpdate(pos);
						}
						if (!world.isRemote) {
							return true;
						}
					} else if ((current != null) && (current.getItem() == KeyItems.item_key_ring)) {
						MessageUtil.sendSpecial(player,
								TextFormatting.YELLOW + "You can only lock a door with a single key.");
					} else {
						return false;
					}
				}
			}
		}
		return false;
	}
}
