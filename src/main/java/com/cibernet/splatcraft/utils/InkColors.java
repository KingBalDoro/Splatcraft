package com.cibernet.splatcraft.utils;

import com.google.common.collect.Maps;
import net.minecraft.block.material.MapColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.common.util.EnumHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum InkColors
{
	ORANGE(0xDF641A, MapColor.getBlockColor(EnumDyeColor.ORANGE), "orange"),
	BLUE(0x26229F, MapColor.getBlockColor(EnumDyeColor.BLUE), "blue"),
	PINK(0xc83d79, MapColor.getBlockColor(EnumDyeColor.PINK), "pink"),
	GREEN(0x409d3b, MapColor.getBlockColor(EnumDyeColor.GREEN), "green"),
	LIGHT_BLUE(0x228cff, MapColor.getBlockColor(EnumDyeColor.LIGHT_BLUE), "lightBlue"),
	TURQUOISE(0x048188, MapColor.getBlockColor(EnumDyeColor.CYAN), "turquoise"),
	YELLOW(0xe1a307, MapColor.getBlockColor(EnumDyeColor.YELLOW), "yellow"),
	LILAC(0x4d24a3, MapColor.getBlockColor(EnumDyeColor.PURPLE), "lilac"),
	LEMON(0x91b00b, MapColor.FOLIAGE, "lemon"),
	PLUM(0x830b9c, MapColor.MAGENTA, "plum"),

	CYAN(0x4ACBCB, MapColor.DIAMOND, "cyan"),
	TANGERINE(0xEA8546, MapColor.ORANGE_STAINED_HARDENED_CLAY, "peach"),
	MINT(0x08B672, MapColor.LIGHT_BLUE, "mint"),
	CHERRY(0xE24F65, MapColor.PINK_STAINED_HARDENED_CLAY, "cherry"),

	NEON_PINK(0xcf0466, MapColor.getBlockColor(EnumDyeColor.PINK), "neonPink"),
	NEON_GREEN(0x17a80d, MapColor.getBlockColor(EnumDyeColor.GREEN), "neonGreen"),
	NEON_ORANGE(0xe85407, MapColor.getBlockColor(EnumDyeColor.ORANGE), "neonOrange"),
	NEON_BLUE(0x2e0cb5, MapColor.getBlockColor(EnumDyeColor.BLUE), "neonBlue"),
	
	SQUID(0xBDDD00, MapColor.getBlockColor(EnumDyeColor.YELLOW), "squid"),
	OCTO(0xE51B5E, MapColor.getBlockColor(EnumDyeColor.MAGENTA), "octo"),
	
	MOJANG(0xDF242F, MapColor.RED_STAINED_HARDENED_CLAY, "mojangRed"),
	COBALT(0x005682, MapColor.getBlockColor(EnumDyeColor.CYAN), "cobalt"),
	ICEARSTORM(0x88ffc1, MapColor.LIGHT_BLUE, "icearstorm"),
	FLORAL(0xFF9BEE, MapColor.PINK, "floral"),
		
	DYE_WHITE(0xFAFAFA, MapColor.getBlockColor(EnumDyeColor.WHITE), "dyeWhite", EnumDyeColor.WHITE),
	DYE_ORANGE(16351261, MapColor.getBlockColor(EnumDyeColor.ORANGE), "dyeOrange", EnumDyeColor.ORANGE),
	DYE_MAGENTA(13061821, MapColor.getBlockColor(EnumDyeColor.MAGENTA), "dyeMagenta", EnumDyeColor.MAGENTA),
	DYE_LIGHT_BLUE(3847130, MapColor.getBlockColor(EnumDyeColor.LIGHT_BLUE), "dyeLightBlue", EnumDyeColor.LIGHT_BLUE),
	DYE_YELLOW(16701501, MapColor.getBlockColor(EnumDyeColor.YELLOW), "dyeYellow", EnumDyeColor.YELLOW),
	DYE_LIME(8439583, MapColor.getBlockColor(EnumDyeColor.LIME), "dyeLime", EnumDyeColor.LIME),
	DYE_PINK(15961002, MapColor.getBlockColor(EnumDyeColor.PINK), "dyePink", EnumDyeColor.PINK),
	DYE_GRAY(4673362, MapColor.getBlockColor(EnumDyeColor.GRAY), "dyeGray", EnumDyeColor.GRAY),
	DYE_SILVER(10329495, MapColor.getBlockColor(EnumDyeColor.SILVER), "dyeSilver", EnumDyeColor.SILVER),
	DYE_CYAN(1481884, MapColor.getBlockColor(EnumDyeColor.CYAN), "dyeCyan", EnumDyeColor.CYAN),
	DYE_PURPLE(8991416, MapColor.getBlockColor(EnumDyeColor.PURPLE), "dyePurple", EnumDyeColor.PURPLE),
	DYE_BLUE(3949738, MapColor.getBlockColor(EnumDyeColor.BLUE), "dyeBlue", EnumDyeColor.BLUE),
	DYE_BROWN(8606770, MapColor.getBlockColor(EnumDyeColor.BROWN), "dyeBrown", EnumDyeColor.BROWN),
	DYE_GREEN(6192150, MapColor.getBlockColor(EnumDyeColor.GREEN), "dyeGreen", EnumDyeColor.GREEN),
	DYE_RED(11546150, MapColor.getBlockColor(EnumDyeColor.RED), "dyeRed", EnumDyeColor.RED),
	DYE_BLACK(1908001, MapColor.getBlockColor(EnumDyeColor.BLACK), "dyeBlack", EnumDyeColor.BLACK),
	
	INK_BLACK(0x1F1F2D, MapColor.CYAN_STAINED_HARDENED_CLAY, "default"),
	;

	InkColors(Integer color, MapColor mapColor, String displayName, EnumDyeColor dyeColor)
	{
		this.color = color;
		this.mapColor = mapColor;
		this.name = displayName;
		this.dyeColor = dyeColor;
	}

	InkColors(Integer color, MapColor mapColor, String displayName)
	{
		this(color, mapColor, displayName, null);
	}
	
	public static InkColors addColor(String name, int color, MapColor mapColor, String unlocalizedName)
	{
		return EnumHelper.addEnum(InkColors.class, name, new Class[] {Integer.class, MapColor.class, String.class}, color, mapColor, unlocalizedName);
	}
	
	public static List<InkColors> getStarterColorArray() {return starterColors;}
	
	public static InkColors getRandomStarterColor()
	{
		return starterColors.get((int) (Math.random()*starterColors.size()));
	}
	
	public static final List<InkColors> creativeTabColors = new ArrayList<InkColors>() {{add(ORANGE); add(BLUE); add(GREEN); add(PINK); }};
	public static final List<InkColors> starterColors = new ArrayList<InkColors>() {{add(ORANGE); add(BLUE); add(GREEN); add(PINK); }};
	
	private final int color;
	private final MapColor mapColor;
	private final String name;
	private final EnumDyeColor dyeColor;
	
	public int getColor()
	{
		return color;
	}
	
	public static InkColors getByColor(int color)
	{
		return BY_INT.get(color);
	}
	
	public static InkColors getByName(String name)
	{
		return BY_NAME.get(name);
	}
	
	public static Set<String> getNameSet() {return BY_NAME.keySet();}
	
	public MapColor getMapColor()
	{
		return mapColor;
	}
	public String getName() {return name;}
	public EnumDyeColor getDyeColor() {return dyeColor;}
	
	private static final Map<String, InkColors> BY_NAME = Maps.<String, InkColors>newHashMap();
	private static final Map<Integer, InkColors> BY_INT = Maps.<Integer, InkColors>newHashMap();
	static
	{
		for (InkColors color : values())
		{
			BY_NAME.put(color.getName(), color);
			BY_INT.put(color.getColor(), color);
		}
	}
}
