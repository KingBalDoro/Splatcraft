package com.cibernet.splatcraft.registries;

import com.cibernet.splatcraft.blocks.*;
import com.cibernet.splatcraft.utils.TabSplatCraft;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class SplatCraftBlocks
{

    public static Block inkedBlock = new BlockInked();
    public static Block inkedSlab = new BlockInkedSlab();
    public static Block inkedStairs = new BlockInkedStairs();
    public static Block glowingInkedBlock = new BlockInked("glowing_inked_block", true);
    public static Block glowingInkedSlab = new BlockInkedSlab("glowing_inked_slab", true);
    public static Block glowingInkedStairs = new BlockInkedStairs("glowing_inked_stairs", true);

    public static Block emptyInkwell = new BlockInkwellEmpty();
    public static Block inkwell = new BlockInkwell();
    public static Block canvas = new BlockCanvas();
    public static Block inkedWool = new BlockInkedWool("inkedWool", "inked_wool");

    public static Block sunkenCrate = new BlockSunkenCrate();
    public static Block oreSardinium = new BlockOre(1, "oreSardinium", "sardinium_ore");
    
    public static Block grate = new BlockGrate("grate", "grate");
    public static Block barrierBar = new BlockBarrierBar("barrierBar", "barrier_bar");
    
    public static Block PowerEggBlock = new BlockPowerEggStorage();
    public static Block sardiniumBlock = new Block(Material.IRON, MapColor.IRON){{setSoundType(SoundType.METAL);}
    
        @Override
        public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {return true;}
    }.setHardness(5.0F).setResistance(10.0F).setUnlocalizedName("sardiniumBlock").setRegistryName("sardinium_block").setCreativeTab(TabSplatCraft.main);
    
    public static Block inkwellVat = new BlockInkwellVat();
    public static Block weaponStation = new BlockWeaponStation();
    
    public static Block stageBarrier = new BlockSCBarrier("stageBarrier", "stage_barrier", false);
    public static Block stageVoid = new BlockSCBarrier("stageVoid", "stage_void", true);
    
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        IForgeRegistry<Block> registry = event.getRegistry();
    
        registry.register(new BlockIronBars());
        
        registerBlock(registry, inkedBlock);
        registerBlock(registry, inkedSlab);
        registerBlock(registry, inkedStairs);
        registerBlock(registry, glowingInkedBlock);
        registerBlock(registry, glowingInkedSlab);
        registerBlock(registry, glowingInkedStairs);
        
        registerBlock(registry, emptyInkwell, true);
        registerBlock(registry, inkwell);
        registerBlock(registry, canvas, true);
        registerBlock(registry, inkedWool);
        registerBlock(registry, grate, true);
        registerBlock(registry, barrierBar, true);
        
        registerBlock(registry, sunkenCrate, true);
        registerBlock(registry, oreSardinium, true);
        
        registerBlock(registry, PowerEggBlock, true);
        registerBlock(registry, sardiniumBlock, true);
        
        registerBlock(registry,inkwellVat, true);
        registerBlock(registry,weaponStation, true);
        
        registerBlock(registry, stageBarrier, true);
        registerBlock(registry, stageVoid, true);

    }


    private static Block registerBlock(IForgeRegistry<Block> registry, Block block, boolean... hasItem)
    {
        registry.register(block);
        SplatCraftModelManager.blocks.add(block);

        if(hasItem.length > 0)
            SplatCraftItems.itemBlocks.add(block);

        return block;
    }
}
