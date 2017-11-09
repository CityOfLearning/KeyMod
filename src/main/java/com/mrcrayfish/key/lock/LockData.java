package com.mrcrayfish.key.lock;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LockCode;

public class LockData {

	private BlockPos pos;
	private LockCode code;

	public LockData() {
		code = LockCode.EMPTY_CODE;
	}

	public LockData(BlockPos pos) {
		this.pos = pos;
		code = LockCode.EMPTY_CODE;
	}

	public LockCode getLockCode() {
		return code;
	}

	public BlockPos getPos() {
		return pos;
	}

	public boolean isLocked() {
		return (code != null) && !code.isEmpty();
	}

	public void readFromNBT(NBTTagCompound compound) {
		pos = new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
		code = LockCode.fromNBT(compound);
	}

	public void setLockCode(LockCode code) {
		this.code = code;
	}

	public void writeToNBT(NBTTagCompound compound) {
		compound.setInteger("x", pos.getX());
		compound.setInteger("y", pos.getY());
		compound.setInteger("z", pos.getZ());

		if (code != null) {
			code.toNBT(compound);
		}
	}
}
