package com.cibernet.splatcraft.handlers;

import com.cibernet.splatcraft.blocks.InkwellBlock;
import com.cibernet.splatcraft.capabilities.PlayerInfoCapability;
import com.cibernet.splatcraft.tileentities.InkColorTileEntity;
import com.cibernet.splatcraft.util.ColorUtils;
import com.cibernet.splatcraft.util.InkBlockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SquidFormHandler
{
	@SubscribeEvent
	public static void playerTick(TickEvent.PlayerTickEvent event)
	{
		PlayerEntity player = event.player;
		
		if(PlayerInfoCapability.isSquid(player))
		{
			player.setPose(Pose.FALL_FLYING);
			player.stopActiveHand();
			
			if(InkBlockUtils.canSquidSwim(player))
			{
				player.fallDistance = 0;
				if(player.ticksExisted % 5 == 0)
					player.heal(0.5f);
			}
			
			if(player.world.getBlockState(player.getPosition().down()).getBlock() instanceof InkwellBlock)
			{
				InkColorTileEntity inkwell = (InkColorTileEntity) player.world.getTileEntity(player.getPosition().down());
				
				ColorUtils.setPlayerColor(player, inkwell.getColor());
			}
		}
	}
	
	@SubscribeEvent
	public static void playerVisibility(PlayerEvent.Visibility event)
	{
		if(PlayerInfoCapability.get(event.getPlayer()).isSquid() && InkBlockUtils.canSquidHide(event.getPlayer()))
			event.modifyVisibility(0);
	}
}