package com.mrcrayfish.key.blocks;

import javax.annotation.Nullable;

import com.mrcrayfish.key.MrCrayfishKeyMod;
import com.mrcrayfish.key.Reference;
import com.mrcrayfish.key.gui.GuiKeyRack;
import com.mrcrayfish.key.tileentity.TileEntityKeyRack;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockKeyRack extends BlockHorizontal implements ITileEntityProvider {
	public BlockKeyRack(Material materialIn) {
		super(materialIn);
		blockSoundType = SoundType.WOOD;
		setHardness(0.5F);
		setCreativeTab(MrCrayfishKeyMod.tabKey);
		setRegistryName(new ResourceLocation(Reference.MOD_ID, "block_key_rack"));
		setUnlocalizedName("block_key_rack");
		setDefaultState(blockState.getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.SOUTH));
		isBlockContainer = true;
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
		EnumFacing enumfacing = state.getValue(BlockHorizontal.FACING);
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
		return new BlockStateContainer(this, new IProperty[] { BlockHorizontal.FACING });
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityKeyRack();
	}

	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing enumfacing = state.getValue(BlockHorizontal.FACING);
		float f = 0.28125F;
		float f1 = 0.78125F;
		float f2 = 0.0F;
		float f3 = 1.0F;
		float f4 = 0.125F;

		switch (enumfacing) {
		case NORTH:
			return new AxisAlignedBB(f2, f, 1.0F - f4, f3, f1, 1.0F);
		case SOUTH:
			return new AxisAlignedBB(f2, f, 0.0F, f3, f1, f4);
		case WEST:
			return new AxisAlignedBB(1.0F - f4, f, f2, 1.0F, f1, f3);
		case EAST:
			return new AxisAlignedBB(0.0F, f, f2, f4, f1, f3);
		default:
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return Block.NULL_AABB;
	}

	public Item getItemBlock() {
		return new ItemBlock(this).setRegistryName(getRegistryName());
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BlockHorizontal.FACING).getHorizontalIndex();
	}

	/**
	 * Called by ItemBlocks just before a block is actually set in the world, to
	 * allow for adjustments to the IBlockstate
	 */
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(BlockHorizontal.FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.getHorizontal(meta));
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if (tileEntity instanceof TileEntityKeyRack) {
				playerIn.openGui(MrCrayfishKeyMod.instance, GuiKeyRack.ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		IBlockState state = world.getBlockState(pos);
		if (canPlaceCheck((World) world, pos, state)) {
			EnumFacing enumfacing = state.getValue(BlockHorizontal.FACING);

			if (!world.getBlockState(pos.offset(enumfacing)).getBlock().isNormalCube(state)) {
				breakBlock((World) world, pos, state);
				dropBlockAsItem((World) world, pos, state, 0);
				((World) world).setBlockToAir(pos);
			}
		}
	}
}
