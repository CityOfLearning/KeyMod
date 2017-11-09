package com.mrcrayfish.key.lock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;

public interface ILock {

	public BlockPos fix(World world, BlockPos pos);

	public LockCode getLockCode(World world, BlockPos pos);

	public boolean handleBreak(EntityPlayer player, World world, BlockPos pos);

	public boolean handleInteract(EntityPlayer player, EnumHand hand, World world, BlockPos pos);

}
