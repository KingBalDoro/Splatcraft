package com.cibernet.splatcraft.tileentities;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;

public class TileEntityInkedBlock extends TileEntityColor
{
	private IBlockState savedState = Blocks.STONE.getDefaultState();
	private int savedColor = 0;
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		
		super.readFromNBT(compound);
		
		Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("savedBlock")));
		int meta = compound.getInteger("savedMeta");
		
		savedState = block.getStateFromMeta(meta);
		
		if(compound.hasKey("savedColor"))
			savedColor = compound.getInteger("savedColor");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		
		compound.setString("savedBlock", savedState.getBlock().getRegistryName().toString());
		compound.setInteger("savedMeta", savedState.getBlock().getMetaFromState(savedState));
		
		if(savedColor != 0)
			compound.setInteger("savedColor", savedColor);
		
		return super.writeToNBT(compound);
	}

	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	public TileEntityInkedBlock setSavedState(IBlockState state)
	{
		this.savedState = state;
		return this;
	}
	public TileEntityInkedBlock setSavedColor(int color)
	{
		this.savedColor = color;
		return this;
	}

	public IBlockState getSavedState() {return savedState;}
	public int getSavedColor() {return savedColor;}

}
