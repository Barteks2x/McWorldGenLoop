package com.github.barteks2x.worldgenloop.mapgen;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;

import com.github.barteks2x.worldgenloop.Config;
import com.github.barteks2x.worldgenloop.util.Bits;
import com.github.barteks2x.worldgenloop.util.ReflectionUtil;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenBase;

public class MapGenTileableUtil {

	public static void generateTileable(MapGenBase base, IChunkProvider chunkProvider, //
			World worldIn, int chunkX, int chunkZ, ChunkPrimer blocks) {
		// radius is the inly int field in MapGenBase
		int radius = ReflectionUtil.getValueByType(int.class, MapGenBase.class, base, false);

		// set world
		Field worldField = ReflectionUtil.getFieldByType(World.class, MapGenBase.class, false);
		ReflectionUtil.setValue(worldField, base, worldIn);

		// This is the same rand instance that will be used by other generator
		Random rand = ReflectionUtil.getValueByType(Random.class, MapGenBase.class, base, false);
		rand.setSeed(worldIn.getSeed());
		long randX = rand.nextLong();
		long randZ = rand.nextLong();

		int bitmask = Bits.bitmaskClamped(Config.loopBits(worldIn.provider.getDimensionId()) - 4);

		Method meth = ReflectionUtil.findMethod(MapGenBase.class, void.class, false, //
				World.class, int.class, int.class, int.class, int.class, ChunkPrimer.class);

		for (int xOrigin = chunkX - radius; xOrigin <= chunkX + radius; ++xOrigin) {
			for (int zOrigin = chunkZ - radius; zOrigin <= chunkZ + radius; ++zOrigin) {
				int xOriginMod = xOrigin & bitmask;
				int zOriginMod = zOrigin & bitmask;

				long xSeed = (long) xOriginMod * randX;
				long zSeedi2 = (long) zOriginMod * randZ;
				rand.setSeed(xSeed ^ zSeedi2 ^ worldIn.getSeed());

				ReflectionUtil.invokeMethod(void.class, base, meth, //
						worldIn, xOrigin, zOrigin, chunkX, chunkZ, blocks);
			}
		}
	}
}