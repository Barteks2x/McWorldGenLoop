package com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers;

import static com.github.barteks2x.worldgenloop.util.ReflectionUtil.copyNonStaticFieldsByType;
import static com.github.barteks2x.worldgenloop.util.ReflectionUtil.getValue;
import static com.github.barteks2x.worldgenloop.util.ReflectionUtil.setValue;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerHills;
import net.minecraft.world.gen.layer.GenLayerRiverMix;

import com.github.barteks2x.worldgenloop.biomegenlayer.GenLayerTileableUtil;
import com.github.barteks2x.worldgenloop.biomegenlayer.TileableGenLayer;
import com.github.barteks2x.worldgenloop.util.ReflectionUtil;

public class HillsAndMutatedTileable extends GenLayerHills implements TileableGenLayer {

	private TileableGenLayer[] layers = new TileableGenLayer[1];
	private int size;

	public HillsAndMutatedTileable(GenLayerHills original) {
		super(0, null, null);
		copyNonStaticFieldsByType(GenLayer.class, long.class, original, this);
		copyNonStaticFieldsByType(GenLayer.class, GenLayer.class, original, this);
		copyNonStaticFieldsByType(GenLayerHills.class, GenLayer.class, original, this);

		this.parent = GenLayerTileableUtil.getTileableLayer(this.parent);

		List<Field> genLayers = ReflectionUtil.getFieldsByType(GenLayer.class, GenLayerHills.class, false);
		assert genLayers.size() == layers.length;
		int i = 0;
		for (Field field : genLayers) {
			GenLayer layer = getValue(field, this, GenLayer.class);
			GenLayer newLayer = GenLayerTileableUtil.getTileableLayer(layer);
			setValue(field, this, newLayer);
			layers[i++] = (TileableGenLayer) newLayer;
		}
	}

	public void initChunkSeed(long x, long z) {
		super.initChunkSeed(x & size, z & size);
	}

	@Override
	public void setSizeBits(int bits) {
		for (TileableGenLayer layer : layers) {
			layer.setSizeBits(bits);
		}
		((TileableGenLayer) this.parent).setSizeBits(bits);
		this.size = GenLayerTileableUtil.sizeBitmask(bits);
	}
}
