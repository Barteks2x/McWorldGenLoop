package com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers;

import static com.github.barteks2x.worldgenloop.util.ReflectionUtil.copyNonStaticFieldsByType;
import static com.github.barteks2x.worldgenloop.util.ReflectionUtil.getValue;
import static com.github.barteks2x.worldgenloop.util.ReflectionUtil.setValue;

import java.lang.reflect.Field;
import java.util.List;

import com.github.barteks2x.worldgenloop.biomegenlayer.GenLayerTileableUtil;
import com.github.barteks2x.worldgenloop.biomegenlayer.TileableGenLayer;
import com.github.barteks2x.worldgenloop.util.ReflectionUtil;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerAddMushroomIsland;
import net.minecraft.world.gen.layer.GenLayerEdge;
import net.minecraft.world.gen.layer.GenLayerHills;

public class EdgeTemperaturesTileable extends GenLayerEdge implements TileableGenLayer {

	private int size;

	public EdgeTemperaturesTileable(GenLayerEdge original) {
		super(0, null, null);
		copyNonStaticFieldsByType(GenLayer.class, long.class, original, this);
		copyNonStaticFieldsByType(GenLayer.class, GenLayer.class, original, this);
		copyNonStaticFieldsByType(GenLayerEdge.class, GenLayerEdge.Mode.class, original, this);

		this.parent = GenLayerTileableUtil.getTileableLayer(this.parent);
	}

	public void initChunkSeed(long x, long z) {
		super.initChunkSeed(x & size, z & size);
	}

	@Override
	public void setSizeBits(int bits) {
		((TileableGenLayer) this.parent).setSizeBits(bits);
		this.size = GenLayerTileableUtil.sizeBitmask(bits);
	}
}
