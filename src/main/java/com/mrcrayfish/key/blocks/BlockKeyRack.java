package com.mrcrayfish.key.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.mrcrayfish.key.MrCrayfishKeyMod;
import com.mrcrayfish.key.gui.GuiKeyRack;
import com.mrcrayfish.key.tileentity.TileEntityKeyRack;
import com.mrcrayfish.key.util.CollisionHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockKeyRack extends BlockDirectional implements ITileEntityProvider {
	public BlockKeyRack(Material materialIn) {
		super(materialIn);
		blockSoundType = SoundType.WOOD;
		setHardness(0.5F);
		setCreativeTab(MrCrayfishKeyMod.tabKey);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		isBlockContainer = true;
	}

	@Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_)
    {
		int meta = getMetaFromState(state);
		CollisionHelper.setBlockBounds(this, meta, 0.8F, 0.2F, 0F, 1F, 0.8F, 1F);
		super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, p_185477_7_);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof IInventory) {
			IInventory inv = (IInventory) tileEntity;
			InventoryHelper.dropInventoryItems(world, pos, inv);
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		if (side.getHorizontalIndex() == -1) {
			return false;
		}
		return world.isSideSolid(pos.offset(side.getOpposite()), side, false);
	}

	private boolean canPlaceCheck(World world, BlockPos pos, IBlockState state) {
		EnumFacing enumfacing = state.getValue(FACING);
		if (!canPlaceBlockOnSide(world, pos, enumfacing)) {
			dropBlockAsItem(world, pos, state, 0);
			world.setBlockToAir(pos);
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityKeyRack();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if (!worldIn.isRemote) {
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if (tileEntity instanceof TileEntityKeyRack) {
				playerIn.openGui(MrCrayfishKeyMod.instance, GuiKeyRack.ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}

	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
		super.eventReceived(state,worldIn, pos,  id, param);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
		state.withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	 public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		IBlockState state = world.getBlockState(pos);
		if (canPlaceCheck((World) world, pos, state)) {
			EnumFacing enumfacing = state.getValue(FACING);

			if (!world.getBlockState(pos.offset(enumfacing)).getBlock().isNormalCube(state)) {
				breakBlock((World) world, pos, state);
				dropBlockAsItem((World) world, pos, state, 0);
				((World) world).setBlockToAir(pos);
			}
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
		int meta = getMetaFromState(worldIn.getBlockState(pos));
		CollisionHelper.setBlockBounds(this, meta, 0.8F, 0.2F, 0F, 1F, 0.8F, 1F);
	}
}
