package com.github.barteks2x.worldgenloop;

import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.barteks2x.worldgenloop.biomegenlayer.GenLayerTileableUtil;
import com.github.barteks2x.worldgenloop.biomegenlayer.TileableGenLayer;
import com.github.barteks2x.worldgenloop.mapgen.MapGenTileable;
import com.github.barteks2x.worldgenloop.noisegen.LoopedNoiseGenOctaves;
import com.github.barteks2x.worldgenloop.noisegen.LoopedNoiseGenSimplex2d;
import com.github.barteks2x.worldgenloop.util.Bits;

import net.minecraft.world.World;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.NoiseGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;

public class WorldGenUtil {
	private static final Logger logger = LogManager.getLogger(WorldgenLoop.MODID + ":"
			+ WorldGenUtil.class.getSimpleName());

	public static void transformNoiseGenerators(NoiseGenerator[] oldGen, NoiseGenerator[] newGen, World world) {
		int dimId =  world.provider.getDimensionId();
		logger.info("Replacing noise generator for world " + world.getWorldInfo().getWorldName() + ", dimensionID="
				+ dimId);
		
		System.out.println("WorldClass"+world.getClass().getName());
		

		if (oldGen.length != newGen.length) {
			throw new IllegalArgumentException("oldGen and newGen lengths are not equal!");
		}

		for (int i = 0; i < oldGen.length; i++) {
			NoiseGenerator noiseGen = oldGen[i];
			int loopBits = Config.loopBits(world.provider.getDimensionId());
			if (noiseGen instanceof NoiseGeneratorOctaves) {
				newGen[i] = new LoopedNoiseGenOctaves((NoiseGeneratorOctaves) noiseGen, world.getSeed() + i, loopBits);
				continue;
			}
			if (noiseGen instanceof NoiseGeneratorPerlin) {
				newGen[i] = new LoopedNoiseGenSimplex2d((NoiseGeneratorPerlin) noiseGen, world.getSeed() + i, loopBits);
				continue;
			}
			// other mods?
			logger.warn("Unknown noise generator class: "
					+ noiseGen.getClass().getName()
					+ ", if there are no other mods - it's bug in this mod, if there are other mods - weird things may happen...");

		}
	}

	public static void transformGenLayers(GenLayer[] originalBiomeGens, GenLayer[] newBiomeGens, long seed) {
		logger.info("Replacing biome gen layers");

		for (int i = 0; i < originalBiomeGens.length; i++) {
			newBiomeGens[i] = GenLayerTileableUtil.getTileableLayer(originalBiomeGens[i]);
		}
	}

	public static void setLayerSize(List<GenLayer> layers, int size) {
		for (GenLayer layer : layers) {
			if (layer instanceof GenLayerVoronoiZoom) {
				((TileableGenLayer) layer).setSizeBits(size);
			} else {
				((TileableGenLayer) layer).setSizeBits(size - 2);
			}
		}
	}

	public static void modifySeed(Random rand, int chunkX, int chunkZ, World world) {
		int bitmask = Bits.bitmaskClamped(Config.loopBits(world.provider.getDimensionId()) - 4);
		chunkX &= bitmask;
		chunkZ &= bitmask;
		long seed = world.getSeed();
		rand.setSeed(seed);
		long xRand = rand.nextLong() / 2L * 2L + 1L;
		long zRand = rand.nextLong() / 2L * 2L + 1L;
		rand.setSeed((long) chunkX * xRand + (long) chunkZ * zRand ^ seed);
	}

	public static MapGenBase tileableMapGen(MapGenBase originalGen) {
		logger.info("Creating new Generator for typr: " + originalGen.getClass());
		return new MapGenTileable(originalGen);
	}
}
