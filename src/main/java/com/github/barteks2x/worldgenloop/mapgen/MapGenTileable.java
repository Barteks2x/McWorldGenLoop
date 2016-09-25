package com.github.barteks2x.worldgenloop.mapgen;

import com.google.common.base.Objects;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;

public class MapGenTileable extends MapGenBase {
	private MapGenBase generator;
	
	public MapGenTileable(MapGenBase generator){
		this.generator = generator;
	}
	@Override
	// generate
	public void func_175792_a(IChunkProvider chunkProv, World worldIn, int x, int z, ChunkPrimer blocks) {
		MapGenTileableUtil.generateTileable(generator, chunkProv, worldIn, x, z, blocks);
	}
}