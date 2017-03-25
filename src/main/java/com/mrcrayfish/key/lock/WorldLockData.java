package com.mrcrayfish.key.lock;

import java.util.ArrayList;
import java.util.List;

import com.mrcrayfish.key.MrCrayfishKeyMod;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class WorldLockData extends WorldSavedData {
	private static final String IDENTIFIER = "locked_data";

	public static WorldLockData get(World world) {
		WorldLockData data = (WorldLockData) world.loadItemData(WorldLockData.class, IDENTIFIER);
		if (data == null) {
			data = new WorldLockData(IDENTIFIER);
			world.setItemData(IDENTIFIER, data);
		}
		return data;
	}

	private List<LockData> lockedData = new ArrayList<LockData>();

	public WorldLockData() {
		this(IDENTIFIER);
	}
	
	public WorldLockData(String identifier) {
		super(identifier);
		if(identifier != IDENTIFIER){
			MrCrayfishKeyMod.logger.error("Uh oh... the identifiers dont match, its likely that locks will not work");
		}
	}

	public void addLock(BlockPos pos) {
		markDirty();
		lockedData.add(new LockData(pos));
	}

	public LockData getLock(BlockPos pos) {
		for (LockData lock : lockedData) {
			if (lock.getPos().equals(pos)) {
				return lock;
			}
		}
		return null;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		lockedData.clear();

		if (nbt.hasKey("locked_blocks")) {
			NBTTagList tagList = (NBTTagList) nbt.getTag("locked_blocks");

			for (int i = 0; i < tagList.tagCount(); i++) {
				NBTTagCompound lockTag = tagList.getCompoundTagAt(i);
				LockData lock = new LockData();
				lock.readFromNBT(lockTag);
				lockedData.add(lock);
			}
		}
	}

	public void removeLock(BlockPos pos) {
		for (LockData lock : lockedData) {
			if (lock.getPos().equals(pos)) {
				markDirty();
				lockedData.remove(lock);
				return;
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagList tagList = new NBTTagList();

		for (LockData lock : lockedData) {
			NBTTagCompound lockTag = new NBTTagCompound();
			lock.writeToNBT(lockTag);
			tagList.appendTag(lockTag);
		}

		nbt.setTag("locked_blocks", tagList);
	}
}
