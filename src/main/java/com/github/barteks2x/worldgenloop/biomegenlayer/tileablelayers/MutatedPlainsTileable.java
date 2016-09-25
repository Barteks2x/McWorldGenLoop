package com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerAddIsland;
import net.minecraft.world.gen.layer.GenLayerRareBiome;

import com.github.barteks2x.worldgenloop.biomegenlayer.GenLayerTileableUtil;
import com.github.barteks2x.worldgenloop.biomegenlayer.TileableGenLayer;
import com.github.barteks2x.worldgenloop.util.ReflectionUtil;

public class MutatedPlainsTileable extends GenLayerRareBiome implements TileableGenLayer {

	private int size;

	public MutatedPlainsTileable(GenLayerRareBiome originalLayer) {
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
