package com.cibernet.splatcraft.world.save;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.HashMap;
import java.util.Iterator;

public class SplatCraftGamerules
{
	public static HashMap<String, Gamerule> ruleList = new HashMap<>();
	
	public static void registerRules()
	{
		registerGamerule(new Gamerule("inkDecay", true));
		registerGamerule(new Gamerule("keepWeaponsOnDeath", true));
	}
	
	public static void registerGamerule(Gamerule rule)
	{
		ruleList.put(rule.getName(), rule);
	}
	
	public static boolean getGameruleValue(String name) {return ruleList.get(name).getValue();}
	public static void setGameruleValue(String name, boolean value) {ruleList.get(name).setValue(value);}
	
	public static String[] getGameruleNames() {return ruleList.keySet().toArray(new String[ruleList.size()]);}
	
	public static void writeToNBT(NBTTagCompound nbt)
	{
		NBTTagCompound ruleCompound = new NBTTagCompound();
		Iterator<Gamerule> iter = ruleList.values().iterator();
		
		while(iter.hasNext())
		{
			Gamerule gamerule = iter.next();
			ruleCompound.setBoolean(gamerule.getName(), gamerule.getValue());
		}
		
		nbt.setTag("gamerules", ruleCompound);
		
	}
	
	public static void readFromNBT(NBTTagCompound nbt)
	{
		if(nbt == null)
			return;
		NBTTagCompound ruleCompound = nbt.getCompoundTag("gamerules");
		
		for(Gamerule rule : ruleList.values())
		{
			if(!ruleCompound.hasKey(rule.getName()))
				continue;
			rule.setValue(ruleCompound.getBoolean(rule.getName()));
		}
	}
}

class Gamerule
{
	String name;
	boolean defaultValue;
	boolean value;
	
	public Gamerule(String name, boolean defaultValue)
	{
		this.name = name;
		this.defaultValue = defaultValue;
		this.value = defaultValue;
	}
	
	public String getName() {return name;}
	public boolean getValue() {return value;}
	public Gamerule setValue(boolean value) {this.value = value; return this;}
}

