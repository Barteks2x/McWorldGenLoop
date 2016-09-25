package com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers;

import com.github.barteks2x.worldgenloop.biomegenlayer.GenLayerTileableUtil;
import com.github.barteks2x.worldgenloop.biomegenlayer.TileableGenLayer;
import com.github.barteks2x.worldgenloop.util.ReflectionUtil;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerBiomeEdge;
import net.minecraft.world.gen.layer.GenLayerDeepOcean;

public class DeepOceanTileable extends GenLayerDeepOcean implements TileableGenLayer {

	private int size;

	public DeepOceanTileable(GenLayerDeepOcean originalLayer) {
		super(0, null);
		ReflectionUtil.copyNonStaticFieldsByType(GenLayer.class, long.class, originalLayer, this);
		ReflectionUtil.copyNonStaticFieldsByType(GenLayer.class, GenLayer.class, originalLayer, this);
		this.parent = GenLayerTileableUtil.getTileableLayer(this.parent);
	}

	public void initChunkSeed(long x, long z) {
		super.initChunkSeed(x & size, z & size);
	}

	@Override
	public void setSizeBits(int bits) {
		this.size = GenLayerTileableUtil.sizeBitmask(bits);
		TileableGenLayer tileable = (TileableGenLayer) this.parent;
		tileable.setSizeBits(bits);
	}
}
