package com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers;

import com.github.barteks2x.worldgenloop.biomegenlayer.GenLayerTileableUtil;
import com.github.barteks2x.worldgenloop.biomegenlayer.TileableGenLayer;
import com.github.barteks2x.worldgenloop.util.ReflectionUtil;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerIsland;
import net.minecraft.world.gen.layer.GenLayerRemoveTooMuchOcean;
import net.minecraft.world.gen.layer.IntCache;

public class PlainsIslandTileable extends GenLayerIsland implements TileableGenLayer {

	private int size;

	public PlainsIslandTileable(GenLayerIsland originalLayer) {
		super(0);
		ReflectionUtil.copyNonStaticFieldsByType(GenLayer.class, long.class, originalLayer, this);
	}

	public void initChunkSeed(long x, long z) {
		super.initChunkSeed(x & size, z & size);
	}

	@Override
	public void setSizeBits(int bits) {
		this.size = GenLayerTileableUtil.sizeBitmask(bits);
	}

	@Override
	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
		int[] aint = IntCache.getIntCache(areaWidth * areaHeight);

		for (int i1 = 0; i1 < areaHeight; ++i1) {
			for (int j1 = 0; j1 < areaWidth; ++j1) {
				this.initChunkSeed((long) (areaX + j1), (long) (areaY + i1));
				aint[j1 + i1 * areaWidth] = this.nextInt(10) == 0 ? 1 : 0;
			}
		}

		// don't do that
		// if(areaX>-areaWidth&&areaX<=0&&areaY>-areaHeight&&areaY<=0){
		// ////aint[-areaX + -areaY * areaWidth] = 1;
		// }

		return aint;
	}
}
