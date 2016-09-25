package com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers;

import java.util.List;

import com.github.barteks2x.worldgenloop.biomegenlayer.GenLayerTileableUtil;
import com.github.barteks2x.worldgenloop.biomegenlayer.TileableGenLayer;
import com.github.barteks2x.worldgenloop.util.ReflectionUtil;

import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerBiome;
import net.minecraft.world.gen.layer.GenLayerBiomeEdge;

public class BiomeTileable extends GenLayerBiome implements TileableGenLayer{

	private int size;

	public BiomeTileable(GenLayerBiome originalLayer) {
		super(0, null, null, null);
		ReflectionUtil.copyNonStaticFieldsByType(GenLayer.class, long.class, originalLayer, this);
		ReflectionUtil.copyNonStaticFieldsByType(GenLayer.class, GenLayer.class, originalLayer, this);
		ReflectionUtil.copyNonStaticFieldsByType(GenLayerBiome.class, List[].class, originalLayer, this);
		ReflectionUtil.copyNonStaticFieldsByType(GenLayerBiome.class, ChunkProviderSettings.class, originalLayer, this);
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
