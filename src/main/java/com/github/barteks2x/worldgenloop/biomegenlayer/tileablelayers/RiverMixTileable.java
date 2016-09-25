package com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers;

import java.lang.reflect.Field;
import java.util.List;

import com.github.barteks2x.worldgenloop.biomegenlayer.GenLayerTileableUtil;
import com.github.barteks2x.worldgenloop.biomegenlayer.TileableGenLayer;
import com.github.barteks2x.worldgenloop.util.ReflectionUtil;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import static com.github.barteks2x.worldgenloop.util.ReflectionUtil.*;

public class RiverMixTileable extends GenLayerRiverMix implements TileableGenLayer {

	private TileableGenLayer[] layers = new TileableGenLayer[2];
	private int size;

	public RiverMixTileable(GenLayerRiverMix original) {
		super(0, null, null);
		copyNonStaticFieldsByType(GenLayer.class, long.class, original, this);
		copyNonStaticFieldsByType(GenLayerRiverMix.class, GenLayer.class, original, this);

		List<Field> genLayers = ReflectionUtil.getFieldsByType(GenLayer.class, GenLayerRiverMix.class, false);
		assert genLayers.size() == layers.length;
		int i = 0;
		for (Field field : genLayers) {
			GenLayer layer = getValue(field, this, GenLayer.class);
			GenLayer newLayer = GenLayerTileableUtil.getTileableLayer(layer);
			setValue(field, this, newLayer);
			layers[i++] = (TileableGenLayer) newLayer;
		}
	}

	@Override
	public void initChunkSeed(long x, long z) {
		super.initChunkSeed(x & size, z & size);
	}

	@Override
	public void setSizeBits(int bits) {
		for (TileableGenLayer layer : layers) {
			layer.setSizeBits(bits);
		}
		this.size = GenLayerTileableUtil.sizeBitmask(bits);
	}

}
