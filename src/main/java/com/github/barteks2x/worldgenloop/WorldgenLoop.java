package com.github.barteks2x.worldgenloop;

import net.minecraft.init.Blocks;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = WorldgenLoop.MODID, version = WorldgenLoop.VERSION)
public class WorldgenLoop {
	public static final String MODID = "worldgenloop";
	public static final String VERSION = "1.0";

	public static WorldType customizedPlanet = new CustomizedPlanetWorldType();

	@EventHandler
	public void init(FMLInitializationEvent event) {
		FMLLog.getLogger();
		MinecraftForge.TERRAIN_GEN_BUS.register(new EventListener());
		MinecraftForge.EVENT_BUS.register(new EventListener());
	}
}
