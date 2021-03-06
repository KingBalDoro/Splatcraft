package com.cibernet.splatcraft.items;

import javax.annotation.Nullable;

import com.cibernet.splatcraft.entities.classes.EntityInkProjectile;
import com.cibernet.splatcraft.entities.classes.EntitySquidBumper;
import com.cibernet.splatcraft.entities.models.ModelPlayerOverride;
import com.cibernet.splatcraft.utils.ColorItemUtils;
import com.cibernet.splatcraft.utils.SplatCraftDamageSource;
import com.cibernet.splatcraft.world.save.SplatCraftPlayerData;
import com.cibernet.splatcraft.utils.SplatCraftUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemRollerBase extends ItemWeaponBase
{
    private final AttributeModifier SPEED_MODIFIER;
    private final AttributeModifier NO_INK_SPEED_MODIFIER;

    protected double weaponSpeed;
    protected float flingSpeed;
    protected boolean isBrush;
    protected int rollRadius;
    protected float flingSize;
    protected float rollDamage;
    protected float flingDamage;
    protected float flingConsumption;

    private double rollSpeed;
    
    
    public ItemRollerBase(String unlocName, String registryName, double weaponSpeed, float flingSpeed, float flingDamage, float flingSize, float flingConsumption, double rollSpeed, int rollRadius, float rollDamage, float inkConsumption, boolean isBrush)
    {
        super(unlocName, registryName, inkConsumption);
        
        this.weaponSpeed = weaponSpeed;
        this.flingSpeed = flingSpeed;
        this.rollRadius = rollRadius;
        this.flingSize = flingSize;
        this.rollDamage = rollDamage;
        this.flingDamage = flingDamage;
        this.flingConsumption = flingConsumption;
        this.isBrush = isBrush;
        
        this.rollSpeed = rollSpeed;

        SPEED_MODIFIER = (new AttributeModifier( "Rolling speed boost", rollSpeed-1d, 2)).setSaved(false);
        NO_INK_SPEED_MODIFIER = (new AttributeModifier( "Rolling speed boost", -(rollSpeed-1d)*2, 2)).setSaved(false);
        
        this.addPropertyOverride(new ResourceLocation("unfolded"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
    }
    
    public ItemRollerBase(String unlocName, String registryName, ItemRollerBase parent)
    {
        this(unlocName, registryName, parent.weaponSpeed, parent.flingSpeed, parent.flingDamage, parent.flingSize, parent.flingConsumption, parent.rollSpeed, parent.rollRadius, parent.rollDamage, parent.inkConsumption, parent.isBrush);
    }
    
    public ItemRollerBase(String unlocName, String registryName, Item parent)
    {
        this(unlocName, registryName, (ItemRollerBase) parent);
    }
    
    @Override
    public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player)
    {
        return false;
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
        if(playerIn.isSneaking() || playerIn.getCooldownTracker().getCooldown(this, 0) == 0)
            return super.onItemRightClick(worldIn, playerIn, handIn);
        
        ItemStack stack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult(EnumActionResult.SUCCESS, stack);

    }
    
    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
    {
        return super.onEntitySwing(entityLiving, stack);
    }
    
    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();
        if(slot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", getWeaponSpeed(stack), 0));
        }
        return multimap;
    }
    
    public double getWeaponSpeed(ItemStack stack)
    {
        return weaponSpeed;
    }
    
    @Override
    public void onItemTickUse(World worldIn, EntityPlayer playerIn, ItemStack stack, int useTime)
    {
        int actualUseTime = (getMaxItemUseDuration(stack) - useTime);
        
        if(actualUseTime <= 1)
            playerIn.resetCooldown();
            
        boolean glowingInk = SplatCraftUtils.getPlayerGlowingInk(playerIn);
        
        if(hasInk(playerIn, stack))
        {
            reduceInk(playerIn);
            
            int color = ColorItemUtils.getInkColor(stack);
            int downReach = playerIn.posY % 1 < 0.5 ? 1 : 0;
            Vec3d fwd = getFwd(0, playerIn.rotationYaw).normalize();
            fwd = new Vec3d(Math.round(fwd.x), Math.round(fwd.y), Math.round(fwd.z));
    
            BlockPos pos = new BlockPos(Math.floor(playerIn.posX) + 0.5, playerIn.posY - downReach, Math.floor(playerIn.posZ) + 0.5);
    
            for(int i = 0; i < rollRadius; i++)
                for(int rollDepth = 0; rollDepth < 2; rollDepth++)
                {
                    double xOff = i == 0 ? 0 : Math.round(fwd.z) * Math.ceil(i/2.0);
                    double zOff = i == 0 ? 0 : Math.round(fwd.x) * Math.ceil(i/2.0);
                    
                    if(i % 2 == 0)
                    {
                        xOff *= -1;
                        zOff *= -1;
                    }
                    
                    if (playerIn.getHorizontalFacing().equals(EnumFacing.NORTH) || playerIn.getHorizontalFacing().equals(EnumFacing.SOUTH))
                         zOff = (rollDepth - 1) * playerIn.getHorizontalFacing().getAxisDirection().getOffset();
                    else xOff = (rollDepth - 1) * playerIn.getHorizontalFacing().getAxisDirection().getOffset();
                    
                
                        BlockPos checkPos = pos.add(fwd.x * 1 + xOff, 0, fwd.z * 1 + zOff);
                        BlockPos inkPos = pos.add(fwd.x * 2 + xOff, -1, fwd.z * 2 + zOff);
                
                        boolean canInk;
                    
                    int h = 0;
                    while(h <= downReach)
                    {
                        if(SplatCraftUtils.canInkPassthrough(worldIn, inkPos.up()))
                            break;
                        else inkPos = inkPos.up();
                        h++;
                    }
    
                    canInk = SplatCraftUtils.canInk(worldIn, inkPos);
            
                    if(canInk && !worldIn.isRemote)
                        SplatCraftUtils.playerInkBlock(playerIn, worldIn, inkPos, color, rollDamage, glowingInk);
            
                    Entity knockbackEntity = null;
                    {
                        List<EntityLivingBase> inkedPlayers = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(inkPos.up()));
                        int j = 0;
    
                        if(playerIn.getCooledAttackStrength(0) >= 1f)
                        for(EntityLivingBase target : inkedPlayers)
                        {
                            if(target.equals(playerIn))
                                continue;
                    
                            boolean isTargetSameColor = false;
                            if(target instanceof EntityPlayer)
                                isTargetSameColor = SplatCraftPlayerData.getInkColor((EntityPlayer) target) == color;
                    
                            float rollDamage = this.rollDamage;
                            boolean damaged = SplatCraftUtils.dealRollDamage(worldIn, target, rollDamage, color, playerIn, false, glowingInk);
                    
                            if((target instanceof EntitySquidBumper && (((EntitySquidBumper) target).getColor() == color) || !damaged))
                                rollDamage = 0;
                    
                            if(!isTargetSameColor && (((!(target.getHealth()-rollDamage > 0) && !(target instanceof EntitySquidBumper)) || (target instanceof EntitySquidBumper && ((EntitySquidBumper) target).getInkHealth()-rollDamage > 0))))
                                knockbackEntity = target;
                    
                    
                            j++;
                            if(j >= 5)
                            {
                                knockbackEntity = target;
                                break;
                            }
                        }
                    }
                    if(knockbackEntity != null && worldIn.isRemote)
                        applyEntityCollision(knockbackEntity, playerIn, 10);
    
                    if(h > downReach)
                        break;
                }
            
        } else playerIn.sendStatusMessage(new TextComponentTranslation("status.noInk").setStyle(new Style().setColor(TextFormatting.RED)), true);
    }
    
    public void applyEntityCollision(Entity source, Entity target, double power)
    {
        if (!target.isRidingSameEntity(source) && !target.equals(source))
        {
            if (!source.noClip && !target.noClip)
            {
                double d0 = target.posX - source.posX;
                double d1 = target.posZ - source.posZ;
                double d2 = MathHelper.absMax(d0, d1);
                
                if (d2 >= 0.009999999776482582D)
                {
                    d2 = (double)MathHelper.sqrt(d2);
                    d0 = d0 / d2;
                    d1 = d1 / d2;
                    double d3 = 1.0D / d2;
                    
                    if (d3 > 1.0D)
                    {
                        d3 = 1.0D;
                    }
                    
                    d0 = d0 * d3;
                    d1 = d1 * d3;
                    d0 = d0 * 0.05000000074505806D;
                    d1 = d1 * 0.05000000074505806D;
                    d0 = d0 * (double)(1.0F - source.entityCollisionReduction);
                    d1 = d1 * (double)(1.0F - source.entityCollisionReduction);
                    d0 *= power;
                    d1 *= power;
                    
                    if (!target.isBeingRidden())
                    {
                        target.motionX = 0;
                        target.motionZ = 0;
                        target.addVelocity(d0, 0.0D, d1);
                    }
                }
            }
        }
    }
    
    @Override
    public AttributeModifier getSpeedModifier() {
        return SPEED_MODIFIER;
    }
    
    @Override
    public AttributeModifier getNoInkSpeed() { return NO_INK_SPEED_MODIFIER; }
    
    @Override
    public boolean onItemLeftClick(World worldIn, EntityPlayer playerIn, ItemStack stack)
    {
        if(hasInk(playerIn, stack, flingConsumption))
        {
            if(playerIn.getCooledAttackStrength(0) >= 0.95f)
            {
                reduceInk(playerIn, flingConsumption);
                for(int i = -1; i <= 1; i++)
                {
                    EntityInkProjectile proj = new EntityInkProjectile(worldIn, playerIn, ColorItemUtils.getInkColor(stack), flingDamage);
                    proj.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw + ((!isBrush) ? 0 : 20 * i), isBrush ? 0 : 20 * i, flingSpeed, 4f);
                    proj.setProjectileSize(flingSize);
                    worldIn.spawnEntity(proj);
                }
                
                return true;
            }
        } else playerIn.sendStatusMessage(new TextComponentTranslation("status.noInk").setStyle(new Style().setColor(TextFormatting.RED)), true);
        
        return false;
    }
    
    @Override
    public ModelPlayerOverride.EnumAnimType getAnimType()
    {
        return ModelPlayerOverride.EnumAnimType.ROLLER;
    }
    
    private Vec3d getFwd(float pitch, float yaw)
    {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3d((double)(f1 * f2), (double)f3, (double)(f * f2));
    }
}