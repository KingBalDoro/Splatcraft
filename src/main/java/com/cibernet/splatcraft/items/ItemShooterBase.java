package com.cibernet.splatcraft.items;

import com.cibernet.splatcraft.entities.classes.EntityInkProjectile;
import com.cibernet.splatcraft.entities.models.ModelPlayerOverride;
import com.cibernet.splatcraft.registries.SplatCraftSounds;
import com.cibernet.splatcraft.utils.ColorItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemShooterBase extends ItemWeaponBase
{

    public float projectileSize;
    public float inaccuracy;
    public float projectileSpeed;
    public int firingSpeed;
    public boolean automatic;
    public float damage;

    public ItemShooterBase(String unlocName, String registryName, float projectileSize, float projectileSpeed, float inaccuracy, int firingSpeed, float damage, float inkConsumption, boolean automatic)
    {
        super(unlocName, registryName, inkConsumption);
        this.inaccuracy = inaccuracy;
        this.projectileSize = projectileSize;
        this.projectileSpeed = projectileSpeed;
        this.firingSpeed = firingSpeed;
        this.automatic = automatic;
        this.damage = damage;
    }

    public ItemShooterBase(String unlocName, String registryName, float projectileSize, float projectileSpeed, float inaccuracy, int firingSpeed, float damage,  float inkConumption)
    {
        this(unlocName, registryName, projectileSize, projectileSpeed, inaccuracy, firingSpeed, damage, inkConumption, true);
    }
    
    public ItemShooterBase(String unlocName, String registryName, ItemShooterBase parent)
    {
        this(unlocName, registryName, parent.projectileSize, parent.projectileSpeed, parent.inaccuracy, parent.firingSpeed, parent.damage, parent.inkConsumption, parent.automatic);
    }
    
    public ItemShooterBase(String unlocName, String registryName, Item parent)
    {
        this(unlocName, registryName, (ItemShooterBase) parent);
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        return EnumActionResult.PASS;
    }

    /**
     * Called when the equipped item is right clicked.
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if(playerIn.isSneaking())
            return super.onItemRightClick(worldIn, playerIn, handIn);

        ItemStack stack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);

        if(!automatic && !worldIn.isRemote)
        {
            EntityInkProjectile proj = new EntityInkProjectile(worldIn, playerIn, ColorItemUtils.getInkColor(stack), damage);
            proj.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, projectileSpeed, inaccuracy);
            proj.setProjectileSize(projectileSize);
            worldIn.spawnEntity(proj);
            playerIn.getCooldownTracker().setCooldown(this, firingSpeed);
        }

        return new ActionResult(EnumActionResult.SUCCESS, stack);

    }

    public int getMaxItemUseDuration(ItemStack stack)
    {
        return automatic ? super.getMaxItemUseDuration(stack) : 0;
    }

    @Override
    public void onItemTickUse(World worldIn, EntityPlayer playerIn, ItemStack stack, int useTime)
    {
        int actualUseTime = (getMaxItemUseDuration(stack) - useTime);
        int startupLag = 2;
        
        if(hasInk(playerIn, stack))
        {
            if(actualUseTime > startupLag && (actualUseTime-startupLag) % firingSpeed == 1 && automatic)
            {
                if(!worldIn.isRemote)
                {
                    reduceInk(playerIn);
                    EntityInkProjectile proj = new EntityInkProjectile(worldIn, playerIn, ColorItemUtils.getInkColor(stack), damage);
                    proj.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, projectileSpeed, inaccuracy);
                    proj.setProjectileSize(projectileSize);
                    worldIn.spawnEntity(proj);
                }
                //else worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, SplatCraftSounds.shooterShot, SoundCategory.PLAYERS, 0.8F, ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.1F + 1.0F) * 0.95F);
            }
        } else playerIn.sendStatusMessage(new TextComponentTranslation("status.noInk").setStyle(new Style().setColor(TextFormatting.RED)), true);
    }
    
    @Override
    public ModelPlayerOverride.EnumAnimType getAnimType()
    {
        return ModelPlayerOverride.EnumAnimType.SHOOTER;
    }
}
