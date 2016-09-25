package com.github.barteks2x.worldgenloop.biomegenlayer;

import com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers.AddForestIslandTileable;
import com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers.AddMushroomIslandTileable;
import com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers.AddSnowTileable;
import com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers.BiomeEdgeTileable;
import com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers.BiomeTileable;
import com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers.DeepOceanTileable;
import com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers.EdgeTemperaturesTileable;
import com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers.FuzzyZoomTileable;
import com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers.HillsAndMutatedTileable;
import com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers.MutatedPlainsTileable;
import com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers.PlainsIslandTileable;
import com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers.RemoveTooMuchOceanTileable;
import com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers.RiverInitTileable;
import com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers.RiverMixTileable;
import com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers.RiverTileable;
import com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers.ShoreTileable;
import com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers.SmoothTileable;
import com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers.VoronoiZoomTileable;
import com.github.barteks2x.worldgenloop.biomegenlayer.tileablelayers.ZoomTileable;
import com.github.barteks2x.worldgenloop.util.Bits;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerAddIsland;
import net.minecraft.world.gen.layer.GenLayerAddMushroomIsland;
import net.minecraft.world.gen.layer.GenLayerAddSnow;
import net.minecraft.world.gen.layer.GenLayerBiome;
import net.minecraft.world.gen.layer.GenLayerBiomeEdge;
import net.minecraft.world.gen.layer.GenLayerDeepOcean;
import net.minecraft.world.gen.layer.GenLayerEdge;
import net.minecraft.world.gen.layer.GenLayerFuzzyZoom;
import net.minecraft.world.gen.layer.GenLayerHills;
import net.minecraft.world.gen.layer.GenLayerIsland;
import net.minecraft.world.gen.layer.GenLayerRareBiome;
import net.minecraft.world.gen.layer.GenLayerRemoveTooMuchOcean;
import net.minecraft.world.gen.layer.GenLayerRiver;
import net.minecraft.world.gen.layer.GenLayerRiverInit;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import net.minecraft.world.gen.layer.GenLayerShore;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;

public class GenLayerTileableUtil {
	private GenLayerTileableUtil() {
		throw new Error();
	}

	public static GenLayer getTileableLayer(GenLayer layer) {
		Class<?> cl = layer.getClass();

		if (cl == GenLayerVoronoiZoom.class) {
			return new VoronoiZoomTileable((GenLayerVoronoiZoom) layer);
		}
		if (cl == GenLayerRiverMix.class) {
			return new RiverMixTileable((GenLayerRiverMix) layer);
		}
		if (cl == GenLayerSmooth.class) {
			return new SmoothTileable((GenLayerSmooth) layer);
		}
		if (cl == GenLayerZoom.class) {
			return new ZoomTileable((GenLayerZoom) layer);
		}
		if (cl == GenLayerShore.class) {
			return new ShoreTileable((GenLayerShore) layer);
		}
		if (cl == GenLayerAddIsland.class) {
			return new AddForestIslandTileable((GenLayerAddIsland) layer);
		}
		if (cl == GenLayerRareBiome.class) {
			return new MutatedPlainsTileable((GenLayerRareBiome) layer);
		}
		if (cl == GenLayerHills.class) {
			return new HillsAndMutatedTileable((GenLayerHills) layer);
		}
		if (cl == GenLayerBiomeEdge.class) {
			return new BiomeEdgeTileable((GenLayerBiomeEdge) layer);
		}
		if (cl == GenLayerBiome.class) {
			return new BiomeTileable((GenLayerBiome) layer);
		}
		if (cl == GenLayerDeepOcean.class) {
			return new DeepOceanTileable((GenLayerDeepOcean) layer);
		}
		if (cl == GenLayerAddMushroomIsland.class) {
			return new AddMushroomIslandTileable((GenLayerAddMushroomIsland) layer);
		}
		if (cl == GenLayerEdge.class) {
			return new EdgeTemperaturesTileable((GenLayerEdge) layer);
		}
		if (cl == GenLayerAddSnow.class) {
			return new AddSnowTileable((GenLayerAddSnow) layer);
		}
		if (cl == GenLayerRemoveTooMuchOcean.class) {
			return new RemoveTooMuchOceanTileable((GenLayerRemoveTooMuchOcean) layer);
		}
		if (cl == GenLayerFuzzyZoom.class) {
			return new FuzzyZoomTileable((GenLayerFuzzyZoom) layer);
		}
		if (cl == GenLayerIsland.class) {
			return new PlainsIslandTileable((GenLayerIsland) layer);
		}
		if (cl == GenLayerRiverInit.class) {
			return new RiverInitTileable((GenLayerRiverInit) layer);
		}
		if (cl == GenLayerRiver.class) {
			return new RiverTileable((GenLayerRiver) layer);
		}
		throw new UnknownGenLayerException(layer);
	}

	public static int sizeBitmask(int bits) {
		return Bits.bitmaskClamped(bits);
	}
}
