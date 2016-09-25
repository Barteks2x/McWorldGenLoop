package com.github.barteks2x.worldgenloop.noisegen;

import java.lang.reflect.Field;
import java.util.Random;

import com.github.barteks2x.worldgenloop.util.ReflectionUtil;

import net.minecraft.world.gen.NoiseGeneratorImproved;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class LoopedNoiseGenOctaves extends NoiseGeneratorOctaves {

	private final int octaves;
	private final PerlinBasis perlin;
	private double loopSize;
	private final double xCoord;
	private final double yCoord;
	private final double zCoord;
	private final double wCoord;
	private final double tCoord;

	public LoopedNoiseGenOctaves(Random rand, int numOctaves, int bits) {
		super(rand, 0);// don't construct any NoiseGenerator instances
		this.octaves = numOctaves;
		this.perlin = new PerlinBasis();
		perlin.setSeed(rand.nextInt());
		this.loopSize = 1 << (bits - 2);
		//this.loopSize /= 1.65;//size  27 --> farlands starting near x=z=3208300
		this.xCoord = rand.nextDouble() * 256.0D;
		this.yCoord = rand.nextDouble() * 256.0D;
		this.zCoord = rand.nextDouble() * 256.0D;
		this.wCoord = rand.nextDouble() * 256.0D;
		this.tCoord = rand.nextDouble() * 256.0D;
	}

	public LoopedNoiseGenOctaves(NoiseGeneratorOctaves noiseGen, long seed, int bits) {
		this(new Random(seed), getNumOctaves(noiseGen), bits);
	}

	@Override
	public double[] generateNoiseOctaves(double[] noise, int x, int y, int z, int xSize, int ySize, int zSize,
			double xScale, double yScale, double zScale) {
		yScale *= 4;
		if (noise == null || noise.length != xSize * ySize * zSize) {
			noise = new double[xSize * ySize * zSize];
		}
		int index = 0;

		for (int xLoc = 0; xLoc < xSize; ++xLoc) {
			double xCoord = (x + (double) xLoc) * xScale;

			for (int zLoc = 0; zLoc < zSize; ++zLoc) {
				double zCoord = (z + (double) zLoc) * zScale;

				for (int yLoc = 0; yLoc < ySize; ++yLoc) {
					double yCoord = (y + (double) yLoc) * yScale;

					double sx = 2 * Math.PI / xScale / loopSize;
					double sz = 2 * Math.PI / zScale / loopSize;

					double alpha = xCoord * sx;
					double gamma = zCoord * sz;

					double x1 = Math.sin(alpha) / sx;
					double x2 = Math.cos(alpha) / sx;

					double z1 = Math.sin(gamma) / sz;
					double z2 = Math.cos(gamma) / sz;

					noise[index++] = simplexOctaves(x1, x2, yCoord, z1, z2);
				}
			}
		}
		return noise;
	}

	private double simplexOctaves(double x, double y, double z, double w, double t) {
		double scale = 1.0D;
		double val = 0;
		for (int var16 = 0; var16 < this.octaves; ++var16) {
			double xx = x * scale + this.xCoord;
			double yy = y * scale + this.yCoord;
			double zz = z * scale + this.zCoord;
			double ww = w * scale + this.wCoord;
			double tt = t * scale + this.tCoord;

			val += this.perlin.getValue(xx, yy, zz, ww, tt) / scale;
			scale /= 2.0D;
		}
		return val;
	}

	private static int getNumOctaves(NoiseGeneratorOctaves noiseGen) {
		Field f = ReflectionUtil.getFieldByType(NoiseGeneratorImproved[].class, NoiseGeneratorOctaves.class, false);
		if (f == null)
			throw new Error("Field of type NoiseGeneratorImproved[] not found in class NoiseGeneratorOctaves!");
		return ReflectionUtil.getArrayLength(f, noiseGen);
	}

}
