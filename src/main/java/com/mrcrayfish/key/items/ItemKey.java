package com.mrcrayfish.key.items;

import com.mrcrayfish.key.lock.LockData;
import com.mrcrayfish.key.lock.WorldLockData;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;

public class ItemKey extends Item {
	public ItemKey() {
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if (!worldIn.isRemote) {
			if (player.isSneaking()) {
				EntityPlayerMP playerMp = (EntityPlayerMP) player;
				TileEntity tileEntity = worldIn.getTileEntity(pos);
				if (tileEntity instanceof TileEntityLockable) {
					TileEntityLockable tileEntityLockable = (TileEntityLockable) tileEntity;
					if (tileEntityLockable.isLocked()) {
						if (tileEntityLockable.getLockCode().getLock().equals(player.getHeldItem(hand).getDisplayName())) {
							tileEntityLockable.setLockCode(LockCode.EMPTY_CODE);
							playerMp.connection
									.sendPacket(new SPacketChat(
											(new TextComponentString(
													TextFormatting.GREEN + "Succesfully unlocked this block."))));
						} else {
							playerMp.connection
									.sendPacket(new SPacketChat(
											(new TextComponentString(TextFormatting.YELLOW
													+ "You need to have correct key to unlock this block."))));
						}
					}
				} else if ((worldIn.getBlockState(pos).getBlock() instanceof BlockDoor)
						&& (worldIn.getBlockState(pos).getBlock() != Blocks.IRON_DOOR)) {
					worldIn.getBlockState(pos).getBlock();
					IBlockState state = worldIn.getBlockState(pos);
					if ((state.getValue(BlockDoor.HALF)) == BlockDoor.EnumDoorHalf.UPPER) {
						pos = pos.down();
					}

					WorldLockData lockedDoorData = WorldLockData.get(worldIn);
					LockData lockedDoor = lockedDoorData.getLock(pos);
					if (lockedDoor != null) {
						if (lockedDoor.isLocked()) {
							if (lockedDoor.getLockCode().getLock().equals(player.getHeldItem(hand).getDisplayName())) {
								lockedDoor.setLockCode(LockCode.EMPTY_CODE);
								playerMp.connection.sendPacket(new SPacketChat(
										(new TextComponentString(
												TextFormatting.GREEN + "Succesfully unlocked this block."))));
								lockedDoorData.markDirty();
							} else {
								playerMp.connection
										.sendPacket(new SPacketChat(
												(new TextComponentString(TextFormatting.YELLOW
														+ "You need to have correct key to unlock this block."))));
							}
						}
					}
				}
			}
		}
		return EnumActionResult.PASS;
	}
}
