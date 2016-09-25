package com.github.barteks2x.worldgenloop;

import java.util.List;
import java.util.UUID;

import com.github.barteks2x.worldgenloop.util.ReflectionUtil;

import net.minecraft.world.World;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.layer.GenLayer;

public class TileableChunkProvider extends ChunkProviderGenerate {

	public TileableChunkProvider(World worldIn, long seed, boolean someFlag, String settings) {
		super(worldIn, seed, someFlag, getChunkProviderGenerateSettingsAndSetConfig(settings, worldIn));

		int size = Config.loopBits(worldIn.provider.getDimensionId());
		Config.setSizeBits(worldIn.provider.getDimensionId(), size);
		WorldChunkManager wcm = worldIn.provider.getWorldChunkManager();
		List<GenLayer> genLayers = ReflectionUtil.getValuesByType(GenLayer.class, WorldChunkManager.class, wcm, false);
		WorldGenUtil.setLayerSize(genLayers, size);
	}

	private static String getTileableProviderSettings(String settings) {
		if (settings == null) {
			return null;
		}
		int end = settings.indexOf('|');
		String ret = settings.substring(0, end);
		return ret;
	}

	private static String getChunkProviderGenerateSettingsAndSetConfig(String settings, World world) {
		if (settings == null) {
			return null;
		}
		int start = settings.indexOf('|');
		String mySettings = getTileableProviderSettings(settings);
		// for now it will be just a single number
		int size = 0;
		try {
			size = Integer.parseInt(mySettings);
		} catch (NumberFormatException e) {
			size = Config.defaultLoopBits();
		}
		Config.setSizeBits(world.provider.getDimensionId(), size);
		String ret = settings.substring(start + 1);
		return ret;
	}

}
