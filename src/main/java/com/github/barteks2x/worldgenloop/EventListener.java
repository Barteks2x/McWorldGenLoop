package com.github.barteks2x.worldgenloop;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.barteks2x.worldgenloop.noisegen.LoopedNoiseGenOctaves;
import com.github.barteks2x.worldgenloop.noisegen.LoopedNoiseGenSimplex2d;
import com.github.barteks2x.worldgenloop.util.ReflectionUtil;
import com.ibm.icu.math.MathContext;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.NoiseGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.InitNoiseGensEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.WorldTypeEvent;
import net.minecraftforge.event.terraingen.WorldTypeEvent.InitBiomeGens;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventListener {
	private static final Logger logger = LogManager.getLogger(WorldgenLoop.MODID + ":"
			+ EventListener.class.getSimpleName());

	private final Set<Integer> replacedMapGensDimensions = new HashSet<Integer>(3);
	@SubscribeEvent
	public void onEvent(InitNoiseGensEvent event) {
		WorldGenUtil.transformNoiseGenerators(event.originalNoiseGens, event.newNoiseGens, event.world);
	}

	@SubscribeEvent
	public void onEvent(WorldTypeEvent.InitBiomeGens event) {
		WorldGenUtil.transformGenLayers(event.originalBiomeGens, event.newBiomeGens, event.seed);
	}

	@SubscribeEvent
	public void onEvent(PopulateChunkEvent.Pre event) {
		WorldGenUtil.modifySeed(event.rand, event.chunkX, event.chunkZ, event.world);
	}
	
	@SubscribeEvent
	public void onEvent(ChunkProviderEvent.ReplaceBiomeBlocks event){
		int dim = event.world.provider.getDimensionId();
		if(this.replacedMapGensDimensions.contains(dim)){
			//don't touch, we already replaced it
			return;
		}
		this.replacedMapGensDimensions.add(dim);
		IChunkProvider prov = event.chunkProvider;
		if(!(prov instanceof ChunkProviderGenerate)){
			return;
		}
		List<Field> genFields = ReflectionUtil.getFieldsByType(MapGenBase.class, ChunkProviderGenerate.class, false);
		
		for(Field field : genFields){
			MapGenBase value = ReflectionUtil.getValue(field, prov, MapGenBase.class);
			value = WorldGenUtil.tileableMapGen(value);
			ReflectionUtil.setValue(field, prov, value);
		}
	}
}
