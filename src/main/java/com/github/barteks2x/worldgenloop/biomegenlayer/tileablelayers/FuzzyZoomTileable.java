package com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers;

import com.github.barteks2x.worldgenloop.biomegenlayer.GenLayerTileableUtil;
import com.github.barteks2x.worldgenloop.biomegenlayer.TileableGenLayer;
import com.github.barteks2x.worldgenloop.util.ReflectionUtil;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerFuzzyZoom;
import net.minecraft.world.gen.layer.GenLayerRemoveTooMuchOcean;

public class FuzzyZoomTileable extends GenLayerFuzzyZoom implements TileableGenLayer {

	private int size;

	public FuzzyZoomTileable(GenLayerFuzzyZoom originalLayer) {
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
		bits--;
		this.size = GenLayerTileableUtil.sizeBitmask(bits);
		TileableGenLayer tileable = (TileableGenLayer) this.parent;
		tileable.setSizeBits(bits);
	}
}
