package com.github.barteks2x.worldgenloop.noisegen;

import java.lang.reflect.Field;
import java.util.Random;

import net.minecraft.world.gen.NoiseGeneratorImproved;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.NoiseGeneratorSimplex;

import java.util.Random;

import com.github.barteks2x.worldgenloop.util.ReflectionUtil;

public class LoopedNoiseGenSimplex2d extends NoiseGeneratorPerlin {

    private int octaves;
    private SimplexBasis[] gens;
    private double loopSize;

    public LoopedNoiseGenSimplex2d(Random rand, int octaves, int bits) {
        super(rand, 0);
        this.loopSize = 1 << bits;
        this.octaves = octaves;
        int seed = rand.nextInt();
        this.gens = new SimplexBasis[octaves];
        for(int i = 0; i < octaves; i++) {
            gens[i] = new SimplexBasis();
            gens[i].setSeed(seed + i);
        }
    }
    
    public LoopedNoiseGenSimplex2d(NoiseGeneratorPerlin noiseGen, long seed, int bits) {
		this(new Random(seed), getNumOctaves(noiseGen), bits);
	}

    public double func_151601_a(double xPos, double zPos) {
        double zCoord = (zPos);
        double xCoord = (xPos);
        double sx = 2 * Math.PI / loopSize;
        double sz = 2 * Math.PI / loopSize;

        double alpha = xCoord * sx;
        double beta = zCoord * sz;

        double x1 = Math.sin(alpha) / sx;
        double x2 = Math.cos(alpha) / sx;

        double z1 = Math.sin(beta) / sz;
        double z2 = Math.cos(beta) / sz;

        return get4dOctaves(x1, x2, z1, z2, 1, 0.5);
    }

    public double[] func_151600_a(double[] array, double xPos, double zPos, int xSize, int zSize, double xScale,
            double zScale, double shouldBeOne, double shouldBeHalf) {
        if(array != null && array.length >= xSize * zSize) {
            for(int var16 = 0; var16 < array.length; ++var16) {
                array[var16] = 0.0D;
            }
        } else {
            array = new double[xSize * zSize];
        }

        int index = 0;
        for(int z = 0; z < zSize; z++) {
            double zCoord = (z + zPos) * zScale;
            for(int x = 0; x < xSize; x++) {
                double xCoord = (x + xPos) * xScale;
                double sx = 2 * Math.PI / loopSize / xScale;
                double sz = 2 * Math.PI / loopSize / zScale;

                double alpha = xCoord * sx;
                double beta = zCoord * sz;

                double x1 = Math.sin(alpha) / sx;
                double x2 = Math.cos(alpha) / sx;

                double z1 = Math.sin(beta) / sz;
                double z2 = Math.cos(beta) / sz;

                array[index++] = get4dOctaves(x1, x2, z1, z2, shouldBeOne, shouldBeHalf);
            }
        }

        return array;
    }

    private double get4dOctaves(double xPos, double yPos, double zPos, double wPos,
            double shouldBeOne, double shouldBeHalf) {
        double freqAmpInv = 1.0D;
        double scaleCoord = 1.0D;
        double value = 0;
        for(int i = 0; i < this.octaves; ++i) {
            value += this.gens[i].getValue4D(
                    xPos * scaleCoord * freqAmpInv, 
                    yPos * scaleCoord * freqAmpInv, 
                    zPos * scaleCoord * freqAmpInv, 
                    wPos * scaleCoord * freqAmpInv) * 0.55D / freqAmpInv;
            scaleCoord *= shouldBeOne;
            freqAmpInv *= shouldBeHalf;
        }
        return value;
    }
    private static int getNumOctaves(NoiseGeneratorPerlin noiseGen) {
		Field f = ReflectionUtil.getFieldByType(NoiseGeneratorSimplex[].class, NoiseGeneratorPerlin.class, false);
		if (f == null)
			throw new Error("Field of type NoiseGeneratorSimplex[] not found in class NoiseGeneratorPerlin!");
		return ReflectionUtil.getArrayLength(f, noiseGen);
	}
}