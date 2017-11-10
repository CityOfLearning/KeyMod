package com.mrcrayfish.key.items;

import java.util.List;

import javax.annotation.Nullable;

import com.mrcrayfish.key.MrCrayfishKeyMod;
import com.mrcrayfish.key.gui.GuiKeys;
import com.mrcrayfish.key.gui.InventoryKeys;
import com.mrcrayfish.key.lock.LockData;
import com.mrcrayfish.key.lock.WorldLockData;
import com.mrcrayfish.key.util.NBTHelper;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;

public class ItemKeys extends Item {
	public static IInventory getInv(EntityPlayer playerIn) {
		ItemStack keys = playerIn.getHeldItemMainhand();
		if ((keys != null) && (keys.getItem() instanceof ItemKeys)) {
			return new InventoryKeys(playerIn, keys);
		}
		return null;
	}

	public ItemKeys() {
		setMaxStackSize(1);
		setHasSubtypes(true);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		NBTTagCompound container = NBTHelper.getCompoundTag(stack, "KeyRing");
		if (container.hasKey("Keys")) {
			NBTTagList keys = (NBTTagList) container.getTag("Keys");
			if (keys.tagCount() > 0) {
				tooltip.add(TextFormatting.GOLD.toString() + "Keys:");
				for (int i = 0; i < keys.tagCount(); i++) {
					ItemStack key = new ItemStack(keys.getCompoundTagAt(i));
					if ((key != ItemStack.EMPTY) && (key.getItem() == MrCrayfishKeyMod.item_key)) {
						tooltip.add("- " + key.getDisplayName());
					}
				}
			}
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		NBTTagList tagList = (NBTTagList) NBTHelper.getCompoundTag(stack, "KeyRing").getTag("Keys");
		if (tagList != null) {
			int keys = 0;
			for (int i = 0; i < tagList.tagCount(); i++) {
				ItemStack key = new ItemStack(tagList.getCompoundTagAt(i));
				if ((key != ItemStack.EMPTY) && (key.getItem() == MrCrayfishKeyMod.item_key)) {
					keys++;
				}
			}
			if (keys > 0) {
				return super.getItemStackDisplayName(stack) + TextFormatting.YELLOW + " (" + keys + " "
						+ (keys > 1 ? "Keys" : "Key") + ")";
			}
		}
		return super.getItemStackDisplayName(stack);
	}

	@Override
	public int getMetadata(ItemStack stack) {
		NBTTagList tagList = (NBTTagList) NBTHelper.getCompoundTag(stack, "KeyRing").getTag("Keys");
		if (tagList == null) {
			return 0;
		}
		int retVal = 0;
		for (int i = 0; i < tagList.tagCount(); i++) {
			ItemStack key = new ItemStack(tagList.getCompoundTagAt(i));
			if ((key != ItemStack.EMPTY) && (key.getItem() == MrCrayfishKeyMod.item_key)) {
				retVal++;
			}
		}
		return (retVal + 1) / 2;
	}

	@Override
	public boolean getShareTag() {
		return true;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "_" + getMetadata(stack);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (!worldIn.isRemote) {
			playerIn.openGui(MrCrayfishKeyMod.instance, GuiKeys.ID, worldIn, 0, 0, 0);
		}
		return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			if (player.isSneaking()) {
				EntityPlayerMP playerMp = (EntityPlayerMP) player;
				TileEntity tileEntity = worldIn.getTileEntity(pos);
				if (tileEntity instanceof TileEntityLockable) {
					TileEntityLockable tileEntityLockable = (TileEntityLockable) tileEntity;
					if (tileEntityLockable.isLocked()) {
						boolean hasCorrectKey = false;
						NBTTagList keys = (NBTTagList) NBTHelper.getCompoundTag(player.getHeldItem(hand), "KeyRing")
								.getTag("Keys");
						if (keys != null) {
							for (int i = 0; i < keys.tagCount(); i++) {
								ItemStack key = new ItemStack(keys.getCompoundTagAt(i));
								if (tileEntityLockable.getLockCode().getLock().equals(key.getDisplayName())) {
									hasCorrectKey = true;
									break;
								}
							}
						}
						if (hasCorrectKey) {
							tileEntityLockable.setLockCode(LockCode.EMPTY_CODE);
							playerMp.connection.sendPacket(new SPacketChat((new TextComponentString(
									TextFormatting.GREEN + "Succesfully unlocked this block."))));
						} else {
							playerMp.connection.sendPacket(new SPacketChat((new TextComponentString(
									TextFormatting.YELLOW + "You need to have correct key to unlock this block."))));
						}
					}
					return EnumActionResult.PASS;
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
							boolean hasCorrectKey = false;
							NBTTagList keys = (NBTTagList) NBTHelper.getCompoundTag(player.getHeldItem(hand), "KeyRing")
									.getTag("Keys");
							if (keys != null) {
								for (int i = 0; i < keys.tagCount(); i++) {
									ItemStack key = new ItemStack(keys.getCompoundTagAt(i));
									if (lockedDoor.getLockCode().getLock().equals(key.getDisplayName())) {
										hasCorrectKey = true;
									}
								}
							}
							if (hasCorrectKey) {
								lockedDoor.setLockCode(LockCode.EMPTY_CODE);
								playerMp.connection.sendPacket(new SPacketChat((new TextComponentString(
										TextFormatting.GREEN + "Succesfully unlocked this block."))));
								lockedDoorData.markDirty();
							} else {
								playerMp.connection
										.sendPacket(new SPacketChat((new TextComponentString(TextFormatting.YELLOW
												+ "You need to have correct key to unlock this block."))));
							}
						}
					}
					return EnumActionResult.PASS;
				}
			}
		}
		return EnumActionResult.FAIL;
	}
}
