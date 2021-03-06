package com.cibernet.splatcraft.items;

import com.cibernet.splatcraft.blocks.IInked;
import com.cibernet.splatcraft.utils.TabSplatCraft;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemInkDisruptor extends ItemRemote
{
	public ItemInkDisruptor()
	{
		super();
		setUnlocalizedName("inkDisruptor");
		setRegistryName("ink_disruptor");
		setMaxStackSize(1);
		setCreativeTab(TabSplatCraft.main);
	}
	
	@Override
	public RemoteResult onRemoteUse(World world, BlockPos blockpos, BlockPos blockpos1, ItemStack stack, int colorIn, int mode)
	{
		BlockPos blockpos2 = new BlockPos(Math.min(blockpos.getX(), blockpos1.getX()), Math.min(blockpos.getY(), Math.min(blockpos1.getY(), blockpos.getY())), Math.min(blockpos.getZ(), blockpos1.getZ()));
		BlockPos blockpos3 = new BlockPos(Math.max(blockpos.getX(), blockpos1.getX()), Math.max(blockpos.getY(), Math.max(blockpos1.getY(), blockpos.getY())), Math.max(blockpos.getZ(), blockpos1.getZ()));
		
		if (!(blockpos2.getY() >= 0 && blockpos3.getY() < 256))
			return createResult(false, new TextComponentTranslation("commands.clearInk.outOfWorld"));
		
		
		for(int j = blockpos2.getZ(); j <= blockpos3.getZ(); j += 16)
		{
			for(int k = blockpos2.getX(); k <= blockpos3.getX(); k += 16)
			{
				if(!world.isBlockLoaded(new BlockPos(k, blockpos3.getY() - blockpos2.getY(), j)))
				{
					return createResult(false, new TextComponentTranslation("commands.clearInk.outOfWorld"));
				}
			}
		}
		int count = 0;
		for(int x = blockpos2.getX(); x <= blockpos3.getX(); x++)
			for(int y = blockpos2.getY(); y <= blockpos3.getY(); y++)
				for(int z = blockpos2.getZ(); z <= blockpos3.getZ(); z++)
				{
					BlockPos pos = new BlockPos(x,y,z);
					Block block = world.getBlockState(pos).getBlock();
					if(block instanceof IInked)
					{
						if(((IInked) block).clearInk(world, pos))
							count++;
					}
				}
		
		return createResult(true, new TextComponentTranslation("commands.clearInk.success", count));
	}
}
